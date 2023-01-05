package com.ishift.auction.configuration.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 로그인한 사용자가 중도매인 권한이 없는 경우 처리를 위한 handler
 * @author Yuchan
 */
@Slf4j
@Component
public class BidUserAccessDeniedHandler implements AccessDeniedHandler {

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		String uri = request.getRequestURI();
		String type = (uri.startsWith("/my/entry") ? "1" : "0").replaceAll("\r", "").replaceAll("\n", "");
		String place = (request.getParameter("place") == null) ? "" : request.getParameter("place").replaceAll("\r", "").replaceAll("\n", "");
		response.sendRedirect("/user/login?place=" + place + "&type=" + type);
	}

}
