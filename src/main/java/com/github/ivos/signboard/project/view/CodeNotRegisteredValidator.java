package com.github.ivos.signboard.project.view;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.github.ivos.signboard.config.jsf.ViewContext;

@FacesValidator("codeNotRegistered")
@RequestScoped
public class CodeNotRegisteredValidator implements Validator {

	@Inject
	private EntityManager entityManager;

	@Inject
	private ViewContext viewContext;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (isCodeRegistered((String) value)) {
			throw new ValidatorException(
					viewContext.createError("project.code.already.registered"));
		}
	}

	private boolean isCodeRegistered(String code) {
		return entityManager
				.createQuery(
						"select project.code from Project project where project.code=:code",
						String.class).setParameter("code", code)
				.setMaxResults(1).getResultList().size() > 0;
	}

}
