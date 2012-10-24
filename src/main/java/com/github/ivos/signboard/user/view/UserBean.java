package com.github.ivos.signboard.user.view;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.seam.security.Identity;
import org.jboss.solder.exception.control.ExceptionHandled;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.user.model.User;
import com.github.ivos.signboard.view.ViewContext;

@Named
@ViewScoped
@ExceptionHandled
public class UserBean implements Serializable {

	@Inject
	private Logger log;

	@Inject
	ViewContext viewContext;

	@NotNull
	@Size(min = 4, max = 100)
	private String confirmPassword;

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String register() {
		user.digestPassword();
		entityManager.persist(user);
		viewContext.info("saved");
		log.infov("Register user {0}.", user.toLog());
		return "login?faces-redirect=true";
	}

	@Inject
	Identity identity;

	public String login() {
		user.digestPassword();
		if (identity.login() == Identity.RESPONSE_LOGIN_SUCCESS) {
			log.infov("Log in user {0}.", user.getEmail());
			return "search?faces-redirect=true";
		}
		viewContext.error("login.invalid");
		log.warnv("Login invalid for user {0}.", user.getEmail());
		return null;
	}

	public String logout() {
		log.infov("Log out user {0}.", identity.getUser().getId());
		identity.logout();
		return "/index?faces-redirect=true";
	}

	// generated:

	private static final long serialVersionUID = 1L;

	/*
	 * Support creating and retrieving User entities
	 */

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	private User user;

	public User getUser() {
		if (null == user) {
			user = new User();
		}
		return user;
	}

	@Inject
	private EntityManager entityManager;

	public void retrieve() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (id != null) {
			user = entityManager.find(User.class, id);
		}
	}

	/*
	 * Support updating and deleting User entities
	 */

	public String update() {
		// if (id == null) {} else {}
		entityManager.merge(user);
		viewContext.info("saved");
		return "edit?faces-redirect=true&id=" + user.getId();
	}

	public String delete() {
		user = entityManager.merge(user);
		entityManager.remove(user);
		entityManager.flush();
		return "search?faces-redirect=true";
	}

}