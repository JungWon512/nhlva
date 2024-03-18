package com.ishift.auction.service.auction;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import com.ishift.auction.util.AlarmTalkForm;
import com.ishift.auction.util.McaUtil;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.vo.FarmUserDetails;

@Service("auctionService")
@Transactional(transactionManager = "txManager", rollbackFor = Exception.class)
public class AuctionServiceImpl implements AuctionService {

	private static Logger log = LoggerFactory.getLogger(AuctionServiceImpl.class);
	
	@Resource(name = "auctionDAO")
	AuctionDAO auctionDAO;
	
	@Autowired
	private SessionUtill sessionUtill;
	
	@Autowired
	private AlarmTalkForm alarmTalkForm;
	
	@Autowired
	private McaUtil mcaUtil;
	
	@Value("${ncloud.storage.end-point}") private String endPoint;
	@Value("${ncloud.storage.bucket-name}") private String bucketName;
	
	@Override
	public List<Map<String,Object>> noticeSelectList(Map<String, Object> reqMap)throws SQLException {
		return auctionDAO.noticeSelectList(reqMap);
	}
	
	@Override
	public Map<String,Object> selectOneNotice(Map<String, Object> map) throws SQLException{
		return auctionDAO.selectOneNotice(map);
	}
	@Override
	public List<Map<String, Object>> entrySelectList(Map<String, Object> reqMap) throws SQLException{
		List<Map<String, Object>> result = null;
		
		String str = (String) reqMap.get("indvSexC");
		
		if (str != null && !"".equals(str) ) {
			String[] list = str.split(",");
			reqMap.put("indvSexC", list);
		}
		
		result = auctionDAO.entrySelectList(reqMap) ; 
		return result;
	}
	@Override
	public Map<String, Object> selectNearAucDate(Map<String, Object> reqMap) throws SQLException{
		return auctionDAO.selectNearAucDate(reqMap);
	}
	
	@Override
	public List<Map<String, Object>> selectAucDateQcn(Map<String, Object> map) throws SQLException{
		return auctionDAO.selectAucDateQcn(map);
	}
	
	@Override
	public List<Map<String, Object>> selectAucDateStn(Map<String, Object> map) throws SQLException{
		return auctionDAO.selectAucDateStn(map);
	}
	
	@Override
	public List<Map<String, Object>> selectAucDateList(Map<String, Object> map) throws SQLException{
		return auctionDAO.selectAucDateList(map);
	}
	@Override
	public List<Map<String, Object>> selectBizLocList(Map<String, Object> map) throws SQLException{
		return auctionDAO.selectBizLocList(map);
	}
	@Override
	public List<Map<String, Object>> selectBizList(Map<String, Object> map) throws SQLException{
		return auctionDAO.selectBizList(map);
	}
	@Override
	public List<Map<String, Object>> selectCalendarList(Map<String, Object> map) throws SQLException{
		return auctionDAO.selectCalendarList(map);
	}
	
	/**
	 * 경매예정 찜하기 추가/수정
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	public int insertUpdateZim(Map<String, Object> param) throws SQLException {
		return ObjectUtils.isEmpty(auctionDAO.selectZimPriceChk(param)) ? 0 : auctionDAO.insertUpdateZim(param); 
	}

	/**
	 * 경매 결과 업데이트
	 * @throws SQLException 
	 */
	@Override
	public int updateAuctionResult(Map<String, Object> params) throws SQLException {
		if ("v2".equals(params.get("version"))) {
			params.put("naBzplc", params.get("naBzPlc"));
			
			int cnt = auctionDAO.updateAuctionResult(params);
			params.put("chg_pgid", "API");
			params.put("chg_rmk_cntn", "API 경매결과 변경");
			auctionDAO.insertSogCowLog(params);
			if (cnt == 0) {
				return 0;
			}
			
			// 수수료 정보 저장 [s]
			// 1. 기등록 수수료 정보 삭제
			auctionDAO.deleteFeeInfo(params);
			
			// 2. 경매 정보 조회 ( 송아지, 번식우, 비육우 여부, 중도매인, 출하주의 조합 가입 여부, 자가 운송 여부 )
			final Map<String, Object> auctionInfo = auctionDAO.selectAuctionInfo(params);

			// 3. 낙찰인 경우 수수료 정보 저장
			if (auctionInfo != null) {
				// 4. 조합 경매 기본 정보 조회
				final Map<String, Object> bizAuctionInfo = auctionDAO.selectBizAuctionInfo(params);
				
				List<Map<String, Object>> feeInfoList = this.calcFeeInfo(params,auctionInfo,bizAuctionInfo);			
				params.put("feeInfoList", feeInfoList);
			
				auctionDAO.insertFeeInfo(params);
			}
			// 수수료 정보 저장 [e]

			return cnt;
		}
		else {
			int updateNum = auctionDAO.updateAuctionResult(params);
			params.put("chg_pgid", "API");
			params.put("chg_rmk_cntn", "API 경매결과 변경");
			auctionDAO.insertSogCowLog(params);
			return updateNum; 
		}
	}
	
	/**
	 * 경매관전 카운트 조회
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectCountEntry(Map<String, Object> map) throws SQLException{
		return auctionDAO.selectCountEntry(map);
	}
	
	/**
	 * 경매예정 찜하기 취소
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int deleteZimPrice(Map<String, Object> params) throws SQLException{
		return auctionDAO.deleteZimPrice(params);
	}

	/**
	 * 경매 출품 리스트 
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public List<Map<String, Object>> selectAuctionEntry(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectAuctionEntry(params);
	}

	/**
	 * 출품된 소의 찜가격 조회
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public Map<String, Object> selectMyFavoriteInfo(final Map<String, Object> params) throws SQLException {
		return auctionDAO.selectMyFavoriteInfo(params);
	}
	
	/**
	 * App 버전 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectAppVersionInfo(Map<String, Object> params) throws SQLException{
		return auctionDAO.selectAppVersionInfo(params);
	}

	/**
	 * 통계 - 나의 구매현황
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectMyBuyList(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectMyBuyList(params);
	}

	/**
	 * 경매 가능 지점 리스트
	 * @param result
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public List<Map<String, Object>> selectAuctionBizList(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectAuctionBizList(params);
	}

	/**
	 * 현재 위치에서 가까운 지점 (2km이내)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectNearestBranchInfo(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectNearestBranchInfo(params);
	}

	/**
	 * 카카오 커넥트 라이브 서비스ID, Key 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectKakaoServiceInfo(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectKakaoServiceInfo(params);
	}

	/**
	 * 부하테스트용 토큰 발급(삭제예정)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectTestUserList(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectTestUserList(params);
	}

	/**
	 * 나의 경매 참가 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectMyEntryList(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectMyEntryList(params);
	}
	@Override
	public Map<String, Object> sealectAuctQcn(Map<String, Object> params) throws SQLException {
		return auctionDAO.sealectAuctQcn(params);
	}

	@Override
	public int sealectAuctCowCnt(Map<String, Object> params) throws SQLException {
		return auctionDAO.sealectAuctCowCnt(params);
	}

	/**
	 * 출하우 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectAuctCowList(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectAuctCowList(params);
	}

	@Override
	public int updateLowSbidAmt(Map<String, Object> params) throws SQLException {
		return auctionDAO.updateLowSbidAmt(params);
	}

	@Override
	public int updateAuctCowSt(Map<String, Object> params) throws SQLException {
		return auctionDAO.updateAuctCowSt(params);
	}

	@Override
	public int updateAuctCowResult(Map<String, Object> params) throws SQLException {
		return auctionDAO.updateAuctCowResult(params);
	}

	@Override
	public int selectBidLogCnt(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectBidLogCnt(params);
	}

	@Override
	public Map<String, Object> selectNextBidNum(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectNextBidNum(params);
	}

	public int insertBidLog(Map<String, Object> params) throws SQLException {
		return auctionDAO.insertBidLog(params);
	}

	/**
	 * 수수료 정보 조회
	 */
	@Override
	public List<Map<String, Object>> selectFeeInfo(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectFeeInfo(params);
	}

	/**
	 * 수수료 정보 삭제
	 */
	@Override
	public int deleteFeeInfo(Map<String, Object> params) throws SQLException {
		return auctionDAO.deleteFeeInfo(params);
	}

	/**
	 * 수수료 정보 저장
	 */
	@Override
	public int insertFeeLog(Map<String, Object> params) throws SQLException {
		return auctionDAO.insertFeeLog(params);
	}

	@Override
	public Map<String, Object> selectAuctBidNum(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectAuctBidNum(params);
	}
	
	@Override
	public List<Map<String, Object>> selectAucStnList(Map<String, Object> params) throws SQLException{
		return auctionDAO.selectAucStnList(params);
	}
	
