package com.ishift.auction.base.utils;

public class StringUtils<T> {
	
	public static String getStringValue(Object obj) {
		return obj == null ? "" : obj.toString();
	}

}
