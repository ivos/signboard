package com.github.ivos.signboard.user.view;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jboss.solder.exception.control.ExceptionHandled;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.config.PersistenceUtil;
import com.github.ivos.signboard.config.security.SystemAdministrator;
import com.github.ivos.signboard.user.model.SystemRole;
import com.github.ivos.signboard.user.model.User;
import com.github.ivos.signboard.user.model.UserStatus;
import com.github.ivos.signboard.view.SelectUtils;
import com.github.ivos.signboard.view.ViewContext;

@Named
@ViewScoped
@ExceptionHandled
public class UserBean implements Serializable {

	@Inject
	private Logger log;

	@Inject
	ViewContext viewContext;

	@Inject
	PersistenceUtil persistenceUtil;

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
		user.setStatus(UserStatus.active);
		boolean isFirstUserInSystem = persistenceUtil.findAll(User.class, 0, 1)
				.isEmpty();

		entityManager.persist(user);

		if (isFirstUserInSystem) {
			user.getSystemRoles().add(SystemRole.admin);
		} else {
			user.getSystemRoles().add(SystemRole.user);
		}
		viewContext.info("saved");
		log.infov("Register user {0}.", user.toLog());
		return "login?faces-redirect=true";
	}

	@SystemAdministrator
	public String disable() {
		user.setStatus(UserStatus.disabled);
		return update();
	}

	@SystemAdministrator
	public String activate() {
		user.setStatus(UserStatus.active);
		return update();
	}

	public boolean isActive() {
		return UserStatus.active.equals(getUser().getStatus());
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

	@SystemAdministrator
	public void retrieve() {
		if (FacesContext.getCurrentInstance().isPostback()) {
			return;
		}
		if (id != null) {
			log.debugv("Retrieve user by id {0}.", id);
			user = entityManager.find(User.class, id);
		}
	}

	/*
	 * Support updating and deleting User entities
	 */

	@SystemAdministrator
	public String update() {
		// if (id == null) {} else {}
		log.infov("Update user {0}.", user.toLog());
		user = entityManager.merge(user);
		viewContext.info("saved");
		return "view?faces-redirect=true&id=" + user.getId();
	}

	public String delete() {
		log.infov("Delete user {0}.", user.toLog());
		user = entityManager.merge(user);
		entityManager.remove(user);
		entityManager.flush();
		return "search?faces-redirect=true";
	}

	// Select options

	@Inject
	SelectUtils selectUtils;

	public Collection<String> getSystemRoles() {
		return selectUtils.convertToStrings(getUser().getSystemRoles());
	}

	public void setSystemRoles(Collection<String> systemRoles) {
		getUser().setSystemRoles(
				selectUtils.convertToEnumSet(systemRoles, SystemRole.class));
	}

	public List<SelectItem> getSystemRoles__Options() {
		return selectUtils.convertToSelectItems(SystemRole.class);
	}

	public List<SelectItem> getStatus__Options() {
		return selectUtils.convertToSelectItems(UserStatus.class);
	}

}
