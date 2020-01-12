
package acme.features.authenticated.job;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.applications.Application;
import acme.entities.descriptors.Descriptor;
import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.entities.Authenticated;
import acme.framework.services.AbstractShowService;

@Service
public class AuthenticatedJobShowService implements AbstractShowService<Authenticated, Job> {

	@Autowired
	private AuthenticatedJobRepository repository;


	@Override
	public boolean authorise(final Request<Job> request) {
		assert request != null;

		return true;
	}

	@Override
	public void unbind(final Request<Job> request, final Job entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		request.unbind(entity, model, "reference", "title", "status", "deadline");
		request.unbind(entity, model, "salary", "moreInfo", "description");

		Descriptor descriptor = entity.getDescriptor();
		if (descriptor != null) {
			String descriptorTitle = descriptor.getTitle();
			model.setAttribute("descriptor", descriptorTitle);

			model.setAttribute("descriptorId", descriptor.getId());

			Collection<Duty> duties = descriptor.getDuty();
			model.setAttribute("duties", duties);

			Double sum = 0.0;

			for (Duty duty : duties) {
				if (duty.getPercentageTimeForWeek() != null) {
					sum = sum + duty.getPercentageTimeForWeek();
				}
			}

			model.setAttribute("sumPercentage", sum == 100.0);
		}
		Collection<Application> applications = entity.getApplication();
		model.setAttribute("application", applications);

		int idJob = entity.getId();
		model.setAttribute("idJob", idJob);

		boolean applied = false;
		for (Application a : applications) {
			if (a.getWorker() != null) {
				applied = true;
				break;
			}
		}

		model.setAttribute("applied", applied);
	}

	@Override
	public Job findOne(final Request<Job> request) {
		assert request != null;

		Job result;
		int id;

		id = request.getModel().getInteger("id");
		result = this.repository.findOneById(id);

		return result;
	}

}
