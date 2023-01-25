package com.ishift.auction.web;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class TradeMcaMsgDataController {

	private static Logger log = LoggerFactory.getLogger(TradeMcaMsgDataController.class);
	
	@Autowired
	RestApiJsonController rest;

	public Map<String, Object> tradeMcaMsg(String ctgrm_cd, Map<String, Object> paraMap) throws Exception {

		log.info(" ################# TradeMcaMsgDataController :tradeMcaMsg() START #################");

		Map<String, Object> sb = new HashMap<String, Object>();

		StringBuffer data = new StringBuffer();

		// 전문 중 data 내용 만들기
		// 2200 : 개체이력 수신(4700으로 변경 예정)
		// 4700 : 개체이력 수신
		// 4900 : 후대정보 수신
		if ("2200".equals(ctgrm_cd) || "4700".equals(ctgrm_cd) || "4900".equals(ctgrm_cd)) {
			data.append("\"SRA_INDV_AMNNO\":\"" + padLeftBlank((String) paraMap.get("SRA_INDV_AMNNO"), 20) + "\"");
		}
		// 형매정보 수신
		else if ("4800".equals(ctgrm_cd)) {
			data.append("\"SRA_INDV_AMNNO\":\"" + padLeftBlank((String) paraMap.get("SRA_INDV_AMNNO"), 20) + "\"");
			data.append(",\"MCOW_SRA_INDV_AMNNO\":\"" + padLeftBlank((String) paraMap.get("MCOW_SRA_INDV_AMNNO"), 20) + "\"");
		}
		// 4200 : 분만정보
		else if ("4200".equals(ctgrm_cd)) {
			data.append("\"RC_NA_TRPL_C\":\"" + padLeftBlank((String) paraMap.get("RC_NA_TRPL_C"), 13) + "\"");
			data.append(",\"INDV_ID_NO\":\"" + padLeftBlank((String) paraMap.get("INDV_ID_NO"), 15) + "\"");
		}
		// 4300 : 개체친자여부수신
		else if ("4300".equals(ctgrm_cd)) {
			data.append("\"RC_NA_TRPL_C\":\"" + padLeftBlank((String) paraMap.get("RC_NA_TRPL_C"), 13) + "\"");
			data.append(",\"INDV_ID_NO\":\"" + padLeftBlank((String) paraMap.get("INDV_ID_NO"), 15) + "\"");
		}
		// 5100 : 알림톡 발송
		else if("5100".equals(ctgrm_cd)) {
			data.append("\"IO_TGRM_KEY\":\""    + padLeftBlank(""+(String)paraMap.get("IO_TGRM_KEY"),10)    + "\"");
			data.append(",\"ADJ_BRC\":\"" + padLeftBlank((String)paraMap.get("ADJ_BRC"), 6) + "\"");
			data.append(",\"RLNO\":\"" + padLeftBlank((String)paraMap.get("RLNO"), 13) + "\"");
			data.append(",\"IO_DPAMN_MED_ADR\":\"" + padLeftBlank((String)paraMap.get("IO_DPAMN_MED_ADR"), 80) + "\"");
			data.append(",\"IO_SDMN_MED_ADR\":\"" + padLeftBlank((String)paraMap.get("IO_SDMN_MED_ADR"), 80) + "\"");
			data.append(",\"IO_TIT\":\"" + padLeftBlank((String)paraMap.get("IO_TIT"), 40) + "\"");
			data.append(",\"KAKAO_TPL_C\":\"" + padLeftBlank((String)paraMap.get("KAKAO_TPL_C"), 30) + "\"");
			data.append(",\"KAKAO_MSG_CNTN\":\"" + padLeftBlank((String)paraMap.get("KAKAO_MSG_CNTN"), 4000)  + "\"");
			data.append(",\"FBK_UYN\":\"" + padLeftBlank((String)paraMap.get("FBK_UYN"), 1) + "\"");
			data.append(",\"FBK_MSG_DSC\":\"" + padLeftBlank((String)paraMap.get("FBK_MSG_DSC"), 1) + "\"");
			data.append(",\"FBK_TIT\":\"" + padLeftBlank((String)paraMap.get("FBK_TIT"), 20) + "\"");
			data.append(",\"IO_ATGR_ITN_TGRM_LEN\":\"" + padLeftBlank(paraMap.get("IO_ATGR_ITN_TGRM_LEN").toString(), 4) + "\"");
			data.append(",\"UMS_FWDG_CNTN\":\"" + padLeftBlank(paraMap.get("UMS_FWDG_CNTN").toString(), 4000) + "\"");
		}

		int io_all_yn = 0;

		if (paraMap.containsKey("IO_ALL_YN")) {
			if ("1".equals((String) paraMap.get("IO_ALL_YN"))) {
				io_all_yn = 1;
			}
		}
		sb = rest.mcaSendMsg(ctgrm_cd, io_all_yn, "0200", data.toString());
		log.info(sb.toString());
		return sb;
	}

	// 공백
	public String padLeftBlank(String inputString, int length) throws UnsupportedEncodingException {
		if (inputString == null) {
			inputString = "";
		}
		byte[] inputByte = inputString.getBytes("EUC-KR");

		int byteLen = inputByte.length;
		if (byteLen == length) {
			return inputString;
		} else if (byteLen > length) {
			StringBuilder stringBuilder = new StringBuilder(length);
			int nCnt = 0;
			for (char ch : inputString.toCharArray()) {
				nCnt += String.valueOf(ch).getBytes("EUC-KR").length;
				if (nCnt > length)
					break;
				stringBuilder.append(ch);
			}
			return stringBuilder.toString();
		}
		StringBuilder sb = new StringBuilder();
		sb.append(inputString);
		while (sb.toString().getBytes("EUC-KR").length < length) {
			sb.append(" ");
		}
		return sb.toString();
	}

	// 공백 제로
	public String padLeftZero(String inputString, int length) throws UnsupportedEncodingException {
		if (inputString == null) {
			inputString = "";
		}
		byte[] inputByte = inputString.getBytes("EUC-KR");

		int byteLen = inputByte.length;
		if (byteLen == length) {
			return inputString;
		} else if (byteLen > length) {
			StringBuilder stringBuilder = new StringBuilder(length);
			int nCnt = 0;
			for (char ch : inputString.toCharArray()) {
				nCnt += String.valueOf(ch).getBytes("EUC-KR").length;
				if (nCnt > length)
					break;
				stringBuilder.append(ch);
			}
			return stringBuilder.toString();
		}
		StringBuilder sb = new StringBuilder();
		// while(sb.length() < length - inputString.length()) {
		while (sb.toString().getBytes("EUC-KR").length < length - byteLen) {
			sb.append("0");
		}
		sb.append(inputString);
		return sb.toString();
	}

}
