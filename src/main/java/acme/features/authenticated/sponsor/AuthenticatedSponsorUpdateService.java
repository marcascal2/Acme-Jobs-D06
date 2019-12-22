
package acme.features.authenticated.sponsor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Sponsor;
import acme.framework.components.Errors;
import acme.framework.components.HttpMethod;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.components.Response;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractUpdateService;

@Service
public class AuthenticatedSponsorUpdateService implements AbstractUpdateService<Authenticated, Sponsor> {

	//Internal state
	@Autowired
	AuthenticatedSponsorRepository repository;


	//AbstractUpdateService<Authenticated, Sponsor>

	@Override
	public boolean authorise(final Request<Sponsor> request) {
		assert request != null;

		boolean result = false;

		int idUA = request.getPrincipal().getAccountId();
		UserAccount ua = this.repository.findOneUserAccountById(idUA);
		Sponsor s = this.repository.findOneSponsorByUserAccountId(idUA);
		if (ua.getRoles().contains(s)) {
			result = true;
		}

		return result;
	}

	@Override
	public void bind(final Request<Sponsor> request, final Sponsor entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Sponsor> request, final Sponsor entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		int accId = request.getPrincipal().getAccountId();
		Sponsor s = this.repository.findOneSponsorByUserAccountId(accId);
		boolean creditCard = s.getCreditCard() != null;

		model.setAttribute("creditCard", creditCard);

		int sponsorId = entity.getId();
		model.setAttribute("sponsorId", sponsorId);

		request.unbind(entity, model, "organisationName");
	}

	@Override
	public Sponsor findOne(final Request<Sponsor> request) {
		assert request != null;

		Sponsor result;
		Principal principal;
		int uaId;

		principal = request.getPrincipal();
		uaId = principal.getAccountId();

		result = this.repository.findOneSponsorByUserAccountId(uaId);

		return result;
	}

	@Override
	public void validate(final Request<Sponsor> request, final Sponsor entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

	}

	@Override
	public void update(final Request<Sponsor> request, final Sponsor entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}

	@Override
	public void onSuccess(final Request<Sponsor> request, final Response<Sponsor> response) {
		assert request != null;
		assert response != null;

		if (request.isMethod(HttpMethod.POST)) {
			PrincipalHelper.handleUpdate();
		}
	}
}
