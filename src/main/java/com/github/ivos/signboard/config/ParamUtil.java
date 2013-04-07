package com.github.ivos.signboard.config;

public class ParamUtil {

	public static String anywhere(String value) {
		if (null == value) {
			return null;
		}
		return '%' + value + '%';
	}

}
