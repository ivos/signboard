package com.github.ivos.signboard.user.view;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import com.github.ivos.signboard.view.support.ViewContext;


@FacesValidator("emailNotRegistered")
public class EmailNotRegisteredValidator implements Validator {

	@Inject
	private EntityManager entityManager;

	@Inject
	private ViewContext viewContext;

	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (isEmailRegistered((String) value)) {
			throw new ValidatorException(
					viewContext.createError("email.already.registered"));
		}
	}

	private boolean isEmailRegistered(String email) {
		return entityManager
				.createQuery(
						"select user.id from User user where user.email=:email",
						Long.class).setParameter("email", email)
				.setMaxResults(1).getResultList().size() > 0;
	}

}
