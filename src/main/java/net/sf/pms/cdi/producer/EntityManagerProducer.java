package net.sf.pms.cdi.producer;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;

@ApplicationScoped
public class EntityManagerProducer {

	@PersistenceUnit
	EntityManagerFactory factory;

	@Produces
	@ConversationScoped
	EntityManager produce() {
		return factory.createEntityManager();
	}

}
