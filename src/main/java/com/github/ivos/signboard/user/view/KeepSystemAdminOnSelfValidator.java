package com.github.ivos.signboard.user.view;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import org.jboss.solder.core.Client;

import com.github.ivos.signboard.user.model.User;
import com.github.ivos.signboard.view.ViewContext;

@FacesValidator("keepSystemAdminOnSelf")
@RequestScoped
public class KeepSystemAdminOnSelfValidator implements Validator {

	@Inject
	private ViewContext viewContext;

	@Inject
	UserBean userBean;

	@Inject
	@Client
	User clientUser;

	@SuppressWarnings("unchecked")
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (isRemovingSystemAdministratorOnSelf((List<String>) value)) {
			throw new ValidatorException(
					viewContext
							.createError("cannot.remove.system.administrator.on.self"));
		}
	}

	private boolean isRemovingSystemAdministratorOnSelf(List<String> values) {
		boolean removing = !values.contains("admin");
		boolean onSelf = userBean.getUser().equals(clientUser);
		return removing && onSelf;
	}
}
