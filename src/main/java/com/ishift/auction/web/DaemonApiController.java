package com.ishift.auction.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ishift.auction.service.deamon.DaemonApiService;

/**
 * 데몬 호출 api controller
 * @author iShift
 */
@RestController
@RequestMapping("/daemon/api")
public class DaemonApiController {
	
	@Autowired
	DaemonApiService daemonApiService;

	/**
	 * timestamp 시점 이후의 개체 정보 리스트
	 * @param naBzplc
	 * @param timestamp
	 * @return
	 * @throws SQLException 
	 */
	@GetMapping("/indvs/{naBzplc}/{timeStamp}")
	public Map<String, Object> indvs(@PathVariable("naBzplc") long naBzplc
									, @PathVariable("timeStamp") long timeStamp) throws SQLException {
		
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("naBzplc", naBzplc);
		params.put("timeStamp", timeStamp);
		final List<Map<String, Object>> list = daemonApiService.selectIndvs(params);

		return this.createResultSetListData(list);
	}

	@GetMapping("/indvs2/{naBzplc}/{timestamp}")
	public ResponseEntity<Map<String, Object>> indvs2(@PathVariable("naBzplc") long naBzplc
													, @PathVariable("timestamp") long timestamp) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		return ResponseEntity.status(HttpStatus.OK).body(returnMap);
	}
	
	/**
	 * 리스트 응답 생성
	 * @param list
	 * @return
	 */
	public Map<String, Object> createResultSetListData(List<Map<String, Object>> list){
		
		//데이터 암호화해서 result 추가, 상태코드 추가, 조회 count 추가
		Map<String, Object> reMap = new HashMap<String, Object>();
		
		//조회 결과가 0건일 경우 201 리턴
		if(list.size() < 1) {
			reMap.put("status", 204);
			reMap.put("code", "C001");
			reMap.put("message", "조회된 내역이 없습니다.");
		}else {
			reMap.put("status", 200);
			reMap.put("code", "C000");
			reMap.put("message", "");
		}
		reMap.put("data", list);
		
		return reMap;
	}
	
}
