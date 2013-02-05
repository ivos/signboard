package com.github.ivos.signboard.config.security;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.security.annotations.Secures;
import org.jboss.solder.core.Client;

import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.project.view.ProjectBean;
import com.github.ivos.signboard.user.model.SystemRole;
import com.github.ivos.signboard.user.model.User;

public class SecurityCheck {

	@Inject
	@Client
	User clientUser;

	@Secures
	@SystemAdministrator
	public boolean isSystemAdministrator() {
		return (null != clientUser)
				&& clientUser.getSystemRoles().contains(SystemRole.admin);
	}

	@Secures
	@SystemUser
	public boolean isSystemUser() {
		return (null != clientUser)
				&& clientUser.getSystemRoles().contains(SystemRole.user);
	}

	@Inject
	ProjectBean projectBean;

	@Secures
	@ProjectAdministrator
	public boolean isProjectAdministrator(HttpServletRequest request) {
		Project project = projectBean.getProject();
		if (null != request && null == project.getId()) {
			project.setId(getIdFromRequestURI(request.getRequestURI()));
		}
		if (null != clientUser) {
			return project.isAdministrator(clientUser);
		}
		return false;
	}

	public String getIdFromRequestURI(String requestURI) {
		if (-1 == requestURI.indexOf('/')) {
			return "";
		}
		String id = requestURI.substring(0, requestURI.lastIndexOf('/'));
		id = id.substring(id.lastIndexOf('/') + 1);
		return id;
	}

}
