package com.github.ivos.signboard.config.security;

import javax.inject.Inject;

import org.jboss.seam.security.annotations.Secures;
import org.jboss.solder.core.Client;

import com.github.ivos.signboard.user.model.SystemRole;
import com.github.ivos.signboard.user.model.User;

public class SecurityCheck {

	@Inject
	@Client
	User clientUser;

	@Secures
	@SystemAdministrator
	public boolean isSystemAdministrator() {
		return (null != clientUser)
				&& clientUser.getSystemRoles().contains(SystemRole.admin);
	}

	@Secures
	@SystemUser
	public boolean isSystemUser() {
		return (null != clientUser)
				&& clientUser.getSystemRoles().contains(SystemRole.user);
	}

}
