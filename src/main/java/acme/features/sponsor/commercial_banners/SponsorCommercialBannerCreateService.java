
package acme.features.sponsor.commercial_banners;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.banners.CommercialBanner;
import acme.entities.credit_cards.CreditCard;
import acme.entities.roles.Sponsor;
import acme.entities.spam_words.SpamWord;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractCreateService;

@Service
public class SponsorCommercialBannerCreateService implements AbstractCreateService<Sponsor, CommercialBanner> {

	//Internal state
	@Autowired
	SponsorCommercialBannerRepository repository;


	@Override
	public boolean authorise(final Request<CommercialBanner> request) {
		Principal principal;
		Sponsor sponsor;
		boolean hasCreditCard;

		principal = request.getPrincipal();
		sponsor = this.repository.findSponsorbySponsorId(principal.getActiveRoleId());

		CreditCard card = sponsor.getCreditCard();

		hasCreditCard = card != null;

		return hasCreditCard;
	}

	@Override
	public void bind(final Request<CommercialBanner> request, final CommercialBanner entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "titleHolder", "creditCardNumber", "month", "year", "cvc");
	}

	@Override
	public void unbind(final Request<CommercialBanner> request, final CommercialBanner entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "picture", "slogan", "target");

		Principal principal = request.getPrincipal();
		Sponsor sponsor = this.repository.findSponsorbySponsorId(principal.getActiveRoleId());
		CreditCard creditCard = sponsor.getCreditCard();
		model.setAttribute("titleHolder", creditCard.getTitleHolder());
		model.setAttribute("creditCardNumber", creditCard.getCreditCardNumber());
		model.setAttribute("month", creditCard.getMonth());
		model.setAttribute("year", creditCard.getYear());
		model.setAttribute("cvc", creditCard.getCvc());
	}

	@Override
	public CommercialBanner instantiate(final Request<CommercialBanner> request) {
		assert request != null;

		CommercialBanner commercialBanner = new CommercialBanner();
		Principal principal = request.getPrincipal();
		int idPrincipal = principal.getActiveRoleId();

		Sponsor sponsor = this.repository.findSponsorbySponsorId(idPrincipal);
		commercialBanner.setSponsor(sponsor);

		return commercialBanner;
	}

	@Override
	public void validate(final Request<CommercialBanner> request, final CommercialBanner entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		Collection<SpamWord> spamWords;
		spamWords = this.repository.findAllSpamWords();

		boolean isSpam = this.is_spam(entity.getSlogan(), spamWords);
		errors.state(request, !isSpam, "slogan", "sponsor.commercial-banner.form.error.span");

		String titleHolder = request.getModel().getString("titleHolder");
		errors.state(request, titleHolder != "", "titleHolder", "javax.validation.constraints.NotBlank.message");

		String creditCardNumber = request.getModel().getString("creditCardNumber");
		errors.state(request, creditCardNumber != "", "titleHolder", "javax.validation.constraints.NotBlank.message");

		String month = request.getModel().getString("month");
		errors.state(request, month != "", "titleHolder", "javax.validation.constraints.NotBlank.message");

		String year = request.getModel().getString("year");
		errors.state(request, year != "", "titleHolder", "javax.validation.constraints.NotBlank.message");

		String cvc = request.getModel().getString("cvc");
		errors.state(request, cvc != "", "titleHolder", "javax.validation.constraints.NotBlank.message");

		errors.state(request, Integer.valueOf(month) <= 12, "month", "sponsor.credit-card.form.errors.month");
		errors.state(request, Integer.valueOf(month) >= 1, "month", "sponsor.credit-card.form.errors.month");

		try {
			if (month != "" && month != null && year != "" && year != null) {
				LocalDate newDate = LocalDate.of(Integer.parseInt(year), Integer.parseInt(month), 1);
				LocalDate actualDate = LocalDate.now();
				LocalDate limitDate = actualDate.withDayOfMonth(1);
				boolean expiredCard = limitDate.isBefore(newDate);
				errors.state(request, expiredCard, "year", "sponsor.credit-card.form.errors.expiredCard");
			}
		} catch (Exception e) {
			System.out.println("error en la fecha");
		}
	}

	@Override
	public void create(final Request<CommercialBanner> request, final CommercialBanner entity) {
		assert request != null;
		assert entity != null;

		String creditCardNumber = request.getModel().getString("creditCardNumber");
		CreditCard creditCardAlreadyCreated = this.repository.findOneCreditCarByNumber(creditCardNumber);
		if (creditCardAlreadyCreated == null) {
			String titleHolder = request.getModel().getString("titleHolder");
			String month = request.getModel().getString("month");
			String year = request.getModel().getString("year");
			String cvc = request.getModel().getString("cvc");

			Principal principal = request.getPrincipal();
			Sponsor sponsor = this.repository.findSponsorbySponsorId(principal.getActiveRoleId());

			CreditCard newCreditCard = new CreditCard();
			newCreditCard.setSponsor(sponsor);
			newCreditCard.setTitleHolder(titleHolder);
			newCreditCard.setCvc(cvc);
			newCreditCard.setMonth(month);
			newCreditCard.setYear(year);
			newCreditCard.setCreditCardNumber(creditCardNumber);
			this.repository.save(newCreditCard);
			entity.setCreditCard(newCreditCard);
		} else {
			entity.setCreditCard(creditCardAlreadyCreated);
		}

		this.repository.save(entity);
	}

	private boolean is_spam(final String text, final Collection<SpamWord> spamWords) {
		List<String> list = Arrays.asList(text.split(" "));

		for (SpamWord spamWord : spamWords) {
			double spanishFrequency = (double) StringUtils.countMatches(text.toLowerCase(), spamWord.getSpanishTranslation().toLowerCase()) / list.size() * 100;
			if (spanishFrequency > spamWord.getSpamThreshold()) {
				return true;
			}
			double englishFrequency = (double) StringUtils.countMatches(text.toLowerCase(), spamWord.getEnglishTranslation().toLowerCase()) / list.size() * 100;
			if (englishFrequency > spamWord.getSpamThreshold()) {
				return true;
			}
		}

		return false;
	}

}
