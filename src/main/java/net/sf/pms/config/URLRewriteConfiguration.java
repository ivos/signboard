package net.sf.pms.config;

import javax.servlet.ServletContext;

import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

public class URLRewriteConfiguration extends HttpConfigurationProvider {

	private static final String ENTITY_NAME = "[a-zA-Z$_0-9]+";

	@Override
	public Configuration getConfiguration(ServletContext context) {

		return ConfigurationBuilder
				.begin()

				.addRule(Join.path("/").to("/index.jsf"))

				.addRule(Join.path("/register").to("/page/user/register.jsf"))

				.addRule(
						Join.path("/{domain}/page/{page}").where("domain")
								.matches(ENTITY_NAME).where("page")
								.matches("\\d+")
								.to("/page/{domain}/search.jsf")
								.withInboundCorrection())

				.addRule(
						Join.path("/{domain}").where("domain")
								.matches(ENTITY_NAME)
								.to("/page/{domain}/search.jsf")
								.withInboundCorrection())

				.addRule(
						Join.path("/{domain}/{id}").where("domain")
								.matches(ENTITY_NAME).where("id")
								.matches("\\d+").to("/page/{domain}/view.jsf")
								.withInboundCorrection())

				.addRule(
						Join.path("/{domain}/{id}/edit").where("domain")
								.matches(ENTITY_NAME).where("id")
								.matches("\\d+").to("/page/{domain}/edit.jsf")
								.withInboundCorrection())

				.addRule(
						Join.path("/{domain}/create").where("domain")
								.matches(ENTITY_NAME)
								.to("/page/{domain}/edit.jsf")
								.withInboundCorrection())

				.addRule(Join.path("/error").to("/error.jsf"));
	}

	@Override
	public int priority() {
		return 1;
	}

}
