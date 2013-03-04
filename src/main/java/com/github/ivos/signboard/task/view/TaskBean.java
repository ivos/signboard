package com.github.ivos.signboard.task.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.seam.security.annotations.LoggedIn;
import org.jboss.solder.core.Client;
import org.jboss.solder.exception.control.ExceptionHandled;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.config.security.ActiveProjectUserByTask;
import com.github.ivos.signboard.config.security.SystemUser;
import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.project.view.ProjectBean;
import com.github.ivos.signboard.task.model.Task;
import com.github.ivos.signboard.user.model.User;
import com.github.ivos.signboard.view.SelectUtils;
import com.github.ivos.signboard.view.ViewContext;

@Named
@ViewScoped
@ExceptionHandled
public class TaskBean implements Serializable {

	@Inject
	private Logger log;

	@Inject
	ViewContext viewContext;

	@Inject
	@Client
	User clientUser;

	private static final long serialVersionUID = 1L;

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private Task task;

	public Task getTask() {
		if (null == task) {
			task = new Task();
		}
		return task;
	}

	@Inject
	private EntityManager entityManager;

	@SystemUser
	public void retrieve() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (id != null) {
			log.debugv("Retrieve task by id {0}.", id);
			task = entityManager.find(Task.class, id);
		}
	}

	@SystemUser
	@ActiveProjectUserByTask
	public String create() {
		// clientUser = entityManager.find(User.class, clientUser.getId());
		task.setAuthor(clientUser);
		entityManager.persist(task);
		log.infov("Create task {0}.", task);
		viewContext.info("saved");
		return "view?faces-redirect=true&id=" + task.getId();
	}

	// Select options

	@Inject
	SelectUtils selectUtils;

	@Inject
	ProjectBean projectBean;

	@LoggedIn
	public List<SelectItem> getProject__Options() {
		final List<Project> allMyMemberProjects = projectBean
				.getAllMyActiveUserProjects();
		List<SelectItem> list = new ArrayList<SelectItem>();
		list.add(new SelectItem(null, viewContext.getLabel("select.chooseOne")));
		for (Project project : allMyMemberProjects) {
			list.add(new SelectItem(project, project.getName()));
		}
		return list;
	}

}
