package com.github.ivos.signboard.project.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.jboss.seam.security.annotations.LoggedIn;
import org.jboss.solder.exception.control.ExceptionHandled;

import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.project.model.ProjectCriteria;

@Named
@Stateful
@SessionScoped
@ExceptionHandled
public class ProjectListBean implements Serializable {

	public String reset() {
		criteria = new ProjectCriteria();
		return search();
	}

	private static final long serialVersionUID = 1L;

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

	public String search() {
		page = 1;
		return "search?faces-redirect=true";
	}

	@LoggedIn
	public void paginate() {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		// Populate count
		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<Project> root = countCriteria.from(Project.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		count = entityManager.createQuery(countCriteria).getSingleResult();

		// Populate pageItems
		CriteriaQuery<Project> criteria = builder.createQuery(Project.class);
		root = criteria.from(Project.class);
		TypedQuery<Project> query = entityManager.createQuery(criteria.select(
				root).where(getSearchPredicates(root)));
		query.setFirstResult((page - 1) * getPageSize()).setMaxResults(
				getPageSize());
		pageItems = query.getResultList();
	}

	public Predicate[] getSearchPredicates(Root<Project> root) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String code = criteria.getCode();
		if (code != null && !"".equals(code)) {
			predicatesList.add(builder.like(root.<String> get("code"),
					'%' + code + '%'));
		}
		String name = criteria.getName();
		if (name != null && !"".equals(name)) {
			predicatesList.add(builder.like(root.<String> get("name"),
					'%' + name + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<Project> getPageItems() {
		return pageItems;
	}

	public long getCount() {
		return count;
	}

	public int getLastPage() {
		return (int) (count / getPageSize()) + 1;
	}

	/*
	 * Support listing and POSTing back User entities (e.g. from inside an
	 * HtmlSelectOneMenu)
	 */

	public List<Project> getAll() {
		CriteriaQuery<Project> criteria = entityManager.getCriteriaBuilder()
				.createQuery(Project.class);
		return entityManager.createQuery(
				criteria.select(criteria.from(Project.class))).getResultList();
	}

	public Converter getConverter() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {
				return entityManager.find(Project.class, value);
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {
				if (value == null) {
					return "";
				}
				return ((Project) value).getId();
			}
		};
	}

}
