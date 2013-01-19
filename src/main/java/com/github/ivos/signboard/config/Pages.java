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

		// user

		@ViewPattern("/page/user/register.xhtml")
		register,

		@ViewPattern("/page/user/login.xhtml")
		login,

		@ViewPattern("/page/user/search.xhtml")
		@SystemAdministrator
		userSearch,

		@ViewPattern("/page/user/view.xhtml")
		@SystemAdministrator
		userView,

		@ViewPattern("/page/user/edit.xhtml")
		@SystemAdministrator
		userEdit,

		// project

		@ViewPattern("/page/project/*")
		@LoggedIn
		projectAll,

		// common

		@ViewPattern("/*")
		@LoginView("/page/user/login.xhtml")
		@AccessDeniedView("/page/access-denied.xhtml")
		all;

	}

}
