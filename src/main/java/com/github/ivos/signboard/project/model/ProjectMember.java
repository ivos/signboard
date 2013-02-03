package com.github.ivos.signboard.project.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;

import org.hibernate.annotations.ForeignKey;

import com.github.ivos.signboard.user.model.User;

@Entity
public class ProjectMember implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Version
	private int version;

	@ManyToOne(optional = false)
	@ForeignKey(name = "project_member__project")
	private Project project;

	@ManyToOne(optional = false)
	@ForeignKey(name = "project_member__user")
	private User user;

	public ProjectMember() {
	}

	public ProjectMember(Project project, User user) {
		this.project = project;
		this.user = user;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime * result + ((user == null) ? 0 : user.hashCode());
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
		ProjectMember other = (ProjectMember) obj;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (user == null) {
			if (other.user != null)
				return false;
		} else if (!user.equals(other.user))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ProjectMember [id=" + id + ", version=" + version
				+ ", project=" + project.getId() + ", user=" + user.getId()
				+ "]";
	}

	private static final long serialVersionUID = 1L;

}
