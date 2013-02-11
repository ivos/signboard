package com.github.ivos.signboard.config;

import javax.servlet.ServletContext;

import org.ocpsoft.rewrite.config.Configuration;
import org.ocpsoft.rewrite.config.ConfigurationBuilder;
import org.ocpsoft.rewrite.servlet.config.HttpConfigurationProvider;
import org.ocpsoft.rewrite.servlet.config.rule.Join;

public class URLRewriteConfiguration extends HttpConfigurationProvider {

	private static final String ENTITY_NAME = "[a-zA-Z_0-9]+";
	private static final String ENTITY_ID = "[\\w][\\w\\-]*";

	// private static final String ENTITY_ID = "\\d+";

	@Override
	public Configuration getConfiguration(ServletContext context) {

		return ConfigurationBuilder
				.begin()

				.addRule(
						Join.path("/").to("/page/index.jsf")
								.withInboundCorrection())
				.addRule(
						Join.path("/register").to("/page/user/register.jsf")
								.withInboundCorrection())
				.addRule(
						Join.path("/login").to("/page/user/login.jsf")
								.withInboundCorrection())
				.addRule(
						Join.path("/profile").to("/page/user/profile.jsf")
								.withInboundCorrection())
				.addRule(
						Join.path("/dashboard").to("/page/user/dashboard.jsf")
								.withInboundCorrection())
				.addRule(
						Join.path("/project/create")
								.to("/page/project/create.jsf")
								.withInboundCorrection())
				.addRule(
						Join.path("/project/{projectId}/member/page/{page}")
								.to("/page/projectMember/search.jsf")
								.where("projectId").matches(ENTITY_ID)
								.where("page").matches("\\d+")
								.withInboundCorrection())
				.addRule(
						Join.path("/project/{projectId}/member")
								.to("/page/projectMember/search.jsf")
								.where("projectId").matches(ENTITY_ID)
								.withInboundCorrection())

				.addRule(
						Join.path("/{domain}/page/{page}")
								.to("/page/{domain}/search.jsf")
								.where("domain").matches(ENTITY_NAME)
								.where("page").matches("\\d+")
								.withInboundCorrection())

				.addRule(
						Join.path("/{domain}/{id}/edit")
								.to("/page/{domain}/edit.jsf").where("domain")
								.matches(ENTITY_NAME).where("id")
								.matches(ENTITY_ID).withInboundCorrection())

				.addRule(
						Join.path("/{domain}/create")
								.to("/page/{domain}/edit.jsf").where("domain")
								.matches(ENTITY_NAME).withInboundCorrection())

				.addRule(
						Join.path("/{domain}/{id}")
								.to("/page/{domain}/view.jsf").where("domain")
								.matches(ENTITY_NAME).where("id")
								.matches(ENTITY_ID).withInboundCorrection())

				.addRule(
						Join.path("/{domain}").to("/page/{domain}/search.jsf")
								.where("domain").matches(ENTITY_NAME)
								.withInboundCorrection())

				.addRule(Join.path("/error").to("/page/error.jsf"));
	}

	@Override
	public int priority() {
		return 1;
	}

}
