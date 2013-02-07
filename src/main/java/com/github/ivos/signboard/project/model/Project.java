package com.github.ivos.signboard.project.model;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.joda.time.DateMidnight;

import com.github.ivos.signboard.user.model.User;

@Entity
public class Project implements Serializable {

	@Id
	@NotNull
	@Size(min = 3, max = 64)
	@Pattern(regexp = "[\\w][\\w\\-]*", message = "{project.code.regexp}")
	private String code;

	@Version
	private int version;

	@NotNull
	@Size(max = 128)
	private String name;

	@Size(max = 1024)
	private String description;

	@NotNull
	@Column(updatable = false)
	@Temporal(TemporalType.DATE)
	private Date dateCreated;

	@OneToMany(mappedBy = "project")
	private Set<ProjectMember> projectMembers = new HashSet<ProjectMember>();

	// business logic

	public Project() {
	}

	public Project(String code) {
		this.code = code;
	}

	public String getId() {
		return code;
	}

	public void setId(String id) {
		this.code = id;
	}

	@PrePersist
	public void initDateCreated() {
		dateCreated = DateMidnight.now().toDate();
	}

	public boolean isMember(User user) {
		for (ProjectMember projectMember : user.getProjectMembers()) {
			boolean isMember = projectMember.getProject().equals(this);
			if (isMember) {
				return true;
			}
		}
		return false;
	}

	public boolean isActiveMember(User user) {
		for (ProjectMember projectMember : user.getProjectMembers()) {
			boolean isMember = projectMember.getProject().equals(this);
			if (isMember) {
				boolean isActive = ProjectMemberStatus.active
						.equals(projectMember.getStatus());
				return isActive;
			}
		}
		return false;
	}

	public boolean isActiveAdministrator(User user) {
		for (ProjectMember projectMember : user.getProjectMembers()) {
			boolean isMember = projectMember.getProject().equals(this);
			if (isMember) {
				boolean isAdministrator = projectMember.getRoles().contains(
						ProjectMemberRole.admin);
				boolean isActive = ProjectMemberStatus.active
						.equals(projectMember.getStatus());
				return isAdministrator && isActive;
			}
		}
		return false;
	}

	// Java bean

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Set<ProjectMember> getProjectMembers() {
		return projectMembers;
	}

	public void setProjectMembers(Set<ProjectMember> projectMembers) {
		this.projectMembers = projectMembers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Project other = (Project) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Project [code=" + code + ", version=" + version + ", name="
				+ name + ", description=" + description + ", dateCreated="
				+ dateCreated + "]";
	}

	private static final long serialVersionUID = 1L;

}
