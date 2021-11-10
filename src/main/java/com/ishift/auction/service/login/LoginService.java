package com.ishift.auction.service.login;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface LoginService {
	
	/**
	 * 중도매인, 출하주 로그인 프로세스
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> loginProc(Map<String, Object> params) throws SQLException;

	/**
	 * 로그인 중도매인 검색
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectWholesalerList(Map<String, Object> params) throws SQLException;

	/**
	 * 조합코드(NA_BZPLC)와 거래인 관리번호(TRMN_AMNNO)로 중도매인 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectWholesaler(Map<String, Object> params) throws SQLException;
	
	/**
	 * 방문로그 저장
	 * @throws SQLException
	 */
	void insertVisitor(Map<String, Object> vo) throws SQLException;

	/**
	 * 로그인 출하주 검색
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectFarmUserList(Map<String, Object> params) throws SQLException;

	/**
	 * 조합코드(NA_BZPLC)와 농가 식별번호(FHS_ID_NO)로 출하주 조회
	 * @param params
	 * @return
	 */
	Map<String, Object> selectFarmUser(Map<String, Object> params) throws SQLException;


}
