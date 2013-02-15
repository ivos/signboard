package com.github.ivos.signboard.config.security;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.jboss.seam.security.annotations.Secures;
import org.jboss.solder.core.Client;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.project.view.ProjectBean;
import com.github.ivos.signboard.projectmember.model.ProjectMember;
import com.github.ivos.signboard.user.model.SystemRole;
import com.github.ivos.signboard.user.model.User;

public class SecurityCheck {

	@Inject
	@Client
	private User clientUser;

	@Inject
	private Logger log;

	@Inject
	private EntityManager entityManager;

	@Secures
	@SystemAdministrator
	public boolean isSystemAdministrator() {
		boolean result = (null != clientUser)
				&& clientUser.getSystemRoles().contains(SystemRole.admin);
		log.debugv("Verifying system administrator {0}: {1}.", clientUser,
				result);
		return result;
	}

	@Secures
	@SystemUser
	public boolean isSystemUser() {
		boolean result = (null != clientUser)
				&& clientUser.getSystemRoles().contains(SystemRole.user);
		log.debugv("Verifying system user {0}: {1}.", clientUser, result);
		return result;
	}

	@Inject
	private ProjectBean projectBean;

	@Inject
	HttpServletRequest request;

	@Secures
	@ActiveProjectAdministrator
	public boolean isActiveProjectAdministrator() {
		Project project = projectBean.getProject();
		if (null != request && null == project.getId()) {
			project.setId(getProjectIdFromRequestURI(request.getRequestURI()));
		}
		boolean result = false;
		if (null != clientUser) {
			result = project.isActiveAdministrator(clientUser);
		}
		log.debugv(
				"Verifying {0} is active project administrator of {1}: {2}.",
				clientUser, project, result);
		return result;
	}

	public String getProjectIdFromRequestURI(String requestURI) {
		log.debugv("Extracting project id from request URI {0}.", requestURI);
		String id = extractProjectIdFromProjectURI(requestURI);
		if (!id.isEmpty()) {
			return id;
		}
		id = extractProjectIdFromProjectMemberURI(requestURI);
		if (!id.isEmpty()) {
			return id;
		}
		throw new RuntimeException(
				"Project id cannot be extracted from request URI " + requestURI);
	}

	private String extractProjectIdFromProjectURI(String requestURI) {
		return extractId(requestURI, "/signboard/project/");
	}

	private String extractProjectIdFromProjectMemberURI(String requestURI) {
		final String projectMemberId = extractId(requestURI,
				"/signboard/projectMember/");
		if (projectMemberId.isEmpty()) {
			return projectMemberId;
		}
		final ProjectMember projectMember = entityManager.find(
				ProjectMember.class, Long.valueOf(projectMemberId));
		if (null != projectMember) {
			return projectMember.getProject().getId();
		}
		return "";
	}

	private String extractId(String requestURI, String prefix) {
		if (!requestURI.startsWith(prefix)) {
			return "";
		}
		String id = requestURI.substring(prefix.length());
		final int nextSlash = id.indexOf('/');
		if (-1 != nextSlash) {
			id = id.substring(0, nextSlash);
		}
		return id;
	}

	@Secures
	@ActiveProjectMember
	public boolean isActiveProjectMember() {
		Project project = projectBean.getProject();
		if (null != request && null == project.getId()) {
			project.setId(getProjectIdFromRequestURI(request.getRequestURI()));
			projectBean.retrieve();
		}
		boolean result = false;
		if (null != clientUser) {
			result = project.isActiveMember(clientUser);
		}
		log.debugv("Verifying {0} is active project member of {1}: {2}.",
				clientUser, project, result);
		return result;
	}

	// setters

	public void setLog(Logger log) {
		this.log = log;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

}
