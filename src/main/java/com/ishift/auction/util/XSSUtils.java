package com.ishift.auction.util;

import org.apache.commons.lang.StringUtils;

public class XSSUtils {
	
	public static byte[] fnXSSEncode(byte[] data) {
		String strData = new String(data);
		return strData.replaceAll("\\<", "&lt;")
				.replaceAll("\\>", "&gt;")
				.replaceAll("\\(", "&#40;")
				.replaceAll("\\)", "&#41;")
				.getBytes();
	}
	
	public static String fnXSSEncode(String value) {
		if (StringUtils.isEmpty(value)) return "";
		return value.replaceAll("\\<", "&lt;")
				.replaceAll("\\>", "&gt;")
				.replaceAll("\\(", "&#40;")
				.replaceAll("\\)", "&#41;");
	}

}
