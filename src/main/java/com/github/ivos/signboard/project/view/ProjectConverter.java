package com.github.ivos.signboard.project.view;

import javax.faces.component.UIComponent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import org.jboss.seam.faces.conversion.Converter;

import com.github.ivos.signboard.project.model.Project;

@Named
public class ProjectConverter extends Converter<Project> {

	@Inject
	private EntityManager entityManager;

	@Override
	public Project toObject(UIComponent comp, String value) {
		return entityManager.find(Project.class, value);
	}

	@Override
	public String toString(UIComponent comp, Project value) {
		if (null == value || null == value.getId()) {
			return "";
		}
		return value.getId().toString();
	}

}
