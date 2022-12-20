package com.ishift.auction.configuration.security.entrypoint;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Jwt 인증에 실패 처리 Entry Point
 * @author Yuchan
 */
@Slf4j
@Component
public class JwtTokenAdminEntryPoint implements AuthenticationEntryPoint {

	/**
	 * 인증에 실패 할 경우 이동할 url을 호출 또는 에러메시지 호출 
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		log.error("Unauthorized Error : {}", authException.getMessage());
		if (request.getRequestURI().startsWith("/api")
		) {
			response.setCharacterEncoding("utf-8"); 
			response.setContentType("application/json");
			StringBuffer sb = new StringBuffer();
			sb.append("{\"success\" : false");
			sb.append(", \"message\" : \"사용자 인증에 실패했습니다.\"}");
			response.getWriter().println(sb.toString());
		}
		else if (request.getRequestURI().startsWith("/daemon/api")
			   ||request.getRequestURI().startsWith("/kiosk/api")) {
			
			response.setCharacterEncoding("utf-8"); 
			response.setContentType("application/json");
			
			StringBuffer sb = new StringBuffer();
			sb.append("{\"status\" : ");
			sb.append(HttpStatus.UNAUTHORIZED.value());
			sb.append(", \"code\" : \"C");
			sb.append(HttpStatus.UNAUTHORIZED.value());
			sb.append("\" , \"message\" : \"사용자 인증에 실패했습니다.\"}");
			response.getWriter().println(sb.toString());
		}
		else {
			response.sendRedirect("/office/user/login");
		}
	}

}
