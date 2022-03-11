package com.ishift.auction.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.codehaus.jettison.json.JSONException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;

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
			
			conn.disconnect();
			
		}catch (IOException ie) {
			log.error("HttpUtils.postRequest : {} ",ie);
		}catch (RuntimeException re) {
			log.error("HttpUtils.postRequest : {} ",re);
		}
	}
	
	
	public String setJsonData(Map<String, Object> params) {
		String recPhoneNum = (String)params.getOrDefault("smsRmsMpno", "");
		recPhoneNum = recPhoneNum.replaceAll("-", "");
        Calendar calendar = Calendar.getInstance();
        Date calcDate = calendar.getTime();
		StringBuffer sb = new StringBuffer();
        Random r = new Random();
        r.setSeed(new Date().getTime());     
		sb.append("{");
		sb.append("\"header\":{");
        sb.append("\"CSTX\":\"\\u0002\"");
        sb.append(",\"CTGRM_CD\":\"3100\"");
		sb.append(",\"CTGRM_CHSNB\":\"00FD68_" + String.format("%08d", r.nextInt(99999999))+"\"");
		sb.append(",\"CCRT_DATE\":\""+new SimpleDateFormat("yyyyMMdd").format(calcDate)+"\"");
		sb.append(",\"CCRT_TIME\":\""+new SimpleDateFormat("HHmmss").format(calcDate) +"\"");
		sb.append(",\"CORG_CD\":\"NHAH\"");
		sb.append(",\"CBSN_CD\":\"FD68\"");
		sb.append(",\"CCLS_CD\":\"0200\"");
		sb.append(",\"CTRN_CD\":\"IFLM0031\"");
		sb.append(",\"CSPR_CD\":\"I\"");   //CTSPR_CD
		sb.append(",\"CTGRM_TRNSM_DATETIME\":\""+new SimpleDateFormat("yyyyMMddHHmmss").format(calcDate)+"\"");
		sb.append(",\"CTGRM_RSP_CD\":\"0000\"");
		sb.append(",\"CRSRVD\":\"                        \"");
		sb.append("}");
		sb.append(", \"data\" : {");
		sb.append("\"INQ_CN\":1");
		sb.append(",\"RPT_DATA\" : [{");
		sb.append("\"NA_BZPLC\":\""+padLeftBlank((String)params.getOrDefault("naBzplc", ""),13)+"\" ");//13
		sb.append(",\"MPNO\":\""+padLeftBlank(recPhoneNum,11)+"\" ");//11
		sb.append(",\"USRNM\":\""+padLeftBlank((String)params.getOrDefault("rmsMnName", ""),20)+"\" ");//20
		sb.append(",\"MSG_CNTN\":\""+padLeftBlank((String)params.getOrDefault("smsFwdgCntn", ""),200)+"\"");//200
		sb.append("}]");
		sb.append("}}");
		log.info(sb.toString());
		return sb.toString();
	}
	
	public String sendPostJson(Map<String, Object> param) throws Exception{

    	//개발
        //URL url = new URL("http://192.168.114.11:18211/http/lvaca-fmec");
    	//운영
    	URL url = new URL("http://192.168.97.141:18211/http/lvaca-fmec");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        String responseBody = "";
        String jsonValue = setJsonData(param);
        boolean result = true;
        try {
            
            log.debug("REST API START");
            
            byte[] sendData = jsonValue.getBytes("UTF-8");

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Cache-Control", "no-cache");
            con.setRequestProperty("Pragma", "no-cache");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            con.setRequestProperty("Content-Length", String.valueOf(sendData.length));
            con.setConnectTimeout(5000);//서버 접속시 연결시간
            con.setReadTimeout(30000);//Read시 연결시간
            con.getOutputStream().write(sendData);
            con.getOutputStream().flush();
            con.connect();

            int responseCode = con.getResponseCode();
            String responseMessage = con.getResponseMessage();

            log.debug("REST API responseCode : " + responseCode);
            log.debug("REST API responseMessage : " + responseMessage);
            log.debug("REST API Error Stream : " + con.getErrorStream());
            
            if(con.getResponseCode() == 301 || con.getResponseCode() == 302 || con.getResponseCode() == 307) // 300번대 응답은 redirect
            {
            }else {
                StringBuffer sb = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                for(String line; (line = reader.readLine()) != null; )
                {
                    sb.append(line).append("\n");
                }
                responseBody = sb.toString();
            }
            
            con.disconnect();
            
            log.debug("REST API END"); 
            
        } catch (RuntimeException e) {
        	log.error(e.getMessage() ,e);
            throw new Exception("서버 수행중 오류가 발생하였습니다.");
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            throw new Exception("서버 수행중 오류가 발생하였습니다.");
        } finally {
            if (con!= null) con.disconnect();
        }
        
        return responseBody;
    }

	public String padLeftBlank(String inputString, int length) {
		if(inputString == null) {
			inputString = "";
		}
		if(inputString.length() >= length) {
			return inputString;
		}
		StringBuilder sb = new StringBuilder();
		sb.append(inputString);
		while(sb.length() < length - inputString.length()) {
			sb.append(" ");
		}
		return sb.toString();
	}
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public Map<String, Object> getMapFromJsonObject(String jsonObj) throws JSONException{
        
        Map<String, Object> map = new HashMap<String, Object>();
        
        Gson gson = new Gson();
        
        Map<String, Object> jsonObject = new HashMap<String, Object>();
        jsonObject = gson.fromJson(jsonObj,  jsonObject.getClass());
          
        Map<String, Object> jsonHeader = (Map)jsonObject.get("header");
        Map<String, Object> jsonData = (Map)jsonObject.get("data");
        List<Map<String, Object>> jsonList = null;
        if(jsonData.containsKey("RPT_DATA")) {
            jsonList = (List)jsonData.get("RPT_DATA");
        }
        
        int cnt = 0;
        
        if(jsonData.containsKey("IO_ROW_CNT")) {
            cnt  = (int)(double)jsonData.get("IO_ROW_CNT");
        }
        
        log.info("mca receive >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+jsonObj);
        log.info("mca receive data to map >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info("jsonHeader : " + jsonHeader.toString());
        log.info("dataCnt : "    + cnt);
        log.info("jsonData : "   + jsonData.toString());
        log.info("mca receive data to map <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
        
        map.put("jsonHeader", jsonHeader);
        map.put("dataCnt", cnt);
        map.put("jsonData", jsonData);
       	map.put("jsonList", jsonList);
        
        return map;
    }
}
