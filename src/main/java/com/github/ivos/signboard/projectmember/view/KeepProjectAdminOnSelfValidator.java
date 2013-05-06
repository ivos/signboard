package com.github.ivos.signboard.projectmember.view;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.validator.FacesValidator;
import javax.faces.validator.Validator;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;

import org.jboss.solder.core.Client;

import com.github.ivos.signboard.config.jsf.ViewContext;
import com.github.ivos.signboard.user.model.User;

@FacesValidator("keepProjectAdminOnSelf")
@RequestScoped
public class KeepProjectAdminOnSelfValidator implements Validator {

	@Inject
	private ViewContext viewContext;

	@Inject
	ProjectMemberBean projectMemberBean;

	@Inject
	@Client
	User clientUser;

	@SuppressWarnings("unchecked")
	@Override
	public void validate(FacesContext context, UIComponent component,
			Object value) throws ValidatorException {
		if (isRemovingProjectAdministratorOnSelf((List<String>) value)) {
			throw new ValidatorException(
					viewContext
							.createError("cannot.remove.project.administrator.on.self"));
		}
	}

	private boolean isRemovingProjectAdministratorOnSelf(List<String> values) {
		boolean removing = !values.contains("admin");
		boolean onSelf = projectMemberBean.getProjectMember().getUser()
				.equals(clientUser);
		return removing && onSelf;
	}

}
