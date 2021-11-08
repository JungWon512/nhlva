package com.ishift.auction.configuration.security.wrapper;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class RequestBodyParamRequestWrapper extends HttpServletRequestWrapper{
	private String body;
	private Map<String,String[]> params;
	public RequestBodyParamRequestWrapper(HttpServletRequest request) throws IOException{		
		super(request);
		
		body="";
		byte[] rawData = new byte[16384];
		this.params =  new HashMap<String,String[]>(request.getParameterMap());
		String collect = "";
		
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;
        if("application/json".equals(request.getContentType())) {
	        try {
	            InputStream inputStream = request.getInputStream();
	            if (inputStream != null) {
	                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
	                char[] charBuffer = new char[128];
	                int bytesRead = -1;
	                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
	                    stringBuilder.append(charBuffer, 0, bytesRead);
	                }
	            } else {
	                stringBuilder.append("");
	            }			
								
	        } catch (IOException ex) {
	            throw ex;
	        } finally {
	            if (bufferedReader != null) {
	                try {
	                    bufferedReader.close();	
	                } catch (IOException ex) {
	                    throw ex;
	                }
	            }
	        }

    		body = stringBuilder.toString();
        }
	}

	public String getParam(String key) {
		StringBuilder result= new StringBuilder();
		String[] tmp = this.params.get(key);
		if(tmp != null) {
			for(String str : tmp) {
				result.append(str);
			}			
		}
		return result.toString();
	}

	@Override
	public ServletInputStream getInputStream() throws IOException {
	  final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body.getBytes());
	  ServletInputStream servletInputStream = new ServletInputStream() {
		public int read() throws IOException {
		  return byteArrayInputStream.read();
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return false;
		}

		@Override
		public void setReadListener(ReadListener listener) {
			
		}
	  };
	  return servletInputStream;
	}
	  
	@Override
	public BufferedReader getReader() throws IOException {
		BufferedReader buffer = null;
		try {
			buffer = new BufferedReader(new InputStreamReader(this.getInputStream()));
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			if(buffer != null) {
				buffer.close();
			}
		}
	    return buffer;
	}
	public void setParam() throws IOException {
		try {
			if(!this.body.isEmpty()) {
				JSONParser parser = new JSONParser();
				JSONObject obj;
				obj = (JSONObject)parser.parse( this.body );
				Set<String> keys = obj.keySet();
				for(String key : keys) {
					//System.out.println("##################### KEY : "+key +" VAL ## : "+obj.get(key));
					this.params.put(key, new String[] { (String) obj.get(key) });
				}	
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}
	@Override
    public ServletRequest getRequest() {
        return super.getRequest();
    }
}
