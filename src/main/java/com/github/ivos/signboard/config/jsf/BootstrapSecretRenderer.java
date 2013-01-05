package com.github.ivos.signboard.config.jsf;

import java.io.IOException;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import com.sun.faces.renderkit.Attribute;
import com.sun.faces.renderkit.AttributeManager;
import com.sun.faces.renderkit.RenderKitUtils;
import com.sun.faces.renderkit.html_basic.SecretRenderer;

public class BootstrapSecretRenderer extends SecretRenderer {

	protected static final Attribute[] ATTRIBUTES = AttributeManager
			.getAttributes(AttributeManager.Key.INPUTSECRET);

	@Override
	protected void getEndTextToRender(FacesContext context,
			UIComponent component, String currentValue) throws IOException {

		ResponseWriter writer = context.getResponseWriter();
		assert (writer != null);

		// Bootstrap: prepend
		UIComponent prependFacet = getFacet(component, "prepend");
		if (null != prependFacet) {
			writer.startElement("div", null);
			writer.writeAttribute("class", "input-prepend", null);
			writeInputAddon(context, writer, prependFacet);
		}

		String redisplay = String.valueOf(component.getAttributes().get(
				"redisplay"));
		if (redisplay == null || !redisplay.equals("true")) {
			currentValue = "";
		}

		writer.startElement("input", component);
		writeIdAttributeIfNecessary(context, writer, component);
		writer.writeAttribute("type", "password", "type");
		writer.writeAttribute("name", component.getClientId(context),
				"clientId");

		String autoComplete = (String) component.getAttributes().get(
				"autocomplete");
		if (autoComplete != null) {
			// only output the autocomplete attribute if the value
			// is 'off' since its lack of presence will be interpreted
			// as 'on' by the browser
			if ("off".equals(autoComplete)) {
				writer.writeAttribute("autocomplete", "off", "autocomplete");
			}
		}

		// render default text specified
		if (currentValue != null) {
			writer.writeAttribute("value", currentValue, "value");
		}

		RenderKitUtils.renderPassThruAttributes(context, writer, component,
				ATTRIBUTES, getNonOnChangeBehaviors(component));
		RenderKitUtils.renderXHTMLStyleBooleanAttributes(writer, component);

		RenderKitUtils.renderOnchange(context, component, false);

		String styleClass;
		if (null != (styleClass = (String) component.getAttributes().get(
				"styleClass"))) {
			writer.writeAttribute("class", styleClass, "styleClass");
		}

		// Bootstrap: additional attributes
		rendererSupport.writeAdditionalInputAttributes(context, component);

		writer.endElement("input");

		// Bootstrap: prepend
		if (null != prependFacet) {
			writer.endElement("div");
		}

	}

	private void writeInputAddon(FacesContext context, ResponseWriter writer,
			UIComponent facet) throws IOException {
		writer.startElement("span", null);
		writer.writeAttribute("class", "add-on", null);
		encodeRecursive(context, facet);
		writer.endElement("span");
	}

	// Bootstrap support instance
	private final BootstrapRendererSupport rendererSupport = BootstrapRendererSupport
			.getInstance();

}
