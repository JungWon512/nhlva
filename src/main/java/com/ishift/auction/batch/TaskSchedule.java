package com.ishift.auction.batch;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ishift.auction.service.batch.BatchService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TaskSchedule {
	
	@Value("${spring.profiles.active}")
	private String applicationProfile;
	
	@Autowired
	private BatchService batchService;

	/**
	 * 경매(단일, 일괄) 일정 정보 연동 배치
	 * 주기 : 5분 단위 (업무 시간 이후 제외)
	 * @throws Exception
	 */
	@Scheduled(cron = "0 0/5 7-18 * * *")
//	@Scheduled(cron = "0 0/5 * * * *")
	public void batch_auction_calendar() throws Exception {
		log.debug("##### batch_auction_calendar [s] #####");
		log.debug("##### profile : {}", applicationProfile);
		log.debug("##### time    : {}", LocalDateTime.now());
		
		if ("production".equals(applicationProfile)) {

			final Map<String, Object> params = new HashMap<String, Object>();
			// 지역코드(TB_LA_IS_BM_BZLOC 참조) > 153 : 화순, 163 : 무진장, 119 : 하동
			final int[] arrPlace = {119, 153, 163};
//			final int[] arrPlace = {119};
	
			// 실행 실패 리스트
			final List<String> failList = new ArrayList<String>();
	
			params.put("isAll", params.getOrDefault("isAll", "Y"));
	
			for (int i = 0; i < arrPlace.length; i++) {
				// 1. 단일 경매 (TB_LA_IS_MH_AUC_QCN) 리스트 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터 
				params.put("place", arrPlace[i]);
				List<Map<String, Object>> qcnList = batchService.selectQcnAuctionList(params);
	
				// 2. 단일 경매 리스트 데이터 INSERT OR UPDATE
				if (qcnList.size() > 0) {
					try {
						for (Map<String, Object> qcnInfo : qcnList) {
							batchService.insertQcnAuctionInfo(qcnInfo);
						}
					}
					catch(Exception e) {
						failList.add(arrPlace[i] + "- 일괄 경매");
						log.error("클래스 : {}, 지역코드 : {}, 일괄 경매 실행 실패 : {}", this.getClass(), arrPlace[i], e.getMessage());
					}
				}
				
				// 3. 일괄 경매 (TB_LA_IS_MH_AUC_STN) 리스트 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터 
				List<Map<String, Object>> stnList = batchService.selectStnAuctionList(params);
				
				// 4. 일괄 경매 리스트 데이터 INSERT OR UPDATE
				if (stnList.size() > 0) {
					try {
						for (Map<String, Object> stnInfo : stnList) {
							batchService.insertStnAuctionInfo(stnInfo);
						}
					}
					catch(Exception e) {
						failList.add(arrPlace[i] + "- 일괄 경매");
						log.error("클래스 : {}, 지역코드 : {}, 일괄 경매 실행 실패 : {}", this.getClass(), arrPlace[i], e.getMessage());
					}
				}
			}
		}
		
		log.debug("##### batch_auction_calendar [e] #####");
	}

	/**
	 * 중도매인 정보 연동 배치 (지역조합DB조회 변경으로 사용안함)
	 * 주기 : 5분 단위 (업무 시간 이후 제외)
	 * @throws Exception
	 */
//	@Scheduled(cron = "0 0/5 * * * *")
	@Deprecated
	public void batch_mwmn_user_info() throws Exception {
		log.debug("##### batch_mwmn_user_info [s] #####");
		log.debug("##### profile : {}", applicationProfile);
		log.debug("##### time    : {}", LocalDateTime.now());
		
		if ("production".equals(applicationProfile)) {
			
			final Map<String, Object> params = new HashMap<String, Object>();
	
			// 지역코드(TB_LA_IS_BM_BZLOC 참조) > 153 : 화순, 163 : 무진장, 119 : 하동
			final int[] arrPlace = {119, 153, 163};
//			final int[] arrPlace = {119};
	
			// 실행 실패 리스트
			final List<String> failList = new ArrayList<String>();
	
			params.put("isAll", params.getOrDefault("isAll", "N"));
	
			for (int i = 0; i < arrPlace.length; i++) {
				// 1. 중도매인 정보 (TB_LA_IS_MM_MWMN) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터 
				params.put("place", arrPlace[i]);
				List<Map<String, Object>> userList = batchService.selectMwmnUserList(params);

				// 2. 중도매인 리스트 데이터 INSERT OR UPDATE
				if (userList.size() > 0) {
					try {
						for (Map<String, Object> userInfo : userList) {
							batchService.insertMwmnUserInfo(userInfo);
						}
					}
					catch(Exception e) {
						failList.add(arrPlace[i] + "- 중도매인");
						log.error("클래스 : {}, 지역코드 : {}, 실행 실패 : {}", this.getClass(), arrPlace[i], e.getMessage());
					}
				}
				
				// 3. 블랙리스트 정보 (TB_LA_IS_MH_BAD_TRMN) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터
				List<Map<String, Object>> blackList = batchService.selectBlackUserList(params);
				
				// 4. 블랙리스트 데이터 INSERT OR UPDATE
				if (blackList.size() > 0) {
					try {
						for (Map<String, Object> userInfo : blackList) {
							batchService.insertBlackUserInfo(userInfo);
						}
					}
					catch(Exception e) {
						failList.add(params.get("place").toString() + "- 블랙리스트");
						log.error("클래스 : {}, 지역코드 : {}, 실행 실패 : {}", this.getClass(), params.get("place").toString(), e.getMessage());
					}
				}
			}
		}
		log.debug("##### batch_mwmn_user_info [e] #####");
	}
	
	/**
	 * 관리자 정보 연동 배치
	 * 주기 : 1시간 단위 (업무 시간 이후 제외)
	 * @throws Exception
	 */
	@Scheduled(cron = "0 0 7-19 * * *")
	public void batch_admin_user_info() throws Exception {
		log.debug("##### batch_admin_user_info [s] #####");
		log.debug("##### profile : {}", applicationProfile);
		log.debug("##### time    : {}", LocalDateTime.now());
		
		if ("production".equals(applicationProfile)) {

			final Map<String, Object> params = new HashMap<String, Object>();
			
			// 지역코드(TB_LA_IS_BM_BZLOC 참조) > 153 : 화순, 163 : 무진장, 119 : 하동
			final int[] arrPlace = {119, 153, 163};
//			final int[] arrPlace = {119};
			
			// 실행 실패 리스트
			final List<String> failList = new ArrayList<String>();
			
			params.put("isAll", params.getOrDefault("isAll", "Y"));
			
			for (int i = 0; i < arrPlace.length; i++) {
				// 1. 관리자 정보 (TB_LA_IS_MM_USR) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터 
				params.put("place", arrPlace[i]);
				List<Map<String, Object>> userList = batchService.selectAdminUserList(params);
				
				// 2. 관리자 리스트 데이터 INSERT OR UPDATE
				if (userList.size() > 0) {
					try {
						for (Map<String, Object> userInfo : userList) {
							batchService.insertAdminUserInfo(userInfo);
						}
					}
					catch(Exception e) {
						failList.add(arrPlace[i] + "- 관리자 정보");
						log.error("클래스 : {}, 지역코드 : {}, 실행 실패 : {}", this.getClass(), arrPlace[i], e.getMessage());
					}
				}
			}
		}
		log.debug("##### batch_admin_user_info [e] #####");
	}
	
	/**
	 * 농가 정보 연동 배치 (지역조합DB조회 변경으로 사용안함)
	 * 주기 : 5분 단위 (업무 시간 이후 제외)
	 * @throws Exception
	 */
//	@Scheduled(cron = "0 0/5 * * * *")
	public void batch_fhs_user_info() throws Exception {
		log.debug("##### batch_fhs_user_info [s] #####");
		log.debug("##### profile : {}", applicationProfile);
		log.debug("##### time    : {}", LocalDateTime.now());
		
		if ("production".equals(applicationProfile)) {
			
			final Map<String, Object> params = new HashMap<String, Object>();
			
			// 지역코드(TB_LA_IS_BM_BZLOC 참조) > 153 : 화순, 163 : 무진장, 119 : 하동
			final int[] arrPlace = {119, 153, 163};
//			final int[] arrPlace = {119};
			
			// 실행 실패 리스트
			final List<String> failList = new ArrayList<String>();
			
			params.put("isAll", params.getOrDefault("isAll", "N"));
			
			for (int i = 0; i < arrPlace.length; i++) {
				// 1. 관리자 정보 (TB_LA_IS_MM_USR) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터 
				params.put("place", arrPlace[i]);
				List<Map<String, Object>> userList = batchService.selectFhsUserList(params);
				
				// 2. 관리자 리스트 데이터 INSERT OR UPDATE
				if (userList.size() > 0) {
					try {
						for (Map<String, Object> userInfo : userList) {
							batchService.insertFhsUserInfo(userInfo);
						}
					}
					catch(Exception e) {
						failList.add(arrPlace[i] + "- 농가 정보");
						log.error("클래스 : {}, 지역코드 : {}, 실행 실패 : {}", this.getClass(), arrPlace[i], e.getMessage());
					}
				}
			}
		}
		log.debug("##### batch_fhs_user_info [e] #####");
	}
	
	/**
	 * 출장우, 개체 정보 연동 배치
	 * 주기 : 30초 단위 (7시 시작 18시 59분 30초 종료)
	 * @throws Exception
	 */
	@Scheduled(cron = "0/30 * * * * *")
//	@Scheduled(cron = "0/30 * 7-18 * * *")
	public void batch_cow_info() throws Exception {
		log.debug("##### batch_cow_info [s] #####");
		log.debug("##### profile : {}", applicationProfile);
		log.debug("##### time    : {}", LocalDateTime.now());

		if ("production".equals(applicationProfile)) {

			final Map<String, Object> params = new HashMap<String, Object>();
	
			// 지역코드(TB_LA_IS_BM_BZLOC 참조) > 153 : 화순, 163 : 무진장, 119 : 하동
			final int[] arrPlace = {119, 153, 163};
//			final int[] arrPlace = {119};
	
			// 실행 실패 리스트
			final List<String> failList = new ArrayList<String>();

			// 오늘 진행/예정 경매 리스트
			final List<Map<String, Object>> auctionList = batchService.selectTodayAuctionList(params);

			if (auctionList.size() > 0) {
				params.put("isAll", params.getOrDefault("isAll", "Y"));
				
				for (int i = 0; i < arrPlace.length; i++) {
					// 1. 출장우 정보 (TB_LA_IS_MH_SOG_COW) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터
					params.put("place", arrPlace[i]);
					List<Map<String, Object>> cowList = batchService.selectCowList(params);
					
					// 2. 출장우 리스트 데이터 INSERT OR UPDATE
					if (cowList.size() > 0) {
						try {
							for (Map<String, Object> cowInfo : cowList) {
								batchService.insertCowInfo(cowInfo);
							}
						}
						catch(Exception e) {
							failList.add(params.get("place").toString() + "- 출장우");
							log.error("클래스 : {}, 지역코드 : {}, 실행 실패 : {}", this.getClass(), params.get("place").toString(), e.getMessage());
						}
					}
					
					// 3. 개체 정보 (TB_LA_IS_MM_INDV) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터
					List<Map<String, Object>> indvList = batchService.selectIndvList(params);
					
					// 4. 개체 정보 데이터 INSERT OR UPDATE
					if (indvList.size() > 0) {
						try {
							for (Map<String, Object> indvInfo : indvList) {
								batchService.insertIndvInfo(indvInfo);
							}
						}
						catch(Exception e) {
							failList.add(params.get("place").toString() + "- 개체정보");
							log.error("클래스 : {}, 지역코드 : {}, 실행 실패 : {}", this.getClass(), params.get("place").toString(), e.getMessage());
						}
					}
				}
			}
		}
		log.debug("##### batch_cow_info [e] #####");
	}
	
}
