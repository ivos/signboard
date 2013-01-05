package com.github.ivos.signboard.user.view;

import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Event;
import javax.enterprise.inject.Instance;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.international.Alter;
import org.jboss.solder.core.Client;
import org.jboss.solder.logging.Logger;

@Named
@SessionScoped
public class LanguageBean implements Serializable {

	@Inject
	Logger log;

	@Inject
	@Client
	@Alter
	private Event<Locale> alterLocale;

	@Inject
	@Client
	private Instance<Locale> clientLocale;

	private boolean clientLocaleOverride = false;

	public String setLanguage(String language) {
		Locale locale;
		if ("cs".equals(language)) {
			locale = new Locale("cs");
		} else {
			locale = new Locale("en");
		}
		log.debugv("Set locale {0}, language requested {1}.", locale, language);
		alterLocale.fire(locale);
		clientLocaleOverride = true;
		return FacesContext.getCurrentInstance().getExternalContext()
				.getRequestServletPath()
				+ "?faces-redirect=true";
	}

	public boolean isLanguage(String language) {
		return clientLocale.get().toString().startsWith(language);
	}

	public boolean isClientLocaleOverride() {
		return clientLocaleOverride;
	}

	private static final long serialVersionUID = 1L;

}
