
package acme.features.authenticated.message;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.messages.Message;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.entities.Principal;
import acme.framework.entities.UserAccount;
import acme.framework.services.AbstractShowService;

@Service
public class AuthenticatedMessageShowService implements AbstractShowService<Authenticated, Message> {

	@Autowired
	private AuthenticatedMessageRepository repository;


	@Override
	public boolean authorise(final Request<Message> request) {
		assert request != null;

		int messageId;
		Message message;
		Principal principal;

		messageId = request.getModel().getInteger("id");
		message = this.repository.findOne(messageId);
		principal = request.getPrincipal();

		List<UserAccount> authorisedUsers = message.getMessageThread().getMessageThreadsOfUserAccount().stream().map(x -> x.getUserAccount()).collect(Collectors.toList());

		return authorisedUsers.stream().anyMatch(x -> x.getId() == principal.getAccountId());
	}

	@Override
	public void unbind(final Request<Message> request, final Message entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;

		request.unbind(entity, model, "title", "moment", "tags", "body");
	}

	@Override
	public Message findOne(final Request<Message> request) {
		assert request != null;

		Message result;
		int id;

		id = request.getModel().getInteger("id");

		result = this.repository.findOne(id);

		return result;
	}

}
