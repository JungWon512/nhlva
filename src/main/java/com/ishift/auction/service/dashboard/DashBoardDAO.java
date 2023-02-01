package com.ishift.auction.service.dashboard;

import com.ishift.auction.base.dao.MainDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Repository("dashBoardDAO")
public class DashBoardDAO {
	
	@Autowired
	private MainDao mainDao;
	@Value("${spring.profiles.active}") private String profile;
		
	public List<Map<String, Object>> findFilterRegionList(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("dashboard.findFilterRegionList", reqMap);
	}

	public List<Map<String, Object>> findCowPriceList(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("dashboard.findCowPriceList", reqMap);
	}

	public List<Map<String, Object>> findAvgPlaceBidAmList(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("dashboard.findAvgPlaceBidAmList", reqMap);
	}
	
	public List<Map<String, Object>> findRecentDateTopList(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("dashboard.findRecentDateTopList", reqMap);
	}

	public Map<String, Object> findPartiBidderInfo(Map<String, Object> params) throws SQLException{
		return mainDao.selectOne("dashboard.findPartiBidderInfo", params);
	}

	public List<Map<String, Object>> findPartiBidderPerList(Map<String, Object> params) throws SQLException{
		return mainDao.selectList("dashboard.findPartiBidderPerList", params);
	}

	public Map<String, Object> getBtcAucDateInfo(Map<String, Object> params) throws SQLException{
		return mainDao.selectOne("dashboard.getBtcAucDateInfo", params);
	}

	public List<Map<String, Object>> getBtcAucGradeList(Map<String, Object> params) throws SQLException{
		return mainDao.selectList("dashboard.getBtcAucGradeList", params);
	}

	public Map<String, Object> getBtcAucPriceAvgInfo(Map<String, Object> params) throws SQLException{
		return mainDao.selectOne("dashboard.getBtcAucPriceAvgInfo", params);
	}

	public List<Map<String, Object>> getBtcAucAreaAvgList(Map<String, Object> params) throws SQLException{
		return mainDao.selectList("dashboard.getBtcAucAreaAvgList", params);
	}

	public Map<String, Object> getBtcAucAreaAvgSum(Map<String, Object> params) throws SQLException{
		return mainDao.selectOne("dashboard.getBtcAucAreaAvgSum", params);
	}

	public Map<String, Object> findSbidPriceInfo(Map<String, Object> params) throws SQLException{
		return mainDao.selectOne("dashboard.findSbidPriceInfo", params);
	}
	
	public Map<String, Object> findSogCowInfo(Map<String, Object> params) throws SQLException{
		return mainDao.selectOne("dashboard.findSogCowInfo", params);
	}
	
	public List<Map<String, Object>> findSogCowInfoList(Map<String, Object> params) throws SQLException{
		return mainDao.selectList("dashboard.findSogCowInfoList", params);
	}

	public List<Map<String, Object>> findAreaSbidList(Map<String, Object> params) throws SQLException{
		return mainDao.selectList("dashboard.findAreaSbidList", params);
	}

	public List<Map<String, Object>> findMonthlySbidPriceList(Map<String, Object> params) throws SQLException{
		return mainDao.selectList("dashboard.findMonthlySbidPriceList", params);
	}
	
	public List<Map<String, Object>> findMonthlySogCowList(Map<String, Object> params) throws SQLException{
		return mainDao.selectList("dashboard.findMonthlySogCowList", params);
	}

	public Map<String, Object> selStaticInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("dashboard.selStaticInfo", params);
	}

	public List<Map<String, Object>> selStaticList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("dashboard.selStaticList", params);
	}

	public Map<String, Object> selAucStaticInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("dashboard.selAucStaticInfo", params);
	}

	public List<Map<String, Object>> selMhSogCowRowDataList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("dashboard.selMhSogCowRowDataList", params);
	}
}