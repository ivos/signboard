package com.github.ivos.signboard.config.security;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.seam.security.BaseAuthenticator;
import org.picketlink.idm.impl.api.model.SimpleUser;

import com.github.ivos.signboard.user.view.UserBean;

public class SystemAuthenticator extends BaseAuthenticator {

	@Inject
	private EntityManager entityManager;

	@Inject
	UserBean userBean;

	@Override
	public void authenticate() {
		try {
			entityManager
					.createQuery(
							"select user.id from User user where user.email=:email "
									+ "and user.password=:password", Long.class)
					.setParameter("email", userBean.getUser().getEmail())
					.setParameter("password", userBean.getUser().getPassword())
					.getSingleResult();
			setStatus(AuthenticationStatus.SUCCESS);
			setUser(new SimpleUser(userBean.getUser().getEmail()));
		} catch (NoResultException e) {
			setStatus(AuthenticationStatus.FAILURE);
		}
	}

}
