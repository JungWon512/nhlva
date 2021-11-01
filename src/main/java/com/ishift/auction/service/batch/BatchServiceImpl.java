package com.ishift.auction.service.batch;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional(transactionManager = "txManager", rollbackFor = Exception.class)
public class BatchServiceImpl implements BatchService {
	
	@Autowired
	private BatchDao batchDao;

	/**
	 * 단일 경매 저장 후 결과 리턴
	 */
	@Override
	public Map<String, Object> refreshQcnAuctionInfo(final Map<String, Object> params) throws Exception {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		// 지역코드(TB_LA_IS_BM_BZLOC 참조) > 153 : 화순, 163 : 무진장, 119 : 하동
		final int[] arrPlace = {119, 153, 163};
		
		if (params.get("place") != null) {
			// 1. 단일 경매 (TB_LA_IS_MH_AUC_QCN) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 오늘인 리스트 조회 
			List<Map<String, Object>> qcnList = this.selectQcnAuctionList(params);
			
			// 2. 단일 경매 리스트 데이터 INSERT OR UPDATE
			if (qcnList.size() > 0) {
				for (Map<String, Object> qcnInfo : qcnList) {
					try {
						int cnt = this.insertQcnAuctionInfo(qcnInfo);
						log.debug(cnt > 0 ? "성공" : "실패");
					}
					catch(Exception e) {
						log.error(e.getMessage());
					}
				}
			}
		}
		else {
			for (int i = 0; i < arrPlace.length; i++) {
				// 1. 단일 경매 (TB_LA_IS_MH_AUC_QCN) 리스트
				params.put("place", arrPlace[i]);
				List<Map<String, Object>> qcnList = this.selectQcnAuctionList(params);

				// 2. 단일 경매 리스트 데이터 INSERT OR UPDATE
				if (qcnList.size() > 0) {
					for (Map<String, Object> qcnInfo : qcnList) {
						try {
							int cnt = this.insertQcnAuctionInfo(qcnInfo);
							log.debug(cnt > 0 ? "성공" : "실패");
						}
						catch(Exception e) {
							log.error(e.getMessage());
						}
					}
				}
			}
		}
		
		result.put("", arrPlace);
		return result;
	}
	
	/**
	 * 단일 경매 리스트 조회
	 * @param params
	 * @return 단일 경매 리스트
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> selectQcnAuctionList(Map<String, Object> params) throws Exception {
		return batchDao.selectQcnAuctionList(params);
	}

	/**
	 * 단일 경매 저장
	 * @param qcnInfo 단일 경매 정보
	 * @return 성공 수
	 * @throws Exception 
	 */
	@Override
	public int insertQcnAuctionInfo(Map<String, Object> qcnInfo) throws Exception {
		return batchDao.insertQcnAuctionInfo(qcnInfo);
	}
	
	/**
	 * 일괄 경매 저장
	 */
	@Override
	public Map<String, Object> refreshStnAuctionInfo(final Map<String, Object> params) throws Exception {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		// 지역코드(TB_LA_IS_BM_BZLOC 참조) > 153 : 화순, 163 : 무진장, 119 : 하동
		final int[] arrPlace = {119, 153, 163};
		
		if (params.get("place") != null) {
			// 1. 일괄 경매 (TB_LA_IS_MH_AUC_STN) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 오늘인 리스트 조회 
			List<Map<String, Object>> stnList = this.selectStnAuctionList(params);
			
			// 2. 일괄 경매 리스트 데이터 INSERT OR UPDATE
			if (stnList.size() > 0) {
				for (Map<String, Object> stnInfo : stnList) {
					try {
						int cnt = this.insertStnAuctionInfo(stnInfo);
						log.debug(cnt > 0 ? "성공" : "실패");
					}
					catch(Exception e) {
						log.error(e.getMessage());
					}
				}
			}
		}
		else {
			for (int i = 0; i < arrPlace.length; i++) {
				// 1. 일괄 경매 (TB_LA_IS_MH_AUC_STN) 리스트
				params.put("place", arrPlace[i]);
				List<Map<String, Object>> stnList = this.selectStnAuctionList(params);
				
				// 2. 일괄 경매 리스트 데이터 INSERT OR UPDATE
				if (stnList.size() > 0) { 
					for (Map<String, Object> stnInfo : stnList) {
						try {
							int cnt = this.insertStnAuctionInfo(stnInfo);
							log.debug(cnt > 0 ? "성공" : "실패");
						}
						catch(Exception e) {
							log.error(e.getMessage());
						}
					}
				}
			}
		}
		
		return result;
	}
	
	/**
	 * 일괄 경매 리스트 조회
	 * @param params
	 * @return 일괄 경매 리스트
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> selectStnAuctionList(Map<String, Object> params) throws Exception {
		return batchDao.selectStnAuctionList(params);
	}
	
	/**
	 * 일괄 경매 정보 저장
	 * @param stnInfo
	 * @return 성공 수
	 * @throws Exception 
	 */
	@Override
	public int insertStnAuctionInfo(Map<String, Object> stnInfo) throws Exception {
		return batchDao.insertStnAuctionInfo(stnInfo);
	}