	/**
	 * 경매 구간 정보(전체)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectAuctStn(Map<String, Object> params) throws SQLException{
		return auctionDAO.selectAuctStn(params);
	}

	/**
	 * 출장우 상세 정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectCowInfo(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectCowInfo(params);
	}

	/**
	 * 출장우 정보 업데이트(중량, 계류대, 하한가)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int updateCowInfo(Map<String, Object> params) throws SQLException {
		final int cnt = auctionDAO.updateCowInfo(params);
		
		if (cnt == 0) {
			return cnt;
		}
		else {
			// 업데이트 유형이 중량 등록(W), 중량 일괄 등록(AW), 중량 일괄 등록(AWL)이고 비육우인 경우 낙찰 금액을 다시 계산해준다.
			if (("W".equals(params.get("regType")) || "AW".equals(params.get("regType")) || "AWL".equals(params.get("regType")) || "LW".equals(params.get("regType")))
			  && "2".equals(params.get("aucObjDsc")) 
			  || "S".equals(params.get("regType"))) {

				final Map<String, Object> cowInfo = this.selectCowInfo(params);

				// 비육우 경매단위가 KG별이고 낙찰 상태인 경우
				if ("22".equals(cowInfo.get("SEL_STS_DSC")) || "S".equals(params.get("regType"))) {
					params.put("naBzPlc", params.get("naBzplc"));
					params.put("selStsDsc", cowInfo.get("SEL_STS_DSC"));
					params.put("sraSbidUpr", cowInfo.get("SRA_SBID_UPR"));
					params.put("trmnAmnno", cowInfo.get("TRMN_AMNNO"));
					params.put("lvstAucPtcMnNo", cowInfo.get("LVST_AUC_PTC_MN_NO"));
					
					final Map<String, Object> returnMap = this.updateAuctionResultMap(params);
					if (returnMap != null && !(boolean)returnMap.getOrDefault("success", false)) {
						throw new SQLException("출장우 정보 업데이트에 실패했습니다.");
					}
				}
			}else {
				params.put("naBzPlc", params.get("naBzplc"));
				params.put("chg_pgid", "OFFICE");
				params.put("chg_rmk_cntn", "출장우정보변경");
				auctionDAO.insertSogCowLog(params);
			}
			return cnt;
		}
	}

	/**
	 * 응찰자 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectBidEntryList(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectBidEntryList(params);
	}
		
	@Override
	public int insertAuctStnLog(Map<String, Object> params) throws SQLException{
		return auctionDAO.insertAuctStnLog(params);
	}
	@Override
	public int updateAuctStn(Map<String, Object> temp) throws SQLException{
		return auctionDAO.updateAuctStn(temp);
	}

	@Override
	public int updateAuctSogCow(Map<String, Object> temp) throws SQLException{
		return auctionDAO.updateAuctSogCow(temp);
	}
	@Override
	public int updateAuctSogCowFinish(Map<String, Object> temp) throws SQLException{
		return auctionDAO.updateAuctSogCowFinish(temp);
	}
	@Override
	public Map<String, Object> selectMaxDdlQcn(Map<String, Object> params) throws SQLException{
		return auctionDAO.selectMaxDdlQcn(params);
	}
	
	@Override
	public int insertAuctSogCowLog(Map<String, Object> temp) throws SQLException{
		return auctionDAO.insertAuctSogCowLog(temp);
	}

	/**
	 * 경매 결과 업데이트 - 실패시 실패 정보 return
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> updateAuctionResultMap(final Map<String, Object> params) throws SQLException {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		params.put("naBzplc", params.get("naBzPlc"));
		
		// 1. 필수 인자 체크
		if ((params.get("naBzPlc") == null && params.get("naBzplc") == null)
		 || params.get("aucObjDsc") == null
		 || params.get("aucDt") == null
		 || params.get("oslpNo") == null
		 || params.get("ledSqno") == null
		 || params.get("selStsDsc") == null
		 || params.get("sraSbidUpr") == null
		) {
			// 실패한 정보를 다시 return
			params.put("message", "필수 인자가 없습니다.");
			return params;
		}

		// 2. 조합 경매 기본 정보 조회
		final Map<String, Object> bizAuctionInfo = auctionDAO.selectBizAuctionInfo(params);
		
		// 3. 경매 정보 조회 ( 송아지, 번식우, 비육우 여부, 중도매인, 출하주의 조합 가입 여부, 자가 운송 여부 )
		final Map<String, Object> info = auctionDAO.selectAuctionInfo(params);
		if (info == null) {
			// 조회한 정보가 없는 경우 return
			params.put("message", "출하우 정보가 없습니다.");
			return params;
		}
		
		if (bizAuctionInfo == null) {
			// 조회한 정보가 없는 경우 return
			params.put("message", "경매 기본 정보가 없습니다.");
			return params;
		}

		final String aucDt			= info.get("AUC_DT").toString();
		final String aucObjDsc		= info.get("AUC_OBJ_DSC").toString();
		final String oslpNo			= info.get("OSLP_NO").toString();
		final String ledSqno		= info.get("LED_SQNO").toString();
		final String aucUprDsc		= bizAuctionInfo.getOrDefault("NBFCT_AUC_UPR_DSC", "1").toString();				// 비육우 경매단가 구분 코드 ( 1. kg 단위, 2. 두 단위 )
		final String sgNoPrcDsc		= bizAuctionInfo.getOrDefault("SGNO_PRC_DSC", "1").toString();					// 절사구분 ( 1.소수점 이하 버림, 2. 소수점 이상 절상, 3. 반올림 )
		final int cutAm				= Integer.parseInt(bizAuctionInfo.getOrDefault("CUT_AM", "1").toString());		// 절사금액
		String cowSogWt				= (info.get("COW_SOG_WT") == null || "".equals(info.get("COW_SOG_WT"))) ? "0" : info.get("COW_SOG_WT").toString();
		long sraSbidUpr				= Long.parseLong(params.get("sraSbidUpr").toString());							// 응찰금액
		long sraSbidAm				= 0L;
		
		// 4. 낙찰 금액 계산 [s] > 상태가 22인 경우만 낙찰 금액을 새로 계산해준다.
		if ("22".equals(params.get("selStsDsc"))) {
			// 경매 금액 단위 > 보통 10000원이지만 비육우인 경우 kg단위로 계산한다.
			int aucAtdrUntAm			= 10000;
			// 비육우 & 경매 단가가 kg인 경우
			if ("2".equals(aucObjDsc) && "1".equals(aucUprDsc)) {
				// 중량 정보가 없으면 낙찰가를 0으로 넣어준다.
				if ("0".equals(cowSogWt)) {
					params.put("sraSbidAm", 0);
				}
				else {
					aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("NBFCT_AUC_ATDR_UNT_AM", "1").toString());
					double bidAmt = Double.parseDouble(cowSogWt) * sraSbidUpr * aucAtdrUntAm/ cutAm;
//					double bidAmt = Double.parseDouble(cowSogWt) * sraSbidUpr / cutAm;
					if ("1".equals(sgNoPrcDsc)) {
						sraSbidAm = (long)Math.floor(bidAmt) * cutAm;
					}
					else if ("2".equals(sgNoPrcDsc)) {
						sraSbidAm = (long)Math.ceil(bidAmt) * cutAm;
					}
					else if ("3".equals(sgNoPrcDsc)) {
						sraSbidAm = (long)Math.round(bidAmt) * cutAm;
					}
					params.put("sraSbidAm", sraSbidAm);
				}
			}
			else {
				if ("1".equals(aucObjDsc)) {
					aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("CALF_AUC_ATDR_UNT_AM", "10000").toString());	// 송아지 경매 금액 단위
				}
				else if ("2".equals(aucObjDsc)) {
					aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("NBFCT_AUC_ATDR_UNT_AM", "10000").toString());	// 비육우 경매 금액 단위
				}
				else if ("3".equals(aucObjDsc)) {
					aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("PPGCOW_AUC_ATDR_UNT_AM", "10000").toString());	// 번식우 경매 금액 단위
				}
				else if ("5".equals(aucObjDsc)) {					
					aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("GT_AUC_ATDR_UNT_AM", "10000").toString());	// 염소 경매 금액 단위
				}
				else if ("6".equals(aucObjDsc)) {
					aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("HS_AUC_ATDR_UNT_AM", "10000").toString());	// 말 경매 금액 단위
				}
				sraSbidAm = sraSbidUpr * aucAtdrUntAm;
				params.put("sraSbidAm", sraSbidAm);
			}
		}
		// 낙찰 금액 계산 [e]
		
		// 5. 경매결과 업데이트
		int cnt = auctionDAO.updateAuctionResult(params);
		params.put("chg_pgid", params.getOrDefault("chg_pgid", "API"));
		params.put("chg_rmk_cntn", params.getOrDefault("chg_rmk_cntn", "API 경매결과 변경"));
		auctionDAO.insertSogCowLog(params);
		if (cnt == 0) {
			// 실패한 정보를 다시 return
			params.put("message", "출하우 정보가 없습니다.");
			return params;
		}

		// 수수료 정보 저장 [s]
		// 6. 기등록 수수료 정보 삭제
		auctionDAO.deleteFeeInfo(params);
		
		// 7. 경매 정보 조회 ( 송아지, 번식우, 비육우 여부, 중도매인, 출하주의 조합 가입 여부, 자가 운송 여부 )
		final Map<String, Object> auctionInfo = auctionDAO.selectAuctionInfo(params);
		
		// 8. 낙찰인 경우 수수료 정보 저장
		if (auctionInfo != null) {
			
			//params.put("feeInfoList", this.calcFeeInfo(info,params,bizAuctionInfo));
			//낙찰가 경매정보에 저장하여 수수료계산하게 수정
			auctionInfo.put("sraSbidAm", params.get("sraSbidAm"));
			List<Map<String, Object>> feeInfoList = this.calcFeeInfo(params,auctionInfo,bizAuctionInfo);			
			params.put("feeInfoList", feeInfoList);
			if(feeInfoList.size() >0) auctionDAO.insertFeeInfo(params);
			
			// TODO :: 경매 낙찰시 SMS 발송하도록 설정 한 경우 출하자에게 낙찰가 알림톡 전송
			this.sendAlamTalkProc(bizAuctionInfo, auctionInfo);
		}
		// 수수료 정보 저장 [e]
		
		result.put("success", true);
		result.put("message", "경매내역 변경에 성공했습니다.");
		
		return result;
	}

	@Override
	public Map<String, Object> updateEtcAuctionResultMap(final Map<String, Object> params) throws SQLException {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		params.put("naBzplc", params.get("naBzPlc"));
		
		// 1. 필수 인자 체크
		if ((params.get("naBzPlc") == null && params.get("naBzplc") == null)
		 || params.get("aucObjDsc") == null
		 || params.get("aucDt") == null
		 || params.get("oslpNo") == null
		 || params.get("ledSqno") == null
		 || params.get("selStsDsc") == null
		 || params.get("sraSbidUpr") == null
		) {
			// 실패한 정보를 다시 return
			params.put("message", "필수 인자가 없습니다.");
			return params;
		}

		// 2. 조합 경매 기본 정보 조회
		final Map<String, Object> bizAuctionInfo = auctionDAO.selectBizAuctionInfo(params);
		
		// 3. 경매 정보 조회 ( 송아지, 번식우, 비육우 여부, 중도매인, 출하주의 조합 가입 여부, 자가 운송 여부 )
		final Map<String, Object> info = auctionDAO.selectAuctionInfo(params);
		if (info == null) {
			// 조회한 정보가 없는 경우 return
			params.put("message", "출하우 정보가 없습니다.");
			return params;
		}
		
		if (bizAuctionInfo == null) {
			// 조회한 정보가 없는 경우 return
			params.put("message", "경매 기본 정보가 없습니다.");
			return params;
		}

		final String aucUprDsc		= info.getOrDefault("AUC_UPR_DSC", "2").toString();				// 경매단가 구분 코드 ( 1. kg 단위, 2. 두 단위 )
		final String sgnoPrcDsc		= info.getOrDefault("SGNO_PRC_DSC", "1").toString();				// 절사구분 ( 1.소수점 이하 버림, 2. 소수점 이상 절상, 3. 반올림 )
		final int cutAm				= Integer.parseInt(info.getOrDefault("CUT_AM", "1").toString());	// 절사금액
		final String cowSogWt		= (info.get("COW_SOG_WT") == null || "".equals(info.get("COW_SOG_WT"))) ? "0" : info.get("COW_SOG_WT").toString();
		final long sraSbidUpr		= Long.parseLong(params.get("sraSbidUpr").toString());							// 응찰금액
		final int aucAtdrUntAm		= Integer.parseInt(info.getOrDefault("AUC_ATDR_UNT_AM", "1").toString());	
		long sraSbidAm				= 0L;
		
		// 4. 낙찰 금액 계산 [s] > 상태가 22인 경우만 낙찰 금액을 새로 계산해준다.
		if ("22".equals(params.get("selStsDsc"))) {
			// 2024-03-18 : ycsong
			// 사업장 정보에 설정한 경매 단가 기준으로 낙찰가를 계산한다. ( 1: KG, 2: 두 )
			if ("1".equals(aucUprDsc)) {
				// 중량 정보가 없으면 낙찰가를 0으로 넣어준다.
				if ("0".equals(cowSogWt)) {
					params.put("sraSbidAm", 0);
				}
				else {
					double bidAmt = Double.parseDouble(cowSogWt) * sraSbidUpr * aucAtdrUntAm/ cutAm;
					if ("1".equals(sgnoPrcDsc)) {
						sraSbidAm = (long)Math.floor(bidAmt) * cutAm;
					}
					else if ("2".equals(sgnoPrcDsc)) {
						sraSbidAm = (long)Math.ceil(bidAmt) * cutAm;
					}
					else if ("3".equals(sgnoPrcDsc)) {
						sraSbidAm = (long)Math.round(bidAmt) * cutAm;
					}
					params.put("sraSbidAm", sraSbidAm);
				}
			}
			else {
				sraSbidAm = sraSbidUpr * aucAtdrUntAm;
				params.put("sraSbidAm", sraSbidAm);
			}
		}
		// 낙찰 금액 계산 [e]
		
		// 5. 경매결과 업데이트
		int cnt = auctionDAO.updateAuctionResult(params);
		params.put("chg_pgid", params.getOrDefault("chg_pgid", "API"));
		params.put("chg_rmk_cntn", params.getOrDefault("chg_rmk_cntn", "API 경매결과 변경"));
		auctionDAO.insertSogCowLog(params);
		if (cnt == 0) {
			// 실패한 정보를 다시 return
			params.put("message", "출하우 정보가 없습니다.");
			return params;
		}

		// 수수료 정보 저장 [s]
		// 6. 기등록 수수료 정보 삭제
		auctionDAO.deleteFeeInfo(params);
		
		// 7. 경매 정보 조회 ( 송아지, 번식우, 비육우 여부, 중도매인, 출하주의 조합 가입 여부, 자가 운송 여부 )
		final Map<String, Object> auctionInfo = auctionDAO.selectAuctionInfo(params);
		
		// 8. 낙찰인 경우 수수료 정보 저장
		if (auctionInfo != null) {
			//params.put("feeInfoList", this.calcFeeInfo(info,params,bizAuctionInfo));
			//낙찰가 경매정보에 저장하여 수수료계산하게 수정
			auctionInfo.put("sraSbidAm", params.get("sraSbidAm"));
			List<Map<String, Object>> feeInfoList = this.calcFeeInfo(params,auctionInfo,bizAuctionInfo);			
			params.put("feeInfoList", feeInfoList);
			if(feeInfoList.size() >0) auctionDAO.insertFeeInfo(params);
			
			// TODO :: 경매 낙찰시 SMS 발송하도록 설정 한 경우 출하자에게 낙찰가 알림톡 전송
			this.sendAlamTalkProc(bizAuctionInfo, auctionInfo);
		}
		// 수수료 정보 저장 [e]
		
		result.put("success", true);
		result.put("message", "경매내역 변경에 성공했습니다.");
		
		return result;
	}
	
	/**
	 * 낙찰시 알림톡 발송
	 * 대상 : 출하주
	 * @param bizInfo
	 * @param auctionInfo
	 */
	@SuppressWarnings("unchecked")
	public boolean sendAlamTalkProc(final Map<String, Object> bizInfo, final Map<String, Object> auctionInfo) {
		try {
			if(!"1".equals(bizInfo.getOrDefault("SBID_SMS_YN", "0")) || !"22".equals(auctionInfo.getOrDefault("SEL_STS_DSC", ""))) return true;
			
			Map<String, Object> map = new HashMap<String, Object>();	
			Map<String, Object> msgMap = new HashMap<String, Object>();	
			Map<String, Object> mcaMap = null;
			// 알림톡 전문키 생성 (YYMMDD + 연번4자리)
			Map<String, Object> tempMap = auctionDAO.selelctMca5100AlarmTalkId(map);
			msgMap.put("IO_TGRM_KEY", tempMap.get("IO_TGRM_KEY"));	// IO_TGRM_KEY (SEQ - 전문키 YYMMDD + 연번4자리)

			// 알림톡 내용 조회
			final String templateId = "NHKKO00252";
			map.put("code", templateId);
			tempMap = auctionDAO.selectTemplateInfo(map);
			
			final Map<String, Object> msgCntnInfo = auctionDAO.selectMsgCntnInfo(auctionInfo);

			msgCntnInfo.put("MSG", " 낙찰내역을 ");
			msgCntnInfo.put("REVE_USR_NM", auctionInfo.getOrDefault("FTSNM", "").toString().trim());
			msgCntnInfo.put("COW_INFO1", "경매번호 : " + msgCntnInfo.get("AUC_PRG_SQ")+"번");
			msgCntnInfo.put("COW_INFO2", "등록구분 : " + msgCntnInfo.get("AUC_OBJ_DSCNM")+"("+msgCntnInfo.get("INDV_SEX_CNM")+")");
			msgCntnInfo.put("COW_INFO3", "개체번호 : " + msgCntnInfo.get("SRA_INDV_AMNNO_FORMAT"));
			msgCntnInfo.put("COW_INFO4", "낙찰금액 : " + msgCntnInfo.get("SRA_SBID_AM_FORMAT")+"원");
			
			// SMS 로그테이블(TB_LA_IS_MM_SMS) 에 필요한 파라미터
			msgMap.put("MSG_CNTN", alarmTalkForm.makeAlarmMsgCntn(msgCntnInfo, tempMap.get("TALK_CONTENT").toString()));
			msgMap.put("AUC_OBJ_DSC", auctionInfo.get("AUC_OBJ_DSC"));		// 경매대상구분코드
			msgMap.put("TRMN_AMNNO", auctionInfo.get("FHS_ID_NO"));			// 중도매인 코드 or 출하주 코드
			msgMap.put("DPAMN_DSC", "2");									// 출하자, 응찰자 구분 : 새로운 코드
			msgMap.put("SEL_STS_DSC", "02");								// 판매상태구분코드 : 새로운 코드
			msgMap.put("RMS_MN_NAME", auctionInfo.get("FTSNM"));			// 수신자명 (중도매인명 or 출하주명)
			msgMap.put("IO_TRMSNM", bizInfo.get("NA_BZPLNM"));				// 발신자명 (조합명)
			msgMap.put("SMS_RMS_MPNO", auctionInfo.get("CUS_MPNO"));		// 수신자 전화번호	
			msgMap.put("SMS_TRMS_TELNO", bizInfo.get("TELNO"));				// 발신자 전화번호
			msgMap.put("SS_USERID", "BATCH");

			// 인터페이스에 필요한 파라미터
			msgMap.put("KAKAO_MSG_CNTN", alarmTalkForm.getAlarmTalkTemplateToJson(templateId, msgMap));
			msgMap.put("KAKAO_TPL_C", templateId);								// 템플릿 코드
			msgMap.put("ADJ_BRC", bizInfo.get("ADJ_BRC"));						// 사무소 코드 
			msgMap.put("RLNO", sessionUtill.getEno());							// RLNO (사용자 사번)
			msgMap.put("IO_TIT","");											// 제목 : 미사용이라 space로 채움
			msgMap.put("IO_DPAMN_MED_ADR", auctionInfo.get("CUS_MPNO"));		// 수신 휴대폰번호
			msgMap.put("IO_SDMN_MED_ADR", bizInfo.get("TELNO"));				// 발신 조합전화번호

			// fail-back 필요 파라메터(알람톡 실패시 문자 전송)
			msgMap.put("FBK_UYN", "Y");			//fail-back 사용여부
			msgMap.put("FBK_TIT","");
			msgMap.put("FBK_MSG_DSC","7");		//3:SMS, 7:LMS
			msgMap.put("UMS_FWDG_CNTN", msgMap.getOrDefault("MSG_CNTN","").toString());	// UMS_FWDG_CNTN fail-back 메세지 내용
			msgMap.put("IO_ATGR_ITN_TGRM_LEN", msgMap.getOrDefault("UMS_FWDG_CNTN","").toString().getBytes().length);	// IO_ATGR_ITN_TGRM_LEN (UMS_FWDG_CNTN의 바이트 수)
			
			mcaMap = mcaUtil.tradeMcaMsg("5100", msgMap);

			final Map<String, Object> dataMap = (Map<String, Object>) mcaMap.get("jsonData");	
			if (!ObjectUtils.isEmpty(dataMap)) {
				//전송여부
				msgMap = this.changeKeyUpper(msgMap);
				msgMap.put("TMS_YN", dataMap.get("RZTC"));
				msgMap.put("TMS_TYPE", "02");
				msgMap.put("NA_BZPLC", bizInfo.get("NA_BZPLC"));				// 조합코드
				msgMap.put("LS_CMENO", sessionUtill.getEno());					// 사용자 사번
				
				auctionDAO.insertSmsInfo(msgMap);
			}
		}
		catch(RuntimeException | SQLException se) {
			log.error("AuctionServiceImpl.sendAlamTalkProc : {} ", se);
			return false;
		}
		catch(Exception e) {
			log.error("AuctionServiceImpl.sendAlamTalkProc : {} ", e);
			return false;
		}
		return true;
	}

