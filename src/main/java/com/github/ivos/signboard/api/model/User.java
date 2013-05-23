package com.github.ivos.signboard.api.model;

public class User {

	public Long id;
	public String name;

	public User() {
	}

	public User(com.github.ivos.signboard.user.model.User user) {
		this.id = user.getId();
		this.name = user.getDistinctiveFullName();
	}

}
