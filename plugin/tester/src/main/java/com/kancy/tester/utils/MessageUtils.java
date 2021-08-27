package com.kancy.tester.utils;

import java.util.ResourceBundle;

/**
 * @author kancy
 * @date 2019/6/29 18:46
 */
public class MessageUtils {
	private static ResourceBundle resourceBundle;
	static {
		resourceBundle = ResourceBundle.getBundle("message");
	}

	public static String getResString(String key){
		return getResString(key,"");
	}
	public static String getResString(String key, String def){
		String bundleString = resourceBundle.getString(key);
		if (bundleString == null || bundleString.isEmpty()){
			return def;
		}
		return bundleString;
	}

}
