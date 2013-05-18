package com.github.ivos.signboard.config.jsf;

import javax.faces.component.UIComponent;
import javax.faces.convert.ConverterException;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.seam.faces.conversion.Converter;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

@Named
public class DurationConverter extends Converter<Long> {

	@Override
	public Long toObject(UIComponent comp, String value) {
		if (null == value || value.isEmpty()) {
			return null;
		}
		try {
			long result;
			value = value.replace(',', '.');
			value = value.replace(' ', ':');
			final int colon = value.indexOf(':');
			if (colon > -1) {
				result = parseWithMinutes(value, colon);
			} else {
				result = parseDecimal(value);
			}
			if (result >= 1000L * 60 * 60 * 24) {
				throw new ConverterException(
						viewContext.createError("duration.exceeds.limit"));
			}
			return result;
		} catch (NumberFormatException nfe) {
			throw new ConverterException(
					viewContext.createError("invalid.duration"));
		}
	}

	private long parseWithMinutes(String value, int colon) {
		final String hoursPart = value.substring(0, colon);
		int hours = 0;
		if (!hoursPart.isEmpty()) {
			hours = Integer.valueOf(hoursPart);
		}
		final String minutesPart = value.substring(colon + 1);
		int minutes = 0;
		if (!minutesPart.isEmpty()) {
			minutes = Integer.valueOf(minutesPart);
		}
		return 1000L * 60 * (minutes + (60 * hours));
	}

	private long parseDecimal(String value) {
		final Double decimalHours = Double.valueOf(value);
		return (long) (1000L * decimalHours * 60 * 60);
	}

	@Override
	public String toString(UIComponent comp, Long value) {
		if (null == value) {
			return "";
		}
		return PRINT_FORMAT.print(value);
	}

	private static final DateTimeFormatter PRINT_FORMAT = DateTimeFormat
			.forPattern("H:mm").withZoneUTC();

	@Inject
	private ViewContext viewContext;

	public void setViewContext(ViewContext viewContext) {
		this.viewContext = viewContext;
	}

}
