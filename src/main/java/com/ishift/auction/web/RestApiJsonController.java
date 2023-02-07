package com.ishift.auction.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jettison.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;

import com.google.gson.Gson;

@Controller
public class RestApiJsonController {

	private static Logger logger = LoggerFactory.getLogger(RestApiJsonController.class);
	//String ctrn_cd = "";// 거래코드
	//String responseBody = "";

	@Value("${mca.url}")
	private String mcaUrl;
	
	@Value("${spring.profiles.active}")
	private String profile;

	// 전문 보내고 받기
	public Map<String, Object> mcaSendMsg(String ctgrm_cd, int io_all_yn, String ccls_cd, String data) throws Exception {
		// 보낸메시지
		String sendMsg = setSendMsg(ctgrm_cd, io_all_yn, ccls_cd, data);
		// 전문보내기
		// 받은메시지
		String recvMsg = sendPostJson(sendMsg);
		
//		if ("local".equals(profile)) {
//			recvMsg = this.localResponse(ctgrm_cd);
//		}
		logger.info(recvMsg);
		Map<String, Object> map = new HashMap<String, Object>();
		// 받은메시지를 map에 담기
		if (recvMsg.length() > 0) {
			map = getMapFromJsonObject(recvMsg);
		} else {
			map.put("jsonHeader", "Error");
			map.put("dataCnt", "0");
			map.put("jsonList", null);
		}
		return map;
	}

	// 전문 전체(header + body)
	public String setSendMsg(String ctgrm_cd, int io_all_yn, String ccls_cd, String data) throws Exception {
		String header = setSndHeader(ctgrm_cd, ccls_cd, io_all_yn);
		StringBuffer str = new StringBuffer();
		str.append("{");
		str.append(header);
		str.append(",");
		str.append("\"data\":{");
		str.append(data);
		str.append("}");
		str.append("}");
		logger.info("mca sendMsg >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>\n" + str);
		return str.toString();
	}

	// 전문 header
	public String setSndHeader(String ctgrm_cd, String ccls_cd, int io_all_yn) throws Exception {

		String cspr_cd = "I";
		if ("0210".equals(ccls_cd)) {
			cspr_cd = "O";
		}
		String ctrn_cd = setData(ctgrm_cd);

		String ctgrm_chsnb = "";// 전문 추적번호
		Calendar calendar = Calendar.getInstance();
		Date calcDate = calendar.getTime();
		String ccrt_date = new SimpleDateFormat("yyyyMMdd").format(calcDate);
		String ccrt_time = new SimpleDateFormat("HHmmss").format(calcDate);
		String ctgrm_trnsm_datetime = new SimpleDateFormat("yyyyMMddHHmmss").format(calcDate);

		Random numGen = SecureRandom.getInstance("SHA1PRNG");
		ctgrm_chsnb = "00FD68_" + String.format("%08d", numGen.nextInt(99999999));

		StringBuffer sb = new StringBuffer();
		sb.append("\"header\":{");
		sb.append("\"CSTX\":\"\\u0002\"");
		sb.append(",\"CTGRM_CD\":\"" + ctgrm_cd + "\"");
		sb.append(",\"CTGRM_CHSNB\":\"" + ctgrm_chsnb + "\"");
		sb.append(",\"CCRT_DATE\":\"" + ccrt_date + "\"");
		sb.append(",\"CCRT_TIME\":\"" + ccrt_time + "\"");
		sb.append(",\"CORG_CD\":\"NHAH\"");
		sb.append(",\"CBSN_CD\":\"FD68\"");
		sb.append(",\"CCLS_CD\":\"" + ccls_cd + "\"");
		sb.append(",\"CTRN_CD\":\"" + ctrn_cd + "\"");
		sb.append(",\"CSPR_CD\":\"" + cspr_cd + "\"");
		sb.append(",\"CTGRM_TRNSM_DATETIME\":\"" + ctgrm_trnsm_datetime + "\"");
		sb.append(",\"CTGRM_RSP_CD\":\"0000\"");
		if ("2200".equals(ctgrm_cd) || // 개체이력(4700으로 변경 예정)
			"4700".equals(ctgrm_cd) || // 개체이력
			"4800".equals(ctgrm_cd) || // 형매정보
			"4900".equals(ctgrm_cd) // 후대정보
			) { // 개체이력조회
			if (io_all_yn == 1) { // 전체여부
				sb.append(",\"CRSRVD\":\"                       1\"");
			} else {
				sb.append(",\"CRSRVD\":\"                       0\"");
			}
		} else {
			sb.append(",\"CRSRVD\":\"                        \"");
		}
		sb.append("}");

		logger.info("mca Header >>>>>>>>>>>>>>>>>>>>>>>> " + sb);
		return sb.toString();
	}

