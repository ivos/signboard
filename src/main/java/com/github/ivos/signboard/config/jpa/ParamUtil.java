package com.github.ivos.signboard.config.jpa;

public class ParamUtil {

	public static String anywhere(String value) {
		if (null == value) {
			return null;
		}
		return '%' + value + '%';
	}

	public static String asString(Object value) {
		if (null == value) {
			return null;
		}
		return value.toString();
	}

	public static String nonNull(String string) {
		if (null == string) {
			return "";
		}
		return string;
	}

}
