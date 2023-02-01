package com.ishift.auction.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;

import com.amazonaws.util.IOUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unused")
public class XssRequestWrapper extends HttpServletRequestWrapper {
	
	private byte[] rawData;

	public XssRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			if (request.getMethod().equalsIgnoreCase("post")
			&& (MediaType.APPLICATION_JSON_VALUE.equals(request.getContentType()) || MediaType.MULTIPART_FORM_DATA_VALUE.equals(request.getContentType()))) {
				InputStream is = request.getInputStream();
				this.rawData = XSSUtils.fnXSSEncode(IOUtils.toByteArray(is));
			}
		}
		catch (IOException e) {
		}
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
		if (this.rawData == null) return super.getInputStream();
		
		final ByteArrayInputStream bais = new ByteArrayInputStream(this.rawData);

		return new ServletInputStream() {
			
			@Override
			public int read() throws IOException {
				return bais.read();
			}
			
			@Override
			public void setReadListener(ReadListener listener) {
			}
			
			@Override
			public boolean isReady() {
				return false;
			}
			
			@Override
			public boolean isFinished() {
				return false;
			}
		};
	}
//
//	@Override
//	public String getQueryString() {
//		return XSSUtils.fnXSSEncode(super.getQueryString());
//	}
	
	@Override
	public String getParameter(String name) {
		return XSSUtils.fnXSSEncode(super.getParameter(name));
	}
	
	@Override
	public String[] getParameterValues(String name) {
		String[] values = super.getParameterValues(name);
		if (values == null) return null;
		
		int count = values.length;
		String[] encodeValues = new String[count];
		for (int i = 0; i < count; i++) {
			encodeValues[i] = XSSUtils.fnXSSEncode(values[i]);
		}
		
		return encodeValues;
	}
	
	@Override
	public Map<String, String[]> getParameterMap() {
		Map<String, String[]> params = super.getParameterMap();
		if (ObjectUtils.isEmpty(params)) return params;
		params.forEach((key, value) -> {
			for (int i = 0; i < value.length; i++) {
				value[i] = XSSUtils.fnXSSEncode(value[i]);
			}
		});
		return params;
	}
	
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(this.getInputStream(), "UTF-8"));
	}

}
