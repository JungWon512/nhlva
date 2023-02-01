package com.ishift.auction.util;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtil {
	@Value("${spring.profiles.active}") private String profiles;
	/**
	 * 쿠키 생성
	 * @param cookieName
	 * @param value
	 * @return
	 */
	public Cookie createCookie(String cookieName, String value) {
		Cookie cookie = new Cookie(cookieName, value);
//		cookie.setHttpOnly(true);
		cookie.setMaxAge(60*60*24);
		if("production".equals(profiles)) {
			cookie.setSecure(true);
		}
		cookie.setPath("/");
		return cookie;
	}
	
	/**
	 * 쿠키 정보 가져오기
	 * @param req
	 * @param cookieName
	 * @return
	 */
	public Cookie getCookie(HttpServletRequest req, String cookieName){
		try {
			final Cookie[] cookies = req.getCookies();
			if(cookies==null) return null;
			for(Cookie cookie : cookies){
				if(cookie.getName().equals(cookieName)) {
					return cookie;
				}
			}
		}catch (RuntimeException re) {
			return null;
		}
		return null;
	}
	
	/**
	 * 쿠키 값 가져오기
	 * @param req
	 * @param cookieName
	 * @return
	 */
	public String getCookieValue(HttpServletRequest req, String cookieName) {
		try {
			if (this.getCookie(req, cookieName) == null) {
				return "";
			}
			else {
				return this.getCookie(req, cookieName).getValue();
			}
		}catch (RuntimeException re) {
			return "";
		}
	}
	
	/**
	 * 쿠키 삭제
	 * @param req
	 * @param cookieName
	 * @return
	 */
	public Cookie deleteCookie(HttpServletRequest req, String cookieName) {
		Cookie cookie = new Cookie(cookieName, null);
		cookie.setMaxAge(0);
		cookie.setPath("/");
		return cookie;
	}

}
