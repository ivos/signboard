package com.github.ivos.signboard.view;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;

import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.BundleKey;

import com.github.ivos.signboard.cdi.qualifier.MessageResourceBundle;

@Named
public class ViewContext implements Serializable {

	@Inject
	private FacesContext facesContext;

	@Inject
	@MessageResourceBundle
	ResourceBundle msg;

	@Inject
	Messages messages;

	private final static String MESSAGE_BUNDLE = "i18n.msg";

	public FacesMessage createError(String messageCode) {
		return new FacesMessage(FacesMessage.SEVERITY_ERROR,
				msg.getString(messageCode), null);
	}

	public void error(String messageCode) {
		messages.error(new BundleKey(MESSAGE_BUNDLE, messageCode));
	}

	public void error(String elementId, String messageCode) {
		facesContext.addMessage(elementId, new FacesMessage(
				FacesMessage.SEVERITY_ERROR, msg.getString(messageCode), null));
	}

	public void info(String messageCode) {
		messages.info(new BundleKey(MESSAGE_BUNDLE, messageCode));
	}

	public void info(String elementId, String messageCode) {
		facesContext.addMessage(elementId, new FacesMessage(
				FacesMessage.SEVERITY_INFO, msg.getString(messageCode), null));
	}

	@Inject
	ServletContext servletContext;

	public String getApplicationName() {
		// must be available outside faces
		String contextPath = servletContext.getContextPath();
		if (contextPath.startsWith("/")) {
			contextPath = contextPath.substring(1);
		}
		return contextPath;
	}

	private static final long serialVersionUID = 1L;

}
