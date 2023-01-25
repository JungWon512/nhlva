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
	String ctrn_cd = "";// 거래코드
	String responseBody = "";

	@Value("${mca.url}")
	private String mcaUrl;
	
	@Value("${spring.profiles.active}")
	private String profile;

	// 전문 보내고 받기
	public Map<String, Object> mcaSendMsg(String ctgrm_cd, int io_all_yn, String ccls_cd, String data) throws Exception {
		responseBody = "";
		// 보낸메시지
		String sendMsg = setSendMsg(ctgrm_cd, io_all_yn, ccls_cd, data);
		// 전문보내기
		sendPostJson(sendMsg);
		// 받은메시지
		String recvMsg = responseBody;
		
//		if ("local".equals(profile)) {
//			recvMsg = this.localResponse(ctgrm_cd);
//		}
//		String recvMsg = "{\"header\":{\"CSTX\":\"2\",\"CTGRM_CD\":\"2201\",\"CTGRM_CHSNB\":\"00FD68_36918535\",\"CCRT_DATE\":\"20220930\",\"CCRT_TIME\":\"151325\",\"CORG_CD\":\"NHAH\",\"CBSN_CD\":\"FD68\",\"CCLS_CD\":\"0210\",\"CTRN_CD\":\"IFLM0022\",\"CSPR_CD\":\"O\",\"CTGRM_TRNSM_DATETIME\":\"20220930151325\",\"CTGRM_RSP_CD\":\"0000\",\"CRSRVD\":\"                        \"},\"data\":{\"INQ_CN\":1,\"SRA_SRS_DSC\":\"01\",\"SRA_INDV_AMNNO\":\"410000000167347     \",\"FHS_ID_NO\":\"267854         \",\"SRA_INDV_ID_NO\":\"410000000167347\",\"FARM_AMNNO\":\"00000001\",\"KRBF_IPRV_RG_NO\":\"031901008006734     \",\"SRA_INDV_DSC\":\"02   \",\"SRA_INDV_KN_C\":\"01\",\"SRA_INDV_STSC\":\"01   \",\"SRA_INDV_AGNC_ORGNM\":\"                                                  \",\"SRA_INDV_SRSNM\":\"우                                                \",\"SRA_INDV_AGNC_ORG_C\":\"          \",\"INDV_SEX_C\":\"1\",\"SRA_INDV_OWN_DSC\":\"1    \",\"SRA_INDV_BIRTH\":\"20060515\",\"SRA_INDV_BYNG_CLNTNM\":\"                    \",\"SRA_INDV_BYNG_WGH\":\"0000000\",\"SRA_INDV_BYNG_PCS\":\"000000000000000000\",\"SRA_INDV_BYNG_SXPS\":\"               \",\"SRA_INDV_BYNG_DT\":\"        \",\"SRA_INDV_BIR_RGN_C\":\"    \",\"SRA_INDV_BIR_PROV_C\":\"10\",\"SRA_INDV_BIR_CCO_C\":\"131 \",\"SRA_INDV_BRD_PLCNM\":\"                                                  \",\"SRA_INDV_FIR_AGNC_ORG_C\":\"          \",\"SRA_INDV_ESG_BRAN_NO\":\"                    \",\"SRA_INDV_OSLF_AMNNO\":\"00000000\",\"SRA_INDV_BUYMNM\":\"                                                  \",\"SRA_INDV_BYAM\":\"00000000000000000000000000000000000000000000000.00\",\"SRA_INDV_INF_RG_DSC\":\"                 \",\"SRA_INDV_INF_RG_METHC\":\"  \",\"SRA_INDV_BTC_DEAD_DT\":\"        \",\"SRA_INDV_OWNRNM\":\"                    \",\"SRA_FHS_FZIP\":\"476\",\"SRA_FHS_RZIP\":\"871\",\"SRA_FHS_ZIP_SQNO\":\"0000000000\",\"SRA_FHS_DONGUP\":\"경기                                                                            \",\"SRA_FHS_DONGBW\":\"                                                                                                    \",\"SRA_FHS_TEL_NAT_NO\":\"082\",\"SRA_FHS_ATEL\":\"    \",\"SRA_FHS_HTEL\":\"    \",\"SRA_FHS_STEL\":\"    \",\"SRA_INDV_BRD_MTCN\":\"00000000000.000\",\"BTC_DT\":\"        \",\"BCHNM\":\"                                                  \",\"SRA_INDV_BRDSRA_RG_NO\":\"                    \",\"SRA_INDV_BRDSRA_RG_DSC\":\"09\",\"SRA_INDV_BRDSRA_RG_DT\":\"        \",\"SRA_INDV_CASTR_METHC\":\"  \",\"CASTR_DT\":\"        \",\"SRA_INDV_MOTHR_MATIME\":\"00000000000.000\",\"SRA_INDV_LFTM_WGH\":\"0000000\",\"SRA_INDV_LS_MATIME\":\"0000000\",\"SRA_IPRV_INDV_NAPN_CNTN\":\"031901008006734                                                                                     \",\"SRA_INDV_MNG_INDVNO\":\"1766                \",\"SRA_INDV_RFID_NO\":\"                    \",\"SRA_INDV_FIR_HYBZ_DD\":\"0       \",\"SRA_INDV_FIR_PTUR_DD\":\"        \",\"SRA_INDV_NA_BYNG_DSC\":\"        \",\"SRA_INDV_LVST_BUYTN_WGH\":\"0000000\",\"SRA_INDV_AQ_AM\":\"000000000000000\",\"SRA_INDV_SOG_DT\":\"               \",\"SRA_INDV_BFC_UPR\":\"               \",\"SRA_INDV_SLAM\":\"000000000000000\",\"SRA_INDV_ACD_RG_DT\":\"        \",\"SRA_INDV_FIR_AFISM_MOD_DT\":\"        \",\"SRA_INDV_PDAA_C\":\"      \",\"SRA_INDV_LVST_MAD_EXPI_DT\":\"        \",\"SRA_INDV_DEAD_CAS_CNTN\":\"                                                                                                                                                                                                        \",\"SRA_INDV_TRU_BRD_DSC\":\"             \",\"SRA_INDV_TRU_BRD_NA_TRPL_C\":\"             \",\"SRA_INDV_BRN_C\":\"      \",\"SRA_INDV_BRNNM\":\"                  \",\"SRA_INDV_BDNM\":\"                                                  \",\"SRA_INDV_BRMNM\":\"                                                  \",\"SRA_INDV_SBJC\":\"     \",\"SRA_INDV_SRGR_C\":\"     \",\"SRA_INDV_SRGNM\":\"                    \",\"SRA_INDV_LED_STSC\":\"     \",\"SRA_INDV_MNBR_TRF_YN\":\" \",\"FSRG_DTM\":\"2007-05-31 22:\",\"FSRGMN_ENO\":\"119320686\",\"FSRGMN_NA_BZPLC\":\"8808990671406\",\"LSCHG_DTM\":\"2021-11-11 15\",\"LS_CMENO\":\"BT_SYSTEM\",\"LSCGMN_NA_BZPLC\":\"9999999999999\",\"SRA_INDV_SRCH_EART_NO\":\"6734     \",\"SRA_INDV_EXST_EART_NO\":\"000167347\",\"SRA_INDV_EXST_BRDSRA_RG_NO\":\"                    \",\"SRA_INDV_EART_ANW_ADHR_DSC\":\"08   \",\"SRA_INDV_EARSTK_DT\":\"        \",\"SRA_INDV_EART_OMS_DT\":\"        \",\"SRA_INDV_BRAN_NO\":\"000000000000000\",\"SRA_INDV_RPEM_NO\":\"                    \",\"SRA_INDV_RPEM_STCK_DT\":\"        \",\"SRA_INDV_RSTCK_EART_OMS_DT\":\"        \",\"SRA_INDV_RSTCK_EART_NO\":\"        \",\"SRA_INDV_RSTCK_EART_NO_STCK_DT\":\"        \",\"FCOW_SRA_INDV_EART_NO\":\"               \",\"SRA_INDV_FCOW_BRDSRA_RG_NO\":\"                    \",\"SRA_INDV_MCOW_BRDSRA_RG_DSC\":\"     \",\"MCOW_SRA_INDV_EART_NO\":\"               \",\"SRA_INDV_MCOW_BRDSRA_RG_NO\":\"                    \",\"SRA_INDV_ADVC_RG_OMS_YN\":\" \",\"SRA_INDV_PDG_RG_CNTN\":\"                                                  \",\"SRA_INDV_PDG_RG_C\":\"     \",\"CALF_PTUR_RTO\":\"00000000000.000\",\"CALF_PD_STBZ_PTC_YN\":\"0\",\"SRA_INDV_KBCS_BCD\":\"         \",\"SRA_INDV_PRLF_COW_PD_YN\":\"0\",\"SRA_INDV_ADVC_RG_OMS_CNTN\":\"                                                                                                    \",\"SRA_INDV_ADVC_RG_DT\":\"        \",\"SRA_INDV_PTUR_QCN\":\"0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000\",\"SRA_INDV_BRCL_CTFW_NO\":\"                              \",\"SRA_INDV_BRCL_CTFW_ISU_DT\":\"        \",\"SRA_INDV_BRCL_ISP_DT\":\"        \",\"SRA_INDV_PDG_RG_WGH\":\"000000000000.000\",\"SRA_INDV_PASG_CNTN\":\"                                                                                                    \",\"SRA_INDV_PASG_QCN\":\"000000000\",\"SRA_INDV_RG_BRC\":\"171502\",\"SRA_INDV_AMN_ACO_C\":\"171502\",\"SRA_INDV_AMN_COP_C\":\"171502\",\"SRA_INDV_BLDR_RTO\":\"00000000000.000\",\"SRA_INDV_AFISM_MOD_METHC\":\"     \",\"SRA_KPN_NO\":\"                    \",\"SRA_INDV_KPN_GRD\":\"000000\",\"SRA_INDV_BEF_HST_PTC_YN\":\"0\",\"SRA_INDV_BEF_HST_PTC_DT\":\"        \",\"ADT_SRA_INDV_EART_NO\":\"               \",\"RP_DT\":\"               \",\"SRA_INDV_RG_CST\":\"00000000000000.00\",\"SRA_INDV_RSN_DT\":\"                 \",\"SRA_INDV_RSN_WGH\":\"000000000000.000\",\"SRA_INDV_MOD_PGID\":\"                    \",\"NA_TRPL_C\":\"2500004978094\",\"NA_CUSNO\":\"1103611683\",\"NA_REP_CUSNO\":\"1103611683\",\"CORE_FHS_YN\":\"0\",\"SRA_FHS_DSC\":\"02   \",\"SRA_FHSNM\":\"경기0130                                          \",\"SRA_FHS_RG_DT\":\"20080130\",\"SRA_FHS_REPMNM\":\"테**                                              \",\"SRA_FHS_BZNO\":\"          \",\"SRA_FHS_FAX_RGNO\":\"    \",\"SRA_FHS_HFAX\":\"    \",\"SRA_FHS_FAX_SQNO\":\"    \",\"SRA_FHS_REP_MPSVNO\":\"    \",\"SRA_FHS_REP_MPHNO\":\"    \",\"SRA_FHS_REP_MPSQNO\":\"    \",\"SRA_FHS_EMAIL_ADR\":\"    \",\"RMK_CNTN\":\"                                                                                                                                                                                                        \",\"SRA_FHS_AMN_YN\":\"1\",\"DEL_YN\":\" \",\"SRA_FHS_CIF_CNF_YN\":\"1\",\"SRA_FHS_USRID\":\"              \",\"METRB_FHS_NO\":\"              \",\"SRA_FHS_MOD_PGID\":\"LAKC1000            \",\"FARM_ID_NO\":\"          \",\"SRA_FARM_OPER_STSC\":\"1 \",\"SRA_FARM_RG_BRC\":\"171502\",\"SRA_FARM_PGROOM_EQNM\":\"                                                  \",\"SRA_FARM_AMRNM\":\"**                                                \",\"SRA_FARM_AMN_TEL_NAT_NO\":\"082\",\"SRA_FARM_AMN_ATEL\":\"    \",\"SRA_FARM_AMN_HTEL\":\"    \",\"SRA_FARM_AMN_STEL\":\"    \",\"SRA_FARM_FZIP\":\"476\",\"SRA_FARM_RZIP\":\"871\",\"SRA_FARM_ZIP_SQNO\":\"0000000000\",\"SRA_FARM_DONGUP\":\"경기                                                                            \",\"SRA_FARM_DONGBW\":\"                                                                                                    \",\"SRA_FARM_BNK_C\":\"12\",\"SRA_FARM_ACNO\":\"                    \",\"SRA_FARM_DPRNM\":\"                                                  \",\"SRA_FARM_PSTMTT_DPZ_YN\":\"1\",\"SRA_FHS_MACO_ENT_YN\":\"0\",\"MBR_BRC\":\"      \",\"FANG_ABIT_AMNNO\":\"                    \",\"METRB_FARM_NO\":\"          \",\"BRNM\":\"양평축산농협                            \"}}";
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
		setData(ctgrm_cd);

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
	public boolean sendPostJson(String jsonValue) throws Exception {

		URL url = new URL(mcaUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		boolean result = true;
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
				StringBuffer sb = new StringBuffer();
				BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
				for (String line; (line = reader.readLine()) != null;) {
					sb.append(line).append("\n");
				}
				responseBody = sb.toString();
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

		return result;
	}
	
	// 전문유형코드별 설정
	public void setData(String ctgrm_cd) {
		if(!StringUtils.isEmpty(ctgrm_cd)) {
			ctrn_cd = "IFLM00" + ctgrm_cd.substring(0, 2);
		}
	}
	
	public String localResponse(String ctgrm_cd) {
		final String fileResource = "C:\\workspace\\adv\\nhlva\\src\\main\\resources\\static\\json\\" + ctgrm_cd + ".json";
		String str = "";;
		try {
			str = StringUtils.join(Files.readAllLines(Paths.get(fileResource)), "");
		} catch (IOException e) {
			e.printStackTrace();
			return "";
		}
		return str;
	}

}
