package com.ishift.auction.configuration.security.filter;

import com.google.common.net.HttpHeaders;
import com.ishift.auction.configuration.security.wrapper.RequestBodyParamRequestWrapper;
import com.ishift.auction.service.user.FarmUserDetailsService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.CookieUtil;
import com.ishift.auction.util.JwtTokenUtil;

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

/**
 * 중도매인 jwt 토큰 검증을 위한 Filter
 */
@Slf4j
@Component
public class JwtFarmAuthenticationFilter extends OncePerRequestFilter {
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private CookieUtil cookieUtil;
	
	@Autowired
	private FarmUserDetailsService userDetailsService;

	/**
	 * 쿠키에 포함된 access_token의 유효성을 체크한다.
	 */
	protected void doFilterInternal(HttpServletRequest request
								, HttpServletResponse response
								, FilterChain chain
								) throws IOException, ServletException {

		log.debug("#### JwtFarmAuthenticationFilter [s] #####");
		RequestBodyParamRequestWrapper req = new RequestBodyParamRequestWrapper(request);
		req.setParam();
		req.getReader();
		HttpServletRequest tmpRequest = (HttpServletRequest) req.getRequest();
		final String place = req.getParam("place");
		final String cookieAccessToken = cookieUtil.getCookieValue(tmpRequest, Constants.JwtConstants.ACCESS_TOKEN);
		final String headerAccessToken = tmpRequest.getHeader(HttpHeaders.AUTHORIZATION);

		// place가 parameter에 없는 경우 어느 조합의 회원을 검증해야 하는지 모르기 때문에 인증 절차를 pass한다.
		if (place == null || "".equals(place)) {
			log.error("지역코드가 없습니다.");
		}
		// 헤더와 쿠키 둘 다 토큰이 없는 경우 체크
		else if ((cookieAccessToken == null || "".equals(cookieAccessToken))
		&& (headerAccessToken == null || "".equals(headerAccessToken))) {
			log.error("토큰이 없습니다.");
		}
		else {
			try {
				// 헤더와 쿠키 둘 다 토큰이 있는 경우 헤더의 토큰을 우선으로 체크
				String accessToken = (headerAccessToken != null && !"".equals(headerAccessToken)) ? 
						headerAccessToken.substring(Constants.JwtConstants.BEARER.length()) 
						: cookieAccessToken;
				
				if (jwtTokenUtil.isValidToken(accessToken)
				&& Constants.UserRole.FARM.equals(jwtTokenUtil.getValue(accessToken, Constants.JwtConstants.JWT_CLAIM_USER_ROLE))) {
					StringBuffer sb = new StringBuffer();
					sb.append(jwtTokenUtil.getValue(accessToken, Constants.JwtConstants.JWT_CLAIM_USER_MEM_NUM))
					  .append('_')
					  .append(jwtTokenUtil.getValue(accessToken, Constants.JwtConstants.JWT_CLAIM_AUCTION_HOUSE_CODE))
					  .append('_')
					  .append(place);

					UserDetails user = userDetailsService.loadUserByUsername(sb.toString());
					UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, "", user.getAuthorities());
					auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(tmpRequest));
					SecurityContextHolder.getContext().setAuthentication(auth);
				}
			}
			catch(Exception e) {
				log.error("Cannot set user authentication : {}", e.getMessage());
			}
		}
		log.debug("#### JwtFarmAuthenticationFilter [e] #####");
		chain.doFilter(req, response);
	}
	
	/**
	 * jwt 유효성 체크 예외처리
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		String uri = request.getRequestURI();
		return uri.startsWith("/static/")
			|| uri.startsWith("/api")
			|| uri.startsWith("/user/login")
			|| uri.startsWith("/user/loginProc")
			|| uri.startsWith("/admin")
			|| uri.startsWith("/api")
			|| uri.startsWith("/batch");
	}
}
