package com.ishift.auction.service.dashboard;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DashBoardService {
	List<Map<String,Object>> findFilterRegionList(Map<String, Object> reqMap) throws SQLException;
	
	List<Map<String,Object>> findCowPriceList(Map<String, Object> reqMap) throws SQLException;
	
	List<Map<String,Object>> findAvgPlaceBidAmList(Map<String, Object> reqMap) throws SQLException;
	
	List<Map<String,Object>> findRecentDateTopList(Map<String, Object> reqMap) throws SQLException;

	Map<String, Object> findPartiBidderInfo(Map<String, Object> params) throws SQLException;

	List<Map<String, Object>> findPartiBidderPerList(Map<String, Object> params) throws SQLException;

	Map<String, Object> getBtcAucDateInfo(Map<String, Object> params) throws SQLException;

	List<Map<String, Object>> getBtcAucGradeList(Map<String, Object> params) throws SQLException;

	Map<String, Object> getBtcAucPriceAvgInfo(Map<String, Object> params) throws SQLException;

	List<Map<String, Object>> getBtcAucAreaAvgList(Map<String, Object> params) throws SQLException;

	Map<String, Object> getBtcAucAreaAvgSum(Map<String, Object> params) throws SQLException;

	Map<String, Object> findSbidPriceInfo(Map<String, Object> params) throws SQLException;
	
	Map<String, Object> findSogCowInfo(Map<String, Object> params) throws SQLException;
	
	List<Map<String, Object>> findSogCowInfoList(Map<String, Object> params) throws SQLException;

	List<Map<String, Object>> findAreaSbidList(Map<String, Object> params) throws SQLException;

	List<Map<String, Object>> findMonthlySbidPriceList(Map<String, Object> params) throws SQLException;
	
	List<Map<String, Object>> findMonthlySogCowList(Map<String, Object> params) throws SQLException;

	Map<String, Object> selStaticInfo(Map<String, Object> params) throws SQLException;

	List<Map<String, Object>> selStaticList(Map<String, Object> params) throws SQLException;

	Map<String, Object> selAucStaticInfo(Map<String, Object> params) throws SQLException;

	List<Map<String, Object>> selMhSogCowRowDataList(Map<String, Object> params) throws SQLException;

	public void invalidateCacheMap(String cacheName);
	
}
