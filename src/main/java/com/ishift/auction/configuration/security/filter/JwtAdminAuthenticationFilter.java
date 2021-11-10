package com.ishift.auction.configuration.security.filter;

import com.google.common.net.HttpHeaders;
import com.ishift.auction.service.admin.user.AdminUserDetailsService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.CookieUtil;
import com.ishift.auction.util.JwtTokenUtil;
import com.sun.el.parser.ParseException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * 관리자 jwt 토큰 검증을 위한 Filter
 */
@Slf4j
@Component
public class JwtAdminAuthenticationFilter extends OncePerRequestFilter {

	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private AdminUserDetailsService adminUserDateilsService;

	@Autowired
	private CookieUtil cookieUtil;

	/**
	 * 쿠키에 포함된 access_token의 유효성을 체크한다.
	 */
	protected void doFilterInternal(HttpServletRequest request
									, HttpServletResponse response
									, FilterChain chain) throws IOException, ServletException {
		log.debug("#### JwtAdminAuthenticationFilter [s] #####");
		log.debug("#### Already Filter : {}", getAlreadyFilteredAttributeName());
		
		final String cookieAccessToken = cookieUtil.getCookieValue(request, Constants.JwtConstants.ACCESS_TOKEN);
		final String headerAccessToken = request.getHeader(HttpHeaders.AUTHORIZATION);

		// 헤더와 쿠키 둘 다 토큰이 없는 경우 체크
		if ((cookieAccessToken == null || "".equals(cookieAccessToken))
		&& (headerAccessToken == null || "".equals(headerAccessToken))) {
			log.error("토큰이 없습니다.");
		}
		else {
			try {
				// 헤더와 쿠키 둘 다 토큰이 있는 경우 헤더의 토큰을 우선으로 체크
				String accessToken = (headerAccessToken != null && !"".equals(headerAccessToken)) ? 
						headerAccessToken.substring(Constants.JwtConstants.BEARER.length()) 
						: cookieAccessToken;
				
				if (jwtTokenUtil.isValidToken(accessToken)) {
					UserDetails user = adminUserDateilsService.loadUserByUsername(jwtTokenUtil.getValue(accessToken, Constants.JwtConstants.JWT_CLAIM_USER_MEM_NUM));
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}catch (RuntimeException re) {
				log.error("Cannot set user authentication : {} ",re);			
			}
		}
		
		log.debug("#### JwtAdminAuthenticationFilter [e] #####");
		chain.doFilter(request, response);
	}
	
	/**
	 * jwt 유효성 체크 예외처리
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String uri = request.getRequestURI();
		return uri.startsWith("/static/")
			|| uri.startsWith("/admin/user")
			|| !(uri.startsWith("/admin") || uri.startsWith("/api"))
			|| uri.endsWith("/login");
	}
}
