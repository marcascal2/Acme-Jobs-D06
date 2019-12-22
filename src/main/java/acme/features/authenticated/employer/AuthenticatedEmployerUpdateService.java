
package acme.features.authenticated.employer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Employer;
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
public class AuthenticatedEmployerUpdateService implements AbstractUpdateService<Authenticated, Employer> {

	@Autowired
	private AuthenticatedEmployerRepository repository;


	@Override
	public boolean authorise(final Request<Employer> request) {
		assert request != null;

		boolean result = false;

		int idUA = request.getPrincipal().getAccountId();
		UserAccount ua = this.repository.findOneUserAccountById(idUA);
		Employer e = this.repository.findOneEmployerByUserAccountId(idUA);
		if (ua.getRoles().contains(e)) {
			result = true;
		}

		return result;
	}

	@Override
	public void bind(final Request<Employer> request, final Employer entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Employer> request, final Employer entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "company", "sector");
	}

	@Override
	public Employer findOne(final Request<Employer> request) {
		assert request != null;

		Employer result;
		Principal principal;
		int uaId;

		principal = request.getPrincipal();
		uaId = principal.getAccountId();

		result = this.repository.findOneEmployerByUserAccountId(uaId);

		return result;
	}

	@Override
	public void validate(final Request<Employer> request, final Employer entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

	}

	@Override
	public void update(final Request<Employer> request, final Employer entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}

	@Override
	public void onSuccess(final Request<Employer> request, final Response<Employer> response) {
		assert request != null;
		assert response != null;

		if (request.isMethod(HttpMethod.POST)) {
			PrincipalHelper.handleUpdate();
		}
	}
}
