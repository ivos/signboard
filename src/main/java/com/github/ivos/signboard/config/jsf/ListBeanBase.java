package com.github.ivos.signboard.config.jsf;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;

public abstract class ListBeanBase<E, C> implements Serializable {

	public ListBeanBase() {
		resetCriteria();
	}

	protected C criteria;

	public abstract void resetCriteria();

	public C getCriteria() {
		return criteria;
	}

	public void setCriteria(C criteria) {
		this.criteria = criteria;
	}

	public String reset() {
		resetCriteria();
		return search();
	}

	public String search() {
		page = 1;
		return "search?faces-redirect=true";
	}

	protected List<E> pageItems;

	public List<E> getPageItems() {
		return pageItems;
	}

	@Inject
	protected EntityManager entityManager;

	@Inject
	protected ViewContext viewContext;

	protected int page = 1;

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = viewContext.coercePage(page, count, getPageSize());
	}

	public int getPageSize() {
		return 10;
	}

	protected long count;

	public long getCount() {
		return count;
	}

	public int getLastPage() {
		return viewContext.calculateLastPage(count, getPageSize());
	}

	private static final long serialVersionUID = 1L;

}
