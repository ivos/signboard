package com.github.ivos.signboard.config.jsf;

import org.jboss.seam.faces.security.AccessDeniedView;
import org.jboss.seam.faces.security.LoginView;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;
import org.jboss.seam.security.annotations.LoggedIn;

import com.github.ivos.signboard.config.security.ActiveProjectAdministrator;
import com.github.ivos.signboard.config.security.ActiveProjectMember;
import com.github.ivos.signboard.config.security.ActiveProjectUserByTask;
import com.github.ivos.signboard.config.security.ActiveProjectUserByWorkRecord;
import com.github.ivos.signboard.config.security.SystemAdministrator;
import com.github.ivos.signboard.config.security.SystemUser;

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

		@ViewPattern("/page/user/dashboard.xhtml")
		@LoggedIn
		dashboard,

		@ViewPattern("/page/user/profile.xhtml")
		@LoggedIn
		profile,

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

		@ViewPattern("/page/project/edit.xhtml")
		@LoggedIn
		@ActiveProjectAdministrator
		projectEdit,

		@ViewPattern("/page/project/*")
		@LoggedIn
		@SystemUser
		projectAll,

		// project member

		@ViewPattern("/page/projectMember/edit.xhtml")
		@LoggedIn
		@SystemUser
		@ActiveProjectAdministrator
		projectMemberEdit,

		@ViewPattern("/page/projectMember/*")
		@LoggedIn
		@SystemUser
		@ActiveProjectMember
		projectMemberAll,

		// task

		@ViewPattern("/page/task/*")
		@LoggedIn
		@SystemUser
		taskAll,

		@ViewPattern("/page/task/view.xhtml")
		@LoggedIn
		@SystemUser
		@ActiveProjectUserByTask
		taskView,

		@ViewPattern("/page/task/edit.xhtml")
		@LoggedIn
		@SystemUser
		@ActiveProjectUserByTask
		taskEdit,

		// work record

		@ViewPattern("/page/workRecord/create.xhtml")
		@LoggedIn
		@SystemUser
		@ActiveProjectUserByWorkRecord
		workRecordCreate,

		// common

		@ViewPattern("/*")
		@LoginView("/page/user/login.xhtml")
		@AccessDeniedView("/page/access-denied.xhtml")
		all;

	}

}
