package com.ishift.auction.service.admin.login;

import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ishift.auction.base.dao.MainDao;

@Repository("adminLoginDao")
public class AdminLoginDao {
	
	@Autowired
	private MainDao mainDao;

	/**
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectAdminInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("AdminLoginMapper.selectAdminInfo", params);
	}
	
	/**
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectLoginAdminInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("AdminLoginMapper.selectLoginAdminInfo", params);
	}

	public Map<String, Object> selectAdminInfoWherePw(Map<String, Object> params) throws SQLException {
		// TODO Auto-generated method stub
		return mainDao.selectOne("AdminLoginMapper.selectAdminInfoWherePw", params);
	}

}
