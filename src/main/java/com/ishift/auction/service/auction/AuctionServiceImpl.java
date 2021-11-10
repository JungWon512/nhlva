package com.ishift.auction.service.auction;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service("auctionService")
@Transactional(transactionManager = "txManager", rollbackFor = SQLException.class)
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
					String macoYn = ("1".equals(feeInfo.get("FEE_APL_OBJ_C"))) ? trmnMacoYn : fhsMacoYn;
					
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
		return auctionDAO.updateCowInfo(params);
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

		int cnt = auctionDAO.updateAuctionResult(params);
		if (cnt == 0) {
			// 실패한 정보를 다시 return
			params.put("message", "출하우 정보가 없습니다.");
			return params;
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
				String macoYn = ("1".equals(feeInfo.get("FEE_APL_OBJ_C"))) ? trmnMacoYn : fhsMacoYn;
				
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

		// 4. 응찰 완료(낙찰 처리 대상) 리스트 조회
		final List<Map<String, Object>> list = auctionDAO.selectBidCompleteList(params);
		if (list.size() > 0) {
			for (Map<String, Object> info : list) {
				final String naBzplc		= info.get("NA_BZPLC").toString();
				final String aucDt			= info.get("AUC_DT").toString();
				final String aucObjDsc		= info.get("AUC_OBJ_DSC").toString();
				final String oslpNo			= info.get("OSLP_NO").toString();
				final String ledSqno		= info.get("LED_SQNO").toString();
				final String trmnMacoYn		= info.get("TRMN_MACO_YN").toString();		// 중도매인 조합원 여부 ( 0.비조합원, 1.조합원 )
				final String fhsMacoYn		= info.get("FHS_MACO_YN").toString();		// 출하주 조합원 여부 ( 0.비조합원, 1.조합원 )
				final String ppgcowFeeDsc	= info.get("PPGCOW_FEE_DSC").toString();	// 번식우 수수료 구분코드 > 1.임신우, 2.비임신우, 3.임신우+송아지, 4.비임신우+송아지,  5.해당없음
				final String trpcsPyYn		= info.get("TRPCS_PY_YN").toString();		// 운송비 지급 여부
				final String trmnAmnno		= info.get("TRMN_AMNNO").toString();
				final String lvstAucPtcMnNo	= info.get("LVST_AUC_PTC_MN_NO").toString();
				final String sraSbidAm		= info.get("ATDR_AM").toString();
				final String sraSbidUpr		= info.get("ATDR_UPR").toString();
				
				info.put("naBzPlc",			naBzplc);
				info.put("aucDt",			aucDt);
				info.put("aucObjDsc",		aucObjDsc);
				info.put("oslpNo",			oslpNo);
				info.put("ledSqno",			ledSqno);
				info.put("trmnAmnno",		trmnAmnno);
				info.put("lvstAucPtcMnNo",	lvstAucPtcMnNo);
				info.put("sraSbidAm",		sraSbidAm);
				info.put("sraSbidUpr",		sraSbidUpr);
				info.put("selStsDsc",		"22");
				info.put("lsCmeno",			"SYSTEM");

				// 5. 낙찰 정보 업데이트
				int cnt = auctionDAO.updateAuctionResult(info);
				if (cnt == 0) {
					continue;
				}
				
				// 5. 기등록 수수료 정보 삭제
				auctionDAO.deleteFeeInfo(info);
				
				// 6. 수수료 기본 정보 조회
				final List<Map<String, Object>> feeInfoList = auctionDAO.selectFeeInfo(params);
				
				// 7. 수수료 저장
				for (Map<String, Object> feeInfo : feeInfoList) {
					feeInfo.put("AUC_DT",		aucDt);
					feeInfo.put("AUC_OBJ_DSC",	aucObjDsc);
					feeInfo.put("OSLP_NO",		oslpNo);
					feeInfo.put("LED_SQNO",		ledSqno);
					
					// 중도매인, 출하주의 조합원 여부
					String macoYn = ("1".equals(feeInfo.get("FEE_APL_OBJ_C"))) ? trmnMacoYn : fhsMacoYn;
					
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
		}
		
		// 8. 낙찰 상태가 아닌 출장우 로그 저장
		params.put("newCntAucYn", "Y");
		params.put("soldChkYn", "N");
		params.put("fsrgmnEno", "admin");
		params.put("lsCmeno", "[LM0314]");
		params.put("pdaId", "새 차수 경매 시작[성공]");
		auctionDAO.insertAuctSogCowLog(params);

		// 9. 낙찰 상태 출장우 로그 저장
		params.put("newCntAucYn", "N");
		params.put("soldChkYn", "Y");
		params.put("pdaId", "경매종료[낙찰]");
		auctionDAO.insertAuctSogCowLog(params);

		// 10. 유찰 처리
		params.put("sraSbidAm", "0");
		params.put("lsCmeno", "SYSTEM");
		auctionDAO.updateAuctSogCowFinish(params);
		
		// 11. 유찰 상태 출장우 로그 저장
		params.put("newCntAucYn", "N");
		params.put("soldChkYn", "N");
		params.put("pdaId", "경매종료[불낙]");
		auctionDAO.insertAuctSogCowLog(params);
		
		params.put("success", true);
		
		return params;
	}
}
