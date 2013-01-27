package com.github.ivos.signboard.view;

import java.util.Date;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

@FacesValidator("searchDateInterval")
@RequestScoped
public class SearchDateIntervalValidator implements Validator {

	@Inject
	private ViewContext viewContext;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {

		if (null == value) {
			return;
		}

		Date toDate = (Date) value;
		String toComponentId = component.getId();
		String formId = component.getNamingContainer().getId();
		String fromComponentId = toComponentId.substring(0,
				toComponentId.length() - 4);
		UIInput fromComponent = (UIInput) context.getViewRoot().findComponent(
				formId + ":" + fromComponentId);
		Date fromDate = (Date) fromComponent.getLocalValue();

		if (null == fromDate) {
			return;
		}

		if (fromDate.getTime() >= toDate.getTime()) {
			throw new ValidatorException(
					viewContext
							.createError("end.date.must.be.after.start.date"));
		}
	}

}
