package com.ishift.auction.service.batch;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ishift.auction.base.dao.MainDao;

@Repository("batchDao")
public class BatchDao{
	
	@Autowired
	private MainDao mainDao;
	/**
	 * 단일 경매 리스트 조회
	 * @param place : 지역 농/축협 관리 번호 > 153 : 화순, 163 : 무진장, 119 : 하동
	 * @return 단일 경매 리스트
	 * @throws Exception 
	 */
	public List<Map<String, Object>> selectQcnAuctionList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> returnList = null;
		int place = Integer.parseInt(params.getOrDefault("place", "0").toString());
		
		return mainDao.selectList("BatchMapper.selectQcnAuctionList", params);
	}

	/**
	 * 단일 경매 정보 저장
	 * @param qcnInfo : 단일 경매 정보
	 * @return 
	 * @throws Exception
	 */
	public int insertQcnAuctionInfo(Map<String, Object> qcnInfo) throws Exception {
		return mainDao.insert("BatchMapper.insertQcnAuctionInfo", qcnInfo);
	}

	/**
	 * @param place : 지역 농/축협 관리 번호 > 153 : 화순, 163 : 무진장, 119 : 하동
	 * @return 일괄 경매 리스트
	 * @throws Exception 
	 */
	public List<Map<String, Object>> selectStnAuctionList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> returnList = null;
		int place = Integer.parseInt(params.getOrDefault("place", "0").toString());
				
		return mainDao.selectList("BatchMapper.selectStnAuctionList", params);
	}
	
	/**
	 * 일괄 경매 정보 저장
	 * @param stnInfo : 일괄 경매 정보
	 * @return 
	 * @throws Exception
	 */
	public int insertStnAuctionInfo(Map<String, Object> stnInfo) throws Exception {
		return mainDao.insert("BatchMapper.insertStnAuctionInfo", stnInfo);
	}

	/**
	 * 중도매인 정보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectMwmnUserList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> returnList = null;
		int place = Integer.parseInt(params.getOrDefault("place", "0").toString());
				
		return mainDao.selectList("BatchMapper.selectMwmnUserList", params);
	}

	/**
	 * 중도매인 정보 저장
	 * @param userInfo : 중도매인 정보
	 * @return
	 * @throws Exception
	 */
	public int insertMwmnUserInfo(Map<String, Object> userInfo) throws Exception {
		return mainDao.insert("BatchMapper.insertMwmnUserInfo", userInfo);
	}
	
	/**
	 * 블랙리스트 정보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectBlackUserList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> returnList = null;
		int place = Integer.parseInt(params.getOrDefault("place", "0").toString());
				
		return mainDao.selectList("BatchMapper.selectBlackUserList", params);
	}
	
	/**
	 * 블랙리스트 정보 저장
	 * @param userInfo : 블랙리스트 정보
	 * @return
	 * @throws Exception
	 */
	public int insertBlackUserInfo(Map<String, Object> userInfo) throws Exception {
		return mainDao.insert("BatchMapper.insertBlackUserInfo", userInfo);
	}

	/**
	 * 관리자 정보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectAdminUserList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> returnList = null;
		int place = Integer.parseInt(params.getOrDefault("place", "0").toString());
				
		return mainDao.selectList("BatchMapper.selectAdminUserList", params);
	}

	/**
	 * 관리자 정보 저장
	 * @param userInfo : 관리자 정보
	 * @return
	 * @throws Exception
	 */
	public int insertAdminUserInfo(Map<String, Object> userInfo) throws Exception {
		return mainDao.insert("BatchMapper.insertAdminUserInfo", userInfo);
	}
	
	/**
	 * 농가 정보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectFhsUserList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> returnList = null;
		int place = Integer.parseInt(params.getOrDefault("place", "0").toString());
				
		return mainDao.selectList("BatchMapper.selectFhsUserList", params);
	}
	
	/**
	 * 농가 정보 저장
	 * @param userInfo : 농가 정보
	 * @return
	 * @throws Exception
	 */
	public int insertFhsUserInfo(Map<String, Object> userInfo) throws Exception {
		return mainDao.insert("BatchMapper.insertFhsUserInfo", userInfo);
	}

	/**
	 * 출장우 정보 조회
	 * @param params
	 * @return 출장우 리스트
	 * @throws Exception 
	 */
	public List<Map<String, Object>> selectCowList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> returnList = null;
		int place = Integer.parseInt(params.getOrDefault("place", "0").toString());
				
		return mainDao.selectList("BatchMapper.selectCowList", params);
	}

	/**
	 * 출장우 정보 저장
	 * @param cowInfo 출장우 정보
	 * @return 성공 수
	 * @throws Exception 
	 */
	public int insertCowInfo(Map<String, Object> cowInfo) throws Exception {
		return mainDao.insert("BatchMapper.insertCowInfo", cowInfo);
	}

	/**
	 * 개체 정보 조회
	 * @param params
	 * @return 개체 리스트
	 * @throws Exception 
	 */
	public List<Map<String, Object>> selectIndvList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> returnList = null;
		int place = Integer.parseInt(params.getOrDefault("place", "0").toString());
		
		return mainDao.selectList("BatchMapper.selectIndvList", params);
	}

	/**
	 * 개체 정보 저장
	 * @param indvInfo 개체 정보
	 * @return 성공 수
	 * @throws Exception 
	 */
	public int insertIndvInfo(Map<String, Object> indvInfo) throws Exception {
		return mainDao.insert("BatchMapper.insertIndvInfo", indvInfo);
	}

	public List<Map<String, Object>> selectTodayAuctionList(Map<String, Object> params) throws Exception {
		List<Map<String, Object>> returnList = null;
		int place = Integer.parseInt(params.getOrDefault("place", "0").toString());
		
		return mainDao.selectList("BatchMapper.selectTodayAuctionList", params);
	}
	
}
