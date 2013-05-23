package com.github.ivos.signboard.api.resource;

import static com.github.ivos.signboard.config.jpa.ParamUtil.*;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.github.ivos.signboard.api.Constants;
import com.github.ivos.signboard.api.model.Link;
import com.github.ivos.signboard.api.model.User;
import com.github.ivos.signboard.api.model.Users;

@Path("/user")
public class UserResource implements Constants {

	@Inject
	EntityManager entityManager;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Users getCustomers(@Context UriInfo uriInfo,
			@QueryParam("query") String query, @QueryParam("page") int page) {
		List<User> users = new ArrayList<User>();

		String ql = " from User r " + " where :query is null"
				+ " or r.email like :query" + " or r.firstName like :query"
				+ " or r.lastName like :query" + " or r.phone like :query";

		Long count = (Long) entityManager.createQuery("select count(r)" + ql)
				.setParameter("query", anywhere(query)).getSingleResult();

		@SuppressWarnings("unchecked")
		List<com.github.ivos.signboard.user.model.User> items = entityManager
				.createQuery(
						"select r" + ql
								+ " order by r.lastName, r.firstName, r.id")
				.setParameter("query", anywhere(query))
				.setFirstResult(page * PAGE_SIZE).setMaxResults(PAGE_SIZE)
				.getResultList();

		for (com.github.ivos.signboard.user.model.User item : items) {
			users.add(new User(item));
		}

		List<Link> links = new ArrayList<Link>();
		if (page > 0) {
			links.add(new Link("previous", uriInfo.getAbsolutePathBuilder()
					.queryParam("query", nonNull(query))
					.queryParam("page", page - 1).build()));
		}
		if (count > (page + 1) * PAGE_SIZE) {
			links.add(new Link("next", uriInfo.getAbsolutePathBuilder()
					.queryParam("query", nonNull(query))
					.queryParam("page", page + 1).build()));
		}

		return new Users(users, links);
	}

}
