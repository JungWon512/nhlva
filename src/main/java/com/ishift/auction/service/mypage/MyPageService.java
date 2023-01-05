package com.ishift.auction.service.mypage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface MyPageService {
	// 나의 경매내역 > My 현황
	List<Map<String,Object>> selectCowBidCnt(Map<String, Object> map) throws SQLException;
	
	List<Map<String,Object>> selectCowBidPercent(Map<String, Object> map) throws SQLException;
	
	List<Map<String,Object>> selectListAucBidCntAll(Map<String, Object> map) throws SQLException;

	// 나의 출장우 > My 현황
	List<Map<String,Object>> selectCowEntryCnt(Map<String, Object> map) throws SQLException;
	
	List<Map<String,Object>> selectListAucEntryCntAll(Map<String, Object> map) throws SQLException;
}