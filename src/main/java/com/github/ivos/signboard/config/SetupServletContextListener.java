package com.github.ivos.signboard.config;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class SetupServletContextListener implements ServletContextListener {

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		saveEmptyNumericalFieldsAsNull();
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
	}

	private void saveEmptyNumericalFieldsAsNull() {
		System.setProperty("org.apache.el.parser.COERCE_TO_ZERO", "false");
	}

}
