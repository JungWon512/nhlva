package com.ishift.auction.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.google.common.net.HttpHeaders;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpUtils {
	
	public void postRequest() {
		String apiUrl = "https://kakaoi-newtone-openapi.kakao.com/v1/synthesize";
		String apiKey = "a36ce0d1cef0f4618bc716415b289972";
		
		try {
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection)url.openConnection();
			conn.setRequestMethod("POST");
			conn.setRequestProperty(HttpHeaders.AUTHORIZATION, "KakaoAK " + apiKey);
			conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_XML_VALUE);
			conn.setReadTimeout(1000);
			
			conn.setDoOutput(true);
			DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
			dos.writeBytes("<speak>TEST TEST</speak>");
			dos.flush();
			dos.close();
			
			int responseCode = conn.getResponseCode();
			log.debug("responseCode : {}", responseCode);
			
			BufferedReader br;
			
			if (responseCode == HttpStatus.OK.value()) {
				br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			}
			else {
				br = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
			}
			
			StringBuffer res = new StringBuffer();
			while (br.readLine() != null) {
				res.append(br.readLine());
			}
			
			br.close();
			
			System.out.println(res.toString());
			
			conn.disconnect();
			
		}catch (IOException ie) {
			log.error("HttpUtils.postRequest : {} ",ie);
		}catch (RuntimeException re) {
			log.error("HttpUtils.postRequest : {} ",re);
		}
	}
	
	public void sendPost(String baseUrl, HttpHeaders header, String content) {
		
	}

}
