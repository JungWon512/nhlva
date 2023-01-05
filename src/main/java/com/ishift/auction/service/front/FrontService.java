package com.ishift.auction.service.front;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface FrontService {
	// 최근 가축시장 시세 조회 
	List<Map<String,Object>> findCowPriceList(Map<String, Object> reqMap) throws SQLException;
	
	// 지역별 평균 낙찰가
	List<Map<String,Object>> findAvgPlaceBidAmList(Map<String, Object> reqMap) throws SQLException;
	
	// 금주의 TOP3
	List<Map<String,Object>> findRecentDateTopList(Map<String, Object> reqMap) throws SQLException;
}
