package com.github.ivos.signboard.api.model;

import java.util.List;

public class Users {

	public List<Link> links;
	public List<User> users;

	public Users() {
	}

	public Users(List<User> users, List<Link> links) {
		this.users = users;
		this.links = links;
	}

}
