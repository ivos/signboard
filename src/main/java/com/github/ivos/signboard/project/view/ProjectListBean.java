package com.github.ivos.signboard.project.view;

import static com.github.ivos.signboard.config.jpa.ParamUtil.*;

import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.jboss.solder.exception.control.ExceptionHandled;

import com.github.ivos.signboard.config.jsf.ListBeanBase;
import com.github.ivos.signboard.config.security.SystemUser;
import com.github.ivos.signboard.project.model.Project;
import com.github.ivos.signboard.project.model.ProjectCriteria;

@Named
@SessionScoped
@ExceptionHandled
public class ProjectListBean extends ListBeanBase<Project, ProjectCriteria> {

	@Override
	public void resetCriteria() {
		criteria = new ProjectCriteria();
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

	private static final long serialVersionUID = 1L;

}
