package net.sf.pms.view;

import org.jboss.seam.faces.rewrite.UrlMapping;
import org.jboss.seam.faces.view.config.ViewConfig;
import org.jboss.seam.faces.view.config.ViewPattern;

@ViewConfig
public interface Pages {

	static enum Config {

		@ViewPattern("/index.xhtml")
		@UrlMapping(pattern = "/")
		home,

		@ViewPattern("/page/user/search.xhtml?page=*")
		@UrlMapping(pattern = "/user/page/#{page}")
		userSearchPaged,

		@ViewPattern("/page/user/search.xhtml")
		@UrlMapping(pattern = "/user")
		userSearch,

		@ViewPattern("/page/user/view.xhtml")
		@UrlMapping(pattern = "/user/#{id}")
		userView,

		@ViewPattern("/page/user/edit.xhtml?id=*")
		@UrlMapping(pattern = "/user/#{id}/edit")
		userEdit,

		@ViewPattern("/page/user/edit.xhtml")
		@UrlMapping(pattern = "/user/create")
		userCreate,

		@ViewPattern("/*")
		all;

	}

}
