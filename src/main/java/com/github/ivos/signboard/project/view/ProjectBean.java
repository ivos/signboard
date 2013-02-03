package com.github.ivos.signboard.project.view;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.solder.exception.control.ExceptionHandled;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.config.security.SystemUser;
import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.view.ViewContext;

@Named
@ViewScoped
@ExceptionHandled
public class ProjectBean implements Serializable {

	@Inject
	private Logger log;

	@Inject
	ViewContext viewContext;

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

	public void retrieve() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (id != null) {
			project = entityManager.find(Project.class, id);
		}
	}

	@SystemUser
	public String update() {
		if (id == null) {
			entityManager.persist(project);
			log.infov("Create project {0}.", project);
		} else {
			entityManager.merge(project);
		}
		viewContext.info("saved");
		return "view?faces-redirect=true&id=" + project.getId();
	}

}
