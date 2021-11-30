package com.ishift.auction.base.utils;

public class StringUtils<T> {
	
	public static String getStringValue(Object obj) {
		return obj == null ? "" : obj.toString();
	}
	
	public static int getIntValue(Object obj) {
		try {
			return obj == null ? 0 : Integer.parseInt(getStringValue(obj));
		}
		catch(NumberFormatException e) {
			return 0;
		}
	}

	/**
	 * 문자열 Null, Empty, Length 유효성 확인 함수
	 * @param str 확인 문자열
	 * @return boolean true : 유효 문자, false : 무효 문자
	 */
	public static boolean isValidString(String str) {
		if (str == null || str.equals("") || str.isEmpty()) {
			return false;
		}

		if (str.trim().length() <= 0) {
			return false;
		}

		return true;
	}
	
	/**
	 * 출하 지역명 가져오기
	 * @param rgnNm
	 * @return
	 */
	public static String getRgnName(Object rgnNm) {
		if (rgnNm == null) return "";
		try {
			String[] splitAddr = rgnNm.toString().split("\\s+");
			if (splitAddr.length > 2) {
				String tmpAddr = splitAddr[2].trim();
				if (tmpAddr.length() == 3) {
					String subAddr = tmpAddr.substring(0, 2);
					if (isValidString(subAddr)) {
						return subAddr;
					}
					else {
						return "";
					}
				}
				else {
					return tmpAddr;
				}
			}
			else {
				return splitAddr[0];
			}
		}catch (RuntimeException re) {
			return "";
		}
	}
}