	public Map<String, Object> changeKeyUpper(Map<String, Object> map){
		final Map<String, Object> reMap = new HashMap<String, Object>();
		//키 대문자로 변환
		final Set<String> set = map.keySet();
		final Iterator<String> e = set.iterator();
		while(e.hasNext()){
			String key = e.next();
			Object value = (Object) map.get(key);
			reMap.put(key.toUpperCase(), value);
		}
		return reMap;
	}

	/**
	 * 일괄 경매 시작
	 * @param aucStn
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> auctionStart(final Map<String, Object> aucStn, final Map<String, Object> params) throws SQLException {
		
		// 1. 일괄 경매 변경 로그 저장
		params.put("selStsDsc", "22");
		params.put("lsCmeno", "0314시작");
		params.put("fsrgmnEno", "admin");
		auctionDAO.insertAuctStnLog(params);

		// 2. 일괄 경매 상태 변경
		params.put("selStsDsc", "21");
		params.put("lsCmeno", "SYSTEM");
		auctionDAO.updateAuctStn(params);

		// 3. 출장우 경매 상태 변경 (해당 차수 경매 번호 범위의 출장우만 업데이트) 
		params.put("selStsDsc", "22");
		params.put("aucYn", "1");
		params.put("stAucNo", aucStn.get("ST_AUC_NO"));
		params.put("edAucNo", aucStn.get("ED_AUC_NO"));
		auctionDAO.updateAuctSogCow(params);
		
		params.put("success", true);
		
		return params;
	}

	/**
	 * 일괄 경매 중지
	 * @param aucStn
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> auctionPause(Map<String, Object> aucStn, Map<String, Object> params) throws SQLException {
		
		// 1. 일괄 경매 상태 변경
		params.put("selStsDsc", "23");
		params.put("lsCmeno", "SYSTEM");
		auctionDAO.updateAuctStn(params);

		// 2. 출장우 경매 상태 변경 (해당 차수 경매 번호 범위의 출장우만 업데이트)
		params.put("aucYn", "0");
		params.put("stAucNo", aucStn.get("ST_AUC_NO"));
		params.put("edAucNo", aucStn.get("ED_AUC_NO"));
		auctionDAO.updateAuctSogCow(params);
		
		params.put("success", true);
		
		return params;
	}

	/**
	 * 일괄 경매 종료
	 * @param aucStn
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> auctionFinish(Map<String, Object> aucStn, Map<String, Object> params) throws SQLException {
		
		final Map<String, Object> maxDdlQcn = auctionDAO.selectMaxDdlQcn(params);
		
		// 0. 구간 경매 시작, 끝 번호 
		params.put("stAucNo", aucStn.get("ST_AUC_NO"));
		params.put("edAucNo", aucStn.get("ED_AUC_NO"));
		
		// 1. 일괄 경매 상태 변경 
		params.put("selStsDsc", "22");
		params.put("maxDdlQcn", maxDdlQcn.get("MAX_DDL_QCN")); 
		params.put("lsCmeno", "SYSTEM");
		auctionDAO.updateAuctStn(params);

		// 2. 종료 로그 저장
		params.put("lsCmeno", "0314종료");
		params.put("fsrgmnEno", "admin");
		auctionDAO.insertAuctStnLog(params);
		
		// 3. 출장우 경매 상태 변경 (해당 차수 경매 번호 범위의 출장우만 업데이트)
		params.put("aucYn", "0");
		params.put("lsCmeno", "SYSTEM");
		auctionDAO.updateAuctSogCow(params);

		// 4. 조합 경매 기본 정보 조회
		final Map<String, Object> bizAuctionInfo = auctionDAO.selectBizAuctionInfo(params);
		if (bizAuctionInfo == null) {
			// 조회한 정보가 없는 경우 return
			throw new SQLException("조합 경매 기본 정보가 없습니다."); 
		}
		
		// 5. 응찰 완료(낙찰 처리 대상) 리스트 조회
		final List<Map<String, Object>> list = auctionDAO.selectBidCompleteList(params);
		if (list.size() > 0) {
			
			for (Map<String, Object> info : list) {
				final String naBzplc		= info.get("NA_BZPLC").toString();
				final String aucDt			= info.get("AUC_DT").toString();
				final String aucObjDsc		= info.get("AUC_OBJ_DSC").toString();
				final String oslpNo			= info.get("OSLP_NO").toString();
				final String ledSqno		= info.get("LED_SQNO").toString();
				final String trmnMacoYn		= info.get("TRMN_MACO_YN").toString();				// 중도매인 조합원 여부 ( 0.비조합원, 1.조합원 )
				final String fhsMacoYn		= info.get("FHS_MACO_YN").toString();				// 출하주 조합원 여부 ( 0.비조합원, 1.조합원 )
				final String ppgcowFeeDsc	= info.getOrDefault("PPGCOW_FEE_DSC", "5").toString();			// 번식우 수수료 구분코드 > 1.임신우, 2.비임신우, 3.임신우+송아지, 4.비임신우+송아지,  5.해당없음
				final String trmnAmnno		= info.getOrDefault("TRMN_AMNNO", "0").toString();				// 중도매인 번호
				final String lvstAucPtcMnNo	= info.getOrDefault("LVST_AUC_PTC_MN_NO", "0").toString();		// 경매참가번호
				final String cowSogWt		= info.getOrDefault("COW_SOG_WT", "0").toString();
				final String aucUprDsc		= bizAuctionInfo.getOrDefault("NBFCT_AUC_UPR_DSC", "1").toString();			// 비육우 경매단가 구분 코드 ( 1. kg 단위, 2. 두 단위 )
				final String sgNoPrcDsc		= bizAuctionInfo.getOrDefault("SGNO_PRC_DSC", "1").toString();				// 절사구분 ( 1.소수점 이하 버림, 2. 소수점 이상 절상, 3. 반올림 )
				final String selStsDsc		= info.getOrDefault("SEL_STS_DSC", "23").toString();						// 경매상태 
				final int cutAm				= Integer.parseInt(bizAuctionInfo.getOrDefault("CUT_AM", "1").toString());	// 절사금액
				long sraSbidUpr				= Long.parseLong(info.get("ATDR_AM").toString());							// 응찰금액
				long sraSbidAm				= 0L;

				info.put("naBzplc",			naBzplc);
				info.put("naBzPlc",			naBzplc);
				info.put("aucDt",			aucDt);
				info.put("aucObjDsc",		aucObjDsc);
				info.put("oslpNo",			oslpNo);
				info.put("ledSqno",			ledSqno);
				info.put("trmnAmnno",		trmnAmnno);
				info.put("lvstAucPtcMnNo",	lvstAucPtcMnNo);
				info.put("selStsDsc",		selStsDsc);
				info.put("lsCmeno",			sessionUtill.getEno());
				info.put("sraSbidUpr",		sraSbidUpr);

				// 낙찰 금액 계산 [s]
				// 경매 금액 단위 > 보통 10000원이지만 비육우인 경우 kg단위로 계산한다.
				int aucAtdrUntAm			= 10000;
				if ("22".equals(selStsDsc)) {
					// 비육우 & 경매 단가가 kg인 경우
					if ("2".equals(aucObjDsc) && "1".equals(aucUprDsc)) {
						// 중량 정보가 없으면 낙찰가를 0으로 넣어준다.
						if ("0".equals(cowSogWt)) {
							info.put("sraSbidAm", 0);
						}
						else {
							aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("NBFCT_AUC_ATDR_UNT_AM", "1").toString());
							double bidAmt = Double.parseDouble(cowSogWt) * sraSbidUpr * aucAtdrUntAm / cutAm;
							if ("1".equals(sgNoPrcDsc)) {
								sraSbidAm = (long)Math.floor(bidAmt) * cutAm;
							}
							else if ("2".equals(sgNoPrcDsc)) {
								sraSbidAm = (long)Math.ceil(bidAmt) * cutAm;
							}
							else if ("3".equals(sgNoPrcDsc)) {
								sraSbidAm = (long)Math.round(bidAmt) * cutAm;
							}
							info.put("sraSbidAm", sraSbidAm);
						}
					}
					else {
						if ("1".equals(aucObjDsc)) {
							aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("CALF_AUC_ATDR_UNT_AM", "10000").toString());	// 송아지 경매 금액 단위
						}
						else if ("2".equals(aucObjDsc)) {
							aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("NBFCT_AUC_ATDR_UNT_AM", "10000").toString());	// 비육우 경매 금액 단위
						}
						else if ("3".equals(aucObjDsc)) {
							aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("PPGCOW_AUC_ATDR_UNT_AM", "10000").toString());	// 번식우 경매 금액 단위
						}
						else if ("5".equals(aucObjDsc)) {
							aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("GT_AUC_ATDR_UNT_AM", "10000").toString());	// 번식우 경매 금액 단위
						}
						else if ("6".equals(aucObjDsc)) {
							aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("HS_AUC_ATDR_UNT_AM", "10000").toString());	// 번식우 경매 금액 단위
						}
						sraSbidAm = sraSbidUpr * aucAtdrUntAm;
						info.put("sraSbidAm", sraSbidAm);
					}
				}
				else {
					info.put("sraSbidAm", 0);
				}
				// 낙찰 금액 계산 [e]

				// 6. 낙찰 정보 업데이트
				info.put("maxDdlQcn", maxDdlQcn.get("MAX_DDL_QCN"));
				int cnt = auctionDAO.updateAuctionResult(info);
				if (cnt == 0) {
					continue;
				}
				
				// 7. 기등록 수수료 정보 삭제
				auctionDAO.deleteFeeInfo(info);
				
				//수수료 계산로직
				List<Map<String, Object>> feeList = this.calcFeeInfo(info,info,bizAuctionInfo);
				if (feeList.size() > 0) {
					info.put("feeInfoList", feeList);
					auctionDAO.insertFeeInfo(info);
				}
				// params.put("feeInfoList", this.calcFeeInfo(params,info,bizAuctionInfo));
				// auctionDAO.insertFeeInfo(params);
				
				this.sendAlamTalkProc(bizAuctionInfo, info);
			}
		}
		
		// 10. 낙찰 상태가 아닌 출장우 로그 저장
		params.put("chgPgid", "auctionFinish");
		params.put("newCntAucYn", "Y");
		params.put("soldChkYn", "N");
		params.put("fsrgmnEno", "admin");
		params.put("lsCmeno", "[LM0314]");
		params.put("pdaId", "새 차수 경매 시작[성공]");
		params.put("chgRmkCntn", "새 차수 경매 시작[성공]");
		auctionDAO.insertAuctSogCowLog(params);

		// 11. 낙찰 상태 출장우 로그 저장
		params.put("newCntAucYn", "N");
		params.put("soldChkYn", "Y");
		params.put("pdaId", "경매종료[낙찰]");
		params.put("chgRmkCntn", "경매종료[낙찰]");
		auctionDAO.insertAuctSogCowLog(params);

		// 12. 유찰 처리 ( 최저가가 등록되지 않은 출장우 중 SEL_STS_DSC가 '22'가 아닌 것 )
		params.put("sraSbidAm", "0");
		params.put("lsCmeno", "SYSTEM");
		auctionDAO.updateAuctSogCowFinish(params);
		
		// 14. 유찰 상태 출장우 로그 저장
		params.put("soldChkYn", "N");
		params.put("pdaId", "경매종료[불낙]");
		params.put("chgRmkCntn", "경매종료[불낙]");
		auctionDAO.insertAuctSogCowLog(params);
		
		params.put("success", true);
		
		return params;
	}
	
	@Override
	public List<Map<String, Object>> selectAuctQcnForToday() throws SQLException{
		return auctionDAO.selectAuctQcnForToday();
	}
	@Override
	public int updateNetPort(Map<String, Object> params) throws SQLException{
		return auctionDAO.updateNetPort(params);
	}
	@Override
	public Map<String, Object> selectNearAtdrAm(Map<String, Object> params) throws SQLException{
		return auctionDAO.selectNearAtdrAm(params);
	}
	@Override
	public Map<String, Object> selectMyZimPrice(Map<String, Object> params) throws SQLException{
		return auctionDAO.selectMyZimPrice(params);
	}
	@Override
	public List<Map<String, Object>> selectBidLogList(Map<String, Object> params) throws SQLException{
		return auctionDAO.selectBidLogList(params);
	}
	@Override
	public Map<String, Object> selectBidLogListCnt(Map<String, Object> params)  throws SQLException{
		return auctionDAO.selectBidLogListCnt(params);
	}

	/**
	 * 공통 코드 리스트
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectCodeList(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectCodeList(params);
	}
	/**
	 * 나의 구매내역 > 총 구매금액 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectTotSoldPrice(Map<String, Object> map) throws SQLException{
		return auctionDAO.selectTotSoldPrice(map);
	}
	
	/**
	 * 출장우 정보 > APP에서 일괄 경매시 사용
	 * 찜가격과 응찰 가격 모두 조회
	 * 응찰가격이 있는 경우 1순위, 찜가격이 있는 경우 2순위, 나머지 경우 3순위로 정렬
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectCowList(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectCowList(params);
	}
	@Override
	public int updateNoticeReadCnt(Map<String, Object> params) throws SQLException{
		return auctionDAO.updateNoticeReadCnt(params);
	}

	/**
	 * 안드로이드, 아이폰 버전 정보 업데이트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int updateAppVersion(Map<String, Object> params) throws SQLException {
		return auctionDAO.updateAppVersion(params);
	}

	/**
	 * 계류대번호 업데이트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int updateCowInfoForModlNo(Map<String, Object> params) throws SQLException{
		return auctionDAO.updateCowInfoForModlNo(params);
	}
	
	/**
	 * 경매구간정보
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> getStnInfo(Map<String, Object> params) throws SQLException{
		return auctionDAO.getStnInfo(params);
	}

	/**
	 * 수의사 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectVetList(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectVetList(params);
	}

	/***
	 * 
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectAuctMwmn(Map<String, Object> params) throws SQLException{
		return auctionDAO.selectAuctMwmn(params);
	}
	
	/**
	 * 매수인 정산서 - 리스트 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String,Object>> entryStateSelectList(Map<String, Object> reqMap)throws SQLException {
		return auctionDAO.entryStateSelectList(reqMap);
	}
	
	/**
	 * 매수인 정산서 - 매수인 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String,Object> selectStateInfo(Map<String, Object> reqMap)throws SQLException {
		Map<String,Object> map = new HashMap<>();
		
		if ("buy".equals(reqMap.get("stateFlag"))) {
			map = auctionDAO.selectBuyStateInfo(reqMap);
		} else {
			map = auctionDAO.selectEntryStateInfo(reqMap);
		}
		
		return map;
	}
	
	/**
	 * 매수인 정산서 - 낙찰금액 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String,Object> selectTotSoldPriceAndFee(Map<String, Object> reqMap)throws SQLException {
		return auctionDAO.selectTotSoldPriceAndFee(reqMap);
	}
	
	/**
	 * 매수인 정산서 - 조합 입금정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String,Object> selectJohapAccInfo(Map<String, Object> reqMap)throws SQLException {
		return auctionDAO.selectJohapAccInfo(reqMap);
	}
	
	/**
	 * 매수인 정산서 - 수수료 상세 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String,Object>> myFeeStateList(Map<String, Object> reqMap)throws SQLException {
		return auctionDAO.myFeeStateList(reqMap);
	}
	
	/**
	 * 정산서 리스트 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String,Object>> selectStateList(Map<String, Object> reqMap)throws SQLException {
		List<Map<String, Object>> list = new ArrayList<>();
		
		if ("buy".equals(reqMap.get("stateFlag"))) {
			list = auctionDAO.selectStateBuyList(reqMap); 
		} else {
			list = auctionDAO.selectStateEntryList(reqMap);
		}
		
		return list;
	}
	
	/**
	 * 개체형매 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectListIndvSib(Map<String, Object> param) throws SQLException{
		return auctionDAO.selectListIndvSib(param);
	}
	
	/**
	 * 개체 이미지 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectListCowImg(Map<String, Object> param) throws SQLException{
		param.put("imgDomain", endPoint + "/" + bucketName + "/");
		return auctionDAO.selectListCowImg(param);
	}
	
	/**
	 * 개체 이동 정보 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectListIndvMove(Map<String, Object> paramMap) throws SQLException{
		return auctionDAO.selectListIndvMove(paramMap);
	}
	
	/**
	 * 출장우 상세 탭 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectListExpitemSet(Map<String, Object> map) throws SQLException{
		return auctionDAO.selectListExpitemSet(map);
	}

	/**
	 * 가축시장 경매일정
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectcheduleList(Map<String, Object> param) throws SQLException {
		if("today".equals(param.get("type")) || "tomorrow".equals(param.get("type"))) {
			return auctionDAO.selectScheduleListQcn(param);			
		}else {
			return auctionDAO.selectcheduleList(param);			
		}
	}

	/**
	 * 후대정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */	
	@Override
	public List<Map<String, Object>> selectListIndvPost(Map<String, Object> param) throws SQLException{
		return auctionDAO.selectListIndvPost(param);
	}

