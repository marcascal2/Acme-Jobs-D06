
package acme.features.employer.duty;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import acme.entities.duties.Duty;
import acme.entities.jobs.Job;
import acme.entities.roles.Employer;
import acme.framework.components.Model;
import acme.framework.components.Request;
import acme.framework.services.AbstractShowService;

@Service
public class EmployerDutyShowService implements AbstractShowService<Employer, Duty> {

	//Internal state
	@Autowired
	EmployerDutyRepository repository;


	//AbstractShowService<Employer, Job>
	@Override
	public boolean authorise(final Request<Duty> request) {

		assert request != null;
		int employerId = request.getPrincipal().getActiveRoleId();
		int dutyId = request.getModel().getInteger("id");

		Duty duty = this.repository.findOneById(dutyId);
		Job job = duty.getDescriptor().getJob();
		Employer employer = this.repository.findEmployerById(employerId);

		boolean isAuthorised = job.getEmployer().equals(employer);

		return isAuthorised;
	}

	@Override
	public void unbind(final Request<Duty> request, final Duty entity, final Model model) {
		assert request != null;
		assert entity != null;
		assert model != null;
		request.unbind(entity, model, "title", "description", "percentageTimeForWeek");

		model.setAttribute("job", entity.getDescriptor().getJob().getTitle());
	}

	@Override
	public Duty findOne(final Request<Duty> request) {
		assert request != null;
		Duty result;
		int id;
		id = request.getModel().getInteger("id");
		result = this.repository.findOneById(id);
		return result;
	}

}
