package net.sf.pms.user.view;

import java.io.Serializable;

import javax.ejb.Stateful;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import net.sf.pms.user.model.User;

@Named
@Stateful
@ViewScoped
public class UserBean implements Serializable {

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
		try {
			if (id == null) {
				entityManager.persist(user);
				return "edit?faces-redirect=true&id=" + user.getId();
			} else {
				entityManager.merge(user);
				return "edit?faces-redirect=true&id=" + user.getId();
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			e.printStackTrace();
			return null;
		}
	}

	public String delete() {
		try {
			user = entityManager.merge(user);
			entityManager.remove(user);
			entityManager.flush();
			return "search?faces-redirect=true";
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(e.getMessage()));
			e.printStackTrace();
			return null;
		}
	}

}