	/**
	 * 개체번호 기준으로 경매정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectIndvDataInfo(Map<String, Object> param) throws SQLException{
		return auctionDAO.selectIndvDataInfo(param);
	}

	/**
	 * 분만정보 조회
	 * @param param
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectListIndvChildBirth(Map<String, Object> param) throws SQLException{
		return auctionDAO.selectListIndvChildBirth(param);		
	}

	/**
	 * 이용해지 신청 데이터 있는지 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectMySecAplyInfo(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectMySecAplyInfo(params);
	}

	/**
	 * 이용해지 신청 데이터 insert
	 * @param params
	 * @throws SQLException
	 */
	@Override
	public void insertMySecAplyInfo(Map<String, Object> params) throws SQLException {
		auctionDAO.insertMySecAplyInfo(params);
	}

	/**
	 * 이용해지 신청 데이터 delete (해지 철회할 때)
	 * @param params
	 * @throws SQLException
	 */
	@Override
	public void deleteMySecAplyInfo(Map<String, Object> params) throws SQLException {
		auctionDAO.deleteMySecAplyInfo(params);
	}

	/**
	 * 키오스크 인증번호 확인 (중도매인)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectMwmnAuthNoYmdInfo(Map<String, Object> map) throws SQLException {
		return auctionDAO.selectMwmnAuthNoYmdInfo(map);
	}

	/**
	 * 키오스크 인증번호 확인 (출하주)
	 * @param map
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectFhsAuthNoYmdInfo(Map<String, Object> map) throws SQLException {
		return auctionDAO.selectFhsAuthNoYmdInfo(map);
	}

	/**
	 * 키오스크 발급된 인증번호 업데이트 (중도매인)
	 * @param params
	 * @throws SQLException
	 */
	@Override
	public void updateMwmnAuthNoYmdInfo(Map<String, Object> params) throws SQLException {
		auctionDAO.updateMwmnAuthNoYmdInfo(params);
	}

