
package acme.features.sponsor.commercial_banners;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.banners.CommercialBanner;
import acme.entities.credit_cards.CreditCard;
import acme.entities.roles.Sponsor;
import acme.framework.components.Errors;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.services.AbstractDeleteService;

@Service
public class SponsorCommercialBannerDeleteService implements AbstractDeleteService<Sponsor, CommercialBanner> {

	@Autowired
	SponsorCommercialBannerRepository repository;


	@Override
	public boolean authorise(final Request<CommercialBanner> request) {
		int sponsorId;
		int bannerId;
		Sponsor sponsor;
		CommercialBanner banner;
		boolean result;

		sponsorId = request.getPrincipal().getActiveRoleId();
		bannerId = request.getModel().getInteger("id");
		sponsor = this.repository.findSponsorbySponsorId(sponsorId);
		banner = this.repository.findOneById(bannerId);

		result = banner.getSponsor().equals(sponsor);

		return result;
	}

	@Override
	public void bind(final Request<CommercialBanner> request, final CommercialBanner entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors, "creditCard");
	}

	@Override
	public void unbind(final Request<CommercialBanner> request, final CommercialBanner entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "picture", "slogan", "target");
	}

	@Override
	public CommercialBanner findOne(final Request<CommercialBanner> request) {
		int bannerId;
		CommercialBanner banner;

		bannerId = request.getModel().getInteger("id");
		banner = this.repository.findOneById(bannerId);

		return banner;
	}

	@Override
	public void validate(final Request<CommercialBanner> request, final CommercialBanner entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;
	}

	@Override
	public void delete(final Request<CommercialBanner> request, final CommercialBanner entity) {
		assert request != null;
		assert entity != null;

		Collection<CommercialBanner> commercialBannersWithSameCreditCard = this.repository.findCommercialBannersWithOneCreditCard(entity.getCreditCard().getCreditCardNumber());
		Collection<Sponsor> sponsorsWithSameCreditCard = this.repository.findSponsorsWithOneCreditCard(entity.getCreditCard().getCreditCardNumber());
		if (commercialBannersWithSameCreditCard.size() == 1 && sponsorsWithSameCreditCard.size() == 0) {
			CreditCard creditCardToDelete = this.repository.findOneCreditCarByNumber(entity.getCreditCard().getCreditCardNumber());
			this.repository.delete(creditCardToDelete);
		}

		this.repository.delete(entity);
	}

}
