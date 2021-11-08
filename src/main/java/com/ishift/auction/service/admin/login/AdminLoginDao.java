package com.ishift.auction.service.admin.login;

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
	 * @throws Exception
	 */
	public Map<String, Object> selectAdminInfo(Map<String, Object> params) throws Exception {
		return mainDao.selectOne("AdminLoginMapper.selectAdminInfo", params);
	}
	
	/**
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> selectLoginAdminInfo(Map<String, Object> params) throws Exception {
		return mainDao.selectOne("AdminLoginMapper.selectLoginAdminInfo", params);
	}

	public Map<String, Object> selectAdminInfoWherePw(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return mainDao.selectOne("AdminLoginMapper.selectAdminInfoWherePw", params);
	}

}
