package com.github.ivos.signboard.user.view;

import java.io.Serializable;

import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.security.Identity;
import org.jboss.solder.core.Client;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.user.model.User;
import com.github.ivos.signboard.view.ViewContext;

@Named
@SessionScoped
public class LoginBean implements Serializable {

	@Inject
	private Logger log;

	@Inject
	ViewContext viewContext;

	private User user;

	@Inject
	Identity identity;

	@Inject
	UserBean userBean;

	public String login() {
		userBean.getUser().digestPassword();
		if (identity.login() == Identity.RESPONSE_LOGIN_SUCCESS) {
			user = userBean.getUser();
			log.infov("Log in user {0}.", userBean.getUser().getEmail());
			return "search?faces-redirect=true";
		}
		viewContext.error("login.failure");
		log.warnv("Login failure for user {0}.", userBean.getUser().getEmail());
		return null;
	}

	public String logout() {
		log.infov("Log out user {0}.", identity.getUser().getId());
		identity.logout();
		return "/index?faces-redirect=true";
	}

	@Produces
	@Client
	public User getUser() {
		return user;
	}

	private static final long serialVersionUID = 1L;

}
