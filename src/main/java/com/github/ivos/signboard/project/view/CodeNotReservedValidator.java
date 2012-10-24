package com.github.ivos.signboard.project.view;

import java.util.Arrays;
import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import com.github.ivos.signboard.view.ViewContext;

@FacesValidator("codeNotReserved")
@RequestScoped
public class CodeNotReservedValidator implements Validator {

	@Inject
	private ViewContext viewContext;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (RESERVED_CODES.contains(value)) {
			throw new ValidatorException(
					viewContext.createError("project.code.reserved"));
		}
	}

	private static final List<String> RESERVED_CODES = Arrays.asList("create",
			"page");

}
