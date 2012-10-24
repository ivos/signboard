package com.github.ivos.signboard.user.view;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;

import com.github.ivos.signboard.view.ViewContext;

@FacesValidator("confirmPasswordMatching")
@RequestScoped
public class ConfirmPasswordMatchingValidator implements Validator {

	@Inject
	private ViewContext viewContext;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		String confirmPassword = (String) value;
		String formId = component.getNamingContainer().getId();
		UIInput passwordComponent = (UIInput) context.getViewRoot()
				.findComponent(formId + ":password");
		String password = (String) passwordComponent.getLocalValue();

		if (StringUtils.isEmpty(password)
				|| StringUtils.isEmpty(confirmPassword)) {
			return;
		}

		if (!password.equals(confirmPassword)) {
			throw new ValidatorException(
					viewContext.createError("passwords.must.match"));
		}
	}

}
