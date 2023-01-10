package com.ishift.auction.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ishift.auction.util.McaUtil;

@SuppressWarnings("unchecked")
@RestController
public class InterfaceController {
	
	@Autowired
	private McaUtil mcaUtil;

	@ResponseBody
	@PostMapping(value = "/office/interface/mca")
	public Map<String, Object> mca(@RequestBody final Map<String, Object> params) throws Exception {
		Map<String, Object> mcaMap = new HashMap<String, Object>();
		Map<String, Object> dataMap =  new HashMap<String, Object>();
		if ("2200".equals((String) params.get("ctgrm_cd"))) {
			mcaMap = mcaUtil.tradeMcaMsg((String) params.get("ctgrm_cd"), params);
			dataMap = (Map<String, Object>) mcaMap.get("jsonData");
			dataMap = this.createResultSetMapData(dataMap);
		}
		return dataMap;
	}

	public Map<String, Object> createResultSetMapData(Map<String, Object> map) throws Exception {
		// 데이터 암호화해서 result 추가, 상태코드 추가, 조회 count 추가
		Map<String, Object> reMap = new HashMap<String, Object>();

		// 조회 결과가 0건일 경우 201 리턴
		if (map == null || map.isEmpty()) {
			reMap.put("status", 201);
			reMap.put("code", "C001");
			reMap.put("message", "조회된 내역이 없습니다.");
		} else if (map.containsKey("jsonHeader")) {
			if ("Error".equals(map.get("jsonHeader"))) {
				reMap.put("status", 202);
				reMap.put("code", "C002");
				reMap.put("message", "전송 실패 하였습니다.");
			} else {
				reMap.put("status", 200);
				reMap.put("code", "C000");
				reMap.put("dataCnt", map.get("dataCnt"));
				reMap.put("message", "");
			}
		} else {
			reMap.put("status", 200);
			reMap.put("code", "C000");
			reMap.put("message", "");
			// JSON으로 변경
			reMap.put("data", map);
		}

		return reMap;
	}

//	public JSONArray convertListMapToJson(List<Map<String, Object>> list) throws JSONException {
//		JSONArray jsonArray = new JSONArray();
//		if (list != null) {
//			for (Map<String, Object> map : list) {
//				jsonArray.put(convertMaptoJson(map));
//			}
//		}
//		return jsonArray;
//	}
//
//	public JSONObject convertMaptoJson(Map<String, Object> map) throws JSONException {
//		JSONObject jsonObject = new JSONObject();
//		if (map != null) {
//			for (Entry<String, Object> entry : map.entrySet()) {
//				String key = entry.getKey();
//				Object value = entry.getValue();
//				jsonObject.put(key, (value == null) ? "" : value);
//			}
//		}
//		return jsonObject;
//	}
//
//	public JSONObject convertMaptoArrJson(Map<String, Object> map) throws JSONException {
//		JSONObject jsonObject = new JSONObject();
//		if (map != null) {
//			for (Entry<String, Object> entry : map.entrySet()) {
//				String key = entry.getKey();
//				List<Map<String, Object>> value = (List<Map<String, Object>>) entry.getValue();
//
//				jsonObject.put(key, convertListMapToJson(value));
//			}
//		}
//		return jsonObject;
//	}
}
