package com.ishift.auction.service.auction;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.ishift.auction.base.dao.MainDao;


/**
 * @author Yuchan
 *
 */
/**
 * @author 김줴
 *
 */
@Repository("auctionDAO")
public class AuctionDAO {
	
	@Autowired
	private MainDao mainDao;
	@Value("${spring.profiles.active}") private String profile;

	public List<Map<String, Object>> noticeSelectList(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("auction.selectListNotice", reqMap);
	}

	public Map<String, Object> selectOneNotice(Map<String, Object> map) throws SQLException {
		return mainDao.selectOne("auction.selectOneNotice", map);
	}
	
	public Map<String, Object> selectStateEntryCntFhs(Map<String, Object> map) throws SQLException {
		return mainDao.selectOne("auction.selectStateEntryCntFhs", map);
	}

	public List<Map<String, Object>> entrySelectList(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectList("auction.selectListAucEntry", reqMap);
	}

	public Map<String, Object> selectNearAucDate(Map<String, Object> reqMap) throws SQLException {
		return mainDao.selectOne("auction.selectNearAucDate", reqMap);
	}

	public List<Map<String, Object>> selectAucDateQcn(Map<String, Object> map) throws SQLException {
		return mainDao.selectList("auction.selectAucDateQcn", map);
	}

	public List<Map<String, Object>> selectAucDateStn(Map<String, Object> map) throws SQLException {
		return mainDao.selectList("auction.selectAucDateStn", map);
	}

	public List<Map<String, Object>> selectAucDateList(Map<String, Object> map) throws SQLException {
		return mainDao.selectList("auction.selectAucDateList", map);
	}

	public List<Map<String, Object>> selectBizLocList(Map<String, Object> map) throws SQLException {
		return mainDao.selectList("auction.selectBizLocList", map);
	}

	public List<Map<String, Object>> selectBizList(Map<String, Object> map) throws SQLException {
		return mainDao.selectList("auction.selectBizList", map);
	}
	public List<Map<String, Object>> selectCalendarList(Map<String, Object> map) throws SQLException {
		return mainDao.selectList("auction.selectCalendarList", map);
	}

