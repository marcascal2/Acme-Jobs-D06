
package acme.features.sponsor.commercial_banners;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.banners.CommercialBanner;
import acme.entities.roles.Sponsor;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Principal;
import acme.framework.services.AbstractShowService;

@Service
public class SponsorCommercialBannerShowService implements AbstractShowService<Sponsor, CommercialBanner> {

	//Internal state
	@Autowired
	private SponsorCommercialBannerRepository repository;


	//AbstractShowService<Sponsor, Announcement> interface
	@Override
	public boolean authorise(final Request<CommercialBanner> request) {
		assert request != null;
		boolean result;
		int sponsorId;
		Sponsor sponsor;
		CommercialBanner cb;
		Principal principal;
		sponsorId = request.getModel().getInteger("id");
		cb = this.repository.findOneById(sponsorId);
		sponsor = cb.getSponsor();
		principal = request.getPrincipal();
		result = sponsor.getUserAccount().getId() == principal.getAccountId();
		return result;
	}

	@Override
	public void unbind(final Request<CommercialBanner> request, final CommercialBanner entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		request.unbind(entity, model, "picture", "slogan", "target", "creditCard");
	}

	@Override
	public CommercialBanner findOne(final Request<CommercialBanner> request) {
		assert request != null;
		CommercialBanner result;
		int id;
		id = request.getModel().getInteger("id");
		result = this.repository.findOneById(id);
		return result;
	}
}
