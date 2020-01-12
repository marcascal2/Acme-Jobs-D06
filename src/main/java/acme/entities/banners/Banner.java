
package acme.entities.banners;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Banner implements Serializable {

	//Serialisation identifier
	private static final long	serialVersionUID	= 1L;

	//Attributes
	@NotBlank
	@URL
	private String				picture;

	@NotBlank
	private String				slogan;

	@NotBlank
	@URL
	private String				target;

}