	/**
	 * 경매 결과 업데이트 > 경매 결고 업데이트 api 사용(/api/{version}/auction/result)
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public int updateAuctionResult(Map<String, Object> params) throws SQLException {
		return mainDao.update("auction.updateAuctionResult", params);
	}

	public Map<String, Object> selectCountEntry(Map<String, Object> map) throws SQLException {
		return mainDao.selectOne("auction.selectCountEntry", map);
	}

	/**
	 * 찜가격 등록 전 찜가격이 예정가보다 크거나 같은지 체크
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectZimPriceChk(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectZimPriceChk", params);
	}
	
	/**
	 * 찜가격 등록
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int insertUpdateZim(Map<String, Object> param) throws SQLException {
		return mainDao.insert("auction.insertUpdateZim", param);
	}

	/**
	 * 찜가격 삭제
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int deleteZimPrice(Map<String, Object> params) throws SQLException {
		return mainDao.delete("auction.deleteZimPrice", params);
	}

	/**
	 * 경매 출품 리스트
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> selectAuctionEntry(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectAuctionEntry", params);
	}

	/**
	 * 출품된 소의 찜가격 조회
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> selectMyFavoriteInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectMyFavoriteInfo", params);
	}

	/**
	 * App 버전 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectAppVersionInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectAppVersionInfo", params);
	}

	/**
	 * 통계 - 나의 구매현황
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> selectMyBuyList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectMyBuyList", params);
	}

	/**
	 * 경매 가능 지점 리스트
	 * @param result
	 * @return
	 * @throws SQLException 
	 */
	public List<Map<String, Object>> selectAuctionBizList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectAuctionBizList", params);
	}

	/**
	 * 현재 위치에서 가까운 지점 (2km이내)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectNearestBranchInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectNearestBranchInfo", params);
	}

	/**
	 * 카카오 커넥트 라이브 서비스ID, Key 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectKakaoServiceInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectKakaoServiceInfo", params);
	}

	/**
	 * 부하테스트용 토큰 발급(삭제예정)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectTestUserList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectTestUserList", params);
	}

	public List<Map<String, Object>> selectMyEntryList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectMyEntryList", params);
	}

	/**
	 * 경매 회차 정보 검색
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> sealectAuctQcn(Map<String, Object> params) throws SQLException {		
		return mainDao.selectOne("ApiMapper.sealectAuctQcn", params);		
	}
	/**
	 * 출장우 데이터 수 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int sealectAuctCowCnt(Map<String, Object> params) throws SQLException {		
		return mainDao.count("ApiMapper.sealectAuctCowCnt", params);		
	}

	/**
	 * 출장우 데이터 조회 (단일)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectAuctCowList(Map<String, Object> params) throws SQLException {		
		return mainDao.selectList("ApiMapper.selectAuctCowList", params);
	}

	/**
	 * 최저가변경
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int updateLowSbidAmt(Map<String, Object> params) throws SQLException {		
		return mainDao.update("ApiMapper.updateLowSbidAmt", params);
	}

	/**
	 * 경매상태변경(보류)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int updateAuctCowSt(Map<String, Object> params) throws SQLException {		
		return mainDao.update("ApiMapper.updateAuctCowSt", params);
	}

	/**
	 * 경매결과저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int updateAuctCowResult(Map<String, Object> params) throws SQLException {		
		return mainDao.update("ApiMapper.updateAuctCowResult", params);
	}

	/**
	 * 응찰내역카운트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int selectBidLogCnt(Map<String, Object> params) throws SQLException {		
		return mainDao.count("ApiMapper.selectBidLogCnt", params);
	}

	/**
	 * 다음 응츨번호 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectNextBidNum(Map<String, Object> params) throws SQLException {		
		return mainDao.selectOne("ApiMapper.selectNextBidNum", params);
	}

	/**
	 * 응찰로그 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int insertBidLog(Map<String, Object> params) throws SQLException {
		return mainDao.insert("ApiMapper.insertBidLog", params);
	}

	/**
	 * 경매참가사용자조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectAuctBidNum(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("ApiMapper.selectAuctBidNum", params);
	}

	public Map<String, Object> selectAuctStn(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("ApiMapper.selectAuctStn", params);
	}

	/**
	 * 출장우 상세 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectCowInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectCowInfo", params);
	}

	/**
	 * 출장우 정보 업데이트(중량, 계류대, 하한가)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int updateCowInfo(Map<String, Object> params) throws SQLException {
		return mainDao.update("auction.updateCowInfo", params);
	}

	/**
	 * 응찰자 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectBidEntryList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("ApiMapper.selectBidEntryList", params);
	}

	public int insertAuctStnLog(Map<String, Object> params) throws SQLException {
		return mainDao.insert("ApiMapper.insertAuctStnLog", params);
	}

	public int updateAuctStn(Map<String, Object> params) throws SQLException {
		return mainDao.update("ApiMapper.updateAuctStn", params);
	}

	public int updateAuctSogCow(Map<String, Object> params) throws SQLException {
		return mainDao.update("ApiMapper.updateAuctSogCow", params);
	}

	public int updateAuctSogCowFinish(Map<String, Object> params) throws SQLException {
		return mainDao.update("ApiMapper.updateAuctSogCowFinish", params);
	}

	public Map<String, Object> selectMaxDdlQcn(Map<String, Object> params) throws SQLException{
		return mainDao.selectOne("ApiMapper.selectMaxDdlQcn", params);
	}

	public int insertAuctSogCowLog(Map<String, Object> params) throws SQLException {
		return mainDao.insert("ApiMapper.insertAuctSogCowLog", params);
	}

	/**
	 * 수수료 정산을 위한 경매 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectAuctionInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("ApiMapper.selectAuctionInfo", params);
	}

	/**
	 * 수수료 정보 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int insertFeeInfo(Map<String, Object> params) throws SQLException {
		return mainDao.insert("ApiMapper.insertFeeInfo", params);
	}

	/**
	 * 수수료 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectFeeInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("ApiMapper.selectFeeInfo", params);
	}

	/**
	 * 수수료내역 삭제
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int deleteFeeInfo(Map<String, Object> params) throws SQLException {
		return mainDao.delete("ApiMapper.deleteFeeInfo", params);
	}

	/**
	 * 수수료 내역저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int insertFeeLog(Map<String, Object> params) throws SQLException {
		return mainDao.insert("ApiMapper.insertFeeLog", params);
	}

	/**
	 * 낙찰 처리 대상 목록
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectBidCompleteList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("ApiMapper.selectBidCompleteList", params);
	}

	public List<Map<String, Object>> selectAuctQcnForToday() throws SQLException {
		return mainDao.selectList("ApiMapper.selectAuctQcnForToday", new HashMap<String, Object>());
	}

	public int updateNetPort(Map<String, Object> params) throws SQLException {
		return mainDao.update("ApiMapper.updateNetPort", params);
	}

	public Map<String, Object> selectNearAtdrAm(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("ApiMapper.selectNearAtdrAm", params);
	}

	public Map<String, Object> selectMyZimPrice(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("ApiMapper.selectMyZimPrice", params);
	}
	

	/**
	 * 조합 경매 기본 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectBizAuctionInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("ApiMapper.selectBizAuctionInfo", params);
	}

	public List<Map<String, Object>> selectBidLogList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectBidLogList", params);
	}

	public Map<String, Object> selectBidLogListCnt(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectBidLogListCnt", params);
	}

	/**
	 * 공통 코드 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectCodeList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectCodeList", params);
	}

	/**
	 * 출장우 정보 > APP에서 일괄 경매시 사용
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectCowList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("ApiMapper.selectCowList", params);
	}

	/**
	 * 나의 구매내역 > 총 구매금액 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectTotSoldPrice(Map<String, Object> params) throws SQLException {		
		return mainDao.selectOne("auction.selectTotSoldPrice", params);
	}

	public int updateNoticeReadCnt(Map<String, Object> params) throws SQLException {
		return mainDao.update("auction.updateNoticeReadCnt", params);
	}

	/**
	 * 안드로이드, 아이폰 버전 정보 업데이트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public int updateAppVersion(Map<String, Object> params) throws SQLException {
		return mainDao.update("ApiMapper.updateAppVersion", params);
	}

	public int updateCowInfoForModlNo(Map<String, Object> params) throws SQLException {
		return mainDao.update("auction.updateCowInfoForModlNo", params);
	}

	public Map<String, Object> getStnInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.getStnInfo", params);
	}

	/**
	 * 경매 구간 정보(전체)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectAucStnList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("ApiMapper.selectAucStnList", params);
	}

	public List<Map<String, Object>> selectVetList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectVetList", params);
	}

	public Map<String, Object> selectAuctMwmn(Map<String, Object> params) throws SQLException {
		// TODO Auto-generated method stub
		return mainDao.selectOne("ApiMapper.selectAuctMwmn", params);
	}

	/**
	 * @methodName    : insertSogCowLog
	 * @author        : Jung JungWon
	 * @return 
	 * @throws SQLException 
	 * @date          : 2022.08.19
	 * @Comments      : 
	 */
	public int insertSogCowLog(Map<String, Object> params) throws SQLException {
		// TODO Auto-generated method stub
		return mainDao.insert("auction.insertSogCowLog", params);
	}
	
	/**
	 * 정산서(전체) 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> entryStateSelectList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.entryStateSelectList", params);
	}
	
	/**
	 * 매수인 정산서- 매수인 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectBuyStateInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectBuyStateInfo", params);
	}
	
	/**
	 * 출하우 정산서- 매수인 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectEntryStateInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectEntryStateInfo", params);
	}
	
	/**
	 * 정산서 - 낙찰가 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectTotSoldPriceAndFee(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectTotSoldPriceAndFee", params);	
	}
	
	/**
	 * 정산서 - 조합 입금정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectJohapAccInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectJohapAccInfo", params);	
	}
	/**
	 * 정산서 - 수수료 상세 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> myFeeStateList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.myFeeStateList", params);	
	}
	/**
	 * 정산서 리스트 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectStateBuyList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectStateBuyList", params);	
	}
	/**
	 * 정산서 리스트 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectStateEntryList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectStateEntryList", params);	
	}

	/**
	 * 형매정보
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectListIndvSib(Map<String, Object> param) throws SQLException {
		return mainDao.selectList("auction.selectListIndvSib", param);
	}

	/**
	 * 출장우 이미지
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectListCowImg(Map<String, Object> param) throws SQLException {
		return mainDao.selectList("auction.selectListCowImg", param);
	}

	/**
	 * 개체 이동정보
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectListIndvMove(Map<String, Object> param) throws SQLException {
		return mainDao.selectList("auction.selectListIndvMove", param);
	}

	/**
	 * 출장우 상세 페이지 노출 탭 리스트
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectListExpitemSet(Map<String, Object> map) throws SQLException {
		return mainDao.selectList("auction.selectListExpitemSet", map);
	}


	/**
	 * 후대정보
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectListIndvPost(Map<String, Object> param) throws SQLException {
		return mainDao.selectList("auction.selectListIndvPost", param);
	}

	/**
	 * 가축시장 경매일정
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectcheduleList(Map<String, Object> param) throws SQLException {
		return mainDao.selectList("auction.selectcheduleList", param);
	}

	/**
	 * 개체번호 기준으로 경매정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectIndvDataInfo(Map<String, Object> param) throws SQLException {
		return mainDao.selectOne("auction.selectIndvDataInfo", param);
	}


	/**
	 * 개체번호로 분만정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectListIndvChildBirth(Map<String, Object> param) throws SQLException {
		return mainDao.selectList("auction.selectListIndvChildBirth", param);
	}

	/**
	 * 이용해지 신청 데이터 있는지 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectMySecAplyInfo(Map<String, Object> params) throws SQLException{
		return mainDao.selectOne("auction.selectMySecAplyInfo", params);
	}

	/**
	 * 이용해지 신청 데이터 insert
	 * @param params
	 * @throws SQLException
	 */
	public void insertMySecAplyInfo(Map<String, Object> params) throws SQLException{
		mainDao.insert("auction.insertMySecAplyInfo", params);
	}

	/**
	 * 이용해지 신청 데이터 delete (해지 철회할 때)
	 * @param params
	 * @throws SQLException
	 */
	public void deleteMySecAplyInfo(Map<String, Object> params) throws SQLException{
		mainDao.delete("auction.deleteMySecAplyInfo", params);
	}

	/**
	 * 키오스크 인증번호 확인 (중도매인)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectMwmnAuthNoYmdInfo(Map<String, Object> map) throws SQLException{
		return mainDao.selectOne("auction.selectMwmnAuthNoYmdInfo", map);
	}

	/**
	 * 키오스크 인증번호 확인 (출하주)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectFhsAuthNoYmdInfo(Map<String, Object> map) throws SQLException{
		return mainDao.selectOne("auction.selectFhsAuthNoYmdInfo", map);
	}
	
	/**
	 * 키오스크 인증번호 확인 카운트 (중도매인)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectMwmnAuthNoYmdInfoCnt(Map<String, Object> map) throws SQLException{
		return mainDao.selectOne("auction.selectMwmnAuthNoYmdInfoCnt", map);
	}

	/**
	 * 키오스크 인증번호 확인 카운트 (출하주)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectFhsAuthNoYmdInfoCnt(Map<String, Object> map) throws SQLException{
		return mainDao.selectOne("auction.selectFhsAuthNoYmdInfoCnt", map);
	}

	/**
	 * 키오스크 발급된 인증번호 업데이트 (중도매인)
	 * @param params
	 * @throws SQLException
	 */
	public void updateMwmnAuthNoYmdInfo(Map<String, Object> params) throws SQLException{
		mainDao.update("auction.updateMwmnAuthNoYmdInfo", params);
	}

	/**
	 * 키오스크 발급된 인증번호 업데이트 (출하주)
	 * @param params
	 * @throws SQLException
	 */
	public void updateFhsAuthNoYmdInfo(Map<String, Object> params) throws SQLException{
		mainDao.update("auction.updateFhsAuthNoYmdInfo", params);
	}

	/**
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectScheduleListQcn(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectScheduleListQcn", params);
	}

	/**
	 * 이용해지 신청대상 해당 조합조회
	 * @param params
	 * @throws SQLException
	 */
	public List<Map<String, Object>> selectJohqpList(Map<String, Object> params) throws SQLException{
		return mainDao.selectList("auction.selectJohqpList", params);
	}

	/**
	 * 알림톡 아이디 채번
	 * @param map
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> selelctMca5100AlarmTalkId(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selelctMca5100AlarmTalkId", params);
	}

	/**
	 * 알림톡 템플릿 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	public Map<String, Object> selectTemplateInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectTemplateInfo", params);
	}

	/**
	 * 알림톡에 포함 될 정보
	 * @param auctionInfo
	 * @return
	 * @throws SQLException 
	 */
	public Map<String, Object> selectMsgCntnInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectMsgCntnInfo", params);
	}

	/**
	 * SMS 발송정보 저장
	 * @param params
	 * @throws SQLException
	 */
	public void insertSmsInfo(Map<String, Object> params) throws SQLException {
		mainDao.insert("auction.insertSmsInfo", params);
	}

	public List<Map<String, Object>> selectCowInfoList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectCowInfo", params);
	}

	public List<Map<String, Object>> selectMainPopNoticeList(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectMainPopNoticeList", params);
	}

	public Map<String, Object> selectIndvBloodInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("auction.selectIndvBloodInfo", params);
	}

	public Map<String, Object> selectCowEpdInfo(Map<String, Object> params) throws SQLException {
		return mainDao.selectOne("ApiMapper.selectCowEpdInfo", params);
	}

	public List<Map<String, Object>> selectSumEntry(Map<String, Object> params) throws SQLException {
		return mainDao.selectList("auction.selectSumEntry", params);
	}
	
}