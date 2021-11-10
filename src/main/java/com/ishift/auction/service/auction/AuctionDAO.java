package com.ishift.auction.service.auction;

import com.ishift.auction.base.dao.MainDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


/**
 * @author Yuchan
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
}