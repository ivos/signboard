package com.github.ivos.signboard.user.view;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;


import com.github.ivos.signboard.view.support.ViewContext;
import com.google.common.base.Strings;

@FacesValidator("confirmPasswordMatching")
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

		if (Strings.isNullOrEmpty(password)
				|| Strings.isNullOrEmpty(confirmPassword)) {
			return;
		}

		if (!password.equals(confirmPassword)) {
			throw new ValidatorException(
					viewContext.createError("passwords.must.match"));
		}
	}

}