	/**
	 * 키오스크 발급된 인증번호 업데이트 (출하주)
	 * @param params
	 * @throws SQLException
	 */
	@Override
	public void updateFhsAuthNoYmdInfo(Map<String, Object> params) throws SQLException {
		auctionDAO.updateFhsAuthNoYmdInfo(params);
	}

	/**
	 * 키오스크 인증번호 발급하기
	 */
	@Override
	public Map<String, Object> myAuthNumIssue(Map<String, Object> params) throws SQLException {
		
		Map<String, Object> result = new HashMap<String, Object>();
		if(sessionUtill.getNaBzplc() != null) params.put("naBzPlc", sessionUtill.getNaBzplc());
		if(sessionUtill.getUserId() != null) params.put("loginNo", sessionUtill.getUserId());
		
		//로그인된 역할 확인하여 각 테이블 조회
		Map<String, Object> authNoYmd = null;
		if(sessionUtill.getRoleConfirm() != null) {
			if("ROLE_BIDDER".equals(sessionUtill.getRoleConfirm())) {
				authNoYmd = this.selectMwmnAuthNoYmdInfo(params);
			}
			else if("ROLE_FARM".equals(sessionUtill.getRoleConfirm())) {
				if(sessionUtill.getUserVo() != null) {
					FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
					params.put("farmAmnno", userVo.getFarmAmnno());
				}
				authNoYmd = this.selectFhsAuthNoYmdInfo(params);
			}
		}
		
		if(authNoYmd == null) {
			result.put("success", false);
			result.put("type", "isNotLogin");
			result.put("message", "로그인 정보를 확인해주세요.");
			return result;
		}
		else{
			
			if(authNoYmd.get("AUTH_YMD") != null) {
				Date date = new Date();
				SimpleDateFormat dt = new SimpleDateFormat("yyyyMMddHHmmss");
				
				Date ymd;
				try {
					ymd = dt.parse(authNoYmd.get("AUTH_YMD").toString());
					Long gap = ymd.getTime() - date.getTime();
					int days = (int) Math.floor(gap / (1000 * 60 * 60 * 24));
					int hours = (int) Math.floor((gap % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
					int minutes = (int) Math.floor((gap % (1000 * 60 * 60)) / (1000 * 60));
					int seconds = (int) Math.floor((gap % (1000 * 60)) / 1000);
					
					//혹시 아직 유효한 인증번호가 있는지 확인? 유효기간이 10분 이내로 남은 것이 있는 경우
					if(authNoYmd.get("AUTH_YMD") != null && days <= 0 && hours <= 0 && (minutes >= 0 && minutes < 10) && seconds > 0){
						result.put("success", true);
						result.put("type", "existNum");
						result.put("auth_no", authNoYmd.get("AUTH_NO").toString());
						result.put("auth_ymd", authNoYmd.get("AUTH_YMD").toString());
						result.put("message", "유효한 인증번호가 존재합니다. 확인 부탁드립니다.");
						return result;
					}
				} catch (ParseException e) {
					log.error("AuctionServiceImpl.myAuthNumIssue : {} ", e);
				}
			}
			
			//유효시간이 지났거나, 인증번호 발급받은 적이 없으면 인증번호 6자리 발급
			Map<String, Object> authNoCnt = new HashMap<>();
			Random random;
			String authNo = "";
			try {
				do {
					//난수 생성
					random = SecureRandom.getInstance("SHA1PRNG");
					random.setSeed(System.currentTimeMillis());
					authNo = String.format("%06d", random.nextInt(1000000));
					params.put("authNo", authNo);
					
					//해당 유효시간내에 다른 계정에서 발급받은 인증번호가 같은경우 다시 난수발급 
					//중도매인 or 출하주 테이블 AUTH_YMD 기한내에 AUTH_NO 조회
					if (sessionUtill.getRoleConfirm() != null) {
						if("ROLE_BIDDER".equals(sessionUtill.getRoleConfirm())) {
							authNoCnt = auctionDAO.selectMwmnAuthNoYmdInfoCnt(params);
						} else {
							authNoCnt = auctionDAO.selectFhsAuthNoYmdInfoCnt(params);
						}
					}
				} while (Integer.parseInt(authNoCnt.getOrDefault("CNT", 0).toString()) > 0);
				
				params.put("auth_no", authNo);
				
			} catch (NoSuchAlgorithmException e) {
				log.error(e.getMessage());
			}
			
			if("ROLE_BIDDER".equals(sessionUtill.getRoleConfirm())) {
				this.updateMwmnAuthNoYmdInfo(params);
				authNoYmd = this.selectMwmnAuthNoYmdInfo(params);
			}
			else if("ROLE_FARM".equals(sessionUtill.getRoleConfirm())) {
				this.updateFhsAuthNoYmdInfo(params);
				authNoYmd = this.selectFhsAuthNoYmdInfo(params);
			}
			
			result.put("success", true);
			result.put("type", "issueNum");
			result.put("auth_no", authNo);
			result.put("auth_ymd", authNoYmd.get("AUTH_YMD") != null ? authNoYmd.get("AUTH_YMD").toString() : "");
			result.put("message", "인증번호가 발급되었습니다.");
		}
		
		return result;
	}

	@Override
	public Map<String, Object> selectStateEntryCntFhs(Map<String, Object> params) throws SQLException {
		return auctionDAO.selectStateEntryCntFhs(params);
	}

	/**
	 * 이용해지 해당조합 조회
	 * @param params
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>>  selectJohqpList(Map<String, Object> map) throws SQLException {
		return  auctionDAO.selectJohqpList(map);
	}
	
	@Override
	public List<Map<String, Object>> selectCowInfoList(Map<String, Object> params) throws SQLException{
		return  auctionDAO.selectCowInfoList(params);
	}
	@Override
	public List<Map<String, Object>> selectMainPopNoticeList(Map<String, Object> params) throws SQLException{
		return  auctionDAO.selectMainPopNoticeList(params);		
	}
	@Override
	public Map<String, Object> selectIndvBloodInfo(Map<String, Object> params) throws SQLException{
		return  auctionDAO.selectIndvBloodInfo(params);		
	}
	
	//flagAucDsc : 단일,일괄경매 체크
	private List<Map<String, Object>> calcFeeInfo(Map<String, Object> params, Map<String, Object> info, Map<String, Object> bizAuctionInfo) throws SQLException {

		Iterator<String> keys = info.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			if(info.get(key) == null) {
				switch(key) {
				case "PPGCOW_FEE_DSC": info.put(key, "5"); break;
				case "SEL_STS_DSC": info.put(key, "23"); break;
				default: info.put(key, "0"); break;
				}
			}
		}

		final String naBzplc		= info.get("NA_BZPLC").toString();
		final String aucDt			= info.get("AUC_DT").toString();
		final String aucObjDsc		= info.get("AUC_OBJ_DSC").toString();
		final String oslpNo			= info.get("OSLP_NO").toString();
		final String ledSqno		= info.get("LED_SQNO").toString();
		final String trmnMacoYn		= info.get("TRMN_MACO_YN").toString();				// 중도매인 조합원 여부 ( 0.비조합원, 1.조합원 )
		final String fhsMacoYn		= info.get("FHS_MACO_YN").toString();				// 출하주 조합원 여부 ( 0.비조합원, 1.조합원 )
		final String ppgcowFeeDsc	= info.get("PPGCOW_FEE_DSC").toString();			// 번식우 수수료 구분코드 > 1.임신우, 2.비임신우, 3.임신우+송아지, 4.비임신우+송아지,  5.해당없음
		final String selStsDsc		= info.get("SEL_STS_DSC").toString();						// 경매상태 
		//단일
		//long sraSbidUpr				= flagAucDsc?Long.parseLong(params.get("sraSbidUpr").toString()):Long.parseLong(info.get("ATDR_AM").toString());							// 응찰금액
		long sraSbidAm				= Long.parseLong(info.get("sraSbidAm").toString());

		info.put("naBzplc",			naBzplc);
		info.put("naBzPlc",			naBzplc);
		info.put("aucObjDsc",		aucObjDsc);
		info.put("aucDt",			aucDt);

		// 8. 수수료 기본 정보 조회
		final List<Map<String, Object>> feeInfoList = auctionDAO.selectFeeInfo(info);
		
		// 9. 수수료 저장
		for (Map<String, Object> feeInfo : feeInfoList) {
			feeInfo.put("AUC_DT",		aucDt);
			feeInfo.put("AUC_OBJ_DSC",	aucObjDsc);
			feeInfo.put("OSLP_NO",		oslpNo);
			feeInfo.put("LED_SQNO",		ledSqno);
			
			// 중도매인, 출하주의 조합원 여부
			String macoYn = ("1".equals(feeInfo.get("FEE_APL_OBJ_C"))) ? fhsMacoYn : trmnMacoYn;
			long feeAmt = 0L;
			
			// 낙찰인 경우만 수수료 정보를 저장하므로 SBID_YN이 0인(미낙찰) 수수료 정보의 금액은 0으로 넣어준다.
			if ("0".equals(feeInfo.get("SBID_YN")) && "23".equals(selStsDsc)) {
				feeAmt = "0".equals(macoYn) ? Long.parseLong(feeInfo.getOrDefault("NMACO_FEE_UPR", "0").toString())
											: Long.parseLong(feeInfo.getOrDefault("MACO_FEE_UPR", "0").toString());
				feeInfo.put("SRA_TR_FEE", feeAmt);
			}
			else if ("1".equals(feeInfo.get("SBID_YN")) && "22".equals(selStsDsc)) {

				String[] arrNaFeeC = {"010", "011"};
				//출하수수료 / 판매수수료 해당없음 있을시 중복 산정되는 현상 수정
				if(("5".equals(feeInfo.get("PPGCOW_FEE_DSC")) && !Arrays.asList(arrNaFeeC).contains(feeInfo.get("NA_FEE_C"))) 
				|| ppgcowFeeDsc.equals(feeInfo.get("PPGCOW_FEE_DSC"))) {		// 출장우 정보의 번식우 구분코드와 수수료 정보의 번식우 구분코드가 일치하는 경우 
					
					// 수수료가 비율인 경우 TB_LA_IS_MH_FEE의 절사구분(SGNO_PRC_DSC)을 참조하여 처리한다.
					// 1.소수점 이하 버림, 2. 소수점 이상 절상, 3. 반올림
					if ("2".equals(feeInfo.get("AM_RTO_DSC"))) {
						double feeUpr = "0".equals(macoYn) ? Double.parseDouble(feeInfo.getOrDefault("NMACO_FEE_UPR", "0").toString())
														   : Double.parseDouble(feeInfo.getOrDefault("MACO_FEE_UPR", "0").toString());
						if ("1".equals(feeInfo.get("SGNO_PRC_DSC"))) {
							feeAmt = (long)Math.floor(feeUpr / 100 * sraSbidAm);
						}
						else if ("2".equals(feeInfo.get("SGNO_PRC_DSC"))) {
							feeAmt = (long)Math.ceil(feeUpr / 100 * sraSbidAm);
						}
						else if ("3".equals(feeInfo.get("SGNO_PRC_DSC"))) {
							feeAmt = (long)Math.round(feeUpr / 100 * sraSbidAm);
						}
						
						// 임신 감정료
						if ("060".equals(feeInfo.get("NA_FEE_C"))) {
							// 고창부안 : 8808990657189, 장흥 : 8808990656533, 보성 : 8808990656267, 곡성 : 8808990656717,순천광양 : 8808990658896, 영광 : 8808990811710
							// 2023.12.06 : 화순축협 제거
							// 2024.01.04 : 장성축협 제거
							String[] arrNaBzplc = {"8808990657189", "8808990656533", "8808990656267"
												 , "8808990656717", "8808990658896", "8808990811710"};
							if (Arrays.asList(arrNaBzplc).contains(params.get("naBzPlc"))) {
								// 출하자에게는 임신여부가 여(1)인 경우 수수료를 부과하지 않는다, 낙찰자에게는 임신여부가 부(0)인 경우 수수료를 부과하지 않는다.
								if(("1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "1".equals(info.get("PRNY_YN")))
								|| (!"1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "0".equals(info.get("PRNY_YN")))) {
									feeAmt = 0L;
								}
							}
						}
						// 괴사 감정료
						else if ("050".equals(feeInfo.get("NA_FEE_C"))) {
							// 장흥 : 8808990656533, 보성 : 8808990656267, 영광 : 8808990811710, 장성 : 8808990817675
							String[] arrNaBzplc = {"8808990656533", "8808990656267", "8808990811710", "8808990817675"};
							// 위의 조합은 괴사 감정을 
							if (Arrays.asList(arrNaBzplc).contains(params.get("naBzPlc"))) {
								// 출하자에게는 괴사여부가 부(0)인 경우 수수료를 부과하지 않는다, 낙찰자에게는 괴사여부가 여(1)인 경우 수수료를 부과하지 않는다.
								if(("1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "0".equals(info.get("NCSS_YN")))
								|| (!"1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "1".equals(info.get("NCSS_YN")))) {
									feeAmt = 0L;
								}
							}
						}
						else if ("010".equals(feeInfo.get("NA_FEE_C"))) {
							// 출하수수료 수기등록(FEE_CHK_YN)인 경우 해당 금액(FEE_CHK_YN_FEE)을 출하수수료로 부과
							if ("1".equals(info.getOrDefault("FEE_CHK_YN", "0"))) {
								feeAmt = Long.parseLong(info.getOrDefault("FEE_CHK_YN_FEE", "0").toString());
							}
						}
						else if ("011".equals(feeInfo.get("NA_FEE_C"))) {
							// 판매수수료 수기등록(SELFEE_CHK_YN)인 경우 해당 금액(SELFEE_CHK_YN_FEE)을 출하수수료로 부과
							if ("1".equals(info.getOrDefault("SELFEE_CHK_YN", "0"))) {
								feeAmt = Long.parseLong(info.getOrDefault("SELFEE_CHK_YN_FEE", "0").toString());
							}
						}
					}
					else {
					// 수수료가 금액인 경우
						feeAmt = "0".equals(macoYn) ? Long.parseLong(feeInfo.getOrDefault("NMACO_FEE_UPR", "0").toString())
													: Long.parseLong(feeInfo.getOrDefault("MACO_FEE_UPR", "0").toString());
						
						// 출하자 출하수수료
						if ("010".equals(feeInfo.get("NA_FEE_C"))) {
							// 수수료 부과 대상이 낙찰자인 경우 혈통 송아지 수수료를 추가한다 (BLOOD_AM)
							if(!"1".equals(feeInfo.get("FEE_APL_OBJ_C"))) {
								feeAmt += Long.parseLong(info.getOrDefault("BLOOD_AM", "0").toString());
							}
							
							if ("8808990687094".equals(params.get("naBzPlc"))) {
								// 친자 검사 여부(DNA_YN_CHK) 수수료
								if ("1".equals(info.get("DNA_YN_CHK"))) {
									feeAmt += Long.parseLong(bizAuctionInfo.getOrDefault("FEE_CHK_DNA_YN_FEE", "0").toString());
								}
							}
							
							// 사료 미사용 추가 수수료 추가
							if("0".equals(info.get("SRA_FED_SPY_YN"))) {
								feeAmt += Long.parseLong(bizAuctionInfo.getOrDefault("SRA_FED_SPY_YN_FEE", "0").toString());
							}
							
							// 출하수수료 수기등록(FEE_CHK_YN)인 경우 해당 금액(FEE_CHK_YN_FEE)을 출하수수료로 부과
							if ("1".equals(info.getOrDefault("FEE_CHK_YN", "0"))) {
								feeAmt = Long.parseLong(info.getOrDefault("FEE_CHK_YN_FEE", "0").toString());
							}
						}
						// 낙찰자 판매수수료
						else if ("011".equals(feeInfo.get("NA_FEE_C"))) {
							// 수수료 부과 대상이 낙찰자인 경우 혈통 송아지 수수료를 추가한다 (BLOOD_AM)
							if(!"1".equals(feeInfo.get("FEE_APL_OBJ_C"))) {
								feeAmt += Long.parseLong(info.getOrDefault("BLOOD_AM", "0").toString());
							}

							// 영주축협 송아지 12개월이상 수수료 적용
							if ("8808990687094".equals(params.get("naBzPlc"))) {
								if ("1".equals(info.get("MT12_OVR_YN"))) {
									feeAmt += Long.parseLong(bizAuctionInfo.getOrDefault("MT12_OVR_FEE", "0").toString());
								}
								else if ("1".equals(info.get("DNA_YN_CHK"))) {
									// 친자 검사 여부(DNA_YN_CHK) 수수료
									feeAmt += Long.parseLong(bizAuctionInfo.getOrDefault("SELFEE_CHK_DNA_YN_FEE", "0").toString());
								}
							}
							
							// 판매수수료 수기등록(SELFEE_CHK_YN)인 경우 해당 금액(SELFEE_CHK_YN_FEE)을 출하수수료로 부과
							if ("1".equals(info.getOrDefault("SELFEE_CHK_YN", "0"))) {
								feeAmt = Long.parseLong(info.getOrDefault("SELFEE_CHK_YN_FEE", "0").toString());
							}
						}
						// 임신 감정료
						else if ("060".equals(feeInfo.get("NA_FEE_C"))) {
							// 고창부안 : 8808990657189, 장흥 : 8808990656533, 보성 : 8808990656267, 곡성 : 8808990656717,순천광양 : 8808990658896, 영광 : 8808990811710
							// 2023.12.06 : 화순축협 제거
							// 2024.01.04 : 장성축협 제거
							String[] arrNaBzplc = {"8808990657189", "8808990656533", "8808990656267"
												 , "8808990656717", "8808990658896", "8808990811710"};
							if (Arrays.asList(arrNaBzplc).contains(params.get("naBzPlc"))) {
								// 출하자에게는 임신여부가 여(1)인 경우 수수료를 부과하지 않는다, 낙찰자에게는 임신여부가 부(0)인 경우 수수료를 부과하지 않는다.
								if(("1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "1".equals(info.get("PRNY_YN")))
								|| (!"1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "0".equals(info.get("PRNY_YN")))) {
									feeAmt = 0L;
								}
							}
						}
						// 괴사 감정료
						else if ("050".equals(feeInfo.get("NA_FEE_C"))) {
							// 장흥 : 8808990656533, 보성 : 8808990656267, 영광 : 8808990811710, 장성 : 8808990817675
							String[] arrNaBzplc = {"8808990656533", "8808990656267", "8808990811710", "8808990817675"};
							// 위의 조합은 괴사 감정을 
							if (Arrays.asList(arrNaBzplc).contains(params.get("naBzPlc"))) {
								// 출하자에게는 괴사여부가 부(0)인 경우 수수료를 부과하지 않는다, 낙찰자에게는 괴사여부가 여(1)인 경우 수수료를 부과하지 않는다.
								if(("1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "0".equals(info.get("NCSS_YN")))
								|| (!"1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "1".equals(info.get("NCSS_YN")))) {
									feeAmt = 0L;
								}
							}
						}
						
					}
				}
			}
			else {
				feeInfo.put("SRA_TR_FEE", 0L);
			}
			
			// 운송비인 경우 TRPCS_PY_YN(운송비 지급 여부)가 1일 때는 수수를 부과하지 않는다.
			if ("040".equals(feeInfo.get("NA_FEE_C")) && "1".equals(info.get("TRPCS_PY_YN"))) {
				feeAmt = 0L;
			}
			// 임신감정 수수료인 경우 PRNY_JUG_YN(임신감정여부)가 0일 때는 수수료를 부과하지 않는다
			if ("060".equals(feeInfo.get("NA_FEE_C")) && "0".equals(info.get("PRNY_JUG_YN"))) {
				feeAmt = 0L;
			}
			// 괴사감정 수수료인 경우 NCSS_JUG_YN(괴사감정여부)가 0일 때는 수수료를 부과하지 않는다
			if ("050".equals(feeInfo.get("NA_FEE_C")) && "0".equals(info.get("NCSS_JUG_YN"))) {
				feeAmt = 0L;
			}
			// 제각수수료인 경우 경우 RMHN_YN(제각여부)가 0일 때는 수수료를 부과하지 않는다
			if ("110".equals(feeInfo.get("NA_FEE_C")) && "0".equals(info.get("RMHN_YN"))) {
				feeAmt = 0L;
			}
			
			feeInfo.put("SRA_TR_FEE", feeAmt);
		}
		return feeInfoList;
	}

	@Override
	public List<Map<String, Object>> selectCowEpdList(Map<String, Object> params) throws SQLException{
		return auctionDAO.selectCowEpdList(params);
	}
	
	@Override
	public List<Map<String, Object>> selectSumEntry(Map<String, Object> params) throws SQLException{
		return auctionDAO.selectSumEntry(params);
	}

}
