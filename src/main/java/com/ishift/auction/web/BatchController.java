package com.ishift.auction.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ishift.auction.service.batch.BatchService;

import lombok.extern.slf4j.Slf4j;

/**
 * Mssql db -> maria db 데이터 연동 batch(호출용) - 1차에서만 사용 예정
 * @author Yuchan
 */
@Slf4j
@Controller
public class BatchController {

	@Autowired
	private BatchService batchService;
	
	/**
	 * 각 조합마다 관리하는 DB(MsSql)에서 경매 일정 정보를 가져와 MAIN DB(Maria)에 저장한다.
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@PostMapping(path = "/batch/batch_auction_calendar", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object>batch_auction_calendar(@RequestBody Map<String, Object> params) throws Exception {

		final Map<String, Object> result = new HashMap<String, Object>();

		// 지역코드(TB_LA_IS_BM_BZLOC 참조) > 153 : 화순, 163 : 무진장, 119 : 하동
		final int[] arrPlace = {119, 153, 163};

		// 실행 실패 리스트
		final List<String> failList = new ArrayList<String>();

		params.put("isAll", params.getOrDefault("isAll", "N"));

		if (params.get("place") != null) {
			// 1. 단일 경매 (TB_LA_IS_MH_AUC_QCN) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터
			List<Map<String, Object>> qcnList = batchService.selectQcnAuctionList(params);
			
			// 2. 단일 경매 리스트 데이터 INSERT OR UPDATE
			if (qcnList.size() > 0) {
				try {
					for (Map<String, Object> qcnInfo : qcnList) {
						batchService.insertQcnAuctionInfo(qcnInfo);
					}
				}
				catch(Exception e) {
					failList.add(params.get("place").toString() + "- 단일 경매");
					log.error("클래스 : {}, 지역코드 : {}, 단일 경매 실행 실패 : {}", this.getClass(), params.get("place").toString(), e.getMessage());
				}
			}
			
			// 3. 일괄 경매 (TB_LA_IS_MH_AUC_STN) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터 
			List<Map<String, Object>> stnList = batchService.selectStnAuctionList(params);
			
			// 4. 일괄 경매 리스트 데이터 INSERT OR UPDATE
			if (stnList.size() > 0) {
				try {
					for (Map<String, Object> stnInfo : stnList) {
						batchService.insertStnAuctionInfo(stnInfo);
					}
				}
				catch(Exception e) {
					failList.add(params.get("place").toString() + "- 단일 경매");
					log.error("클래스 : {}, 지역코드 : {}, 단일 경매 실행 실패 : {}", this.getClass(), params.get("place").toString(), e.getMessage());
				}
			}
		}
		else {
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

		result.put("failList", failList);
		return result;
	}

	/**
	 * 중도매인 정보, 불량 거래인 정보 연동 배치
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@PostMapping(path = "/batch/batch_mwmn_user_info", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> batch_mwmn_user_info(@RequestBody Map<String, Object> params) throws Exception {

		final Map<String, Object> result = new HashMap<String, Object>();

		// 지역코드(TB_LA_IS_BM_BZLOC 참조) > 153 : 화순, 163 : 무진장, 119 : 하동
		final int[] arrPlace = {119, 153, 163};

		// 실행 실패 리스트
		final List<String> failList = new ArrayList<String>();

		params.put("isAll", params.getOrDefault("isAll", "N"));

		if (params.get("place") != null) {
			// 1. 중도매인 정보 (TB_LA_IS_MM_MWMN) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터
			List<Map<String, Object>> userList = batchService.selectMwmnUserList(params);
			
			// 2. 중도매인 리스트 데이터 INSERT OR UPDATE
			if (userList.size() > 0) {
				try {
					for (Map<String, Object> userInfo : userList) {
						batchService.insertMwmnUserInfo(userInfo);
					}
				}
				catch(Exception e) {
					failList.add(params.get("place").toString() + "- 중도매인");
					log.error("클래스 : {}, 지역코드 : {}, 실행 실패 : {}", this.getClass(), params.get("place").toString(), e.getMessage());
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
		else {
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

		result.put("failList", failList);
		return result;
	}
	
	/**
	 * 관리자 정보 연동 배치
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@PostMapping(path = "/batch/batch_admin_user_info", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> batch_admin_user_info(@RequestBody Map<String, Object> params) throws Exception {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		// 지역코드(TB_LA_IS_BM_BZLOC 참조) > 153 : 화순, 163 : 무진장, 119 : 하동
		final int[] arrPlace = {119, 153, 163};
		
		// 실행 실패 리스트
		final List<String> failList = new ArrayList<String>();
		
		params.put("isAll", params.getOrDefault("isAll", "N"));
		
		if (params.get("place") != null) {
			// 1. 관리자 정보 (TB_LA_IS_MM_USR) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터
			List<Map<String, Object>> userList = batchService.selectAdminUserList(params);
			
			// 2. 관리자 리스트 데이터 INSERT OR UPDATE
			if (userList.size() > 0) {
				try {
					for (Map<String, Object> userInfo : userList) {
						batchService.insertAdminUserInfo(userInfo);
					}
				}
				catch(Exception e) {
					failList.add(params.get("place").toString() + "- 관리자 정보");
					log.error("클래스 : {}, 지역코드 : {}, 실행 실패 : {}", this.getClass(), params.get("place").toString(), e.getMessage());
				}
			}
		}
		else {
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
		
		result.put("failList", failList);
		return result;
	}

	/**
	 * 농가 정보 연동 배치
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@PostMapping(path = "/batch/batch_fhs_user_info", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> batch_fhs_user_info(@RequestBody Map<String, Object> params) throws Exception {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		// 지역코드(TB_LA_IS_BM_BZLOC 참조) > 153 : 화순, 163 : 무진장, 119 : 하동
		final int[] arrPlace = {119, 153, 163};
		
		// 실행 실패 리스트
		final List<String> failList = new ArrayList<String>();
		
		params.put("isAll", params.getOrDefault("isAll", "N"));
		
		if (params.get("place") != null) {
			// 1. 농가 정보 (TB_LA_IS_MM_FHS) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터
			List<Map<String, Object>> userList = batchService.selectFhsUserList(params);
			
			// 2. 농가 리스트 데이터 INSERT OR UPDATE
			if (userList.size() > 0) {
				try {
					for (Map<String, Object> userInfo : userList) {
						batchService.insertFhsUserInfo(userInfo);
					}
				}
				catch(Exception e) {
					failList.add(params.get("place").toString() + "- 농가 정보");
					log.error("클래스 : {}, 지역코드 : {}, 실행 실패 : {}", this.getClass(), params.get("place").toString(), e.getMessage());
				}
			}
		}
		else {
			for (int i = 0; i < arrPlace.length; i++) {
				// 1. 농가 정보 (TB_LA_IS_MM_FHS) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터 
				params.put("place", arrPlace[i]);
				List<Map<String, Object>> userList = batchService.selectFhsUserList(params);
				
				// 2. 농가 리스트 데이터 INSERT OR UPDATE
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

		result.put("failList", failList);
		return result;
	}
	
	/**
	 * 출장우, 개체 정보 연동 배치
	 * @return
	 * @throws Exception 
	 */
	@ResponseBody
	@PostMapping(path = "/batch/batch_cow_info", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> batch_cow_info(@RequestBody Map<String, Object> params) throws Exception {

		final Map<String, Object> result = new HashMap<String, Object>();

		// 지역코드(TB_LA_IS_BM_BZLOC 참조) > 153 : 화순, 163 : 무진장, 119 : 하동
		final int[] arrPlace = {119, 153, 163};

		// 실행 실패 리스트
		final List<String> failList = new ArrayList<String>();

		params.put("isAll", params.getOrDefault("isAll", "N"));

		if (params.get("place") != null) {
			// 1. 출장우 정보 (TB_LA_IS_MH_SOG_COW) 리스트 검색 > 검색 기준 : 마지막 수정일자(LSCHG_DTM)가 실행 시각 기준 10분전 이후 데이터
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
		else {
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

		result.put("failList", failList);
		return result;
	}
	
}
