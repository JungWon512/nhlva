package com.ishift.auction.service.auction;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface AuctionService {

	List<Map<String,Object>> noticeSelectList(Map<String, Object> reqMap) throws SQLException;

	Map<String,Object> selectOneNotice(Map<String, Object> map) throws SQLException;

	List<Map<String, Object>> entrySelectList(Map<String, Object> reqMap) throws SQLException;

	Map<String, Object> selectNearAucDate(Map<String, Object> reqMap) throws SQLException;

	List<Map<String, Object>> selectAucDateQcn(Map<String, Object> map) throws SQLException;

	List<Map<String, Object>> selectAucDateStn(Map<String, Object> map) throws SQLException;

	/**
	 * 경매예정,결과 일정 조회
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectAucDateList(Map<String, Object> map) throws SQLException;

	/**
	 * distict 화면 데이터 조회
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectBizLocList(Map<String, Object> map) throws SQLException;
	
	/**
	 * 최초 home화면 조회
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectBizList(Map<String, Object> map) throws SQLException;

	/**
	 * 일정확인 조회
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectCalendarList(Map<String, Object> map) throws SQLException;

	/**
	 * 경매예정 찜하기 추가/수정
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	int insertUpdateZim(Map<String, Object> param) throws SQLException;
	
	/**
	 * 경매 결과 업데이트
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	int updateAuctionResult(Map<String, Object> params) throws SQLException;
	
	/**
	 * 경매관전 카운트 조회
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectCountEntry(Map<String, Object> map) throws SQLException;

	/**
	 * 경매예정 찜하기 취소
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	int deleteZimPrice(Map<String, Object> params) throws SQLException;

	/**
	 * 경매 출품 리스트 
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	List<Map<String, Object>> selectAuctionEntry(Map<String, Object> params) throws SQLException;

	/**
	 * 출품된 소의 찜가격 조회
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	Map<String, Object> selectMyFavoriteInfo(Map<String, Object> params) throws SQLException;

	/**
	 * App 버전 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectAppVersionInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 통계 - 나의 구매현황 
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	List<Map<String, Object>> selectMyBuyList(Map<String, Object> params) throws SQLException;

	/**
	 * 경매 가능 지점 리스트
	 * @param result
	 * @return
	 * @throws SQLException 
	 */
	List<Map<String, Object>> selectAuctionBizList(Map<String, Object> result) throws SQLException;

	/**
	 * 현재 위치에서 가까운 지점 (2km이내)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectNearestBranchInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 카카오 커넥트 라이브 서비스ID, Key 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectKakaoServiceInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 부하테스트용 토큰 발급(삭제예정)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectTestUserList(Map<String, Object> params) throws SQLException;

	/**
	 * 나의 경매 참가 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectMyEntryList(Map<String, Object> params) throws SQLException;

	Map<String, Object> sealectAuctQcn(Map<String, Object> params) throws SQLException;

	int sealectAuctCowCnt(Map<String, Object> params) throws SQLException ;

	/**
	 * 출하우 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectAuctCowList(Map<String, Object> params) throws SQLException ;

	int updateLowSbidAmt(Map<String, Object> params) throws SQLException;

	int updateAuctCowSt(Map<String, Object> params) throws SQLException;

	int updateAuctCowResult(Map<String, Object> params) throws SQLException;

	int selectBidLogCnt(Map<String, Object> params) throws SQLException;

	Map<String, Object> selectNextBidNum(Map<String, Object> params) throws SQLException;

	int insertBidLog(Map<String, Object> params) throws SQLException;

	List<Map<String, Object>> selectFeeInfo(Map<String, Object> params) throws SQLException;
 
	int deleteFeeInfo(Map<String, Object> params) throws SQLException;

	int insertFeeLog(Map<String, Object> params) throws SQLException;

	Map<String, Object> selectAuctBidNum(Map<String, Object> params) throws SQLException;

	Map<String, Object> selectAuctStn(Map<String, Object> params) throws SQLException;

	/**
	 * 출장우 상세 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectCowInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 출장우 정보 업데이트(중량, 계류대, 하한가)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	int updateCowInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 응찰자 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectBidEntryList(Map<String, Object> params) throws SQLException;

	int insertAuctStnLog(Map<String, Object> temp) throws SQLException;

	int updateAuctStn(Map<String, Object> temp) throws SQLException;

	int updateAuctSogCow(Map<String, Object> temp) throws SQLException;

	int updateAuctSogCowFinish(Map<String, Object> temp) throws SQLException;

	Map<String, Object> selectMaxDdlQcn(Map<String, Object> params) throws SQLException;

	int insertAuctSogCowLog(Map<String, Object> temp) throws SQLException;

	/**
	 * 경매 결과 업데이트 - 실패시 실패 정보 return
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> updateAuctionResultMap(Map<String, Object> params) throws SQLException;

	/**
	 * 일괄 경매 시작
	 * @param aucStn
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> auctionStart(Map<String, Object> aucStn, Map<String, Object> params) throws SQLException;

	/**
	 * 일괄 경매 중지
	 * @param aucStn
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> auctionPause(Map<String, Object> aucStn, Map<String, Object> params) throws SQLException;
	
	/**
	 * 일괄 경매 종료
	 * @param aucStn
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> auctionFinish(Map<String, Object> aucStn, Map<String, Object> params) throws SQLException;

	/**
	 * 금일 경매차수 조회
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectAuctQcnForToday() throws SQLException;

	/**
	 * 응찰서버 정보 업데이트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	int updateNetPort(Map<String, Object> params) throws SQLException;

	/**
	 * 최근 응찰 금액 
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectNearAtdrAm(Map<String, Object> params) throws SQLException;

	/**
	 * 찜 가격 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectMyZimPrice(Map<String, Object> params) throws SQLException;

	/**
	 * 응찰목록
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectBidLogList(Map<String, Object> params) throws SQLException;

	/**
	 * 응찰목록 수
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectBidLogListCnt(Map<String, Object> params) throws SQLException;

	/**
	 * 공통 코드 리스트
	 * @param params
	 * @return
	 */
	List<Map<String, Object>> selectCodeList(Map<String, Object> params) throws SQLException;

	/**
	 * 출장우 정보 > APP에서 일괄 경매시 사용
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectCowList(Map<String, Object> params) throws SQLException;;


	/**
	 * 나의 구매내역 > 총 구매금액 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectTotSoldPrice(Map<String, Object> map) throws SQLException;
	
	int updateNoticeReadCnt(Map<String, Object> params) throws SQLException;

	/**
	 * 안드로이드, 아이폰 버전 정보 업데이트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	int updateAppVersion(Map<String, Object> params) throws SQLException;


	/**
	 * 기등록 계류대번호 초기화
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	int updateCowInfoForModlNo(Map<String, Object> params) throws SQLException;

	/**
	 * STN 정보
	 * @param temp
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> getStnInfo(Map<String, Object> temp) throws SQLException;

	/**
	 * 경매 구간 정보(전체)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectAucStnList(Map<String, Object> params) throws SQLException;

	/**
	 * 수의사 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectVetList(Map<String, Object> params) throws SQLException;

	/***
	 * 
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectAuctMwmn(Map<String, Object> params) throws SQLException;

	
	/**
	 * 매수인 정산서(전체)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> entryStateSelectList(Map<String, Object> params) throws SQLException;
	
	/**
	 * 매수인 정산- 매수인 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectStateInfo(Map<String, Object> params) throws SQLException;
	
	/**
	 * 매수인 정산서 - 낙찰가 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectTotSoldPriceAndFee(Map<String, Object> params) throws SQLException;
	Map<String, Object> selectStateEntryCntFhs(Map<String, Object> params) throws SQLException;
	
	/**
	 * 매수인 정산서 - 조합 입금정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectJohapAccInfo(Map<String, Object> params) throws SQLException;
	
	/**
	 * 매수인 정산서 - 수수료 상세 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> myFeeStateList(Map<String, Object> params) throws SQLException;

	/**
	 * 정산서 리스트 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectStateList(Map<String, Object> params) throws SQLException;

	/**
	 * 형매정보
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectListIndvSib(Map<String, Object> param) throws SQLException;

	/**
	 * 출장우 이미지
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectListCowImg(Map<String, Object> param) throws SQLException;

	/**
	 * 이동정보
	 * @param paramMap
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectListIndvMove(Map<String, Object> paramMap) throws SQLException;

	/**
	 * 노출정보 조회
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectListExpitemSet(Map<String, Object> map) throws SQLException;

	/**
	 * 후대정보 조회
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectListIndvPost(Map<String, Object> param) throws SQLException;
	
	/**
	 * 가축시장 경매일정
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectcheduleList(Map<String, Object> param) throws SQLException;

	/**
	 * 개체번호 기준 경매정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectIndvDataInfo(Map<String, Object> map) throws SQLException;

	/**
	 * 분만정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	List<Map<String, Object>> selectListIndvChildBirth(Map<String, Object> paramMap) throws SQLException;

	/**
	 * 이용해지 신청 데이터 있는지 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectMySecAplyInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 이용해지 신청 데이터 insert
	 * @param params
	 * @throws SQLException
	 */
	void insertMySecAplyInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 이용해지 신청 데이터 delete (해지 철회할 때)
	 * @param params
	 * @throws SQLException
	 */
	void deleteMySecAplyInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 키오스크 인증번호 확인 (중도매인)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectMwmnAuthNoYmdInfo(Map<String, Object> map) throws SQLException;

	/**
	 * 키오스크 인증번호 확인 (출하주)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> selectFhsAuthNoYmdInfo(Map<String, Object> map) throws SQLException;

	/**
	 * 키오스크 발급된 인증번호 업데이트 (중도매인)
	 * @param params
	 * @throws SQLException
	 */
	void updateMwmnAuthNoYmdInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 키오스크 발급된 인증번호 업데이트 (출하주)
	 * @param params
	 * @throws SQLException
	 */
	void updateFhsAuthNoYmdInfo(Map<String, Object> params) throws SQLException;

	/**
	 * 키오스크 인증번호 발급하기
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	Map<String, Object> myAuthNumIssue(Map<String, Object> params) throws SQLException;
	
}