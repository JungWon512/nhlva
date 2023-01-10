package com.ishift.auction.service.login;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public interface LoginService {
	
	/**
	 * 중도매인, 출하주 로그인 프로세스
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> loginProc(Map<String, Object> params) throws SQLException, RuntimeException;

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
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectFarmUserList(Map<String, Object> params) throws SQLException;

	/**
	 * 조합코드(NA_BZPLC)와 농가 식별번호(FHS_ID_NO)로 출하주 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectFarmUser(Map<String, Object> params) throws SQLException;

	/**
	 * 인증번호 조회 (중도매인)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectAuthNumberInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 인증번호 조회 (출하주)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectAuthFhsNumberInfo(Map<String, Object> params) throws SQLException;
	
	/**
	 * 새로 발급한 인증번호 저장 (중도매인)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	int updateAuthNumber(Map<String, Object> params) throws SQLException;
	
	/**
	 * 새로 발급한 인증번호 저장 (출하주)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	int updateAuthFhsNumber(Map<String, Object> params) throws SQLException;

	/**
	 * 로그인 인증번호 발송
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> sendSmsProc(Map<String, Object> params) throws SQLException, RuntimeException;

	/**
	 * 로그인 인증번호 확인
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	Map<String, Object> loginAuthProc(Map<String, Object> params) throws SQLException, RuntimeException;

	/**
	 * 로그인 이력 저장하기
	 * @param params
	 * @param inOutGb
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	void insertLoginConnHistory(HttpServletRequest request, Map<String, Object> params) throws SQLException, RuntimeException;


}
