package net.sf.pms.config;

import javax.inject.Inject;
import javax.persistence.OptimisticLockException;

import net.sf.pms.view.support.ViewContext;

import org.jboss.solder.exception.control.CaughtException;
import org.jboss.solder.exception.control.Handles;
import org.jboss.solder.exception.control.HandlesExceptions;
import org.jboss.solder.logging.Logger;

@HandlesExceptions
public class ExceptionHandler {

	@Inject
	Logger log;

	@Inject
	ViewContext viewContext;

	public void optimisticLockException(
			@Handles CaughtException<OptimisticLockException> event) {
		viewContext.error("optimistic.lock.exception");
	}

}
