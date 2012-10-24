package com.github.ivos.signboard.config.jsf;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.html_basic.TextRenderer;

public class BootstrapInputRenderer extends TextRenderer {

	@Override
	protected void getEndTextToRender(FacesContext context,
			UIComponent component, String currentValue) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		UIComponent prependFacet = getFacet(component, "prepend");
		if (null != prependFacet) {
			writer.startElement("div", null);
			writer.writeAttribute("class", "input-prepend", null);
			writer.startElement("span", null);
			writeInputAddon(context, writer, prependFacet);
		}

		writeAdditionalInputAttributes(context, component);
		super.getEndTextToRender(context, component, currentValue);

		if (null != prependFacet) {
			writer.endElement("div");
		}
	}

	private void writeInputAddon(FacesContext context, ResponseWriter writer,
			UIComponent facet) throws IOException {
		writer.writeAttribute("class", "add-on", null);
		encodeRecursive(context, facet);
		writer.endElement("span");
	}

	private void writeAdditionalInputAttributes(FacesContext context,
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

}
