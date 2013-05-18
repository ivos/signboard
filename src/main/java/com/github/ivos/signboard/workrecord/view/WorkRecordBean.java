package com.github.ivos.signboard.workrecord.view;

import java.io.Serializable;
import java.util.Date;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.solder.core.Client;
import org.jboss.solder.exception.control.ExceptionHandled;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.config.jsf.ViewContext;
import com.github.ivos.signboard.project.view.ProjectBean;
import com.github.ivos.signboard.task.model.Task;
import com.github.ivos.signboard.user.model.User;
import com.github.ivos.signboard.workrecord.model.WorkRecord;

@Named
@ViewScoped
@ExceptionHandled
public class WorkRecordBean implements Serializable {

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

	private WorkRecord workRecord;

	public WorkRecord getWorkRecord() {
		if (null == workRecord) {
			workRecord = new WorkRecord();
			workRecord.setTask(entityManager.find(Task.class, taskId));
			workRecord.setWorker(clientUser);
			workRecord.setDate(new Date());
		}
		return workRecord;
	}

	private Long taskId;

	public Long getTaskId() {
		return taskId;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	@Inject
	private EntityManager entityManager;

	@Inject
	ProjectBean projectBean;

	public void retrieve() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (id != null) {
			log.debugv("Retrieve work record by id {0}.", id);
			workRecord = entityManager.find(WorkRecord.class, id);
		}
	}

	public String create() {
		entityManager.persist(workRecord);
		log.infov("Create {0}.", workRecord);
		viewContext.info("saved");
		return "view?faces-redirect=true&id=" + workRecord.getId();
	}

}
