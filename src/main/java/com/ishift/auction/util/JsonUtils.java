package com.ishift.auction.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SuppressWarnings("unchecked")
public class JsonUtils<T> {
	
	/**
	 * Map을 JSONString으로 변환
	 * 
	 * @param map
	 * @return String
	 */
	public static String getJsonStringFromMap(Map<String, Object> map) {
		JSONObject json = new JSONObject();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			json.put(entry.getKey(), (entry.getValue() == null ? "" : entry.getValue()).toString());
		}
		return json.toJSONString();
	}

	/**
	 * List<Map>을 JSONString으로 변환
	 * 
	 * @param list
	 * @return String
	 */
	public static String getJsonStringFromList(List<Map<String, Object>> list) {
		JSONArray jsonArray = new JSONArray();
		for (Map<String, Object> map : list) {
			jsonArray.add(getJsonStringFromMap(map));
		}
		return jsonArray.toString();
	}
	
	/**
	 * String을 JSONObject를 변환
	 * 
	 * @param jsonStr
	 * @return jsonObject
	 */
	public static JSONObject getJsonObjectFromString(String jsonStr) { 
		JSONObject jsonObject = new JSONObject();
		JSONParser jsonParser = new JSONParser();
		try {
			jsonObject = (JSONObject)jsonParser.parse(jsonStr);
		}
		catch (ParseException e) {
			log.error(e.getMessage());
			return null;
		}
		return jsonObject;
	}
	
	/**
	 * JSONObject를 Map<String, String>으로 변환
	 * 
	 * @param jsonObject
	 * @return map
	 */
	public static Map<String, Object> getMapFromJsonObject(JSONObject jsonObject) { 
		Map<String, Object> map = null;
		try {
			map = new ObjectMapper().readValue(jsonObject.toJSONString(), Map.class);
		}
		catch (JsonParseException e) {
			log.error(e.getMessage());
			return null;
		}
		catch (JsonMappingException e) {
			log.error(e.getMessage());
			return null;
		}
		catch (IOException e) {
			log.error(e.getMessage());
			return null;
		}
		return map;
	}
	
	/**
	 * JSONArray를 List<Map<String, String>>으로 변환
	 * @param jsonArray
	 * @return list
	 */
	public static List<Map<String, Object>> getListMapFromJsonArray(JSONArray jsonArray) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (jsonArray != null) {
			int jsonSize = jsonArray.size();
			for (int i = 0; i < jsonSize; i++) {
				Map<String, Object> map = getMapFromJsonObject((JSONObject)jsonArray.get(i));
				list.add(map);
			}
		}
		return list;
	}
	
	
	/**
	 * JsonString을 List<Map<String, Object>>로 변환
	 * @param jsonStr
	 * @return
	 */
	public static List<Map<String, Object>> getListMapFromJsonString(String jsonStr) throws JsonProcessingException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		try {
			ObjectMapper mapper = new ObjectMapper();
			list = mapper.readValue(jsonStr, ArrayList.class);
		}
		catch (JsonProcessingException e) {
			log.error(e.getMessage());
			throw e;
		}
		return list;
	}
	
// [{"LED_SQNO":"1","SRA_SBID_UPR":"364","SRA_SBID_AM":"3640000","LVST_AUC_PTC_MN_NO":"1","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"22","LOWS_SBID_LMT_AM":"3630000.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"928","OSLP_NO":"2","LSCHG_DTM":"2021-11-04 19:45:30.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"23","LOWS_SBID_LMT_AM":"1910000.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"3","LSCHG_DTM":"2021-11-04 17:15:31.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"4","LSCHG_DTM":"2021-11-04 15:00:06.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"9","LSCHG_DTM":"2021-11-04 15:03:04.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"10","LSCHG_DTM":"2021-11-04 15:03:42.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"11","LSCHG_DTM":"2021-11-04 15:05:58.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"12","LSCHG_DTM":"2021-11-04 15:06:41.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"23","LOWS_SBID_LMT_AM":"2030000.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"13","LSCHG_DTM":"2021-11-04 18:24:36.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"189","SRA_SBID_AM":"1890000","LVST_AUC_PTC_MN_NO":"1","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"22","LOWS_SBID_LMT_AM":"1840000.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"928","OSLP_NO":"14","LSCHG_DTM":"2021-11-04 19:41:56.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"15","LSCHG_DTM":"2021-11-04 15:37:13.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"16","LSCHG_DTM":"2021-11-04 15:41:54.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"17","LSCHG_DTM":"2021-11-04 09:57:28.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"19","LSCHG_DTM":"2021-11-04 09:43:11.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"21","LSCHG_DTM":"2021-11-04 09:52:11.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"22","LSCHG_DTM":"2021-11-04 09:43:43.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"23","LSCHG_DTM":"2021-11-04 09:42:03.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"24","LSCHG_DTM":"2021-11-04 09:41:28.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"25","LSCHG_DTM":"2021-11-04 09:51:30.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"26","LSCHG_DTM":"2021-11-04 09:47:38.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"28","LSCHG_DTM":"2021-11-04 09:57:53.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"29","LSCHG_DTM":"2021-11-04 09:58:22.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"30","LSCHG_DTM":"2021-11-04 09:25:03.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"31","LSCHG_DTM":"2021-11-04 09:25:37.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"192","SRA_SBID_AM":"1920000","LVST_AUC_PTC_MN_NO":"1","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"22","LOWS_SBID_LMT_AM":"1920000.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"928","OSLP_NO":"32","LSCHG_DTM":"2021-11-04 19:45:05.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"33","LSCHG_DTM":"2021-11-04 09:26:45.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"34","LSCHG_DTM":"2021-11-04 09:27:20.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"35","LSCHG_DTM":"2021-11-04 09:27:50.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"36","LSCHG_DTM":"2021-11-04 09:28:21.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"37","LSCHG_DTM":"2021-11-04 09:28:50.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"38","LSCHG_DTM":"2021-11-04 09:29:19.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"39","LSCHG_DTM":"2021-11-04 09:29:56.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"40","LSCHG_DTM":"2021-11-04 09:31:57.0"},{"LED_SQNO":"1","SRA_SBID_UPR":"0","SRA_SBID_AM":"0","LVST_AUC_PTC_MN_NO":"","AUC_OBJ_DSC":"3","LS_CMENO":"admin1","SEL_STS_DSC":"11","LOWS_SBID_LMT_AM":"0.000","AUC_DT":"20211104","NA_BZPLC":"8808990656656","TRMN_AMNNO":"","OSLP_NO":"41","LSCHG_DTM":"2021-11-04 09:32:26.0"}]	
	
}
