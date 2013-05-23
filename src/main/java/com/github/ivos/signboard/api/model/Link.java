package com.github.ivos.signboard.api.model;

import java.net.URI;

public class Link {

	public String rel;
	public URI href;
	public String type;

	public Link() {
	}

	public Link(String rel, URI href) {
		this.rel = rel;
		this.href = href;
		this.type = "application/json";
	}

	public Link(String rel, URI href, String type) {
		this.rel = rel;
		this.href = href;
		this.type = type;
	}

}
