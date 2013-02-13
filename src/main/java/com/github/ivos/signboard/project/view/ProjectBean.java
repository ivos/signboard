package com.github.ivos.signboard.project.view;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.solder.core.Client;
import org.jboss.solder.exception.control.ExceptionHandled;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.config.security.ActiveProjectAdministrator;
import com.github.ivos.signboard.config.security.SystemUser;
import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.projectmember.model.ProjectMember;
import com.github.ivos.signboard.projectmember.model.ProjectMemberRole;
import com.github.ivos.signboard.projectmember.model.ProjectMemberStatus;
import com.github.ivos.signboard.user.model.User;
import com.github.ivos.signboard.user.view.LoginBean;
import com.github.ivos.signboard.view.ViewContext;

@Named
@ViewScoped
@ExceptionHandled
public class ProjectBean implements Serializable {

	@Inject
	private Logger log;

	@Inject
	ViewContext viewContext;

	@Inject
	@Client
	User clientUser;

	public boolean isClientUserMember() {
		clientUser = entityManager.find(User.class, clientUser.getId());
		return project.isMember(clientUser);
	}

	public boolean isClientUserActiveMember() {
		clientUser = entityManager.find(User.class, clientUser.getId());
		return project.isActiveMember(clientUser);
	}

	public boolean isClientUserActiveAdministrator() {
		clientUser = entityManager.find(User.class, clientUser.getId());
		return project.isActiveAdministrator(clientUser);
	}

	private ProjectMember clientProjectMember;

	public ProjectMember getClientProjectMember() {
		// postback eliminated to toggle language
		if (null == clientProjectMember
				&& !FacesContext.getCurrentInstance().isPostback()) {
			return project.getMember(clientUser);
		}
		return clientProjectMember;
	}

	private static final long serialVersionUID = 1L;

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private Project project;

	public Project getProject() {
		if (null == project) {
			project = new Project();
		}
		return project;
	}

	@Inject
	private EntityManager entityManager;

	@SystemUser
	public void retrieve() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (id != null) {
			log.debugv("Retrieve project by id {0}.", id);
			project = entityManager.find(Project.class, id);
		}
	}

	@Inject
	private LoginBean loginBean;

	@SystemUser
	public String create() {
		clientUser = entityManager.find(User.class, clientUser.getId());
		ProjectMember projectMember = new ProjectMember(project, clientUser,
				ProjectMemberStatus.active);
		projectMember.addToMasters();
		projectMember.getRoles().add(ProjectMemberRole.admin);
		projectMember.getRoles().add(ProjectMemberRole.user);
		entityManager.persist(project);
		entityManager.persist(projectMember);
		loginBean.setUser(clientUser);
		log.infov("Create project {0}.", project);
		viewContext.info("saved");
		return "view?faces-redirect=true&id=" + project.getId();
	}

	@SystemUser
	@ActiveProjectAdministrator
	public String update() {
		log.infov("Update project {0}.", project);
		entityManager.merge(project);
		viewContext.info("saved");
		return "view?faces-redirect=true&id=" + project.getId();
	}

	@SystemUser
	public String join() {
		clientUser = entityManager.find(User.class, clientUser.getId());
		project = entityManager.find(Project.class, project.getId());
		ProjectMember projectMember = new ProjectMember(project, clientUser,
				ProjectMemberStatus.pending);
		projectMember.addToMasters();
		projectMember.getRoles().add(ProjectMemberRole.user);
		entityManager.persist(projectMember);
		loginBean.setUser(clientUser);
		log.infov("Join project {0}.", project);
		viewContext.info("project.join.request.created");
		return "view?faces-redirect=true&id=" + project.getId();
	}

}
