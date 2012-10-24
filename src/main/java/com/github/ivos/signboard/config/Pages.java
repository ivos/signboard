package com.github.ivos.signboard.config;

import org.jboss.seam.faces.security.LoginView;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;
import org.jboss.seam.security.annotations.LoggedIn;

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

		@ViewPattern("/*")
		@LoginView("/page/user/login.xhtml")
		all;

	}

}
