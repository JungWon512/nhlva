package com.ishift.auction.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ishift.auction.web.TradeMcaMsgDataController;

@Component
public class McaUtil {

	private static Logger log = LoggerFactory.getLogger(McaUtil.class);

	@Autowired
	TradeMcaMsgDataController tradeMcaMsgDataController;

	public Map<String, Object> tradeMcaMsg(String ctgrm_cd, Map<String, Object> map) throws Exception {
		Map<String, Object> reMap = new HashMap<String, Object>();
		log.info(" ################# McaUtil : START #################");
		reMap = tradeMcaMsgDataController.tradeMcaMsg(ctgrm_cd, changeKeyUpper(map));
		log.info(" ################# McaUtil : END ###################\n" + reMap.toString());
		return reMap;
	}

	/**
	 * KEY 대문자 변환
	 * @param map
	 * @return
	 */
	public Map<String, Object> changeKeyUpper(Map<String, Object> map) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		// 키 대문자로 변환
		Set<String> set = map.keySet();
		Iterator<String> e = set.iterator();
		while (e.hasNext()) {
			String key = e.next();
			Object value = (Object) map.get(key);
			reMap.put(key.toUpperCase(), value);
		}
		return reMap;
	}

	/**
	 * KEY 소문자 변환
	 * @param map
	 * @return
	 */
	public Map<String, Object> changeKeyLower(Map<String, Object> map) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		// 키 대문자로 변환
		Set<String> set = map.keySet();
		Iterator<String> e = set.iterator();
		while (e.hasNext()) {
			String key = e.next();
			Object value = (Object) map.get(key);
			reMap.put(key.toLowerCase(), value);
		}
		return reMap;
	}

	/**
	 * KEY CAMEL CASE 변환
	 * @param map
	 * @return
	 */
	public Map<String, Object> changeKeyToCamelCase(Map<String, Object> map) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		// 키 대문자로 변환
		Set<String> set = map.keySet();
		Iterator<String> e = set.iterator();
		while (e.hasNext()) {
			String key = e.next();
			Object value = (Object) map.get(key);
			reMap.put(key.toLowerCase(), value);
		}
		return reMap;
	}
	
	/**
	 * KEY SNAKE CASE 변환
	 * @param map
	 * @return
	 */
	public Map<String, Object> changeKeyToSnakeCase(Map<String, Object> map) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		// 키 대문자로 변환
		Set<String> set = map.keySet();
		Iterator<String> e = set.iterator();
		while (e.hasNext()) {
			String key = e.next();
			Object value = (Object) map.get(key);
			reMap.put(key.toLowerCase(), value);
		}
		return reMap;
	}
}
