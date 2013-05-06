package com.github.ivos.signboard.config.resource;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.solder.core.Client;
import org.jboss.solder.logging.Logger;


@RequestScoped
public class ResourceBundleProducer {

	@Inject
	private Logger log;

	private ResourceBundle messageResourceBundle;

	@Produces
	@MessageResourceBundle
	ResourceBundle getMessageResourceBundle() {
		if (null == messageResourceBundle) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			log.debugv("Getting message resource bundle for locale {0}.",
					facesContext.getViewRoot().getLocale());
			messageResourceBundle = facesContext.getApplication()
					.getResourceBundle(facesContext, "msg");
		}
		return messageResourceBundle;
	}

	private ResourceBundle labelResourceBundle;

	@Inject
	@Client
	private Locale userLocale;

	@Produces
	@LabelResourceBundle
	ResourceBundle getLabelResourceBundle() {
		if (null == labelResourceBundle) {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			facesContext.getViewRoot().setLocale(userLocale);
			log.debugv("Getting label resource bundle for locale {0}.",
					facesContext.getViewRoot().getLocale());
			labelResourceBundle = facesContext.getApplication()
					.getResourceBundle(facesContext, "label");
		}
		return labelResourceBundle;
	}

}
