package com.github.ivos.signboard.user.view;

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

import net.sf.seaf.util.Generator;

import org.jboss.solder.exception.control.ExceptionHandled;

import com.github.ivos.signboard.config.security.SystemAdministrator;
import com.github.ivos.signboard.user.model.User;
import com.github.ivos.signboard.user.model.UserCriteria;
import com.github.ivos.signboard.user.model.UserStatus;

@Named
@Stateful
@SessionScoped
@ExceptionHandled
public class UserListBean implements Serializable {

	public String generate() {
		Generator g = new Generator();
		for (int i = 0; i < 121; i++) {
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
			entityManager.persist(user);
		}
		return "search?faces-redirect=true";
	}

	public String reset() {
		criteria = new UserCriteria();
		return search();
	}

	// generated:

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

	/*
	 * Support searching User entities with pagination
	 */

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
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		// Populate count
		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<User> root = countCriteria.from(User.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		count = entityManager.createQuery(countCriteria).getSingleResult();

		// Populate pageItems
		CriteriaQuery<User> listCriteria = builder.createQuery(User.class);
		root = listCriteria.from(User.class);
		TypedQuery<User> query = entityManager.createQuery(listCriteria.select(
				root).where(getSearchPredicates(root)));
		query.setFirstResult((page - 1) * getPageSize()).setMaxResults(
				getPageSize());
		pageItems = query.getResultList();
	}

	public Predicate[] getSearchPredicates(Root<User> root) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String email = criteria.getEmail();
		if (email != null && !"".equals(email)) {
			predicatesList.add(builder.like(root.<String> get("email"),
					'%' + email + '%'));
		}
		String firstName = criteria.getFirstName();
		if (firstName != null && !"".equals(firstName)) {
			predicatesList.add(builder.like(root.<String> get("firstName"),
					'%' + firstName + '%'));
		}
		String lastName = criteria.getLastName();
		if (lastName != null && !"".equals(lastName)) {
			predicatesList.add(builder.like(root.<String> get("lastName"),
					'%' + lastName + '%'));
		}
		String phone = criteria.getPhone();
		if (phone != null && !"".equals(phone)) {
			predicatesList.add(builder.like(root.<String> get("phone"),
					'%' + phone + '%'));
		}
		UserStatus status = criteria.getStatus();
		if (status != null) {
			predicatesList.add(builder.equal(root.<UserStatus> get("status"),
					status));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	@SystemAdministrator
	public List<User> getPageItems() {
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

	public List<User> getAll() {
		CriteriaQuery<User> criteria = entityManager.getCriteriaBuilder()
				.createQuery(User.class);
		return entityManager.createQuery(
				criteria.select(criteria.from(User.class))).getResultList();
	}

	public Converter getConverter() {
		return new Converter() {
			@Override
			public Object getAsObject(FacesContext context,
					UIComponent component, String value) {

				return entityManager.find(User.class, Long.valueOf(value));
			}

			@Override
			public String getAsString(FacesContext context,
					UIComponent component, Object value) {

				if (value == null) {
					return "";
				}

				return String.valueOf(((User) value).getId());
			}
		};
	}

}