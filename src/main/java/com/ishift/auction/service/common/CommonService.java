package com.ishift.auction.service.common;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface CommonService {

	/**
	 * 공통코드 조회
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	List<Map<String, Object>> getCommonCode(Map<String, Object> params) throws SQLException;

	/**
	 * 개체 상세정보 조회(한우종합)
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws Exception
	 */
	Map<String, Object> searchIndvDetails(Map<String, Object> params) throws SQLException, Exception;

	/**
	 * 개체 정보 저장 & 로그 저장 
	 * @param params
	 * @throws SQLException
	 */
	void updateIndvInfo(Map<String, Object> params) throws SQLException, Exception;

	/**
	 * 출하주 정보 저장
	 * @param params
	 * @throws SQLException
	 */
	void updateFhsInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 휴면회원 해제
	 * @param params
	 * @throws SQLException
	 * @throws Exception
	 */
	void updateDormcUserFhsClear(Map<String, Object> params) throws SQLException, RuntimeException;
	List<Map<String, Object>> selectBloodInfo(Map<String, Object> params) throws SQLException;

	List<Map<String, Object>> selectIndvPost(Map<String, Object> params) throws SQLException;

	List<Map<String, Object>> selectIndvSib(Map<String, Object> params) throws SQLException;
	void callIndvAiakInfo(String barcode) throws SQLException, RuntimeException;
	void callIndvAiakInfo(Map<String, Object> param) throws SQLException, RuntimeException;
	List<Map<String, Object>> selectAucObjDscList(Map<String, Object> params) throws SQLException;

	int callRenderingAdsLog(String pgid,String ip) throws SQLException;
	
	int insertAdsLog(Map<String, Object> params) throws SQLException;
	public Map<String, Object> readJsonFile(String pgid);
	
}
