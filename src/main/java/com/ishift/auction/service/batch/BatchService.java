package com.ishift.auction.service.batch;

import java.util.List;
import java.util.Map;

public interface BatchService {

	/**
	 * 단일 경매 정보 저장
	 * @param params
	 * @return
	 */
	Map<String, Object> refreshQcnAuctionInfo(Map<String, Object> params) throws Exception;
	
	/**
	 * 일괄 경매 정보 저장
	 * @param params
	 * @return
	 */
	Map<String, Object> refreshStnAuctionInfo(Map<String, Object> params) throws Exception;

	/**
	 * 단일 경매 리스트
	 * @param params
	 * @return 단일 경매 리스트
	 * @throws Exception
	 */
	List<Map<String, Object>> selectQcnAuctionList(Map<String, Object> params) throws Exception;

	/**
	 * 단일 경매 정보 저장
	 * @param qcnInfo 단일 경매 정보
	 * @return 
	 * @throws Exception
	 */
	int insertQcnAuctionInfo(Map<String, Object> qcnInfo) throws Exception;

	/**
	 * 일괄 경매 리스트
	 * @param params
	 * @return 일괄 경매 리스트
	 * @throws Exception
	 */
	List<Map<String, Object>> selectStnAuctionList(Map<String, Object> params) throws Exception;

	/**
	 * 일괄 경매 정보 저장
	 * @param stnInfo 일괄 경매 정보
	 * @return
	 * @throws Exception
	 */
	int insertStnAuctionInfo(Map<String, Object> stnInfo) throws Exception;

	/**
	 * 중도매인 정보 리스트
	 * @param params
	 * @return 중도매인 리스트
	 */
	List<Map<String, Object>> selectMwmnUserList(Map<String, Object> params) throws Exception;
	
	/**
	 * 중도매인 정보 저장
	 * @param userInfo 중도매인 정보
	 * @return
	 * @throws Exception
	 */
	int insertMwmnUserInfo(Map<String, Object> userInfo) throws Exception;
	
	/**
	 * 블랙리스트 정보 리스트
	 * @param params
	 * @return 중도매인 리스트
	 */
	List<Map<String, Object>> selectBlackUserList(Map<String, Object> params) throws Exception;
	
	/**
	 * 블랙리스트 정보 저장
	 * @param userInfo 중도매인 정보
	 * @return
	 * @throws Exception
	 */
	int insertBlackUserInfo(Map<String, Object> userInfo) throws Exception;

	/**
	 * 관리자 정보 리스트
	 * @param params
	 * @return 관리자 리스트
	 * @throws Exception
	 */
	List<Map<String, Object>> selectAdminUserList(Map<String, Object> params) throws Exception;

	/**
	 * 관리자 정보 저장
	 * @param userInfo 관리자 정보
	 * @return
	 * @throws Exception
	 */
	int insertAdminUserInfo(Map<String, Object> userInfo) throws Exception;

	/**
	 * 농가 정보 조회
	 * @param params
	 * @return 단일 경매 리스트
	 * @throws Exception 
	 */
	List<Map<String, Object>> selectFhsUserList(Map<String, Object> params) throws Exception;

	/**
	 * 농가 정보 저장
	 * @param userInfo 관리자 정보
	 * @return 성공 수
	 * @throws Exception 
	 */
	int insertFhsUserInfo(Map<String, Object> userInfo) throws Exception;

	/**
	 * 출장우 정보 조회
	 * @param params
	 * @return 출장우 리스트
	 * @throws Exception 
	 */
	List<Map<String, Object>> selectCowList(Map<String, Object> params) throws Exception;

	/**
	 * 출장우 정보 저장
	 * @param cowInfo 출장우 정보
	 * @return 성공 수
	 * @throws Exception 
	 */
	int insertCowInfo(Map<String, Object> cowInfo) throws Exception;

	/**
	 * 개체 정보 조회
	 * @param params
	 * @return 개체 리스트
	 * @throws Exception 
	 */
	List<Map<String, Object>> selectIndvList(Map<String, Object> params) throws Exception;

	/**
	 * 개체 정보 저장
	 * @param indvInfo 개체 정보
	 * @return 성공 수
	 * @throws Exception 
	 */
	int insertIndvInfo(Map<String, Object> indvInfo) throws Exception;

	/**
	 * 오늘 진행/예정 경매 리스트
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectTodayAuctionList(Map<String, Object> params) throws Exception;

}
