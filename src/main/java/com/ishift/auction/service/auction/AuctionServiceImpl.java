package com.ishift.auction.service.auction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service("auctionService")
@Transactional(transactionManager = "txManager", rollbackFor = Exception.class)
public class AuctionServiceImpl implements AuctionService {

	@Resource(name = "auctionDAO")
	AuctionDAO auctionDAO;
	
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
		return auctionDAO.entrySelectList(reqMap);
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
		return auctionDAO.insertUpdateZim(param);
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
			if (cnt == 0) {
				return 0;
			}
			
			// 수수료 정보 저장 [s]
			// 1. 기등록 수수료 정보 삭제
			auctionDAO.deleteFeeInfo(params);
			
			// 2. 경매 정보 조회 ( 송아지, 번식우, 비육우 여부, 중도매인, 출하주의 조합 가입 여부, 자가 운송 여부 )
			final Map<String, Object> auctionInfo = auctionDAO.selectAuctionInfo(params);
			
			// 3. 낙찰인 경우 수수료 정보 저장
			if (auctionInfo != null && "22".equals(auctionInfo.getOrDefault("SEL_STS_DSC", ""))) {
				
				final String aucDt			= auctionInfo.get("AUC_DT").toString();
				final String aucObjDsc		= auctionInfo.get("AUC_OBJ_DSC").toString();
				final String oslpNo			= auctionInfo.get("OSLP_NO").toString();
				final String ledSqno		= auctionInfo.get("LED_SQNO").toString();
				final String trmnMacoYn		= auctionInfo.get("TRMN_MACO_YN").toString();	// 중도매인 조합원 여부 ( 0.비조합원, 1.조합원 )
				final String fhsMacoYn		= auctionInfo.get("FHS_MACO_YN").toString();	// 출하주 조합원 여부 ( 0.비조합원, 1.조합원 )
				final String ppgcowFeeDsc	= auctionInfo.get("PPGCOW_FEE_DSC").toString();	// 번식우 수수료 구분코드 > 1.임신우, 2.비임신우, 3.임신우+송아지, 4.비임신우+송아지,  5.해당없음
				final String trpcsPyYn		= auctionInfo.get("TRPCS_PY_YN").toString();	// 운송비 지급 여부
				
				// 4. 수수료 기본 정보 조회
				final List<Map<String, Object>> feeInfoList = auctionDAO.selectFeeInfo(params);
				
				// 5. 수수료 저장
				for (Map<String, Object> feeInfo : feeInfoList) {
					feeInfo.put("AUC_DT",		aucDt);
					feeInfo.put("AUC_OBJ_DSC",	aucObjDsc);
					feeInfo.put("OSLP_NO",		oslpNo);
					feeInfo.put("LED_SQNO",		ledSqno);
					
					// 중도매인, 출하주의 조합원 여부
					String macoYn = ("1".equals(feeInfo.get("FEE_APL_OBJ_C"))) ? fhsMacoYn : trmnMacoYn;
					
					// 낙찰인 경우만 수수료 정보를 저장하므로 SBID_YN이 0인(미낙찰) 수수료 정보의 금액은 0으로 넣어준다.
					if ("0".equals(feeInfo.get("SBID_YN"))) {
						feeInfo.put("SRA_TR_FEE", 0);
					}
					else if ("5".equals(feeInfo.get("PPGCOW_FEE_DSC"))			// 수수료 정보의 번식우 구분코드가 5(해당없음)인 경우
					 || ppgcowFeeDsc.equals(feeInfo.get("PPGCOW_FEE_DSC"))		// 출장우 정보의 번식우 구분코드와 수수료 정보의 번식우 구분코드가 일치하는 경우 
					) {
						// 수수료 유형이 운송비인 경우 trpcPyYn(운송비 지급 여부)가 0일 때만 수수료를 부과한다.
						if ("040".equals(feeInfo.get("NA_FEE_C")) && "1".equals(trpcsPyYn)) {
							feeInfo.put("SRA_TR_FEE", 0);
						}
						else {
							feeInfo.put("SRA_TR_FEE", "0".equals(macoYn) ? feeInfo.get("NMACO_FEE_UPR") : feeInfo.get("MACO_FEE_UPR"));
						}
					}
					else {
						feeInfo.put("SRA_TR_FEE", 0);
					}
				}
				
				params.put("feeInfoList", feeInfoList);
				
				auctionDAO.insertFeeInfo(params);
			}
			// 수수료 정보 저장 [e]

