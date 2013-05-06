package com.github.ivos.signboard.project.view;

import static com.github.ivos.signboard.config.ParamUtil.*;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaQuery;

import org.jboss.solder.exception.control.ExceptionHandled;

import com.github.ivos.signboard.config.security.SystemUser;
import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.project.model.ProjectCriteria;
import com.github.ivos.signboard.view.ViewContext;

@Named
@SessionScoped
@ExceptionHandled
public class ProjectListBean implements Serializable {

	@Inject
	ViewContext viewContext;

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
