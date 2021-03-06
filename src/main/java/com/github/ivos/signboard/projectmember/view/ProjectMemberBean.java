package com.github.ivos.signboard.projectmember.view;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.solder.exception.control.ExceptionHandled;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.config.jsf.SelectUtils;
import com.github.ivos.signboard.config.jsf.ViewContext;
import com.github.ivos.signboard.config.security.ActiveProjectAdministrator;
import com.github.ivos.signboard.config.security.SystemUser;
import com.github.ivos.signboard.project.view.ProjectBean;
import com.github.ivos.signboard.projectmember.model.ProjectMember;
import com.github.ivos.signboard.projectmember.model.ProjectMemberRole;
import com.github.ivos.signboard.projectmember.model.ProjectMemberSort;
import com.github.ivos.signboard.projectmember.model.ProjectMemberStatus;

@Named
@ViewScoped
@ExceptionHandled
public class ProjectMemberBean implements Serializable {

	@Inject
	private Logger log;

	@Inject
	ViewContext viewContext;

	public boolean isActive() {
		return ProjectMemberStatus.active
				.equals(getProjectMember().getStatus());
	}

	public boolean isDisabled() {
		return ProjectMemberStatus.disabled.equals(getProjectMember()
				.getStatus());
	}

	@ActiveProjectAdministrator
	public String disable() {
		projectMember.setStatus(ProjectMemberStatus.disabled);
		return update();
	}

	@ActiveProjectAdministrator
	public String activate() {
		projectMember.setStatus(ProjectMemberStatus.active);
		return update();
	}

	private static final long serialVersionUID = 1L;

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private ProjectMember projectMember;

	public ProjectMember getProjectMember() {
		if (null == projectMember) {
			projectMember = new ProjectMember();
		}
		return projectMember;
	}

	@Inject
	private EntityManager entityManager;

	@Inject
	ProjectBean projectBean;

	@SystemUser
	public void retrieve() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (id != null) {
			log.debugv("Retrieve project member by id {0}.", id);
			projectMember = entityManager.find(ProjectMember.class, id);

			projectBean.setId(projectMember.getProject().getId());
			projectBean.retrieve();
		}
	}

	@SystemUser
	@ActiveProjectAdministrator
	public String update() {
		log.infov("Update project member {0}.", projectMember);
		entityManager.merge(projectMember);
		viewContext.info("saved");
		return "view?faces-redirect=true&id=" + projectMember.getId();
	}

	// Select options

	@Inject
	SelectUtils selectUtils;

	public Collection<String> getRoles() {
		return selectUtils.convertToStrings(getProjectMember().getRoles());
	}

	public void setRoles(Collection<String> roles) {
		getProjectMember().setRoles(
				selectUtils.convertToEnumSet(roles, ProjectMemberRole.class));
	}

	public List<SelectItem> getRoles__Options() {
		return selectUtils.convertToSelectItems(ProjectMemberRole.class);
	}

	public List<SelectItem> getStatus__Options() {
		return selectUtils
				.convertToSelectItemsWithEmpty(ProjectMemberStatus.class);
	}

	public List<SelectItem> getSort__Options() {
		return selectUtils.convertToSelectItems(ProjectMemberSort.class);
	}

}
