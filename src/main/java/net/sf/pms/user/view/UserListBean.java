package net.sf.pms.user.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
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

@Named
@Stateful
@RequestScoped
public class UserListBean implements Serializable {

	public String generate() {
		for (int i = 0; i < 121; i++) {
			User user = new User();
			user.setEmail("email" + i + "@bla.com");
			user.setFirstName("firstName" + i);
			user.setLastName("lastName" + i);
			user.setPhone("543-678-" + i);
			user.setSkype("name.surname" + i);
			user.setPassword("qqqq");
			entityManager.persist(user);
		}
		return "search?faces-redirect=true";
	}

	// generated:

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager entityManager;

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

	public String search() {
		page = 0;
		return "search?faces-redirect=true&includeViewParams=true";
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

}