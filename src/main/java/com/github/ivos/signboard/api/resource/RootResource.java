package com.github.ivos.signboard.api.resource;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import com.github.ivos.signboard.api.model.Link;
import com.github.ivos.signboard.api.model.Service;

@Path("/")
public class RootResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Service> getServices(@Context UriInfo uriInfo) {
		List<Service> services = new ArrayList<Service>();
		services.add(new Service(new Link("users", uriInfo
				.getAbsolutePathBuilder().path(UserResource.class).build())));
		return services;
	}

}
