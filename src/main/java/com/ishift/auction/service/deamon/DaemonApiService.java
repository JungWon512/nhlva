package com.ishift.auction.service.deamon;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DaemonApiService {

	/**
	 * TB_LA_IS_MH_AUC_QCN : 경매 차수 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectAucQcnList(Map<String, Object> params) throws SQLException;
	
	/**
	 * TB_LA_IS_MH_SOG_COW : 출장우 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectSogCowList(Map<String, Object> params) throws SQLException;
	
	/**
	 * TB_LA_IS_MH_CALF : 송아지 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectCalfList(Map<String, Object> params) throws SQLException;
	
	/**
	 * TB_LA_IS_MM_INDV : 개체 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectIndvList(Map<String, Object> params) throws SQLException;

	/**
	 * TB_LA_IS_MH_AUC_ENTR : 경매 참가자 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectAucEntrList(Map<String, Object> params) throws SQLException;

	/**
	 * TB_LA_IS_MH_FEE : 경매 수수료 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectFeeList(Map<String, Object> params) throws SQLException;

	/**
	 * TB_LA_IS_MM_FHS : 출하주 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectFhsList(Map<String, Object> params) throws SQLException;

	/**
	 * TB_LA_IS_MM_MWMN : 중도매인 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectMwmnList(Map<String, Object> params) throws SQLException;

	/**
	 * TB_LA_IS_MH_SOG_COW : 출장우 낙/유찰 정보 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> updSogcow(Map<String, Object> params) throws SQLException;

	/**
	 * TB_LA_IS_MH_FEE_IMPS : 낙/유찰 수수료 정보 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> updFeeImps(Map<String, Object> params) throws SQLException;

	/**
	 * TB_LA_IS_MH_ATDR_LOG : 응찰 로그 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> insAtdrLog(Map<String, Object> params) throws SQLException;

	/**
	 * TB_LA_IS_MH_FEE_IMPS : 낙/유찰 수수료 정보 리스트(테스트용)
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	List<Map<String, Object>> selectFeeImpsList(Map<String, Object> params) throws SQLException;

	/**
	 * TB_LA_IS_MM_ATDR_LOG : 응찰 정보 리스트(테스트용)
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	List<Map<String, Object>> selectAtdrLogList(Map<String, Object> params) throws SQLException;

}
