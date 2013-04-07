package com.github.ivos.signboard.task.model;

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
import javax.validation.constraints.Size;

import org.hibernate.annotations.ForeignKey;

import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.user.model.User;

@Entity
public class Task {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Version
	private int version;

	@NotNull
	@ManyToOne(optional = false)
	@ForeignKey(name = "task__project")
	private Project project;

	@NotNull
	@Size(max = 512)
	private String goal;

	@Size(max = 1024)
	private String description;

	@NotNull
	@Column(updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeCreated;

	@ManyToOne(optional = false)
	@ForeignKey(name = "task__author")
	private User author;

	public Task() {
		timeCreated = new Date();
	}

	// Java bean

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

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getTimeCreated() {
		return timeCreated;
	}

	public void setTimeCreated(Date timeCreated) {
		this.timeCreated = timeCreated;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime * result
				+ ((timeCreated == null) ? 0 : timeCreated.hashCode());
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
		Task other = (Task) obj;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (timeCreated == null) {
			if (other.timeCreated != null)
				return false;
		} else if (!timeCreated.equals(other.timeCreated))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Task [id=" + id + ", version=" + version + ", project="
				+ project + ", goal=" + goal + ", description=" + description
				+ ", timeCreated=" + timeCreated + ", author=" + author + "]";
	}

}
