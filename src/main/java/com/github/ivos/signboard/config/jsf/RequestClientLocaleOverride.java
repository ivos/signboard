package com.github.ivos.signboard.config.jsf;

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
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.user.view.LanguageBean;

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

	@Inject
	private LanguageBean languageBean;

	@Inject
	Logger log;

	public void overrideClientLocaleWithRequest(
			@Observes @After @RestoreView PhaseEvent event) {
		if (!facesContext.isReleased()
				&& !languageBean.isClientLocaleOverride()) {
			Locale requestLocale = facesContext.getViewRoot().getLocale();
			log.debugv("Client locale {0}, request locale {1}.", clientLocale,
					requestLocale);
			if (!clientLocale.equals(requestLocale)) {
				alterLocale.fire(requestLocale);
			}
		}
	}

}
