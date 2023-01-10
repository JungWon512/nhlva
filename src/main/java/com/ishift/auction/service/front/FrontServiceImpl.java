package com.ishift.auction.service.front;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("FrontService")
public class FrontServiceImpl implements FrontService {

	@Autowired
	FrontDAO frontDAO;
	
	@Override
	public List<Map<String, Object>> findCowPriceList(Map<String, Object> reqMap) throws SQLException {
		return frontDAO.findCowPriceList(reqMap);
	}

	@Override
	public List<Map<String, Object>> findAvgPlaceBidAmList(Map<String, Object> reqMap) throws SQLException {
		return frontDAO.findAvgPlaceBidAmList(reqMap);
	}

	@Override
	public List<Map<String, Object>> findRecentDateTopList(Map<String, Object> reqMap) throws SQLException {
		return frontDAO.findRecentDateTopList(reqMap);
	}
}
