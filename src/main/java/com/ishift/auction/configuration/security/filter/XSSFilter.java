package com.ishift.auction.configuration.security.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import org.springframework.util.StringUtils;

import com.ishift.auction.util.XssRequestWrapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebFilter(urlPatterns = {"/*"})
@SuppressWarnings("unused")
public class XSSFilter implements Filter {

	private FilterConfig filterConfig;
	
	private final List<String> ALLOWED_PATH = Arrays.asList("/main", "/api", "/static", "/user", "/kiosk", "/deamon");

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		String path = httpServletRequest.getRequestURI().substring(httpServletRequest.getContextPath().length()).replaceAll("[/]+$", "");
		if (!this.isAllowedPath(path)) {
			XssRequestWrapper wrapperRequest = new XssRequestWrapper(httpServletRequest);
			chain.doFilter(wrapperRequest, response);
		}
		else {
			chain.doFilter(request, response);
		};
	}

	@Override
	public void destroy() {
//		Filter.super.destroy();
		this.filterConfig = null;
	}
	
	private boolean isAllowedPath(String path) {
		if (StringUtils.isEmpty(path)) return true;
		boolean rtn = false;
		for(String value : ALLOWED_PATH) {
			if (path.startsWith(value)) rtn = true;
		}
		return rtn;
	}
}
