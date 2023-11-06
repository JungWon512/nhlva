package com.ishift.auction.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.codehaus.jettison.json.JSONException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.google.common.net.HttpHeaders;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class HttpUtils {
	@Value("${mca.url}")
	String mcaUrl;
	@Value("${open.api.url}")
	String openApiUrl;
	@Value("${open.api.serviceKey}")
	String openApiServiceKey;
	
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
	
	
	public String setJsonData(Map<String, Object> params) throws UnsupportedEncodingException {
		String recPhoneNum = (String)params.getOrDefault("smsRmsMpno", "");
		recPhoneNum = recPhoneNum.replaceAll("-", "");
        Calendar calendar = Calendar.getInstance();
        Date calcDate = calendar.getTime();
		StringBuffer sb = new StringBuffer();
        Random r;
        int rnNum = 0;
		try {
			r = SecureRandom.getInstance("SHA1PRNG");
			r.setSeed(System.currentTimeMillis());
			rnNum = r.nextInt(99999999);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
		}
		sb.append("{");
		sb.append("\"header\":{");
        sb.append("\"CSTX\":\"\\u0002\"");
        sb.append(",\"CTGRM_CD\":\"3100\"");
		sb.append(",\"CTGRM_CHSNB\":\"00FD68_" + String.format("%08d", rnNum)+"\"");
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
    	URL url = new URL(mcaUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        String responseBody = "";
        String jsonValue = setJsonData(param);
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

	//공백 제로
	public String padLeftZero(String inputString, int length) throws UnsupportedEncodingException {		
		if(inputString == null) {
			inputString = "";
		}		
		byte[] inputByte=inputString.getBytes("EUC-KR");
			
		int byteLen = inputByte.length;
		if(byteLen == length) {
			return inputString;
		}else if(byteLen > length) {
			StringBuilder stringBuilder = new StringBuilder(length);
			int nCnt = 0;
			for(char ch:inputString.toCharArray()){
				nCnt += String.valueOf(ch).getBytes("EUC-KR").length;
				if(nCnt > length) break;
				stringBuilder.append(ch);
			}
			return stringBuilder.toString();
		}		
		StringBuilder sb = new StringBuilder();		
		//while(sb.length() < length - inputString.length()) {
		while(sb.toString().getBytes("EUC-KR").length < length - byteLen) {
			sb.append("0");
		}
		sb.append(inputString);
		return sb.toString();
	}
	public String padLeftBlank(String inputString, int length) throws UnsupportedEncodingException {
		if(inputString == null) {
			inputString = "";
		}
		byte[] inputByte=inputString.getBytes("EUC-KR");
			
		int byteLen = inputByte.length;
		if(byteLen == length) {
			return inputString;
		}else if(byteLen > length) {
			StringBuilder stringBuilder = new StringBuilder(length);
			int nCnt = 0;
			for(char ch:inputString.toCharArray()){
				nCnt += String.valueOf(ch).getBytes("EUC-KR").length;
				if(nCnt > length) break;
				stringBuilder.append(ch);
			}
			return stringBuilder.toString();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(inputString);
		while(sb.toString().getBytes("EUC-KR").length < length) {
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
        log.info("mca receive >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+jsonObj);
        log.info("mca receive data to map >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        log.info("jsonHeader : " + jsonHeader.toString());
        log.info("jsonData : "   + jsonData.toString());
        
        map.put("jsonHeader", jsonHeader);
        map.put("dataCnt", cnt);
        map.put("jsonData", jsonData);
       	map.put("jsonList", jsonList);
        
        return map;
    }
    

	public String setJsonData(Map<String, Object> params,String ctgrmCd) throws UnsupportedEncodingException {
        Calendar calendar = Calendar.getInstance();
        Date calcDate = calendar.getTime();
		StringBuffer sb = new StringBuffer();
        
		Random r;
		int rnNum = 0;
        try {
			r = SecureRandom.getInstance("SHA1PRNG");
			r.setSeed(System.currentTimeMillis());
			rnNum = r.nextInt(99999999);
		} catch (NoSuchAlgorithmException e) {
			log.error(e.getMessage());
		}
        
        String ctrnCd = "";
        if(ctgrmCd != null) {
        	ctrnCd="IFLM00"+ctgrmCd.substring(0,2);
        }
		StringBuilder data = new StringBuilder();
        switch(ctgrmCd) {
        	case "3100":
        		String recPhoneNum = (String)params.getOrDefault("smsRmsMpno", "");
        		recPhoneNum = recPhoneNum.replaceAll("-", "");
        		data.append("\"INQ_CN\":1");
        		data.append(",\"RPT_DATA\" : [{");
        		data.append("\"NA_BZPLC\":\""+padLeftBlank((String)params.getOrDefault("naBzplc", ""),13)+"\" ");//13
        		data.append(",\"MPNO\":\""+padLeftBlank(recPhoneNum,11)+"\" ");//11
        		data.append(",\"USRNM\":\""+padLeftBlank((String)params.getOrDefault("rmsMnName", ""),20)+"\" ");//20
        		data.append(",\"MSG_CNTN\":\""+padLeftBlank((String)params.getOrDefault("smsFwdgCntn", ""),200)+"\"");//200       
        		data.append("}]"); 		
        	break;
        	case "4600":
            	data.append("\"IN_SQNO\":\""    + padLeftZero(""+(int)params.get("IN_SQNO"),7)    + "\"");
                data.append(",\"IN_REC_CN\":\"" + padLeftZero((String)params.get("IN_REC_CN"), 4) + "\"");
                data.append(",\"NA_BZPLC\":\""  + padLeftBlank((String)params.get("NA_BZPLC"),13) + "\"");
                data.append(",\"INQ_ST_DT\":\"" + padLeftBlank((String)params.get("INQ_ST_DT"),8) + "\"");
                data.append(",\"INQ_ED_DT\":\"" + padLeftBlank((String)params.get("INQ_ED_DT"),8) + "\"");
            break;
        	case "4700":
        	case "4900":
            	data.append("\"SRA_INDV_AMNNO\":\"" + padLeftBlank((String)params.get("SRA_INDV_AMNNO"),20)    + "\"");
        	break;
        	case "4800":
            	data.append("\"SRA_INDV_AMNNO\":\"" + padLeftBlank((String)params.get("SRA_INDV_AMNNO"),20)    + "\"");
                data.append(",\"MCOW_SRA_INDV_AMNNO\":\""  + padLeftBlank((String)params.get("MCOW_SRA_INDV_AMNNO"),20)  + "\"");
        	default:break;
        }
		
		sb.append("{");
		sb.append("\"header\":{");
        sb.append("\"CSTX\":\"\\u0002\"");
        sb.append(",\"CTGRM_CD\":\""+ctgrmCd+"\"");
		sb.append(",\"CTGRM_CHSNB\":\"00FD68_" + String.format("%08d", rnNum)+"\"");
		sb.append(",\"CCRT_DATE\":\""+new SimpleDateFormat("yyyyMMdd").format(calcDate)+"\"");
		sb.append(",\"CCRT_TIME\":\""+new SimpleDateFormat("HHmmss").format(calcDate) +"\"");
		sb.append(",\"CORG_CD\":\"NHAH\"");
		sb.append(",\"CBSN_CD\":\"FD68\"");
		sb.append(",\"CCLS_CD\":\"0200\"");
		sb.append(",\"CTRN_CD\":\""+ctrnCd+"\"");
		sb.append(",\"CSPR_CD\":\"I\"");   //CTSPR_CD
		sb.append(",\"CTGRM_TRNSM_DATETIME\":\""+new SimpleDateFormat("yyyyMMddHHmmss").format(calcDate)+"\"");
		sb.append(",\"CTGRM_RSP_CD\":\"0000\"");
		if("4700".equals(ctgrmCd)) {
			sb.append(",\"CRSRVD\":\"                       0\"");			
		}else {
			sb.append(",\"CRSRVD\":\"                        \"");			
		}
		sb.append("}");
		sb.append(",\"data\":{");
		sb.append(data.toString());
		sb.append("}");
		sb.append("}");
		log.info(sb.toString());
		return sb.toString();
	}
	
	public String sendPostJson(Map<String, Object> param,String ctgrmCd) throws Exception{

        URL url = new URL(mcaUrl);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        String responseBody = "";
        String jsonValue = setJsonData(param,ctgrmCd);
        
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
            
        } catch (RuntimeException | SocketTimeoutException e) {
        	log.error(e.getMessage() ,e);
            throw new RuntimeException("서버 수행중 오류가 발생하였습니다.");
        } catch (Exception e) {
        	log.error(e.getMessage(),e);
            throw new Exception("서버 수행중 오류가 발생하였습니다.");
        } finally {
            if (con!= null) con.disconnect();
        }
        
        return responseBody;
    }
	
	
	public String getOpenApiAnimalTrace(Map<String, Object> map) throws Exception {		
		String sendUrl = openApiUrl+"/animalTrace/traceNoSearch";
		sendUrl += "?serviceKey=" + openApiServiceKey;
		sendUrl += "&traceNo=" + map.get("SRA_INDV_AMNNO");
		log.debug("sendUrl: " + sendUrl);
		StringBuilder urlBuilder = new StringBuilder(sendUrl);
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = null;
        String result = "";
				
		try {
			
			conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-type", "application/json");
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
	       
			
		} catch (UnknownHostException | RuntimeException | SocketTimeoutException e) {
        	log.error(e.getMessage() ,e);
            throw new Exception("서버 수행중 오류가 발생하였습니다.");			
        } catch (Exception e) {
        	log.error(e.getMessage() ,e);
            throw new RuntimeException("서버 수행중 오류가 발생하였습니다.");        	
        } finally {
            if (conn!= null) conn.disconnect();
        }
		 	
        return result;
    }


	public Map<String, Object> sendPostJsonToMap(Map<String, Object> param, String str) {
		// TODO Auto-generated method stub
		String json ="";
		try {
			json = sendPostJson(param,str);
			if(json.length() > 0) {
				return this.getMapFromJsonObject(json);
			}
		}catch(RuntimeException re) {
			log.error(re.getMessage());
		}catch (Exception e) {
			log.debug("MCA 통신중 ERROR",e);
		}
		return null;
	}

	public Map<String, Object> getOpenApiAnimalTraceToMap(Map<String, Object> param) {
		// TODO Auto-generated method stub
		String response ="";
		Map<String, Object> nodeMap      = new HashMap<String, Object>();
		Map<String, Object> moveMap      = new HashMap<String, Object>();
		Map<String, Object> vacnMap      = new HashMap<String, Object>();
		List<Map<String, Object>> moveList      = new ArrayList<>();
		try {
			response = getOpenApiAnimalTrace(param);
			if(response.length() > 0) {
				JSONObject json = XML.toJSONObject(response);
		        
		        if(json != null && json.getJSONObject("response") != null && json.getJSONObject("response").getJSONObject("body") !=null
		        		&& json.getJSONObject("response").getJSONObject("body").getJSONObject("items") != null) {
			        JSONArray jArr = json.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
			        for(Object item : jArr) {
			        	JSONObject jItem = (JSONObject) item;
			        	String infoType = jItem.get("infoType").toString();
			        	if("5,6,7".indexOf(infoType)> -1) {
			        		Iterator<String> it =jItem.keySet().iterator();
			        		while(it.hasNext()) {
			        			String key = (String) it.next();
			        			vacnMap.put(key, jItem.get(key));			        			
			        		}
			        	}else if("2".equals(infoType)){
			        		Iterator<String> it =jItem.keySet().iterator();
			        		while(it.hasNext()) {
			        			String key = (String) it.next();
				        		moveMap.put(key, jItem.get(key));			        			
			        		}
			        		nodeMap.put("sra_indv_amnno", param.get("trace_no"));			
			        		moveList.add(moveMap);
			        	}
			        }
		        }
		        nodeMap.put("moveList", moveList);
		        nodeMap.put("vacnInfo", vacnMap);
			}
		}catch(ConnectException ce) {
        	log.error(ce.getMessage());
        }catch(RuntimeException re) {
        	log.error(re.getMessage());
        }catch (Exception e) {
			log.debug("OPEN API 통신중 ERROR",e);
		}
		return nodeMap;
	}

    public String callApiKauth(String type,String jsonMessage) {
        try {
            URL url = new URL("https://kauth.kakao.com/oauth/token");
            this.SSLVaildBypass();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
            con.setRequestMethod(type);
            //charset=utf-8
            con.setRequestProperty(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");

            if("GET".equals(type)) {
                con.setDoOutput(false);            	
            }else {
                con.setDoInput(true);
                con.setDoOutput(true); //POST 데이터를 OutputStream으로 넘겨 주겠다는 설정
                con.setUseCaches(false);
                con.setDefaultUseCaches(false);
                OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
                wr.write(jsonMessage); //json 형식의 message 전달
                wr.flush();            	
            }
            log.info(jsonMessage);
            StringBuilder sb = new StringBuilder();
            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                log.info("callApi /oauth/token : "+sb.toString());
                return sb.toString();
            }else {
                //InputStream eror = con.getErrorStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                log.info("callApi /oauth/token : "+sb.toString());
            	
            }

        }catch(ConnectException ce) {
        	log.error(ce.getMessage());
        }catch(RuntimeException re) {
        	log.error(re.getMessage());
        }catch (Exception e) {
            System.out.println(e.toString());
        }
        return null;
    }


	/**
	 * @methodName    : callApiKakaoTokenInfo
	 * @author        : Jung JungWon
	 * @date          : 2022.10.19
	 * @Comments      : 
	 */
	public String callApiKakaoTokenInfo(String type, String accessToken) {
        try {
            //get 요청할 url을 적어주시면 됩니다. 형태를 위해 저는 그냥 아무거나 적은 겁니다.
            URL url = new URL("https://kapi.kakao.com/v2/user/me");
            this.SSLVaildBypass();
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
            con.setRequestMethod(type);
			con.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
            con.setDoOutput(false);    

            StringBuilder sb = new StringBuilder();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                log.info("callApiKakaoTokenInfo : "+sb.toString());
                return sb.toString();
            } else {
                System.out.println(con.getResponseMessage());
            }

        }catch(ConnectException ce) {
        	log.error(ce.getMessage());
        }catch(RuntimeException re) {
        	log.error(re.getMessage());
        }catch (Exception e) {
            System.err.println(e.toString());
        }
        return null;
	}
	public Map<String, Object> getOpenDataApi(String url,Map<String, Object> map) {
		// TODO Auto-generated method stub

		Map<String, Object> nodeMap      = new HashMap<String, Object>();
		String sendUrl = "http://data.ekape.or.kr/openapi-data/service/user/"+url;
		sendUrl += "?serviceKey=" + map.get("serviceKey");
		if("/mtrace/breeding/cattleMove".equals(url)) {
			sendUrl += "&cattleNo="+map.get("sraIndvAmnno");
		}else{
			sendUrl += "&traceNo=" + map.get("sraIndvAmnno");//  "002125769192";
		}
        HttpURLConnection conn = null;
		log.debug("sendUrl: " + sendUrl);
		try {
			StringBuilder urlBuilder = new StringBuilder(sendUrl);
	        URL conUrl = new URL(urlBuilder.toString());
				
			
			conn = (HttpURLConnection) conUrl.openConnection();
			conn.setConnectTimeout(1000);
			conn.setReadTimeout(1000);
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-type", "application/json");
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
	        JSONObject json = XML.toJSONObject(sb.toString());
	        
	        if(json != null && json.getJSONObject("response") != null && json.getJSONObject("response").getJSONObject("body") !=null
	        		&& json.getJSONObject("response").getJSONObject("body").getJSONObject("items") != null) {
		        JSONArray jArr = json.getJSONObject("response").getJSONObject("body").getJSONObject("items").getJSONArray("item");
		        for(Object item : jArr) {
		        	JSONObject jItem = (JSONObject) item;
		        	String infoType = jItem.get("infoType").toString();
		        	if("5,6,7".indexOf(infoType)> -1) {
		        		Iterator<String> it =jItem.keySet().iterator();
		        		while(it.hasNext()) {
		        			String key = (String) it.next();
			        		nodeMap.put(key, jItem.get(key));			        			
		        		}
		        		
		        	}
		        }	        	
	        }	        
		}catch(ConnectException ce) {
			nodeMap = null;
        }catch(RuntimeException re) {
        	nodeMap = null;
        }catch (Exception e) {
        	nodeMap = null;
        } finally {
            if (conn!= null) conn.disconnect();
        }
		return nodeMap;
	}
	
	//종축개량 인터페이스
	public String callApiAiak(String barcode) {
		BufferedReader br = null;
		HttpURLConnection con = null;
        try {
        	
            URL url = new URL("http://aiak.or.kr/ka_hims/hapcheon_auction.jsp?barcode="+barcode);
            this.SSLVaildBypass();
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
            con.setRequestMethod("GET");
            con.setDoOutput(false);    

            StringBuilder sb = new StringBuilder();
            log.info("callApiAiak resp code : "+con.getResponseCode());
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                log.info("callApiAiak : "+sb.toString());
                return sb.toString();
            } else {
                log.error(con.getResponseMessage());
            }

        }
        catch (IOException ie) {
        	log.error("IOException - https://aiak.or.kr 테스트 : ", ie);
        }
        catch (Exception e) {
            log.error("Exception - https://aiak.or.kr 테스트 : ",e);
        } finally {
        	try {
                if(con != null)con.disconnect();
                if(br !=null) br.close();
        	}
        	catch (IOException ie) {
            	log.error("IOException - https://aiak.or.kr 테스트 : ", ie);
            }
        	catch(Exception e) {
                log.error("Exception - https://aiak.or.kr 테스트 : ",e);        		
        	}
		}
        return null;
	}

	public String callApiKaKao(String pUrl) {
		BufferedReader br = null;
		HttpURLConnection con = null;
        try {
        	
            URL url = new URL(pUrl);
            this.SSLVaildBypass();
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
            con.setRequestMethod("GET");
            con.setDoOutput(false);    

            StringBuilder sb = new StringBuilder();
            log.info("callApiAiak resp code : "+con.getResponseCode());
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                log.info("callApiKaKao : "+sb.toString());
                return sb.toString();
            } else {
                log.error(con.getResponseMessage());
            }

        } 
        catch (IOException ie) {
            log.error(pUrl+" : ", ie);
        } 
        catch (Exception e) {
            log.error(pUrl+" : ",e);
        } finally {
        	try {
                if(con != null)con.disconnect();
                if(br !=null) br.close();
        	}
        	catch (IOException ie) {
                log.error(pUrl+" : ", ie);
            }
        	catch(Exception e) {
                log.error(pUrl+" : ",e);	
        	}
		}
        return null;
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

	//종축개량 API_TEST
	public String callApiAiakV2(String barcode) {
		BufferedReader br = null;
		HttpURLConnection con = null;
		String result = "";
        try {
			//String tempUrl = "http://api.aiak.or.kr/v1/hims/detailInfo?barcode="+barcode;	
			//if(!port.isEmpty()) {
			//  	tempUrl = "http://api.aiak.or.kr:"+port+"/v1/hims/detailInfo?barcode="+barcode;
			//}
        	String tempUrl = "http://api.aiak.or.kr:9080/v1/hims/detailInfo?barcode="+barcode;
            log.info("callApiAiak tempUrl code : "+tempUrl);
        	URL url = new URL(tempUrl);
            //URL url = new URL("http","api.aiak.or.kr/","9080");
            //if("0".equals(sslFlag)) this.SSLVaildBypass();
            con = (HttpURLConnection) url.openConnection();
            con.setConnectTimeout(5000); //서버에 연결되는 Timeout 시간 설정
            con.setReadTimeout(5000); // InputStream 읽어 오는 Timeout 시간 설정
            con.setRequestMethod("GET");
            con.setRequestProperty("apikey", "KAIA_API8b749c8d2c44700f64f564b5dfd5869a6bbda33c927da182cd515be02b2b0b77");
            con.setDoOutput(false);            

            StringBuilder sb = new StringBuilder();
            log.info("callApiAiak resp code : "+con.getResponseCode());
            log.info("callApiAiak call URL : "+ url.toString());
            log.info("callApiAiak res URL: "+con.getURL());
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
                String line;
                while ((line = br.	readLine()) != null) {
                    sb.append(line).append("\n");
                }
                log.info("callApiAiak : "+sb.toString());
                //return sb.toString();
                result = sb.toString();
            } else {
                log.error(con.getResponseMessage());
            }
        }
        catch (Exception e) {
            log.error("Exception - https://aiak.or.kr 테스트 : ",e);
            //return e.getMessage();
        } finally {
        	try {
                if(con != null)con.disconnect();
                if(br !=null) br.close();
        	}
        	catch(Exception e) {
                log.error("Exception - https://aiak.or.kr 테스트 : ",e);
                //return e.getMessage();
        	}
		}
        return result;
	}
	
	public String getSlaughterInfoApi(Map<String, Object> map) throws Exception{		
		String sendUrl = "http://data.ekape.or.kr/openapi-data/service/user/mtrace/breeding/cattle";
		sendUrl += "?serviceKey=" + openApiServiceKey;
//		sendUrl += "?serviceKey=" + "Z5HnEP8ghGMEUD0ukiBNifYlBV6%2BwI7hxE8hlLI71yY3IirWjvlVwaGsbjRcTWhIzVisaI3%2Fyb4cDhdoa%2BYRcg%3D%3D";
		sendUrl += "&cattleNo=" + map.get("SRA_INDV_AMNNO");
		
		StringBuilder urlBuilder = new StringBuilder(sendUrl);
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = null;
        String result = "";
				
		try {
			log.debug(sendUrl);
			conn = (HttpURLConnection) url.openConnection();
	        conn.setRequestMethod("GET");
	        conn.setRequestProperty("Content-type", "application/json");
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
	       
			
		} catch (UnknownHostException | RuntimeException | SocketTimeoutException e) {
        	log.error(e.getMessage() ,e);
            throw new Exception("서버 수행중 오류가 발생하였습니다.");			
        } catch (Exception e) {
        	log.error(e.getMessage() ,e);
            throw new RuntimeException("서버 수행중 오류가 발생하였습니다.");        	
        } finally {
            if (conn!= null) conn.disconnect();
        }
		 	
        return result;
    }

	public Map<String,Object> callApiAiakV2ForMap(String barcode) {
		Map<String, Object> result = new HashMap<String, Object>();
		String apiResult = this.callApiAiakV2(barcode);
		if(!apiResult.isEmpty()) {
			JSONObject jObj = new JSONObject(apiResult);
			if(jObj != null) {
				JSONObject resultSt  = jObj.getJSONObject("resultStatus");
				if("OK".equals(resultSt.get("code"))) {
					JSONObject resultVal  = jObj.getJSONObject("resultValue");
					JSONArray posterityInfo = resultVal.getJSONArray("posterity_info");
					JSONArray siblingInfo = resultVal.getJSONArray("sibling_info");
					JSONObject detailInfo  = resultVal.getJSONObject("detail_info");
					JSONObject bloodInfo  = resultVal.getJSONObject("pedigree_info");

					List<Map<String, Object>> sibInfo = new ArrayList<>(); //형매 리스트
					List<Map<String, Object>> postInfo = new ArrayList<>(); //후대 리스트
					
					String balBb = detailInfo.getString("bv");
					result.put("BV_GRD_1", balBb.substring(0, 1)); //냉도체중
					result.put("BV_VAL_1", balBb.substring(1, 10));
					result.put("BV_GRD_2", balBb.substring(10, 11)); //배최장근단면적
					result.put("BV_VAL_2", balBb.substring(11, 20));
					result.put("BV_GRD_3", balBb.substring(20, 21)); //등지방두께
					result.put("BV_VAL_3", balBb.substring(21, 30));
					result.put("BV_GRD_4", balBb.substring(30, 31)); //근내지방도
					result.put("BV_VAL_4", balBb.substring(31, 40));
					
					//개체식별번호
					result.put("FCOW_SRA_INDV_AMNNO", bloodInfo.getString("sire_barcode")); //부
					result.put("MCOW_SRA_INDV_AMNNO", bloodInfo.getString("dam_barcode")); //모
					result.put("GRFA_SRA_INDV_AMNNO", bloodInfo.getString("pgs_barcode")); //조부
					result.put("GRMO_SRA_INDV_AMNNO", bloodInfo.getString("pgd_barcode")); //조모
					result.put("MTGRFA_SRA_INDV_AMNNO", bloodInfo.getString("mgs_barcode")); //외조부
					result.put("MTGRMO_SRA_INDV_AMNNO", bloodInfo.getString("mgd_barcode")); //외조모
					//KPN 번호
					result.put("FCOW_SRA_KPN_NO", bloodInfo.getString("sire_name"));
					result.put("GRFA_SRA_KPN_NO", bloodInfo.getString("pgs_name"));
					result.put("MTGRFA_SRA_KPN_NO", bloodInfo.getString("mgs_name"));
					
					//형매정보 저장
					if(!siblingInfo.isEmpty()) {
						siblingInfo.forEach(item ->{
							if(item instanceof JSONObject) {
								Map<String, Object> map = new HashMap<String, Object>();
								JSONObject obj = (JSONObject) item;
								map.put("SRA_INDV_AMNNO", barcode);
								map.put("SIB_SRA_INDV_AMNNO", obj.get("barcode"));
								map.put("BIRTH", obj.get("birthdate"));
								map.put("RG_DSC", obj.get("reggu"));
								map.put("INDV_SEX_C", obj.get("sex"));
								map.put("BIRTH", obj.get("birthdate"));
								sibInfo.add(map);
							}
						});					
					}
					result.put("sibInfo", sibInfo);
					
					//후대정보 저장
					if(!posterityInfo.isEmpty()) {
						posterityInfo.forEach(item ->{
							if(item instanceof JSONObject) {
								Map<String, Object> map = new HashMap<String, Object>();
								JSONObject obj = (JSONObject) item;
								map.put("SRA_INDV_AMNNO", barcode);
								map.put("POST_SRA_INDV_AMNNO", obj.get("barcode"));
								map.put("BIRTH", obj.get("birthdate"));
								map.put("RG_DSC", obj.get("reggu"));
								map.put("INDV_SEX_C", obj.get("sex"));
								map.put("BIRTH", obj.get("birthdate"));
								postInfo.add(map);
							}
						});					
					}
					result.put("postInfo", postInfo);
					
				}
			}
		}
		log.debug("retun Data",result);
		return result;
	}
}