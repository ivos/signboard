package net.sf.pms.user.view;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;

import net.sf.pms.user.model.User;
import net.sf.pms.view.support.ViewContext;

import org.jboss.solder.exception.control.ExceptionHandled;

@Named
@Stateful
@ViewScoped
@ExceptionHandled
public class UserBean implements Serializable {

	@Inject
	ViewContext viewContext;

	@NotNull
	private String confirmPassword;

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String register() {
		if (!user.getPassword().equals(confirmPassword)) {
			viewContext.error("edit:confirmPassword", "passwords.must.match");
			return null;
		}
		return update();
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
		if (id == null) {
			entityManager.persist(user);
			viewContext.info("saved");
			return "edit?faces-redirect=true&id=" + user.getId();
		} else {
			entityManager.merge(user);
			viewContext.info("saved");
			return "edit?faces-redirect=true&id=" + user.getId();
		}
	}

	public String delete() {
		user = entityManager.merge(user);
		entityManager.remove(user);
		entityManager.flush();
		return "search?faces-redirect=true";
	}

}