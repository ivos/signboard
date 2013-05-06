package com.github.ivos.signboard.projectmember.view;

import static com.github.ivos.signboard.config.jpa.ParamUtil.*;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.solder.exception.control.ExceptionHandled;

import com.github.ivos.signboard.config.jsf.ViewContext;
import com.github.ivos.signboard.config.security.ActiveProjectMember;
import com.github.ivos.signboard.config.security.SystemUser;
import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.projectmember.model.ProjectMember;
import com.github.ivos.signboard.projectmember.model.ProjectMemberCriteria;

@Named
@SessionScoped
@ExceptionHandled
public class ProjectMemberListBean implements Serializable {

	private String projectId;

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Project getProject() {
		return entityManager.find(Project.class, projectId);
	}

	@Inject
	ViewContext viewContext;

	public String reset() {
		criteria = new ProjectMemberCriteria();
		return search();
	}

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	private int page = 1;
	private long count;
	private List<ProjectMember> pageItems;

	private ProjectMemberCriteria criteria = new ProjectMemberCriteria();

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		if (page < 1) {
			page = 1;
		}
		if (page > getLastPage()) {
			page = getLastPage();
		}
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public ProjectMemberCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(ProjectMemberCriteria criteria) {
		this.criteria = criteria;
	}

	public String search() {
		page = 1;
		return "search?faces-redirect=true&projectId=" + projectId;
	}

	@SystemUser
	@ActiveProjectMember
	public void paginate() {
		count = setParameters(
				entityManager.createQuery("select count(r) " + getQuery(),
						Long.class)).getSingleResult();

		pageItems = setParameters(
				entityManager.createQuery("select r " + getQuery() + getSort(),
						ProjectMember.class))
				.setFirstResult((page - 1) * getPageSize())
				.setMaxResults(getPageSize()).getResultList();
	}

	public String getQuery() {
		return "from ProjectMember r"
				+ " where r.project.code=:projectId"
				+ " and (:email is null or r.user.email like :email)"
				+ " and (:firstName is null or r.user.firstName like :firstName)"
				+ " and (:lastName is null or r.user.lastName like :lastName)"
				+ " and (:phone is null or r.user.phone like :phone)"
				+ " and (:status is null or r.status=:status)"
				+ " and (:registered__From is null or r.user.registered>=:registered__From)"
				+ " and (:registered__To is null or r.user.registered<:registered__To)"
				+ " and (:lastLogin__From is null or r.user.lastLogin>=:lastLogin__From)"
				+ " and (:lastLogin__To is null or r.user.lastLogin<:lastLogin__To)";
	}

	public <T> TypedQuery<T> setParameters(TypedQuery<T> query) {
		return query
				.setParameter("projectId", projectId)
				.setParameter("email", anywhere(criteria.getEmail()))
				.setParameter("firstName", anywhere(criteria.getFirstName()))
				.setParameter("lastName", anywhere(criteria.getLastName()))
				.setParameter("phone", anywhere(criteria.getPhone()))
				.setParameter("status", asString(criteria.getStatus()))
				.setParameter("registered__From",
						criteria.getRegistered__From())
				.setParameter("registered__To", criteria.getRegistered__To())
				.setParameter("lastLogin__From", criteria.getLastLogin__From())
				.setParameter("lastLogin__To", criteria.getLastLogin__To());
	}

	public String getSort() {
		switch (criteria.getSort()) {
		case byRecentLogin:
			return " order by r.user.lastLogin desc";
		case byRecentRegistration:
			return " order by r.user.registered desc";
		}
		return " order by r.user.lastName, r.user.firstName, r.user.id";
	}

	@SystemUser
	@ActiveProjectMember
	public List<ProjectMember> getPageItems() {
		return pageItems;
	}

	public long getCount() {
		return count;
	}

	public int getLastPage() {
		return viewContext.calculateLastPage(count, getPageSize());
	}

}
