package com.github.ivos.signboard.user.view;

import static com.github.ivos.signboard.config.jpa.ParamUtil.*;

import java.io.Serializable;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import net.sf.seaf.util.Generator;

import org.jboss.solder.exception.control.ExceptionHandled;

import com.github.ivos.signboard.config.jsf.ViewContext;
import com.github.ivos.signboard.config.security.SystemAdministrator;
import com.github.ivos.signboard.user.model.SystemRole;
import com.github.ivos.signboard.user.model.User;
import com.github.ivos.signboard.user.model.UserCriteria;
import com.github.ivos.signboard.user.model.UserStatus;

@Named
@SessionScoped
@ExceptionHandled
public class UserListBean implements Serializable {

	@Inject
	ViewContext viewContext;

	public String generate() {
		Generator g = new Generator();
		for (int i = 0; i < 123; i++) {
			User user = new User();
			user.setEmail(g.word(3, 8).toLowerCase() + "@"
					+ g.word(3, 6).toLowerCase() + ".com");
			user.setFirstName(g.word(3, 7));
			user.setLastName(g.word(5, 9));
			user.setPhone(g.phone());
			user.setSkype(user.getFirstName().toLowerCase() + "."
					+ user.getLastName().toLowerCase());
			user.setPassword("qqqq");
			user.digestPassword();
			user.setRegistered(g.date().getTime());
			user.setStatus(UserStatus.active);
			if (g.numberIncl(0, 100) < 20) {
				user.setStatus(UserStatus.disabled);
			}
			user.getSystemRoles().add(SystemRole.user);
			if (g.numberIncl(0, 100) < 1) {
				user.getSystemRoles().add(SystemRole.admin);
			}
			entityManager.persist(user);
		}
		return "search?faces-redirect=true";
	}

	public String reset() {
		criteria = new UserCriteria();
		return search();
	}

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	private int page = 1;
	private long count;
	private List<User> pageItems;

	private UserCriteria criteria = new UserCriteria();

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

	public UserCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(UserCriteria criteria) {
		this.criteria = criteria;
	}

	public String search() {
		page = 1;
		return "search?faces-redirect=true";
	}

	@SystemAdministrator
	public void paginate() {
		count = setParameters(
				entityManager.createQuery("select count(r) " + getQuery(),
						Long.class)).getSingleResult();

		pageItems = setParameters(
				entityManager.createQuery("select r " + getQuery() + getSort(),
						User.class)).setFirstResult((page - 1) * getPageSize())
				.setMaxResults(getPageSize()).getResultList();
	}

	public String getQuery() {
		return "from User r"
				+ " where (:email is null or r.email like :email)"
				+ " and (:firstName is null or r.firstName like :firstName)"
				+ " and (:lastName is null or r.lastName like :lastName)"
				+ " and (:phone is null or r.phone like :phone)"
				+ " and (:status is null or r.status=:status)"
				+ " and (:registered__From is null or r.registered>=:registered__From)"
				+ " and (:registered__To is null or r.registered<:registered__To)"
				+ " and (:lastLogin__From is null or r.lastLogin>=:lastLogin__From)"
				+ " and (:lastLogin__To is null or r.lastLogin<:lastLogin__To)";
	}

	public <T> TypedQuery<T> setParameters(TypedQuery<T> query) {
		return query
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
			return " order by r.lastLogin desc";
		case byRecentRegistration:
			return " order by r.registered desc";
		}
		return " order by r.lastName, r.firstName, r.id";
	}

	@SystemAdministrator
	public List<User> getPageItems() {
		return pageItems;
	}

	public long getCount() {
		return count;
	}

	public int getLastPage() {
		return viewContext.calculateLastPage(count, getPageSize());
	}

}