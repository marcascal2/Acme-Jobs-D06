
package acme.features.authenticated.worker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.roles.Worker;
import acme.framework.components.Errors;
import acme.framework.components.HttpMethod;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.components.Response;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.helpers.PrincipalHelper;
import acme.framework.services.AbstractCreateService;

@Service
public class AuthenticatedWorkerCreateService implements AbstractCreateService<Authenticated, Worker> {

	@Autowired
	private AuthenticatedWorkerRepository repository;


	@Override
	public boolean authorise(final Request<Worker> request) {
		assert request != null;

		boolean result = true;

		int idUA = request.getPrincipal().getAccountId();
		UserAccount ua = this.repository.findOneUserAccountById(idUA);
		Worker w = this.repository.findOneWorkerByUserAccountId(idUA);
		if (ua.getRoles().contains(w)) {
			result = false;
		}

		return result;
	}

	@Override
	public void bind(final Request<Worker> request, final Worker entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

		request.bind(entity, errors);
	}

	@Override
	public void unbind(final Request<Worker> request, final Worker entity, final Model model) {
		assert request != null;
		assert entity != null;

		request.unbind(entity, model, "qualifications", "skills");
	}

	@Override
	public Worker instantiate(final Request<Worker> request) {
		assert request != null;

		Worker result;
		Principal principal;
		int userAccountId;
		UserAccount ua;

		principal = request.getPrincipal();
		userAccountId = principal.getAccountId();
		ua = this.repository.findOneUserAccountById(userAccountId);

		result = new Worker();
		result.setUserAccount(ua);

		return result;
	}

	@Override
	public void validate(final Request<Worker> request, final Worker entity, final Errors errors) {
		assert request != null;
		assert entity != null;
		assert errors != null;

	}

	@Override
	public void create(final Request<Worker> request, final Worker entity) {
		assert request != null;
		assert entity != null;

		this.repository.save(entity);
	}

	@Override
	public void onSuccess(final Request<Worker> request, final Response<Worker> response) {
		assert request != null;
		assert response != null;

		if (request.isMethod(HttpMethod.POST)) {
			PrincipalHelper.handleUpdate();
		}
	}

}
