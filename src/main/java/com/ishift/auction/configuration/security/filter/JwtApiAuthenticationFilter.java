package com.ishift.auction.configuration.security.filter;

import com.google.common.net.HttpHeaders;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.CookieUtil;
import com.ishift.auction.util.JwtTokenUtil;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt 토큰 검증을 위한 Filter
 */
@Slf4j
//@Component
public class JwtApiAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	CookieUtil cookieUtil;

	/**
	 * 쿠키에 포함된 access_token의 유효성을 체크한다.
	 */
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

		final String cookieAccessToken = cookieUtil.getCookieValue(request, Constants.JwtConstants.ACCESS_TOKEN);
		final String headerAccessToken = request.getHeader(HttpHeaders.AUTHORIZATION);
		
		// 헤더와 쿠키 두루 다 토큰이 없는 경우 체크
		if ((cookieAccessToken == null || "".equals(cookieAccessToken))
		&& (headerAccessToken == null || "".equals(headerAccessToken))) {
			chain.doFilter(request, response);
		}
		
		// 헤더와 쿠키 둘 다 토큰이 있는 경우 헤더의 토큰을 우선으로 체크
		String accessToken = (headerAccessToken != null && !"".equals(headerAccessToken)) ? 
								headerAccessToken.substring(Constants.JwtConstants.BEARER.length()) 
								: cookieAccessToken;
		
		if (jwtTokenUtil.isValidToken(accessToken)) {
//			Authentication auth = jwtTokenUtil.getAuthentication(accessToken);
//			SecurityContextHolder.getContext().setAuthentication(auth);
//			chain.doFilter(request, response);
		}
		else {
			// 토큰 인증 에러
			log.error("Token is not valid");
			chain.doFilter(request, response);
		}
	}
	
	/**
	 * jwt 유효성 체크 예외처리
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String uri = request.getRequestURI();
		return uri.startsWith("/static/") || uri.startsWith("/user/login") || uri.startsWith("/user/loginProc");
	}
}
