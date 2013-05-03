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
import com.github.ivos.signboard.task.model.Task;
import com.github.ivos.signboard.task.view.TaskBean;
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
	private HttpServletRequest request;

	@Secures
	@ActiveProjectAdministrator
	public boolean isActiveProjectAdministrator() {
		Project project = projectBean.getProject();
		if (null != request && null == project.getId()) {
			project.setId(getProjectIdFromRequestURI());
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

	public String getProjectIdFromRequestURI() {
		String requestURI = request.getRequestURI();
		String contextPath = request.getContextPath();
		log.debugv("Extracting project id from request URI {0}.", requestURI);
		String applicationURI = requestURI.substring(contextPath.length());
		String id = extractProjectIdFromProjectURI(applicationURI);
		if (!id.isEmpty()) {
			return id;
		}
		id = extractProjectIdFromProjectMemberURI(applicationURI);
		if (!id.isEmpty()) {
			return id;
		}
		id = extractProjectIdFromTaskURI(applicationURI);
		if (!id.isEmpty()) {
			return id;
		}
		throw new RuntimeException(
				"Project id cannot be extracted from request URI " + requestURI);
	}

	private String extractProjectIdFromProjectURI(String requestURI) {
		return extractId(requestURI, "/project/");
	}

	private String extractProjectIdFromProjectMemberURI(String requestURI) {
		final String projectMemberId = extractId(requestURI, "/projectMember/");
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

	private String extractProjectIdFromTaskURI(String requestURI) {
		final String taskId = extractId(requestURI, "/task/");
		if (taskId.isEmpty()) {
			return taskId;
		}
		final Task task = entityManager.find(Task.class, Long.valueOf(taskId));
		if (null != task) {
			return task.getProject().getId();
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
			project.setId(getProjectIdFromRequestURI());
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

	@Inject
	private TaskBean taskBean;

	@Secures
	@ActiveProjectUserByTask
	public boolean isActiveProjectUserByTask() {
		Project project = taskBean.getTask().getProject();
		if (null != request && null == project) {
			project = entityManager.find(Project.class,
					getProjectIdFromRequestURI());
		}
		boolean result = false;
		if (null != clientUser) {
			result = project.isActiveUser(clientUser);
		}
		log.debugv("Verifying {0} is active project user of {1}: {2}.",
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

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

}
