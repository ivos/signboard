package com.github.ivos.signboard.task.view;

import static com.github.ivos.signboard.config.ParamUtil.*;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.solder.core.Client;
import org.jboss.solder.exception.control.ExceptionHandled;

import com.github.ivos.signboard.config.security.SystemUser;
import com.github.ivos.signboard.task.model.Task;
import com.github.ivos.signboard.task.model.TaskCriteria;
import com.github.ivos.signboard.user.model.User;
import com.github.ivos.signboard.view.ViewContext;

@Named
@SessionScoped
@ExceptionHandled
public class TaskListBean implements Serializable {

	@Inject
	ViewContext viewContext;

	public String reset() {
		criteria = new TaskCriteria();
		return search();
	}

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	private int page = 1;
	private long count;
	private List<Task> pageItems;

	private TaskCriteria criteria = new TaskCriteria();

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

	public TaskCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(TaskCriteria criteria) {
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
						Task.class)).setFirstResult((page - 1) * getPageSize())
				.setMaxResults(getPageSize()).getResultList();
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

	@Inject
	@Client
	User clientUser;

	@SystemUser
	public List<Task> getPageItems() {
		return pageItems;
	}

	public long getCount() {
		return count;
	}

	public int getLastPage() {
		return viewContext.calculateLastPage(count, getPageSize());
	}

}
