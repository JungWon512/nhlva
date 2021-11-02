package com.ishift.auction.service.auction;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service("auctionService")
public class AuctionServiceImpl implements AuctionService {

	@Resource(name = "auctionDAO")
	AuctionDAO auctionDAO;
	
	@Override
	public List<Map<String,Object>> noticeSelectList(Map<String, Object> reqMap)throws Exception {
		return auctionDAO.noticeSelectList(reqMap);
	}
	
	@Override
	public Map<String,Object> selectOneNotice(Map<String, Object> map) throws Exception{
		return auctionDAO.selectOneNotice(map);
	}
	@Override
	public List<Map<String, Object>> entrySelectList(Map<String, Object> reqMap) throws Exception{
		return auctionDAO.entrySelectList(reqMap);
	}
	@Override
	public Map<String, Object> selectNearAucDate(Map<String, Object> reqMap) throws Exception{
		return auctionDAO.selectNearAucDate(reqMap);
	}
	
	@Override
	public List<Map<String, Object>> selectAucDateQcn(Map<String, Object> map) throws Exception{
		return auctionDAO.selectAucDateQcn(map);
	}
	
	@Override
	public List<Map<String, Object>> selectAucDateStn(Map<String, Object> map) throws Exception{
		return auctionDAO.selectAucDateStn(map);
	}
	
	@Override
	public List<Map<String, Object>> selectAucDateList(Map<String, Object> map) throws Exception{
		return auctionDAO.selectAucDateList(map);
	}
	@Override
	public List<Map<String, Object>> selectBizLocList(Map<String, Object> map) throws Exception{
		return auctionDAO.selectBizLocList(map);
	}
	@Override
	public List<Map<String, Object>> selectBizList(Map<String, Object> map) throws Exception{
		return auctionDAO.selectBizList(map);
	}
	@Override
	public List<Map<String, Object>> selectCalendarList(Map<String, Object> map) throws Exception{
		return auctionDAO.selectCalendarList(map);
	}
	
	/**
	 * 경매예정 찜하기 추가/수정
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public int insertUpdateZim(Map<String, Object> param) throws Exception {
		return auctionDAO.insertUpdateZim(param);
	}

	/**
	 * 경매 결과 업데이트
	 * @throws Exception 
	 */
	@Override
	public int updateAuctionResult(Map<String, Object> params) throws Exception {
		return auctionDAO.updateAuctionResult(params);
	} 
	
	/**
	 * 경매관전 카운트 조회
	 * @param map
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectCountEntry(Map<String, Object> map) throws Exception{
		return auctionDAO.selectCountEntry(map);
	}
	
	/**
	 * 경매예정 찜하기 취소
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public int deleteZimPrice(Map<String, Object> params) throws Exception{
		return auctionDAO.deleteZimPrice(params);
	}

	/**
	 * 경매 출품 리스트 
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> selectAuctionEntry(Map<String, Object> params) throws Exception {
		return auctionDAO.selectAuctionEntry(params);
	}

	/**
	 * 출품된 소의 찜가격 조회
	 * @param params
	 * @return
	 * @throws Exception 
	 */
	@Override
	public Map<String, Object> selectMyFavoriteInfo(final Map<String, Object> params) throws Exception {
		return auctionDAO.selectMyFavoriteInfo(params);
	}
	
	/**
	 * App 버전 정보
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectAppVersionInfo(Map<String, Object> params) throws Exception{
		return auctionDAO.selectAppVersionInfo(params);
	}

	/**
	 * 통계 - 나의 구매현황
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> selectMyBuyList(Map<String, Object> params) throws Exception {
		return auctionDAO.selectMyBuyList(params);
	}

	/**
	 * 경매 가능 지점 리스트
	 * @param result
	 * @return
	 * @throws Exception 
	 */
	@Override
	public List<Map<String, Object>> selectAuctionBizList(Map<String, Object> params) throws Exception {
		return auctionDAO.selectAuctionBizList(params);
	}

	/**
	 * 현재 위치에서 가까운 지점 (2km이내)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> selectNearestBranchInfo(Map<String, Object> params) throws Exception {
		return auctionDAO.selectNearestBranchInfo(params);
	}

	/**
	 * 카카오 커넥트 라이브 서비스ID, Key 정보
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectKakaoServiceInfo(Map<String, Object> params) throws Exception {
		return auctionDAO.selectKakaoServiceInfo(params);
	}

	/**
	 * 부하테스트용 토큰 발급(삭제예정)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> selectTestUserList(Map<String, Object> params) throws Exception {
		return auctionDAO.selectTestUserList(params);
	}

	/**
	 * 나의 경매 참가 정보
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public List<Map<String, Object>> selectMyEntryList(Map<String, Object> params) throws Exception {
		return auctionDAO.selectMyEntryList(params);
	}
	@Override
	public Map<String, Object> sealectAuctQcn(Map<String, Object> params) throws Exception {
		return auctionDAO.sealectAuctQcn(params);
	}

	@Override
	public int sealectAuctCowCnt(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return auctionDAO.sealectAuctCowCnt(params);
	}

	@Override
	public List<Map<String, Object>> selectAuctCowList(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return auctionDAO.selectAuctCowList(params);
	}

	@Override
	public int updateLowSbidAmt(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return auctionDAO.updateLowSbidAmt(params);
	}

	@Override
	public int updateAuctCowSt(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return auctionDAO.updateAuctCowSt(params);
	}

	@Override
	public int updateAuctCowResult(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return auctionDAO.updateAuctCowResult(params);
	}

	@Override
	public int selectBidLogCnt(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return auctionDAO.selectBidLogCnt(params);
	}

	@Override
	public Map<String, Object> selectNextBidNum(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return auctionDAO.selectNextBidNum(params);
	}

	public int insertBidLog(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return auctionDAO.insertBidLog(params);
	}

	@Override
	public List<Map<String, Object>> selectFeeInfo(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return auctionDAO.selectFeeInfo(params);
	}

	@Override
	public int deleteFeeInfo(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return auctionDAO.deleteFeeInfo(params);
	}

	@Override
	public int insertFeeLog(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return auctionDAO.insertFeeLog(params);
	}

	@Override
	public Map<String, Object> selectAuctBidNum(Map<String, Object> params) throws Exception {
		// TODO Auto-generated method stub
		return auctionDAO.selectAuctBidNum(params);
	}
	
	@Override
	public Map<String, Object> selectAuctStn(Map<String, Object> params) throws Exception{
		return auctionDAO.selectAuctStn(params);
	}

	/**
	 * 출장우 상세 정보
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public Map<String, Object> selectCowInfo(Map<String, Object> params) throws Exception {
		return auctionDAO.selectCowInfo(params);
	}
	/**
	 * 출장우 정보 업데이트(중량, 계류대, 하한가)
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateCowInfo(Map<String, Object> params) throws Exception {
		return auctionDAO.updateCowInfo(params);
	}
}
