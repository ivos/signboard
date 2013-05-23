package com.github.ivos.signboard.config;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import org.jboss.seam.security.AuthorizationException;
import org.jboss.solder.exception.control.CaughtException;
import org.jboss.solder.exception.control.Handles;
import org.jboss.solder.exception.control.HandlesExceptions;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.config.jsf.ViewContext;

@HandlesExceptions
public class ExceptionHandler {

	@Inject
	private Logger log;

	@Inject
	private ViewContext viewContext;

	public void optimisticLockException(
			@Handles CaughtException<OptimisticLockException> event) {
		log.error("Caught optimistic lock exception.");
		viewContext.error("optimistic.lock.exception");
		event.handled();
	}

	public void authorizationException(
			@Handles CaughtException<AuthorizationException> event) {
		log.error("Caught authorization exception", event.getException());
		viewContext.error("authorization.exception");
		event.handled();
	}

	public void anyThrowable(@Handles CaughtException<Throwable> event) {
		log.error("Caught throwable", event.getException());
		event.handled();
	}

}
