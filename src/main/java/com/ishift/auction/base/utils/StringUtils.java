package com.ishift.auction.base.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

public class StringUtils<T> {
	
	private static Logger log = LoggerFactory.getLogger(StringUtils.class);
	
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
	
	/**
	 * blobToBytes
	 * Blob를 Byte로 변환
	 * @return byte
	 */
	
	public static byte[] blobToBytes(Blob blob) {
		BufferedInputStream is = null;
		byte[] bytes = null;
		try {
			is = new BufferedInputStream(blob.getBinaryStream());
			bytes = new byte[(int)blob.length()];
			int len = bytes.length;
			int offset = 0;
			int read = 0;
			
			while(offset < len && ( read = is.read(bytes, offset, len - offset)) >= 0) {
				offset += read;
			}
		}
		catch(RuntimeException | IOException | SQLException e) {
			log.info("동작중 오류가 발생하였습니다.");
		}
		return bytes;
	}
	

	/**
	 * byteToBase64
	 * Byte를 Base64로 변환
	 * @return String
	 */
	public static String byteToBase64(byte[] arr) {
		String result = "";
		try {
			result = Base64Utils.encodeToString(arr);
		}catch(RuntimeException e) {
			log.info("동작중 오류가 발생하였습니다.");
		}
		return result;
	}
}
