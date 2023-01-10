package com.ishift.auction.service.kiosk;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface KioskApiService {
	
	/**
	 * 키오스크 중도매인 로그인(인증번호 발송)
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	Map<String, Object> mwmnLoginProc(Map<String, Object> params) throws SQLException;

	/**
	 * 키오스크 출장우 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectSogCowList(Map<String, Object> params) throws SQLException;

	/**
	 * 인증번호 확인 프로세스
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	Map<String, Object> authNumChkProc(Map<String, Object> params) throws SQLException;

	/**
	 * 중도매인 경매 참가등록 프로세스
	 * @param params
	 * @return
	 */
	Map<String, Object> mwmnRegEntryProc(Map<String, Object> params) throws SQLException;

	/**
	 * 중도매인 정산내역
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectMwmnStateAccInfo(Map<String, Object> params) throws SQLException;
	
	/**
	 * 출하주 정산내역
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectFhsStateAccInfo(Map<String, Object> params) throws SQLException;

}
