package com.github.ivos.signboard.config.jsf;

import java.util.Locale;

import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.solder.core.Client;
import org.jboss.solder.logging.Logger;

public class DateFormatPatternProducer {

	@Inject
	Logger log;

	@Inject
	@Client
	private Locale clientLocale;

	@Produces
	@Named
	public String getDateFormatPattern() {
		String pattern;
		if (clientLocale.toString().startsWith("cs")) {
			pattern = "d.M.yyyy";
		} else {
			pattern = "MM/dd/yyyy";
		}
		log.debugv("Date format pattern {0}.", pattern);
		return pattern;
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
