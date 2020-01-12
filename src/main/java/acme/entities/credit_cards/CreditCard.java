
package acme.entities.credit_cards;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.CreditCardNumber;

import acme.entities.roles.Sponsor;
import acme.framework.entities.DomainEntity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(indexes = {
	@Index(columnList = "creditCardNumber"),
})
public class CreditCard extends DomainEntity {

	/**
	 *
	 */
	private static final long	serialVersionUID	= 1L;

	@NotBlank
	private String				titleHolder;

	@NotNull
	@Pattern(regexp = "\\d{3}", message = "XXX")
	private String				cvc;

	@CreditCardNumber
	private String				creditCardNumber;

	@NotBlank
	@Pattern(regexp = "\\d{2}", message = "XX")
	private String				month;

	@NotBlank
	@Pattern(regexp = "\\d{4}", message = "XXXX")
	private String				year;

	//	Relationship

	@OneToOne(optional = true)
	private Sponsor				sponsor;
}
