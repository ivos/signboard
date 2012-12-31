package com.github.ivos.signboard.user.view;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.seam.remoting.annotations.WebRemote;
import org.jboss.solder.logging.Logger;

@Named
public class UserRemoting {

	@Inject
	private Logger log;

	@Inject
	private EntityManager entityManager;

	@Inject
	UserListBean userListBean;

	// typeahead

	@WebRemote
	public String[] lastName_Typeahead(String query) {
		// CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		// CriteriaQuery<User> listCriteria = builder.createQuery(User.class);
		// Root<User> root = listCriteria.from(User.class);
		//
		// List<Predicate> predicatesList = new ArrayList<Predicate>();
		// String lastName = criteria.getLastName();
		// if (lastName != null && !"".equals(lastName)) {
		// predicatesList.add(builder.like(root.<String> get("lastName"),
		// '%' + lastName + '%'));
		// }
		//
		// TypedQuery<User> query =
		// entityManager.createQuery(listCriteria.select(
		// root).where(
		// predicatesList.toArray(new Predicate[predicatesList.size()])));
		// query.setMaxResults(getPageSize());
		// pageItems = query.getResultList();

		List<String> values = entityManager
				.createQuery(
						"select user.lastName from User user where user.lastName like :lastName",
						String.class)
				.setParameter("lastName", '%' + query + '%')
				.setMaxResults(userListBean.getPageSize()).getResultList();

		log.debugv("====== Last name {0} / {2} produces values {1}. ======",
				userListBean.getCriteria().getLastName(), values, query);

		return values.toArray(new String[values.size()]);
	}

	@WebRemote
	public String[] firstName_Typeahead(String query) {
		List<String> values = entityManager
				.createQuery(
						"select user.firstName from User user where user.firstName like :firstName",
						String.class)
				.setParameter("firstName", '%' + query + '%')
				.setMaxResults(userListBean.getPageSize()).getResultList();

		log.debugv("====== First name {0} produces values {1}. ======", query,
				values);

		return values.toArray(new String[values.size()]);
	}

	@WebRemote
	public String[] email_Typeahead(String query) {
		List<String> values = entityManager
				.createQuery(
						"select user.email from User user where user.email like :email",
						String.class).setParameter("email", '%' + query + '%')
				.setMaxResults(userListBean.getPageSize()).getResultList();

		log.debugv("====== Email {0} produces values {1}. ======", query,
				values);

		return values.toArray(new String[values.size()]);
	}

}
