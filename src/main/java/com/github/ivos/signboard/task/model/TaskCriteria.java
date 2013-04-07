package com.github.ivos.signboard.task.model;

import java.util.Date;

import javax.validation.constraints.Past;

import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.user.model.User;

public class TaskCriteria {

	private Project project;

	private String goal;

	private String description;

	@Past
	private Date timeCreated__From;

	@Past
	private Date timeCreated__To;

	private User author;

	private TaskSort sort = TaskSort.oldest;

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

	public Date getTimeCreated__From() {
		return timeCreated__From;
	}

	public void setTimeCreated__From(Date timeCreated__From) {
		this.timeCreated__From = timeCreated__From;
	}

	public Date getTimeCreated__To() {
		return timeCreated__To;
	}

	public void setTimeCreated__To(Date timeCreated__To) {
		this.timeCreated__To = timeCreated__To;
	}

	public User getAuthor() {
		return author;
	}

	public void setAuthor(User author) {
		this.author = author;
	}

	public TaskSort getSort() {
		return sort;
	}

	public void setSort(TaskSort sort) {
		this.sort = sort;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((goal == null) ? 0 : goal.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime * result + ((sort == null) ? 0 : sort.hashCode());
		result = prime
				* result
				+ ((timeCreated__From == null) ? 0 : timeCreated__From
						.hashCode());
		result = prime * result
				+ ((timeCreated__To == null) ? 0 : timeCreated__To.hashCode());
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
		TaskCriteria other = (TaskCriteria) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (goal == null) {
			if (other.goal != null)
				return false;
		} else if (!goal.equals(other.goal))
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (sort != other.sort)
			return false;
		if (timeCreated__From == null) {
			if (other.timeCreated__From != null)
				return false;
		} else if (!timeCreated__From.equals(other.timeCreated__From))
			return false;
		if (timeCreated__To == null) {
			if (other.timeCreated__To != null)
				return false;
		} else if (!timeCreated__To.equals(other.timeCreated__To))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "TaskCriteria [project=" + project + ", goal=" + goal
				+ ", description=" + description + ", timeCreated__From="
				+ timeCreated__From + ", timeCreated__To=" + timeCreated__To
				+ ", author=" + author + ", sort=" + sort + "]";
	}

}
