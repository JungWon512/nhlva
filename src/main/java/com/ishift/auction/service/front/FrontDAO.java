package com.ishift.auction.service.front;

import com.ishift.auction.base.dao.MainDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


@Repository("frontDAO")
public class FrontDAO {
	
	@Autowired
	private MainDao mainDao;
	@Value("${spring.profiles.active}") private String profile;

	public List<Map<String, Object>> findCowPriceList(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("front.findCowPriceList", reqMap);
	}

	public List<Map<String, Object>> findAvgPlaceBidAmList(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("front.findAvgPlaceBidAmList", reqMap);
	}
	
	public List<Map<String, Object>> findRecentDateTopList(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("front.findRecentDateTopList", reqMap);
	}
}