	// 리턴 받은 전문에서 header, data 가져오기
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public Map<String, Object> getMapFromJsonObject(String jsonObj) throws JSONException {

		Map<String, Object> map = new HashMap<String, Object>();

		Gson gson = new Gson();

		Map<String, Object> jsonObject = new HashMap<String, Object>();
		jsonObject = gson.fromJson(jsonObj, jsonObject.getClass());

		Map<String, Object> jsonHeader = (Map) jsonObject.get("header");
		Map<String, Object> jsonData = (Map) jsonObject.get("data");
		List<Map<String, Object>> jsonList = null;
		if (jsonData.containsKey("RPT_DATA")) {
			jsonList = (List) jsonData.get("RPT_DATA");
		}

		int cnt = 0;

		if (jsonData.containsKey("IO_ROW_CNT")) {
			cnt = (int) (double) jsonData.get("IO_ROW_CNT");
		}

		logger.info("mca receive >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + jsonObj);
		logger.info("mca receive data to map >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		logger.info("jsonHeader : " + jsonHeader.toString());
		logger.info("dataCnt : " + cnt);
		logger.info("jsonData : " + jsonData.toString());
		logger.info("mca receive data to map <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");

		map.put("jsonHeader", jsonHeader);
		map.put("dataCnt", cnt);
		map.put("jsonData", jsonData);
		map.put("jsonList", jsonList);

		return map;
	}

	// 전문전송
	public String sendPostJson(String jsonValue) throws Exception {
		StringBuffer sb = new StringBuffer();
		URL url = new URL(mcaUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		try {
			logger.debug("REST API START");

			byte[] sendData = jsonValue.getBytes("UTF-8");

			con.setDoOutput(true);
			con.setRequestMethod("POST");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Pragma", "no-cache");
			con.setRequestProperty("Content-Type", "application/json");
			con.setRequestProperty("Accept-Charset", "UTF-8");
			con.setRequestProperty("Content-Length", String.valueOf(sendData.length));
			con.setConnectTimeout(1000);// 서버 접속시 연결시간
			con.setReadTimeout(1000);// Read시 연결시간
			con.getOutputStream().write(sendData);
			con.getOutputStream().flush();
			con.connect();

			int responseCode = con.getResponseCode();
			String responseMessage = con.getResponseMessage();

			logger.debug("REST API responseCode : " + responseCode);
			logger.debug("REST API responseMessage : " + responseMessage);

			if (con.getResponseCode() == 301 || con.getResponseCode() == 302 || con.getResponseCode() == 307) // 300번대
																												// 응답은
																												// redirect
			{
//                String location = con.getHeaderField("Location");
//                con.disconnect();
//                return doJson(new URL(location), param, json, charset);
			} else {
				
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				for (String line; (line = reader.readLine()) != null;) {
					sb.append(line).append("\n");
				}
				//responseBody = sb.toString();
			}

			con.disconnect();

			logger.debug("REST API END");

		} catch (RuntimeException e) {
			logger.debug(e.getMessage());
		} catch (Exception e) {
			logger.debug(e.getMessage());
		} finally {
			if (con != null)
				con.disconnect();
		}

		return sb.toString();
	}
	
	// 전문유형코드별 설정
	public String setData(String ctgrm_cd) {
		String ctrn_cd = "";
		if(!StringUtils.isEmpty(ctgrm_cd)) {
			ctrn_cd = "IFLM00" + ctgrm_cd.substring(0, 2);
		}
		return ctrn_cd;
	}
	
	public String localResponse(String ctgrm_cd) {
		final String fileResource = "C:\\workspace\\adv\\nhlva\\src\\main\\resources\\static\\json\\" + ctgrm_cd + ".json";
		String str = "";;
		try {
			str = StringUtils.join(Files.readAllLines(Paths.get(fileResource)), "");
		} catch (IOException e) {
			logger.error("RestApiJsonController.localResponse : {} ", e);
			return "";
		}
		return str;
	}

}