			return cnt;
		}
		else {
			return auctionDAO.updateAuctionResult(params);
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
			// 업데이트 유형이 중량 등록(W), 중량 일괄 등록(AW)이고 비육우인 경우 낙찰 금액을 다시 계산해준다.
			if (("W".equals(params.get("regType")) || "AW".equals(params.get("regType")))
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
					if (returnMap != null) {
						throw new SQLException("출장우 정보 업데이트에 실패했습니다.");
					}
				}

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
		params.put("naBzplc", params.get("naBzPlc"));
		
		// 1. 필수 인자 체크
		if (params.get("naBzPlc") == null
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
				sraSbidAm = sraSbidUpr * aucAtdrUntAm;
				params.put("sraSbidAm", sraSbidAm);
			}
		}
		// 낙찰 금액 계산 [e]
		
		// 5. 경매결과 업데이트
		int cnt = auctionDAO.updateAuctionResult(params);
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
		if (auctionInfo != null && "22".equals(auctionInfo.getOrDefault("SEL_STS_DSC", ""))) {
			final String trmnMacoYn		= auctionInfo.get("TRMN_MACO_YN").toString();	// 중도매인 조합원 여부 ( 0.비조합원, 1.조합원 )
			final String fhsMacoYn		= auctionInfo.get("FHS_MACO_YN").toString();	// 출하주 조합원 여부 ( 0.비조합원, 1.조합원 )
			final String ppgcowFeeDsc	= auctionInfo.get("PPGCOW_FEE_DSC").toString();	// 번식우 수수료 구분코드 > 1.임신우, 2.비임신우, 3.임신우+송아지, 4.비임신우+송아지,  5.해당없음
			
			// 9. 수수료 기본 정보 조회
			final List<Map<String, Object>> feeInfoList = auctionDAO.selectFeeInfo(params);
			
			// 10. 수수료 저장
			for (Map<String, Object> feeInfo : feeInfoList) {
				feeInfo.put("AUC_DT",		aucDt);
				feeInfo.put("AUC_OBJ_DSC",	aucObjDsc);
				feeInfo.put("OSLP_NO",		oslpNo);
				feeInfo.put("LED_SQNO",		ledSqno);
				
				// 중도매인, 출하주의 조합원 여부
				String macoYn = ("1".equals(feeInfo.get("FEE_APL_OBJ_C"))) ? fhsMacoYn : trmnMacoYn;
				long feeAmt = 0L;
				
				// 낙찰인 경우만 수수료 정보를 저장하므로 SBID_YN이 0인(미낙찰) 수수료 정보의 금액은 0으로 넣어준다.
				if ("0".equals(feeInfo.get("SBID_YN"))) {
					feeInfo.put("SRA_TR_FEE", feeAmt);
				}
				else if ("5".equals(feeInfo.get("PPGCOW_FEE_DSC"))			// 수수료 정보의 번식우 구분코드가 5(해당없음)인 경우
				 || ppgcowFeeDsc.equals(feeInfo.get("PPGCOW_FEE_DSC"))		// 출장우 정보의 번식우 구분코드와 수수료 정보의 번식우 구분코드가 일치하는 경우 
				) {
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
							// 고창부안 : 8808990657189, 장흥 : 8808990656533, 보성 : 8808990656267, 화순 : 8808990661315, 곡성 : 8808990656717,순천광양 : 8808990658896, 영광 : 8808990811710, 장성 : 8808990817675
							String[] arrNaBzplc = {"8808990657189", "8808990656533", "8808990656267", "8808990661315"
												 , "8808990656717", "8808990658896", "8808990811710", "8808990817675"};
							if (Arrays.asList(arrNaBzplc).contains(params.get("naBzPlc"))) {
								// 출하자에게는 임신여부가 여(1)인 경우 수수료를 부과하지 않는다, 낙찰자에게는 임신여부가 부(0)인 경우 수수료를 부과하지 않는다.
								if(("1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "1".equals(auctionInfo.get("PRNY_YN")))
								|| (!"1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "0".equals(auctionInfo.get("PRNY_YN")))) {
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
								if(("1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "0".equals(auctionInfo.get("NCSS_YN")))
								|| (!"1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "1".equals(auctionInfo.get("NCSS_YN")))) {
									feeAmt = 0L;
								}
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
								feeAmt += Long.parseLong(auctionInfo.getOrDefault("BLOOD_AM", "0").toString());
							}
							
							// 친자 검사 여부(DNA_YN_CHK) 수수료
							// 합천인 경우에는 농가 정보 테이블에서 축산사료여부 사용 여부(SRA_FED_SPY_YN) 체크 후 미사용시(0) 친자 확인 수수료를 부과한다.
							if ("1".equals(auctionInfo.get("DNA_YN_CHK"))) {
								feeAmt += Long.parseLong(bizAuctionInfo.getOrDefault("FEE_CHK_DNA_YN_FEE", "0").toString());
							}
						}
						// 낙찰자 판매수수료
						else if ("011".equals(feeInfo.get("NA_FEE_C"))) {
							// 수수료 부과 대상이 낙찰자인 경우 혈통 송아지 수수료를 추가한다 (BLOOD_AM)
							if(!"1".equals(feeInfo.get("FEE_APL_OBJ_C"))) {
								feeAmt += Long.parseLong(auctionInfo.getOrDefault("BLOOD_AM", "0").toString());
							}

							// 친자 검사 여부(DNA_YN_CHK) 수수료
							// 합천인 경우 출장우 정보 테이블의 친자 일치 여부(DNA_YN) 체크 후 일치 할 시(1) 친자 확인 수수료를 부과한다.
							if ("1".equals(auctionInfo.get("DNA_YN_CHK"))) {
								feeAmt += Long.parseLong(bizAuctionInfo.getOrDefault("SELFEE_CHK_DNA_YN_FEE", "0").toString());
							}
							
							// 영주축협 송아지 12개월이상 수수료 적용
							if ("8808990687094".equals(params.get("naBzPlc"))) {
								if ("1".equals(auctionInfo.get("MT12_OVR_YN"))) {
									feeAmt += Long.parseLong(bizAuctionInfo.getOrDefault("MT12_OVR_FEE", "0").toString());
								}
							}
						}
						// 임신 감정료
						else if ("060".equals(feeInfo.get("NA_FEE_C"))) {
							// 고창부안 : 8808990657189, 장흥 : 8808990656533, 보성 : 8808990656267, 화순 : 8808990661315, 곡성 : 8808990656717,순천광양 : 8808990658896, 영광 : 8808990811710, 장성 : 8808990817675
							String[] arrNaBzplc = {"8808990657189", "8808990656533", "8808990656267", "8808990661315"
												 , "8808990656717", "8808990658896", "8808990811710", "8808990817675"};
							if (Arrays.asList(arrNaBzplc).contains(params.get("naBzPlc"))) {
								// 출하자에게는 임신여부가 여(1)인 경우 수수료를 부과하지 않는다, 낙찰자에게는 임신여부가 부(0)인 경우 수수료를 부과하지 않는다.
								if(("1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "1".equals(auctionInfo.get("PRNY_YN")))
								|| (!"1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "0".equals(auctionInfo.get("PRNY_YN")))) {
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
								if(("1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "0".equals(auctionInfo.get("NCSS_YN")))
								|| (!"1".equals(feeInfo.get("FEE_APL_OBJ_C")) && "1".equals(auctionInfo.get("NCSS_YN")))) {
									feeAmt = 0L;
								}
							}
						}
					}
					
					// 운송비인 경우 TRPCS_PY_YN(운송비 지급 여부)가 1일 때는 수수를 부과하지 않는다.
					if ("040".equals(feeInfo.get("NA_FEE_C")) && "1".equals(auctionInfo.get("TRPCS_PY_YN"))) {
						feeAmt = 0L;
					}
					// 임신감정 수수료인 경우 PRNY_JUG_YN(임신감정여부)가 0일 때는 수수료를 부과하지 않는다
					if ("060".equals(feeInfo.get("NA_FEE_C")) && "0".equals(auctionInfo.get("PRNY_JUG_YN"))) {
						feeAmt = 0L;
					}
					// 괴사감정 수수료인 경우 NCSS_JUG_YN(괴사감정여부)가 0일 때는 수수료를 부과하지 않는다
					if ("050".equals(feeInfo.get("NA_FEE_C")) && "0".equals(auctionInfo.get("NCSS_JUG_YN"))) {
						feeAmt = 0L;
					}
					// 제각수수료인 경우 경우 RMHN_YN(제각여부)가 0일 때는 수수료를 부과하지 않는다
					if ("110".equals(feeInfo.get("NA_FEE_C")) && "0".equals(auctionInfo.get("RMHN_YN"))) {
						feeAmt = 0L;
					}
				}
				else {
					feeInfo.put("SRA_TR_FEE", 0);
				}
				
				feeInfo.put("SRA_TR_FEE", feeAmt);
			}
			
			params.put("feeInfoList", feeInfoList);
			if(feeInfoList.size() >0) auctionDAO.insertFeeInfo(params);
		}
		// 수수료 정보 저장 [e]
		
		return null;
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
		params.put("stAucNo", aucStn.get("ST_AUC_NO"));
		params.put("edAucNo", aucStn.get("ED_AUC_NO"));
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
				final String ppgcowFeeDsc	= info.get("PPGCOW_FEE_DSC").toString();			// 번식우 수수료 구분코드 > 1.임신우, 2.비임신우, 3.임신우+송아지, 4.비임신우+송아지,  5.해당없음
				final String trmnAmnno		= info.get("TRMN_AMNNO").toString();				// 중도매인 번호
				final String lvstAucPtcMnNo	= info.get("LVST_AUC_PTC_MN_NO").toString();		// 경매참가번호
				final String cowSogWt		= info.getOrDefault("COW_SOG_WT", "0").toString();
				final String aucUprDsc		= bizAuctionInfo.getOrDefault("NBFCT_AUC_UPR_DSC", "1").toString();			// 비육우 경매단가 구분 코드 ( 1. kg 단위, 2. 두 단위 )
				final String sgNoPrcDsc		= bizAuctionInfo.getOrDefault("SGNO_PRC_DSC", "1").toString();				// 절사구분 ( 1.소수점 이하 버림, 2. 소수점 이상 절상, 3. 반올림 )
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
				info.put("selStsDsc",		"22");
				info.put("lsCmeno",			"SYSTEM");
				info.put("sraSbidUpr",		sraSbidUpr);

				// 낙찰 금액 계산 [s]
				// 경매 금액 단위 > 보통 10000원이지만 비육우인 경우 kg단위로 계산한다.
				int aucAtdrUntAm			= 10000;
				// 비육우 & 경매 단가가 kg인 경우
				if ("2".equals(aucObjDsc) && "1".equals(aucUprDsc)) {
					// 중량 정보가 없으면 낙찰가를 0으로 넣어준다.
					if ("0".equals(cowSogWt)) {
						info.put("sraSbidAm", 0);
					}
					else {
						aucAtdrUntAm = Integer.parseInt(bizAuctionInfo.getOrDefault("NBFCT_AUC_ATDR_UNT_AM", "1").toString());
						double bidAmt = Double.parseDouble(cowSogWt) * sraSbidUpr * aucAtdrUntAm / cutAm;
//						double bidAmt = Double.parseDouble(cowSogWt) * sraSbidUpr / cutAm;
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
					sraSbidAm = sraSbidUpr * aucAtdrUntAm;
					info.put("sraSbidAm", sraSbidAm);
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
					if ("0".equals(feeInfo.get("SBID_YN"))) {
						feeInfo.put("SRA_TR_FEE", feeAmt);
					}
					else if ("5".equals(feeInfo.get("PPGCOW_FEE_DSC"))			// 수수료 정보의 번식우 구분코드가 5(해당없음)인 경우
					 || ppgcowFeeDsc.equals(feeInfo.get("PPGCOW_FEE_DSC"))		// 출장우 정보의 번식우 구분코드와 수수료 정보의 번식우 구분코드가 일치하는 경우 
					) {
						
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
								// 고창부안 : 8808990657189, 장흥 : 8808990656533, 보성 : 8808990656267, 화순 : 8808990661315, 곡성 : 8808990656717,순천광양 : 8808990658896, 영광 : 8808990811710, 장성 : 8808990817675
								String[] arrNaBzplc = {"8808990657189", "8808990656533", "8808990656267", "8808990661315"
													 , "8808990656717", "8808990658896", "8808990811710", "8808990817675"};
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
								
								// 친자 검사 여부(DNA_YN_CHK) 수수료
								// 합천인 경우에는 농가 정보 테이블에서 축산사료여부 사용 여부(SRA_FED_SPY_YN) 체크 후 미사용시(0) 친자 확인 수수료를 부과한다.
								if ("1".equals(info.get("DNA_YN_CHK"))) {
									feeAmt += Long.parseLong(bizAuctionInfo.getOrDefault("FEE_CHK_DNA_YN_FEE", "0").toString());
								}
							}
							// 낙찰자 판매수수료
							else if ("011".equals(feeInfo.get("NA_FEE_C"))) {
								// 수수료 부과 대상이 낙찰자인 경우 혈통 송아지 수수료를 추가한다 (BLOOD_AM)
								if(!"1".equals(feeInfo.get("FEE_APL_OBJ_C"))) {
									feeAmt += Long.parseLong(info.getOrDefault("BLOOD_AM", "0").toString());
								}

								// 친자 검사 여부(DNA_YN_CHK) 수수료
								// 합천인 경우 출장우 정보 테이블의 친자 일치 여부(DNA_YN) 체크 후 일치 할 시(1) 친자 확인 수수료를 부과한다.
								if ("1".equals(info.get("DNA_YN_CHK"))) {
									feeAmt += Long.parseLong(bizAuctionInfo.getOrDefault("SELFEE_CHK_DNA_YN_FEE", "0").toString());
								}
								
								// 영주축협 송아지 12개월이상 수수료 적용
								if ("8808990687094".equals(params.get("naBzPlc"))) {
									if ("1".equals(info.get("MT12_OVR_YN"))) {
										feeAmt += Long.parseLong(bizAuctionInfo.getOrDefault("MT12_OVR_FEE", "0").toString());
									}
								}
							}
							// 임신 감정료
							else if ("060".equals(feeInfo.get("NA_FEE_C"))) {
								// 고창부안 : 8808990657189, 장흥 : 8808990656533, 보성 : 8808990656267, 화순 : 8808990661315, 곡성 : 8808990656717,순천광양 : 8808990658896, 영광 : 8808990811710, 장성 : 8808990817675
								String[] arrNaBzplc = {"8808990657189", "8808990656533", "8808990656267", "8808990661315"
													 , "8808990656717", "8808990658896", "8808990811710", "8808990817675"};
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
					}
					else {
						feeInfo.put("SRA_TR_FEE", 0);
					}
					
					feeInfo.put("SRA_TR_FEE", feeAmt);
				}
				
				params.put("feeInfoList", feeInfoList);
				
				auctionDAO.insertFeeInfo(params);
			}
		}
		
		// 10. 낙찰 상태가 아닌 출장우 로그 저장
		params.put("newCntAucYn", "Y");
		params.put("soldChkYn", "N");
		params.put("fsrgmnEno", "admin");
		params.put("lsCmeno", "[LM0314]");
		params.put("pdaId", "새 차수 경매 시작[성공]");
		auctionDAO.insertAuctSogCowLog(params);

		// 11. 낙찰 상태 출장우 로그 저장
		params.put("newCntAucYn", "N");
		params.put("soldChkYn", "Y");
		params.put("pdaId", "경매종료[낙찰]");
		auctionDAO.insertAuctSogCowLog(params);

		// 12. 유찰 처리 ( 최저가가 등록되지 않은 출장우 중 SEL_STS_DSC가 '22'가 아닌 것 )
		params.put("sraSbidAm", "0");
		params.put("lsCmeno", "SYSTEM");
		auctionDAO.updateAuctSogCowFinish(params);
		
		// 13. 유찰 상태 출장우 로그 저장
		params.put("newCntAucYn", "N");
		params.put("soldChkYn", "N");
		params.put("pdaId", "경매종료[불낙]");
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
	
	@Override
	public int updateCowInfoForModlNo(Map<String, Object> params) throws SQLException{
		return auctionDAO.updateCowInfoForModlNo(params);
	}
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
}
