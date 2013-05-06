package com.github.ivos.signboard.project.view;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.jboss.seam.remoting.annotations.WebRemote;
import org.jboss.solder.logging.Logger;

@Named
public class ProjectRemoting {

	@Inject
	private Logger log;

	@Inject
	private EntityManager entityManager;

	@Inject
	ProjectListBean listBean;

	// typeahead

	public int getItemsCount() {
		return 6;
	}

	public String[] selectItems(String property) {
		TypedQuery<String> query = entityManager.createQuery(
				"select distinct(r." + property + ") " + listBean.getQuery()
						+ " order by r." + property, String.class);
		List<String> items = listBean.setParameters(query)
				.setMaxResults(getItemsCount()).getResultList();

		log.debugv("Property " + property
				+ " search with values {0} produces items {1}.",
				listBean.getCriteria(), items);

		return items.toArray(new String[items.size()]);
	}

	@WebRemote
	public String[] name_Typeahead(String value) {
		listBean.getCriteria().setName(value);
		return selectItems("name");
	}

	@WebRemote
	public String[] code_Typeahead(String value) {
		listBean.getCriteria().setCode(value);
		return selectItems("code");
	}

}
