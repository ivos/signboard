package com.github.ivos.signboard.user.view;

import javax.faces.component.UIComponent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.seam.faces.conversion.Converter;

import com.github.ivos.signboard.user.model.User;

@Named
public class UserConverter extends Converter<User> {

	@Inject
	private EntityManager entityManager;

	@Override
	public User toObject(UIComponent comp, String value) {
		return entityManager.find(User.class, Long.valueOf(value));
	}

	@Override
	public String toString(UIComponent comp, User value) {
		if (null == value || null == value.getId()) {
			return "";
		}
		return value.getId().toString();
	}

}
