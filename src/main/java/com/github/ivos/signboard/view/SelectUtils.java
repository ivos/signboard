package com.github.ivos.signboard.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.faces.model.SelectItem;
import javax.inject.Inject;

public class SelectUtils implements Serializable {

	@Inject
	ViewContext viewContext;

	public <E extends Enum<E>> List<SelectItem> convertToSelectItems(
			Class<E> enumClass) {
		List<SelectItem> list = new ArrayList<SelectItem>();
		Enum<?>[] enums = enumClass.getEnumConstants();
		for (Enum<?> e : enums) {
			String code = "enum_" + enumClass.getSimpleName() + "_"
					+ e.toString();
			list.add(new SelectItem(e, viewContext.getLabel(code)));
		}
		return list;
	}

	public <E extends Enum<E>> Collection<String> convertToStrings(
			Collection<E> enumCollection) {
		Collection<String> collection = new ArrayList<String>();
		for (E e : enumCollection) {
			collection.add(e.toString());
		}
		return collection;
	}

	public <E extends Enum<E>> Set<E> convertToEnumSet(
			Collection<String> stringCollection, Class<E> enumClass) {
		Set<E> set = new HashSet<E>();
		for (String string : stringCollection) {
			set.add(Enum.valueOf(enumClass, string));
		}
		return set;
	}

	public void setViewContext(ViewContext viewContext) {
		this.viewContext = viewContext;
	}

	private static final long serialVersionUID = 1L;

}
