package com.github.ivos.signboard.config.jpa;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Disposes;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class EntityManagerProducer {

	@PersistenceUnit
	EntityManagerFactory factory;

	@Produces
	@RequestScoped
	EntityManager create() {
		return factory.createEntityManager();
	}

	public void close(@Disposes EntityManager entityManager) {
		entityManager.close();
	}

}
