package com.github.ivos.signboard.cdi.producer;

import java.util.ResourceBundle;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import com.github.ivos.signboard.cdi.qualifier.LabelResourceBundle;
import com.github.ivos.signboard.cdi.qualifier.MessageResourceBundle;

@RequestScoped
public class ResourceBundleProducer {

	@Inject
	private FacesContext facesContext;

	private ResourceBundle messageResourceBundle;

	@Produces
	@MessageResourceBundle
	ResourceBundle getMessageResourceBundle() {
		if (null == messageResourceBundle) {
			messageResourceBundle = facesContext.getApplication()
					.getResourceBundle(facesContext, "msg");
		}
		return messageResourceBundle;
	}

	private ResourceBundle labelResourceBundle;

	@Produces
	@LabelResourceBundle
	ResourceBundle getLabelResourceBundle() {
		if (null == labelResourceBundle) {
			labelResourceBundle = facesContext.getApplication()
					.getResourceBundle(facesContext, "label");
		}
		return labelResourceBundle;
	}

}
