package com.github.ivos.signboard.project.view;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jboss.seam.remoting.annotations.WebRemote;
import org.jboss.solder.logging.Logger;

import com.github.ivos.signboard.project.model.Project;

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
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<String> listCriteria = builder.createQuery(String.class);
		Root<Project> root = listCriteria.from(Project.class);

		TypedQuery<String> query = entityManager.createQuery(listCriteria
				.select(root.<String> get(property)).distinct(true)
				.where(listBean.getSearchPredicates(root)));
		query.setMaxResults(getItemsCount());
		List<String> items = query.getResultList();

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
