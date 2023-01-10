package com.ishift.auction.service.mypage;

import com.ishift.auction.base.dao.MainDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * @author Ymcho
 *
 */
@Repository("myPageDAO")
public class MyPageDAO {
	
	@Autowired
	private MainDao mainDao;
	@Value("${spring.profiles.active}") private String profile;

	public List<Map<String, Object>> selectCowBidCnt(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("myPage.selectCowBidCnt", reqMap);
	}

	public List<Map<String, Object>> selectCowBidPercent(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("myPage.selectCowBidPercent", reqMap);
	}

	public List<Map<String, Object>> selectListAucBidCntAll(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("myPage.selectListAucBidCntAll", reqMap);
	}
	
	public List<Map<String, Object>> selectCowEntryCnt(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("myPage.selectCowEntryCnt", reqMap);
	}
	
	public List<Map<String, Object>> selectListAucEntryCntAll(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("myPage.selectListAucEntryCntAll", reqMap);
	}
}