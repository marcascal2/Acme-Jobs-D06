
package acme.features.authenticated.message_thread;

import java.util.Collection;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import acme.entities.message_threads.MessageThread;
import acme.framework.entities.UserAccount;
import acme.framework.repositories.AbstractRepository;

@Repository
public interface AuthenticatedMessageThreadRepository extends AbstractRepository {

	@Query("select mt from MessageThread mt where mt.id = ?1")
	MessageThread findOneById(int id);

	@Query("select mt from MessageThread mt join mt.messageThreadsOfUserAccount u where u.userAccount.id = ?1")
	Collection<MessageThread> findManyByUserId(int userId);

	@Query("select u from UserAccount u where u.id = ?1")
	UserAccount findUserAccountById(int userId);

	@Query("select ua from UserAccount ua")
	Collection<UserAccount> findManyUserAccounts();
}
