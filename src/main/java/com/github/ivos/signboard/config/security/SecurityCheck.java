package com.github.ivos.signboard.config.security;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.security.annotations.Secures;
import org.jboss.solder.core.Client;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.project.view.ProjectBean;
import com.github.ivos.signboard.projectmember.view.ProjectMemberListBean;
import com.github.ivos.signboard.user.model.SystemRole;
import com.github.ivos.signboard.user.model.User;

public class SecurityCheck {

	@Inject
	@Client
	User clientUser;

	@Inject
	private Logger log;

	@Secures
	@SystemAdministrator
	public boolean isSystemAdministrator() {
		log.debugv("Verifying system administrator {0}.", clientUser);
		return (null != clientUser)
				&& clientUser.getSystemRoles().contains(SystemRole.admin);
	}

	@Secures
	@SystemUser
	public boolean isSystemUser() {
		log.debugv("Verifying system user {0}.", clientUser);
		return (null != clientUser)
				&& clientUser.getSystemRoles().contains(SystemRole.user);
	}

	@Inject
	ProjectBean projectBean;

	@Secures
	@ActiveProjectAdministrator
	public boolean isActiveProjectAdministrator(HttpServletRequest request) {
		Project project = projectBean.getProject();
		if (null != request && null == project.getId()) {
			project.setId(getIdFromRequestURI(request.getRequestURI()));
		}
		log.debugv("Verifying {0} is active project administrator of {1}.",
				clientUser, project);
		if (null != clientUser) {
			return project.isActiveAdministrator(clientUser);
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

	@Inject
	ProjectMemberListBean projectMemberListBean;

	@Secures
	@ActiveProjectMember
	public boolean isActiveProjectMember(HttpServletRequest request) {
		Project project = projectMemberListBean.getProject();
		if (null != request && null == project.getId()) {
			project.setId(getIdFromRequestURI(request.getRequestURI()));
			projectMemberListBean.retrieveProject();
		}
		log.debugv("Verifying {0} is active project member of {1}.",
				clientUser, project);
		if (null != clientUser) {
			return project.isActiveMember(clientUser);
		}
		return false;
	}
}
