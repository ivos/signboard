package com.github.ivos.signboard.cdi.producer;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.solder.core.Client;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.cdi.qualifier.LabelResourceBundle;
import com.github.ivos.signboard.cdi.qualifier.MessageResourceBundle;

@RequestScoped
public class ResourceBundleProducer {

	@Inject
	private Logger log;

	@Inject
	private FacesContext facesContext;

	private ResourceBundle messageResourceBundle;

	@Produces
	@MessageResourceBundle
	ResourceBundle getMessageResourceBundle() {
		if (null == messageResourceBundle) {
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
			facesContext.getViewRoot().setLocale(userLocale);
			log.debugv("Getting label resource bundle for locale {0}.",
					facesContext.getViewRoot().getLocale());
			labelResourceBundle = facesContext.getApplication()
					.getResourceBundle(facesContext, "label");
		}
		return labelResourceBundle;
	}

}
