package com.github.ivos.signboard.config;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

@ApplicationScoped
public class PersistenceUtil {

	@Inject
	private EntityManager entityManager;

	public <T> List<T> findAll(Class<T> type, int firstResult, int maxResults) {
		CriteriaQuery<T> cq = entityManager.getCriteriaBuilder().createQuery(
				type);
		return entityManager.createQuery(cq.select(cq.from(type)))
				.setFirstResult(firstResult).setMaxResults(maxResults)
				.getResultList();
	}

}
