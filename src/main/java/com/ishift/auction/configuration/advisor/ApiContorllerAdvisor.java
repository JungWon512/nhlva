package com.ishift.auction.configuration.advisor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice(basePackages = "com.ishift.auction.web")
public class ApiContorllerAdvisor {

	@ExceptionHandler(HttpMessageNotReadableException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public Map<String, Object> httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e) {
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		result.put("message", "지원하지 않는 요청 방식입니다. 요청 방식을 확인하세요.");
		return result;
	}
	
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
	@ResponseBody
	public Map<String, Object> httpRequestMethodNotSupportedExceptionHandler(HttpRequestMethodNotSupportedException e) {
		final Map<String, Object> result = new HashMap<String, Object>();
		result.put("success", false);
		result.put("message", "지원하지 않는 요청 방식입니다. 요청 방식을 확인하세요.");
		return result;
	}
	
}
