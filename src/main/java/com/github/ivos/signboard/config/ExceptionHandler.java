package com.github.ivos.signboard.config;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import org.jboss.seam.security.AuthorizationException;
import org.jboss.solder.exception.control.CaughtException;
import org.jboss.solder.exception.control.Handles;
import org.jboss.solder.exception.control.HandlesExceptions;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.view.ViewContext;

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
		log.error("Caught authorizationaut exception.");
		viewContext.error("authorization.exception");
		event.handled();
	}

}
