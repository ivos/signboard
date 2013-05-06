package com.github.ivos.signboard.project.view;

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
import com.github.ivos.signboard.config.security.SystemUser;
import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.project.model.ProjectCriteria;

@Named
@SessionScoped
@ExceptionHandled
public class ProjectListBean implements Serializable {

	public String reset() {
		criteria = new ProjectCriteria();
		return search();
	}

	public String search() {
		page = 1;
		return "search?faces-redirect=true";
	}

	@Inject
	ViewContext viewContext;

	@Inject
	private EntityManager entityManager;

	private int page = 1;

	private long count;

	private List<Project> pageItems;

	private ProjectCriteria criteria = new ProjectCriteria();

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

	public ProjectCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(ProjectCriteria criteria) {
		this.criteria = criteria;
	}

	@SystemUser
	public void paginate() {
		count = setParameters(
				entityManager.createQuery("select count(r) " + getQuery(),
						Long.class)).getSingleResult();

		pageItems = setParameters(
				entityManager.createQuery("select r " + getQuery() + getSort(),
						Project.class))
				.setFirstResult((page - 1) * getPageSize())
				.setMaxResults(getPageSize()).getResultList();
	}

	public String getQuery() {
		return "from Project r" //
				+ " where (:code is null or r.code like :code)"
				+ " and (:name is null or r.name like :name)";
	}

	public <T> TypedQuery<T> setParameters(TypedQuery<T> query) {
		return query.setParameter("code", anywhere(criteria.getCode()))
				.setParameter("name", anywhere(criteria.getName()));
	}

	public String getSort() {
		return " order by r.code";
	}

	@SystemUser
	public List<Project> getPageItems() {
		return pageItems;
	}

	public long getCount() {
		return count;
	}

	public int getLastPage() {
		return viewContext.calculateLastPage(count, getPageSize());
	}

	private static final long serialVersionUID = 1L;

}
