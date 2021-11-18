package com.ishift.auction.configuration.security.entrypoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 * Jwt 인증에 실패 처리 Entry Point
 * @author Yuchan
 */
@Component
public class JwtTokenEntryPoint implements AuthenticationEntryPoint {

	/**
	 * 인증에 실패 할 경우 이동할 url을 호출 또는 에러메시지 호출 
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		String uri = request.getRequestURI();
		String type = (uri.startsWith("/my/entry") ? "1" : "0").replaceAll("\r", "").replaceAll("\n", "");
		String place = (request.getParameter("place") == null) ? "" : request.getParameter("place").replaceAll("\r", "").replaceAll("\n", "");
		response.sendRedirect("/user/login?place=" + place + "&type=" + type);
	}

}
