package com.github.ivos.signboard.config.jsf;

import java.io.IOException;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

public class BootstrapRendererSupport {

	public void addMessageIcon(ResponseWriter writer, FacesMessage message)
			throws IOException {
		writer.startElement("i", null);
		if (FacesMessage.SEVERITY_ERROR == message.getSeverity()
				|| FacesMessage.SEVERITY_FATAL == message.getSeverity()) {
			writer.writeAttribute("class", "icon-exclamation-sign icon-large",
					null);
		}
		if (FacesMessage.SEVERITY_WARN == message.getSeverity()) {
			writer.writeAttribute("class", "icon-warning-sign icon-large", null);
		}
		if (FacesMessage.SEVERITY_INFO == message.getSeverity()) {
			writer.writeAttribute("class", "icon-ok-sign icon-large", null);
		}
		writer.endElement("i");
		writer.write("&#160;");
	}

	public String getMessageStyleClass(FacesMessage message) {
		return "help-inline " + getMessagesStyleClass(message);
	}

	public String getMessagesStyleClass(FacesMessage message) {
		StringBuilder styleClass = new StringBuilder("alert");
		if (FacesMessage.SEVERITY_ERROR == message.getSeverity()
				|| FacesMessage.SEVERITY_FATAL == message.getSeverity()) {
			styleClass.append(" alert-error");
		}
		if (FacesMessage.SEVERITY_INFO == message.getSeverity()) {
			styleClass.append(" alert-success");
		}
		// severity warning is default in Bootstrap
		return styleClass.toString();
	}

	public void writeAdditionalInputAttributes(FacesContext context,
			UIComponent component) throws IOException {
		String[] attributes = { "placeholder" };
		ResponseWriter writer = context.getResponseWriter();
		for (String attribute : attributes) {
			String value = (String) component.getAttributes().get(attribute);
			if (value != null) {
				writer.writeAttribute(attribute, value, attribute);
			}
		}
	}

	// single instance access
	private static final BootstrapRendererSupport instance = new BootstrapRendererSupport();

	public static BootstrapRendererSupport getInstance() {
		return instance;
	}

}
