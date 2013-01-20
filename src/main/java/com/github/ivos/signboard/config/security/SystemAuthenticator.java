package com.github.ivos.signboard.config.security;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import org.jboss.seam.security.BaseAuthenticator;
import org.jboss.solder.logging.Logger;
import org.picketlink.idm.impl.api.model.SimpleUser;

import com.github.ivos.signboard.user.model.User;
import com.github.ivos.signboard.user.view.LoginBean;
import com.github.ivos.signboard.user.view.UserBean;

public class SystemAuthenticator extends BaseAuthenticator {

	@Inject
	private EntityManager entityManager;

	@Inject
	UserBean userBean;

	@Inject
	LoginBean loginBean;

	@Inject
	Logger log;

	@Override
	public void authenticate() {
		User loginUser = userBean.getUser();
		log.debugv("Authenticate {0}.", loginUser.getEmail());
		try {
			User userFound = entityManager
					.createQuery(
							"select user from User user where user.email=:email "
									+ "and user.password=:password "
									+ "and user.status='active'", User.class)
					.setParameter("email", loginUser.getEmail())
					.setParameter("password", loginUser.getPassword())
					.getSingleResult();
			setStatus(AuthenticationStatus.SUCCESS);
			setUser(new SimpleUser(loginUser.getEmail()));
			loginBean.setUser(userFound);
		} catch (NoResultException e) {
			setStatus(AuthenticationStatus.FAILURE);
		}
	}

}
