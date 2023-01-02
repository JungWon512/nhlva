package com.ishift.auction.service.admin.task;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.ishift.auction.base.dao.MainDao;

@Repository("adminTaskDAO")
public class AdminTaskDAO {

	@Autowired
	private MainDao mainDao;
	
	@Value("${spring.profiles.active}")
	private String profile;

	public Map<String, Object> selectSogCowInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("AdminTaskMapper.selectSogCowInfo", params);
	}
	
	public void insertCowImg(Map<String, Object> params) throws SQLException {
		mainDao.insert("AdminTaskMapper.insertCowImg", params);
	}

	public List<Map<String, Object>> selectCowImg(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("AdminTaskMapper.selectCowImg", params);
	}

	public void deleteCowImg(Map<String, Object> params) throws SQLException {
		mainDao.delete("AdminTaskMapper.deleteCowImg", params);
	}
}
