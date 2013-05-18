package com.github.ivos.signboard.workrecord.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.ForeignKey;

import com.github.ivos.signboard.task.model.Task;
import com.github.ivos.signboard.user.model.User;

@Entity
public class WorkRecord implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Version
	private int version;

	@ManyToOne(optional = false)
	@ForeignKey(name = "work_record__task")
	private Task task;

	@NotNull
	@Temporal(TemporalType.DATE)
	private Date date;

	@NotNull
	private Long duration;

	@NotNull
	@Temporal(TemporalType.TIMESTAMP)
	@Column(updatable = false)
	private final Date created = new Date();

	@ManyToOne(optional = false)
	@ForeignKey(name = "work_record__worker")
	private User worker;

	// business logic

	// java bean

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Date getCreated() {
		return created;
	}

	public User getWorker() {
		return worker;
	}

	public void setWorker(User worker) {
		this.worker = worker;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + ((task == null) ? 0 : task.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WorkRecord other = (WorkRecord) obj;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (task == null) {
			if (other.task != null)
				return false;
		} else if (!task.equals(other.task))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "WorkRecord [id=" + id + ", version=" + version + ", task="
				+ task + ", date=" + date + ", duration=" + duration
				+ ", created=" + created + ", worker=" + worker + "]";
	}

	private static final long serialVersionUID = 1L;

}
