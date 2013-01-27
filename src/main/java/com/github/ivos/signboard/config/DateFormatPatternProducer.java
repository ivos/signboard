package com.github.ivos.signboard.config;

import java.util.Locale;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.solder.core.Client;

public class DateFormatPatternProducer {

	@Inject
	@Client
	private Locale clientLocale;

	@Produces
	@Named
	public String getDateFormatPattern() {
		if (clientLocale.toString().startsWith("cs")) {
			return "d.M.yyyy";
		}
		return "MM/dd/yyyy";
	}

	@Produces
	@Named
	public String getDateFormatPatternLabel() {
		if (clientLocale.toString().startsWith("cs")) {
			return "D.M.RRRR";
		}
		return "MM/DD/YYYY";
	}

}
