package net.sf.pms.user.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.faces.application.FacesMessage;
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

import net.sf.pms.user.model.User;
import net.sf.pms.user.model.UserSearch;

import org.jboss.solder.logging.Logger;

@Named
@Stateful
@RequestScoped
public class UserBean implements Serializable {

	@Inject
	Logger log;

	public UserBean() {
		System.out.println("Constructed.");
	}

	// generated:

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving User entities
	 */

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private User user;

	public User getUser() {
		if (null == user) {
			user = new User();
		}
		return user;
	}

	@Inject
	private EntityManager entityManager;

	public void retrieve() {

		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (id != null) {
			user = entityManager.find(User.class, getId());
		}
	}

	/*
	 * Support updating and deleting User entities
	 */

	public String update() {
		log.infov("update, id={0}, user={1}", id, user);
		try {
			if (id == null) {
				entityManager.persist(user);
				return "search?faces-redirect=true";
			} else {
				entityManager.merge(user);
				return "view?faces-redirect=true&id=" + user.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	public String delete() {

		try {
			entityManager.remove(entityManager.find(User.class, getId()));
			entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			return null;
		}
	}

	/*
	 * Support searching User entities with pagination
	 */

	private int page;
	private long count;
	private List<User> pageItems;

	private UserSearch search = new UserSearch();

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPageSize() {
		return 10;
	}

	public UserSearch getSearch() {
		return search;
	}

	public void setSearch(UserSearch search) {
		this.search = search;
	}

	public void search() {
		page = 0;
	}

	public void paginate() {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();

		// Populate count

		CriteriaQuery<Long> countCriteria = builder.createQuery(Long.class);
		Root<User> root = countCriteria.from(User.class);
		countCriteria = countCriteria.select(builder.count(root)).where(
				getSearchPredicates(root));
		count = entityManager.createQuery(countCriteria).getSingleResult();

		// Populate pageItems

		CriteriaQuery<User> criteria = builder.createQuery(User.class);
		root = criteria.from(User.class);
		TypedQuery<User> query = entityManager.createQuery(criteria
				.select(root).where(getSearchPredicates(root)));
		query.setFirstResult(page * getPageSize()).setMaxResults(getPageSize());
		pageItems = query.getResultList();
	}

	private Predicate[] getSearchPredicates(Root<User> root) {

		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		List<Predicate> predicatesList = new ArrayList<Predicate>();

		String email = search.getEmail();
		if (email != null && !"".equals(email)) {
			predicatesList.add(builder.like(root.<String> get("email"),
					'%' + email + '%'));
		}
		String firstName = search.getFirstName();
		if (firstName != null && !"".equals(firstName)) {
			predicatesList.add(builder.like(root.<String> get("firstName"),
					'%' + firstName + '%'));
		}
		String lastName = search.getLastName();
		if (lastName != null && !"".equals(lastName)) {
			predicatesList.add(builder.like(root.<String> get("lastName"),
					'%' + lastName + '%'));
		}
		String phone = search.getPhone();
		if (phone != null && !"".equals(phone)) {
			predicatesList.add(builder.like(root.<String> get("phone"),
					'%' + phone + '%'));
		}
		String skype = search.getSkype();
		if (skype != null && !"".equals(skype)) {
			predicatesList.add(builder.like(root.<String> get("skype"),
					'%' + skype + '%'));
		}

		return predicatesList.toArray(new Predicate[predicatesList.size()]);
	}

	public List<User> getPageItems() {
		return pageItems;
	}

	public long getCount() {
		return count;
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

	/*
	 * Support adding children to bidirectional, one-to-many tables
	 */

	private User add = new User();

	public User getAdd() {
		return add;
	}

	public User getAdded() {
		User added = add;
		add = new User();
		return added;
	}
}