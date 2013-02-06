package com.github.ivos.signboard.project.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

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

	@ElementCollection(fetch = FetchType.EAGER)
	@Enumerated(EnumType.STRING)
	@Column(name = "role", length = 32, nullable = false)
	@ForeignKey(name = "project_member_roles__project_member")
	private Set<ProjectRole> roles = new HashSet<ProjectRole>();

	@NotNull
	@Enumerated(EnumType.STRING)
	@Column(length = 32)
	private ProjectMemberStatus status;

	public ProjectMember() {
	}

	public ProjectMember(Project project, User user, ProjectMemberStatus status) {
		this.project = project;
		this.user = user;
		this.status = status;
	}

	public void addToMasters() {
		project.getProjectMembers().add(this);
		user.getProjectMembers().add(this);
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

	public Set<ProjectRole> getRoles() {
		return roles;
	}

	public void setRoles(Set<ProjectRole> roles) {
		this.roles = roles;
	}

	public ProjectMemberStatus getStatus() {
		return status;
	}

	public void setStatus(ProjectMemberStatus status) {
		this.status = status;
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
				+ ", roles=" + roles + ", status=" + status + "]";
	}

	private static final long serialVersionUID = 1L;

}
