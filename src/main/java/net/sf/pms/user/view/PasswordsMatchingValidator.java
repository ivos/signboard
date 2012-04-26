package net.sf.pms.user.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import net.sf.pms.view.support.ViewContext;

import org.jboss.seam.faces.validation.InputField;

@FacesValidator("passwordsMatching")
public class PasswordsMatchingValidator implements Validator {

	@Inject
	@InputField
	private String password;

	@Inject
	@InputField
	private String confirmPassword;

	@Inject
	private ViewContext viewContext;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (null != password && !password.equals(confirmPassword)) {
			throw new ValidatorException(
					viewContext.createError("passwords.must.match"));
		}
	}

}
