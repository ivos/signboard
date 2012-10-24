package com.github.ivos.signboard.config;

import java.util.Locale;

import javax.enterprise.event.Event;
import javax.enterprise.event.Observes;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.inject.Inject;

import org.jboss.seam.faces.event.qualifier.After;
import org.jboss.seam.faces.event.qualifier.RestoreView;
import org.jboss.seam.international.Alter;
import org.jboss.solder.core.Client;

public class RequestClientLocaleOverride {

	@Inject
	@Client
	private Locale clientLocale;

	@Inject
	FacesContext facesContext;

	@Inject
	@Client
	@Alter
	private Event<Locale> alterLocale;

	public void overrideClientLocaleWithRequest(
			@Observes @After @RestoreView PhaseEvent event) {
		if (!facesContext.isReleased()) {
			Locale requestLocale = facesContext.getViewRoot().getLocale();
			if (!clientLocale.equals(requestLocale)) {
				alterLocale.fire(requestLocale);
			}
		}
	}

}
