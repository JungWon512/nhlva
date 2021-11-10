package com.ishift.auction.service.admin.login;

import java.sql.SQLException;
import java.util.Map;

public interface AdminLoginService {
	
	/**
	 * 조합코드(NA_BZPLC)와 관리자 아이디(USRID)로 관리자 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	Map<String, Object> selectAdminInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 관리자 아이디(USRID)로 관리자 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectLoginAdminInfo(Map<String, Object> params) throws SQLException;

	Map<String, Object> selectAdminInfoWherePw(Map<String, Object> params) throws SQLException;

}
