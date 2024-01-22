package com.ishift.auction.batch;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.google.common.net.HttpHeaders;

@Component
public class TaskBatch {
	private static Logger log = LoggerFactory.getLogger(TaskBatch.class);
	/**
	 * Agora Project Status : Disabled
	 * after Channel List Kicking
	 */
	@Scheduled(cron = "0 0 14 * * *")
	public void closeAgora() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime date = LocalDateTime.now();
		log.info("s: closeAgora ::"+date.format(format));
		try {
			//projects Disabled
			JSONObject param = new JSONObject();
			param.put("id", "sqHRUMdYW");
			param.put("status", 0);
			String closeProject = postRequest("https://api.agora.io/dev/v1/project_status",param);
			
			//select Channel List
			String strChannelList = getRequest("https://api.agora.io/dev/v1/channel/4b519868a2fa40c8b7c0bc2bd3a9fb9a");
			
			List<String> channelList = this.getChannelList(strChannelList);
			
			for(String cname: channelList) {
				//String cname = "";
				JSONObject kickParam = new JSONObject();
				JSONArray privileges = new JSONArray();
				privileges.put("join_channel");
				kickParam.put("appid", "4b519868a2fa40c8b7c0bc2bd3a9fb9a");
				kickParam.put("cname", cname);
				kickParam.put("uid", "");
				kickParam.put("ip", "");
				kickParam.put("time", "0");
				kickParam.put("privileges", privileges);
				String kickChannel = postRequest("https://api.agora.io/dev/v1/kicking-rule",kickParam);				
			}
		}catch(Exception e) {
			log.error("Batch Agora Start error",e);
		}
		date = LocalDateTime.now();
		log.info("e: closeAgora ::"+date.format(format));
	}

	@Scheduled(cron = "0 0 4 * * *")
	public void openAgora() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime date = LocalDateTime.now();
		log.info("s: openAgora ::"+date.format(format));
		try {
			//projects Enabled
			JSONObject param = new JSONObject();
			param.put("id", "sqHRUMdYW");
			param.put("status", 1);
			String closeProject = postRequest("https://api.agora.io/dev/v1/project_status",param);
		}catch(Exception e) {
			log.error("Batch Agora Start error",e);
		}
		date = LocalDateTime.now();
		log.info("e: openAgora ::"+date.format(format));
	}

	private String postRequest(String apiUrl, JSONObject param) throws Exception {
		String result = "";
		
		log.info("url ::: "+apiUrl+" params ::: "+param.toString());
		URL url = new URL(apiUrl);
		this.SSLVaildBypass();
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("POST");
		
		conn.setDoOutput(true);
		conn.setRequestProperty(HttpHeaders.AUTHORIZATION, "Basic MzQ4NTBkZTlmNmExNGJiY2E3NWQ4NDMzYTRhOGUwOGE6MDM1MTc0NWQwNDk0NGRiMzg4OTU2ZjEyZDQ0ZTJkMzQ=");
		conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
		conn.getOutputStream().write(param.toString().getBytes());
		conn.getOutputStream().flush();
        log.debug("Response code: " + conn.getResponseCode());
        BufferedReader rd = null;
        
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <=300 ) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		}else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
        
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        log.debug(sb.toString());
        result = sb.toString();
		return result;
	}

	public List<String> getChannelList(String strResult) {
		List<String> channelList = new ArrayList<>();
		if(!strResult.isEmpty()) {
			JSONObject jObj = new JSONObject(strResult);
			Boolean success = jObj.getBoolean("success");
			if(success) {
				JSONObject data = jObj.getJSONObject("data");
				int totalSize = data.getInt("total_size");
				if(totalSize>0) {
					JSONArray channels = data.getJSONArray("channels");
					channels.forEach(item ->{
						if(item instanceof JSONObject) {
							JSONObject obj = (JSONObject) item;
							channelList.add(obj.getString("channel_name"));								
						}
					});
				}
			}
		}
		return channelList;		
	}

	public String getRequest(String apiUrl) throws Exception{
		String result = "";
		
		log.info(apiUrl);
		URL url = new URL(apiUrl);
		this.SSLVaildBypass();
		HttpURLConnection conn = (HttpURLConnection)url.openConnection();
		conn.setRequestMethod("GET");
		
		
		conn.setRequestProperty(HttpHeaders.AUTHORIZATION, "Basic MzQ4NTBkZTlmNmExNGJiY2E3NWQ4NDMzYTRhOGUwOGE6MDM1MTc0NWQwNDk0NGRiMzg4OTU2ZjEyZDQ0ZTJkMzQ=");
		conn.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
		conn.setConnectTimeout(5000);
		conn.setReadTimeout(5000);
        log.debug("Response code: " + conn.getResponseCode());
        BufferedReader rd = null;
        
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <=300 ) {
			rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		}else {
			rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
		}
        
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();
        log.debug(sb.toString());
        result = sb.toString();
	        
		return result;
	}
	
	private void SSLVaildBypass() throws NoSuchAlgorithmException, KeyManagementException {
        HostnameVerifier hv = new HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		};
    	TrustManager[] trustAllCerts = new TrustManager[] {
    		new X509TrustManager() {					
				@Override
				public X509Certificate[] getAcceptedIssuers() {
					return null;
				}
				
				@Override
				public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {return;}
				
				@Override
				public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {return;}
			}
    	};
    	
    	SSLContext sc = SSLContext.getInstance("SSL");
    	sc.init(null, trustAllCerts, new SecureRandom());
    	HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
    	HttpsURLConnection.setDefaultHostnameVerifier(hv);		
	}

	@Scheduled(cron = "0/10 * * * * *")
	public void batchTest() {
		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime date = LocalDateTime.now();
		log.info("TEST :::"+date.format(format)+" ::: TEST");		
	}
}
