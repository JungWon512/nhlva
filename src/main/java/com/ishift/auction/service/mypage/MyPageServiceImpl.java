package com.ishift.auction.service.mypage;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service("MyPageService")
public class MyPageServiceImpl implements MyPageService {

	@Resource(name = "myPageDAO")
	MyPageDAO myPageDAO;
	
	@Override
	public List<Map<String, Object>> selectCowBidCnt(Map<String, Object> map) throws SQLException {
		return myPageDAO.selectCowBidCnt(map);
	}

	@Override
	public List<Map<String, Object>> selectCowBidPercent(Map<String, Object> map) throws SQLException {
		return myPageDAO.selectCowBidPercent(map);
	}

	@Override
	public List<Map<String, Object>> selectListAucBidCntAll(Map<String, Object> map) throws SQLException {
		return myPageDAO.selectListAucBidCntAll(map);
	}

	@Override
	public List<Map<String, Object>> selectCowEntryCnt(Map<String, Object> map) throws SQLException {
		return myPageDAO.selectCowEntryCnt(map);
	}
	
	@Override
	public List<Map<String, Object>> selectListAucEntryCntAll(Map<String, Object> map) throws SQLException {
		return myPageDAO.selectListAucEntryCntAll(map);
	}
}
