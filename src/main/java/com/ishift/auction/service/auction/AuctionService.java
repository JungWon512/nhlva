package com.ishift.auction.service.auction;

import java.util.List;
import java.util.Map;

public interface AuctionService {

	List<Map<String,Object>> noticeSelectList(Map<String, Object> reqMap) throws Exception;

	Map<String,Object> selectOneNotice(Map<String, Object> map) throws Exception;

	List<Map<String, Object>> entrySelectList(Map<String, Object> reqMap) throws Exception;

	Map<String, Object> selectNearAucDate(Map<String, Object> reqMap) throws Exception;

	List<Map<String, Object>> selectAucDateQcn(Map<String, Object> map) throws Exception;

	List<Map<String, Object>> selectAucDateStn(Map<String, Object> map) throws Exception;

	/**
	 * 경매예정,결과 일정 조회
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectAucDateList(Map<String, Object> map) throws Exception;

	/**
	 * distict 화면 데이터 조회
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectBizLocList(Map<String, Object> map) throws Exception;
	
	/**
	 * 최초 home화면 조회
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectBizList(Map<String, Object> map) throws Exception;

	/**
	 * 일정확인 조회
	 * @param map
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectCalendarList(Map<String, Object> map) throws Exception;

	/**
	 * 경매예정 찜하기 추가/수정
	 * @param param
	 * @return
	 * @throws Exception
	 */
	int insertUpdateZim(Map<String, Object> param) throws Exception;
	
	/**
	 * 경매 결과 업데이트
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	int updateAuctionResult(Map<String, Object> params) throws Exception;
	
	/**
	 * 경매관전 카운트 조회
	 * @param map
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectCountEntry(Map<String, Object> map) throws Exception;

	/**
	 * 경매예정 찜하기 취소
	 * @param params
	 * @return
	 * @throws Exception
	 */
	int deleteZimPrice(Map<String, Object> params) throws Exception;

	/**
	 * 경매 출품 리스트 
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	List<Map<String, Object>> selectAuctionEntry(Map<String, Object> params) throws Exception;

	/**
	 * 출품된 소의 찜가격 조회
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	Map<String, Object> selectMyFavoriteInfo(Map<String, Object> params) throws Exception;

	/**
	 * App 버전 정보
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectAppVersionInfo(Map<String, Object> params) throws Exception;

	/**
	 * 통계 - 나의 구매현황 
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	List<Map<String, Object>> selectMyBuyList(Map<String, Object> params) throws Exception;

	/**
	 * 경매 가능 지점 리스트
	 * @param result
	 * @return
	 * @throws Exception 
	 */
	List<Map<String, Object>> selectAuctionBizList(Map<String, Object> result) throws Exception;

	/**
	 * 현재 위치에서 가까운 지점 (2km이내)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectNearestBranchInfo(Map<String, Object> params) throws Exception;

	/**
	 * 카카오 커넥트 라이브 서비스ID, Key 정보
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectKakaoServiceInfo(Map<String, Object> params) throws Exception;

	/**
	 * 부하테스트용 토큰 발급(삭제예정)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectTestUserList(Map<String, Object> params) throws Exception;

	/**
	 * 나의 경매 참가 정보
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectMyEntryList(Map<String, Object> params) throws Exception;

	Map<String, Object> sealectAuctQcn(Map<String, Object> params) throws Exception;
	

	int sealectAuctCowCnt(Map<String, Object> params) throws Exception ;

	List<Map<String, Object>> selectAuctCowList(Map<String, Object> params) throws Exception ;

	int updateLowSbidAmt(Map<String, Object> params) throws Exception;

	int updateAuctCowSt(Map<String, Object> params) throws Exception;

	int updateAuctCowResult(Map<String, Object> params) throws Exception;

	int selectBidLogCnt(Map<String, Object> params) throws Exception;

	Map<String, Object> selectNextBidNum(Map<String, Object> params) throws Exception;

	int insertBidLog(Map<String, Object> params) throws Exception;

	List<Map<String, Object>> selectFeeInfo(Map<String, Object> params) throws Exception;
 
	int deleteFeeInfo(Map<String, Object> params) throws Exception;

	int insertFeeLog(Map<String, Object> params) throws Exception;

	Map<String, Object> selectAuctBidNum(Map<String, Object> params) throws Exception;

	Map<String, Object> selectAuctStn(Map<String, Object> params) throws Exception;

	/**
	 * 출장우 상세 정보
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> selectCowInfo(Map<String, Object> params) throws Exception;

	/**
	 * 출장우 정보 업데이트(중량, 계류대, 하한가)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	int updateCowInfo(Map<String, Object> params) throws Exception;

	/**
	 * 응찰자 리스트
	 * @param params
	 * @return
	 * @throws Exception
	 */
	List<Map<String, Object>> selectBidEntryList(Map<String, Object> params) throws Exception;

	int insertAuctStnLog(Map<String, Object> temp) throws Exception;

	int updateAuctStn(Map<String, Object> temp) throws Exception;

	int updateAuctSogCow(Map<String, Object> temp) throws Exception;

	int updateAuctSogCowFinish(Map<String, Object> temp) throws Exception;

	Map<String, Object> selectMaxDdlQcn(Map<String, Object> params) throws Exception;

	int insertAuctSogCowLog(Map<String, Object> temp) throws Exception;

	/**
	 * 경매 결과 업데이트 - 실패시 실패 정보 return
	 * @param params
	 * @return
	 * @throws Exception
	 */
	Map<String, Object> updateAuctionResultMap(Map<String, Object> params) throws Exception;
}
