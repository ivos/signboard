package com.github.ivos.signboard.config;

import org.jboss.seam.faces.security.AccessDeniedView;
import org.jboss.seam.faces.security.LoginView;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;
import org.jboss.seam.security.annotations.LoggedIn;

import com.github.ivos.signboard.config.security.SystemAdministrator;

@ViewConfig
public interface Pages {

	static enum Config {

		@ViewPattern("/page/index.xhtml")
		home,

		@ViewPattern("/page/error.xhtml")
		error,

		@ViewPattern("/page/user/register.xhtml")
		register,

		@ViewPattern("/page/user/login.xhtml")
		login,

		@ViewPattern("/page/project/*")
		@LoggedIn
		projectAll,

		@ViewPattern("/page/user/search.xhtml")
		@SystemAdministrator
		userSearch,

		@ViewPattern("/*")
		@LoginView("/page/user/login.xhtml")
		@AccessDeniedView("/page/access-denied.xhtml")
		all;

	}

}
