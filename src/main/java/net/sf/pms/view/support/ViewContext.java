package net.sf.pms.view.support;

import java.io.Serializable;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import net.sf.pms.cdi.qualifier.MessageResourceBundle;

public class ViewContext implements Serializable {

	@Inject
	private FacesContext facesContext;

	@Inject
	@MessageResourceBundle
	ResourceBundle msg;

	public void addErrorMessage(String messageCode) {
		addErrorMessage(null, messageCode);
	}

	public void addErrorMessage(String elementId, String messageCode) {
		facesContext.addMessage(elementId, new FacesMessage(
				FacesMessage.SEVERITY_ERROR, msg.getString(messageCode), null));
	}

	public void addInfoMessage(String messageCode) {
		addInfoMessage(null, messageCode);
	}

	public void addInfoMessage(String elementId, String messageCode) {
		facesContext.addMessage(elementId, new FacesMessage(
				FacesMessage.SEVERITY_INFO, msg.getString(messageCode), null));
	}

	private static final long serialVersionUID = 1L;

}