	/**
	 * @param params
	 * @return 중도매인 리스트
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> selectMwmnUserList(Map<String, Object> params) throws Exception {
		return batchDao.selectMwmnUserList(params);
	}

	/**
	 * 중도매인 정보 저장
	 * @param userInfo 중도매인 정보
	 * @return 성공 수
	 * @throws Exception 
	 */
	@Override
	public int insertMwmnUserInfo(Map<String, Object> userInfo) throws Exception {
//		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//		userInfo.put("SEC_CUS_RLNO", encoder.encode(DigestUtils.sha512Hex(userInfo.get("CUS_RLNO").toString())));
		userInfo.put("SEC_CUS_RLNO", DigestUtils.sha512Hex(userInfo.get("CUS_RLNO").toString()));
		userInfo.put("SEC_SRA_MWMNNM", DigestUtils.sha512Hex(userInfo.get("SRA_MWMNNM").toString()));
		return batchDao.insertMwmnUserInfo(userInfo);
	}
	
	/**
	 * @param params
	 * @return 블랙리스트
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> selectBlackUserList(Map<String, Object> params) throws Exception {
		return batchDao.selectBlackUserList(params);
	}
	
	/**
	 * 블랙리스트 정보 저장
	 * @param userInfo 블랙리스트 정보
	 * @return 성공 수
	 * @throws Exception 
	 */
	@Override
	public int insertBlackUserInfo(Map<String, Object> userInfo) throws Exception {
		return batchDao.insertBlackUserInfo(userInfo);
	}

	/**
	 * 관리자 정보 조회
	 * @param params
	 * @return 관리자 리스트
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> selectAdminUserList(Map<String, Object> params) throws Exception {
		return batchDao.selectAdminUserList(params);
	}

	/**
	 * 관리자 정보 저장
	 * @param userInfo 관리자 정보
	 * @return 성공 수
	 * @throws Exception 
	 */
	@Override
	public int insertAdminUserInfo(Map<String, Object> userInfo) throws Exception {
//		MessageDigest md = MessageDigest.getInstance("SHA-512");
//		md.update("1111".getBytes());
//		String hex = String.format("%0128x", new BigInteger(1, md.digest()));
//		System.out.println("hex ::: " + hex);
//		System.out.println(DigestUtils.sha512Hex("1111"));
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		userInfo.put("SEC_PW", encoder.encode("nacf1234!"));
		return batchDao.insertAdminUserInfo(userInfo);
	}
	
	/**
	 * 농가 정보 조회
	 * @param params
	 * @return 농가 리스트
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> selectFhsUserList(Map<String, Object> params) throws Exception {
		return batchDao.selectFhsUserList(params);
	}
	
	/**
	 * 농가 정보 저장
	 * @param userInfo 농가 정보
	 * @return 성공 수
	 * @throws Exception 
	 */
	@Override
	public int insertFhsUserInfo(Map<String, Object> userInfo) throws Exception {
		userInfo.put("SEC_FTSNM", DigestUtils.sha512Hex(userInfo.get("FTSNM").toString()));
		userInfo.put("SEC_OHSE_TELNO", DigestUtils.sha512Hex(userInfo.get("OHSE_TELNO").toString()));
		userInfo.put("SEC_CUS_MPNO", DigestUtils.sha512Hex(userInfo.get("CUS_MPNO").toString()));
		return batchDao.insertFhsUserInfo(userInfo);
	}
	
	/**
	 * 출장우 정보 조회
	 * @param params
	 * @return 출장우 리스트
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> selectCowList(Map<String, Object> params) throws Exception {
		return batchDao.selectCowList(params);
	}
	
	/**
	 * 출장우 정보 저장
	 * @param cowInfo 출장우 정보
	 * @return 성공 수
	 * @throws Exception 
	 */
	@Override
	public int insertCowInfo(Map<String, Object> cowInfo) throws Exception {
		return batchDao.insertCowInfo(cowInfo);
	}
	/**
	 * 개체 정보 조회
	 * @param params
	 * @return 개체 리스트
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> selectIndvList(Map<String, Object> params) throws Exception {
		return batchDao.selectIndvList(params);
	}
	
	/**
	 * 개체 정보 저장
	 * @param indvInfo 개체 정보
	 * @return 성공 수
	 * @throws Exception 
	 */
	@Override
	public int insertIndvInfo(Map<String, Object> indvInfo) throws Exception {
		return batchDao.insertIndvInfo(indvInfo);
	}

	/**
	 * 오늘 진행/예정 경매 리스트
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> selectTodayAuctionList(Map<String, Object> params) throws Exception {
		return batchDao.selectTodayAuctionList(params);
	}

}
