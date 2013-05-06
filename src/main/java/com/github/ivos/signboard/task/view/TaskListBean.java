package com.github.ivos.signboard.task.view;

import static com.github.ivos.signboard.config.jpa.ParamUtil.*;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.TypedQuery;

import org.jboss.solder.core.Client;
import org.jboss.solder.exception.control.ExceptionHandled;

import com.github.ivos.signboard.config.jsf.ListBeanBase;
import com.github.ivos.signboard.config.security.SystemUser;
import com.github.ivos.signboard.task.model.Task;
import com.github.ivos.signboard.task.model.TaskCriteria;
import com.github.ivos.signboard.user.model.User;

@Named
@SessionScoped
@ExceptionHandled
public class TaskListBean extends ListBeanBase<Task, TaskCriteria> {

	@Override
	public void resetCriteria() {
		criteria = new TaskCriteria();
	}

	public String getQuery() {
		return "from Task r join r.project p join p.projectMembers pm join pm.roles rl"
				+ " where pm.user=:clientUser and pm.status='active' and rl='user'"
				+ " and (:project is null or r.project=:project)"
				+ " and (:goal is null or r.goal like :goal)"
				+ " and (:description is null or r.description like :description)"
				+ " and (:timeCreated__From is null or r.timeCreated>=:timeCreated__From)"
				+ " and (:timeCreated__To is null or r.timeCreated<:timeCreated__To)"
				+ " and (:author is null or r.author=:author)";
	}

	public <T> TypedQuery<T> setParameters(TypedQuery<T> query) {
		return query
				.setParameter("clientUser", clientUser)
				.setParameter("project", criteria.getProject())
				.setParameter("goal", anywhere(criteria.getGoal()))
				.setParameter("description",
						anywhere(criteria.getDescription()))
				.setParameter("timeCreated__From",
						criteria.getTimeCreated__From())
				.setParameter("timeCreated__To", criteria.getTimeCreated__To())
				.setParameter("author", criteria.getAuthor());
	}

	public String getSort() {
		switch (criteria.getSort()) {
		case mostRecent:
			return " order by r.id desc";
		}
		return " order by r.id";
	}

	@SystemUser
	public void paginate() {
		count = setParameters(
				entityManager.createQuery("select count(r) " + getQuery(),
						Long.class)).getSingleResult();

		pageItems = setParameters(
				entityManager.createQuery("select r " + getQuery() + getSort(),
						Task.class)).setFirstResult((page - 1) * getPageSize())
				.setMaxResults(getPageSize()).getResultList();
	}

	@Inject
	@Client
	User clientUser;

	private static final long serialVersionUID = 1L;

}
