package com.ishift.auction.service.admin.login;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

@Service
public class AdminLoginServiceImpl implements AdminLoginService {
	
	@Resource(name = "adminLoginDao")
	AdminLoginDao adminLoginDao;

	@Override
	public Map<String, Object> selectAdminInfo(Map<String, Object> params) throws Exception {
		return adminLoginDao.selectAdminInfo(params);
	}
	
	@Override
	public Map<String, Object> selectLoginAdminInfo(Map<String, Object> params) throws Exception {
		return adminLoginDao.selectLoginAdminInfo(params);
	}

	@Override
	public Map<String, Object> selectAdminInfoWherePw(Map<String, Object> params) throws Exception{
		return adminLoginDao.selectAdminInfoWherePw(params);
	}
}
