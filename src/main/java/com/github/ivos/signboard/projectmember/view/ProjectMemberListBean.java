package com.github.ivos.signboard.projectmember.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.solder.exception.control.ExceptionHandled;

import com.github.ivos.signboard.config.security.ActiveProjectMember;
import com.github.ivos.signboard.config.security.SystemUser;
import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.projectmember.model.ProjectMember;
import com.github.ivos.signboard.projectmember.model.ProjectMemberCriteria;
import com.github.ivos.signboard.projectmember.model.ProjectMemberStatus;
import com.github.ivos.signboard.user.model.User;
import com.github.ivos.signboard.view.ViewContext;

@Named
@Stateful
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

	private Project project;

	public Project getProject() {
		if (null == project) {
			project = new Project();
		}
		return project;
	}

	public void retrieveProject() {
		if (null != projectId && entityManager.isOpen()) {
			project = entityManager.find(Project.class, projectId);
		} else {
			project = new Project();
		}
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
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		// Populate count
		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<ProjectMember> root = countCriteria.from(ProjectMember.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		count = entityManager.createQuery(countCriteria).getSingleResult();

		// Populate pageItems
		CriteriaQuery<ProjectMember> criteria = builder
				.createQuery(ProjectMember.class);
		root = criteria.from(ProjectMember.class);
		TypedQuery<ProjectMember> query = entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root))
				.orderBy(getOrder(builder, root)));
		query.setFirstResult((page - 1) * getPageSize()).setMaxResults(
				getPageSize());
		pageItems = query.getResultList();
	}

	public Predicate[] getSearchPredicates(Root<ProjectMember> root) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		predicatesList.add(builder.equal(root.<Project> get("project")
				.<String> get("code"), projectId));
		String email = criteria.getEmail();
		if (email != null && !"".equals(email)) {
			predicatesList.add(builder.like(root.<User> get("user")
					.<String> get("email"), '%' + email + '%'));
		}
		String firstName = criteria.getFirstName();
		if (firstName != null && !"".equals(firstName)) {
			predicatesList.add(builder.like(root.<User> get("user")
					.<String> get("firstName"), '%' + firstName + '%'));
		}
		String lastName = criteria.getLastName();
		if (lastName != null && !"".equals(lastName)) {
			predicatesList.add(builder.like(root.<User> get("user")
					.<String> get("lastName"), '%' + lastName + '%'));
		}
		String phone = criteria.getPhone();
		if (phone != null && !"".equals(phone)) {
			predicatesList.add(builder.like(root.<User> get("user")
					.<String> get("phone"), '%' + phone + '%'));
		}
		ProjectMemberStatus status = criteria.getStatus();
		if (status != null) {
			predicatesList.add(builder.equal(
					root.<ProjectMemberStatus> get("status"), status));
		}
		Date registered__From = criteria.getRegistered__From();
		if (null != registered__From) {
			predicatesList.add(builder.greaterThanOrEqualTo(
					root.<User> get("user").<Date> get("registered"),
					registered__From));
		}
		Date registered__To = criteria.getRegistered__To();
		if (null != registered__To) {
			predicatesList.add(builder.lessThan(root.<User> get("user")
					.<Date> get("registered"), registered__To));
		}
		Date lastLogin__From = criteria.getLastLogin__From();
		if (null != lastLogin__From) {
			predicatesList.add(builder.greaterThanOrEqualTo(
					root.<User> get("user").<Date> get("lastLogin"),
					lastLogin__From));
		}
		Date lastLogin__To = criteria.getLastLogin__To();
		if (null != lastLogin__To) {
			predicatesList.add(builder.lessThan(root.<User> get("user")
					.<Date> get("lastLogin"), lastLogin__To));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Order> getOrder(CriteriaBuilder builder,
			Root<ProjectMember> root) {
		List<Order> list = new ArrayList<Order>();
		switch (criteria.getSort()) {
		case alphabetically:
			list.add(builder.asc(root.<User> get("user").<String> get(
					"lastName")));
			list.add(builder.asc(root.<User> get("user").<String> get(
					"firstName")));
			list.add(builder.asc(root.<User> get("user").<Long> get("id")));
			break;
		case byRecentLogin:
			list.add(builder.desc(root.<User> get("user").<Date> get(
					"lastLogin")));
			break;
		case byRecentRegistration:
			list.add(builder.desc(root.<User> get("user").<Date> get(
					"registered")));
			break;
		}
		return list;
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
