package com.ishift.auction.service.common;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.google.common.base.CaseFormat;
import com.ishift.auction.util.HttpUtils;
import com.ishift.auction.util.McaUtil;
import com.ishift.auction.util.SessionUtill;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@SuppressWarnings("unchecked")
public class CommonServiceImpl implements CommonService {

	@Autowired
	private CommonDAO commonDao;
	
	@Autowired
	private McaUtil mcaUtil;
	
	@Autowired
	private HttpUtils httpUtils;
	
	@Autowired
	private SessionUtill sessionUtil;
	
	/**
	 * 공통코드 조회
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@Override
	public List<Map<String, Object>> getCommonCode(Map<String, Object> params) throws SQLException {
		return commonDao.getCommonCode(params);
	}

	/**
	 * 개체 상세정보 조회(한우종합)
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> searchIndvDetails(Map<String, Object> params) {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			// 1. 한우종합에서 개체정보 조회
			final Map<String, Object> mcaMap4700	= mcaUtil.tradeMcaMsg("4700", params);				// mca 응답 전문
			final Map<String, Object> dataMap4700	= (Map<String, Object>) mcaMap4700.get("jsonData");	// 한우종합 개체정보 데이터
			
			// 2. 조회된 개체가 없는 경우 이 후 프로세스 필요x
			if (dataMap4700 == null || "0".equals(dataMap4700.getOrDefault("INQ_CN", "0"))) {
				result.put("success", false);
				result.put("message", "개체정보가 없습니다.");
				return result;
			}
			
			dataMap4700.putAll(params);
			Iterator<String> keys = dataMap4700.keySet().iterator();
			while(keys.hasNext()) {
				String key = keys.next();
				log.debug("{} : {}", key, dataMap4700.get(key).toString().trim());
			}
			
			// 3. 조회 된 개체정보 저장
			dataMap4700.put("NA_BZPLC", sessionUtil.getNaBzplc());
			dataMap4700.put("regUsrid", "MCA4700");
			this.updateIndvInfo(dataMap4700);

			// 4. 출하주 정보 저장
			this.updateFhsInfo(dataMap4700);
			
			// 5. 형매정보 저장
			// 조회한 개체정보에 어미소 개체번호가 있는 경우
			//if (!"".equals(dataMap4700.getOrDefault("MCOW_SRA_INDV_EART_NO", "").toString().trim())) {
			//	Map<String,Object> tempParam = new HashMap<>();				
			//	tempParam.put("SRA_INDV_AMNNO", dataMap4700.getOrDefault("MCOW_SRA_INDV_EART_NO","").toString().trim());
			//	tempParam.put("SRA_INDV_AMNNO_ORI", dataMap4700.getOrDefault("SRA_INDV_AMNNO","").toString().trim());
			//	this.updateIndvSibInfo(tempParam);
			//}

			// 6. 후대정보 저장
			//params.put("SRA_INDV_AMNNO", params.get("sra_indv_amnno"));
			//this.updateIndvPostInfo(params);
			
			//5. 종축개량 데이터 조회 후 해당 데이터 기준으로 MERGE
			String barcode = (String)params.get("sra_indv_amnno");
			if(barcode.length() == 15) barcode = barcode.substring(3);
			Map<String,Object> aiakInfo = httpUtils.callApiAiakMap(barcode);
			if(aiakInfo != null) this.updateIndvAiakInfo(aiakInfo);
			
			//6. 후대/ 형매 MERGE이후 산차 재계산하여 UPDATE
		}
		catch(RuntimeException re) {
			result.put("success", false);
			result.put("message", re.getMessage());
			log.error("RuntimeException::CommonServiceImpl.searchIndvDetails : {} ",re);
			return result;
		}
		catch(SQLException se) {
			result.put("success", false);
			result.put("message", se.getMessage());
			log.error("SQLException::CommonServiceImpl.searchIndvDetails : {} ",se);
			return result;
		}
		catch(Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
			log.error("Exception::CommonServiceImpl.searchIndvDetails : {} ",e);
			return result;
		}
		
		return result;
	}

	/**
	 * 개체 정보 저장
	 * @param params
	 * @throws SQLException
	 */
	@Override
	public void updateIndvInfo(Map<String, Object> params) throws SQLException, RuntimeException {
		final Map<String, Object> map = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			map.put(CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, entry.getKey()), entry.getValue());
		}
		
//		this.paramLog(map);
		
		params.putAll(map);

		// 개체정보 저장
		commonDao.updateIndvInfo(params);
		// 개체정보 로그 저장
		commonDao.insertIndvLog(params);
	}
	
	/**
	 * 출하주 정보 저장
	 * @param params
	 * @throws SQLException
	 */
	@Override
	public void updateFhsInfo(Map<String, Object> params) throws SQLException, RuntimeException {
		// 1. 출하주 정보 수정제외 항목 조회 > 가축시장 사업장 테이블(TB_LA_IS_BM_BZLOC)의 출하주 정보 수정 제외항목(SMS_BUFFER_1)
		final Map<String, Object> bzplcInfo = commonDao.getBzplcInfo(params);
		params.putAll(bzplcInfo);
		params.put("MB_INTG_GB", "02");
		params.put("MB_INTG_NM", params.getOrDefault("SRA_FHSNM", "").toString().trim());													// 출하주 이름
		params.put("MB_RLNO", params.getOrDefault("BIRTH", "").toString().trim());															// 생년월일
		params.put("MB_MPNO", params.getOrDefault("SRA_FHS_REP_MPSVNO", "").toString().trim() + 
							  params.getOrDefault("SRA_FHS_REP_MPHNO", "").toString().trim() +
							  params.getOrDefault("SRA_FHS_REP_MPSQNO", "").toString().trim());												// 휴대전화번호
		params.put("OHSE_TELNO", params.getOrDefault("SRA_FARM_AMN_ATEL", "").toString().trim() + 
								 params.getOrDefault("SRA_FARM_AMN_HTEL", "").toString().trim() +
								 params.getOrDefault("SRA_FARM_AMN_STEL", "").toString().trim());											// 자택전화번호
		params.put("ZIP", params.getOrDefault("SRA_FARM_FZIP", "").toString() + params.getOrDefault("SRA_FARM_RZIP", "").toString());		// 우편번호
		List<Map<String, Object>> list = commonDao.getIntgNoList(params);
		if(list != null && !list.isEmpty()) {
			final Map<String, Object> info = list.get(0);
			params.put("MB_INTG_NO", info.get("MB_INTG_NO"));			
			
			// 휴면 또는 탈퇴 회원이 아닌 경우 통합회원 정보 수정
			if("0".equals(info.get("DORMACC_YN")) && "0".equals(info.get("DELACC_YN"))) {
				commonDao.updateIntgInfo(params);
			}
			else if("1".equals(info.get("DORMACC_YN")) && "0".equals(info.get("DELACC_YN"))){	
				//휴면 해제
				this.updateDormcUserFhsClear(params);
			}
		}else {
			if (!"".equals(params.get("MB_INTG_NM")) && !"".equals(params.get("MB_RLNO")) && !"".equals(params.get("MB_MPNO"))) {
				commonDao.insertIntgInfo(params);
			}
		}	
		
		// 4. 출하주정보 저장
		// 수정인 경우 가축시장 사업장 테이블(TB_LA_IS_BM_BZLOC)의 출하주 정보 수정 제외항목(SMS_BUFFER_1)을 체크 후 UPDATE
		commonDao.updateFhsInfo(params);		
	}
	
	private void updateFhsInfo_bak(Map<String, Object> params) throws SQLException, RuntimeException {
		// 1. 출하주 정보 수정제외 항목 조회 > 가축시장 사업장 테이블(TB_LA_IS_BM_BZLOC)의 출하주 정보 수정 제외항목(SMS_BUFFER_1)
		final Map<String, Object> bzplcInfo = commonDao.getBzplcInfo(params);
		params.putAll(bzplcInfo);
		
		try {
			// 2. 출하주 통합회원 정보(TB_LA_IS_MM_MBINTG) 통합회원 구분(MB_INTG_GB)이 02인 데이터 조회
			// 통합회원 구분 ( 01:중도매인, 02:출하주)
			params.put("MB_INTG_GB", "02");
			params.put("MB_INTG_NM", params.getOrDefault("SRA_FHSNM", "").toString().trim());													// 출하주 이름
			params.put("MB_RLNO", params.getOrDefault("BIRTH", "").toString().trim());															// 생년월일
			params.put("MB_MPNO", params.getOrDefault("SRA_FHS_REP_MPSVNO", "").toString().trim() + 
								  params.getOrDefault("SRA_FHS_REP_MPHNO", "").toString().trim() +
								  params.getOrDefault("SRA_FHS_REP_MPSQNO", "").toString().trim());												// 휴대전화번호
			params.put("OHSE_TELNO", params.getOrDefault("SRA_FARM_AMN_ATEL", "").toString().trim() + 
									 params.getOrDefault("SRA_FARM_AMN_HTEL", "").toString().trim() +
									 params.getOrDefault("SRA_FARM_AMN_STEL", "").toString().trim());											// 자택전화번호
			params.put("ZIP", params.getOrDefault("SRA_FARM_FZIP", "").toString() + params.getOrDefault("SRA_FARM_RZIP", "").toString());		// 우편번호

			// 농가정보 테이블에서 FHS_ID_NO로 이미 등록된 통합회원 코드가 있는지 조회
			final Map<String, Object> fhsInfo = commonDao.getFhsInfo(params);
			// 통합회원 테이블에서 이름, 생년월일 휴대전화번호로 통합회원 정보 조회
			final Map<String, Object> fhsIntgNoInfo = commonDao.getIntgNoInfo(params);
			
			// 3. 농가 정보 테이블에 통합회원 코드가 있는 경우
			if (fhsInfo != null && !fhsInfo.isEmpty() && !"-1".equals(fhsInfo.get("MB_INTG_NO"))) {
				params.put("MB_INTG_NO", fhsInfo.get("MB_INTG_NO"));
				final Map<String, Object> fhsIntgNumInfo = commonDao.getIntgNoInfoForNum(params);
				
				// 휴면 또는 탈퇴 회원이 아닌 경우 통합회원 정보 수정
				if("0".equals(fhsIntgNumInfo.get("DORMACC_YN")) && "0".equals(fhsIntgNumInfo.get("DELACC_YN"))) {
					commonDao.updateIntgInfo(params);
				}
				else if("1".equals(fhsIntgNumInfo.get("DORMACC_YN")) && "0".equals(fhsIntgNumInfo.get("DELACC_YN"))){	
					//휴면 해제
					this.updateDormcUserFhsClear(params);
				}
			}
			// 4. 통합회원 정보가 없으면 통합회원 정보 추가 > 이름, 생년월일, 휴대전화번호가 있는 경우에만 통합 진행
			else if (fhsIntgNoInfo == null || fhsIntgNoInfo.isEmpty()) {
				// 통합회원 테이블에 정보가 없고 출하주 테이블에 통합회원 코드가 없는 경우에만 신규 저장
				if (!"".equals(params.get("MB_INTG_NM")) && !"".equals(params.get("MB_RLNO")) && !"".equals(params.get("MB_MPNO"))) {
					commonDao.insertIntgInfo(params);
				}
			}
			else {
				params.put("MB_INTG_NO", fhsIntgNoInfo.get("MB_INTG_NO"));
			}	
		}catch(Exception e){
			log.error("updateFhsInfo :: 출하주 추가/수정 :: 통합회원 등록",e);
		}
		
		// 4. 출하주정보 저장
		// 수정인 경우 가축시장 사업장 테이블(TB_LA_IS_BM_BZLOC)의 출하주 정보 수정 제외항목(SMS_BUFFER_1)을 체크 후 UPDATE
		commonDao.updateFhsInfo(params);
	}

	/**
	 * 형매정보 저장
	 * @param params
	 * @throws Exception
	 */
	public void updateIndvSibInfo(Map<String, Object> params) throws Exception {
		// 1. 형매정보 mca 호출
		final Map<String, Object> mcaMap			= mcaUtil.tradeMcaMsg("4900", params);
		Map<String, Object> mcaJson = (Map<String, Object>) mcaMap.get("jsonData");
		final List<Map<String, Object>> dataList	= (List<Map<String, Object>>)mcaJson.get("RPT_DATA");			// 형매정보 데이터
		
		if (!ObjectUtils.isEmpty(dataList)) {
			this.paramLog(params);
			// 2. 저장 전 데이터 삭제
			params.put("SRA_INDV_AMNNO", params.get("SRA_INDV_AMNNO_ORI"));;
			commonDao.deleteIndvSibinf(params);
			
			for (Map<String, Object> dataMap : dataList) {
				dataMap.put("SIB_SRA_INDV_AMNNO", dataMap.get("SRA_INDV_AMNNO"));
				dataMap.put("SRA_INDV_AMNNO", params.get("SRA_INDV_AMNNO_ORI"));
				commonDao.insertIndvSibinf(dataMap);
			}
		}
	}
	
	
	/**
	 * 후대정보 저장
	 * @param params
	 * @throws Exception
	 */
	public void updateIndvPostInfo(Map<String, Object> params) throws Exception {
		// 1. 후대정보 mca 호출
		final Map<String, Object> mcaMap			= mcaUtil.tradeMcaMsg("4900", params);		
		Map<String, Object> mcaJson = (Map<String, Object>) mcaMap.get("jsonData");
		final List<Map<String, Object>> dataList	= (List<Map<String, Object>>)mcaJson.get("RPT_DATA");		// 후대정보 데이터
		
		if (!ObjectUtils.isEmpty(dataList)) {
			this.paramLog(params);
			// 2. 저장 전 데이터 삭제
			commonDao.deleteIndvPostinf(params);
			
			for (Map<String, Object> dataMap : dataList) {
				dataMap.put("POST_SRA_INDV_AMNNO", dataMap.get("SRA_INDV_AMNNO"));
				dataMap.put("SRA_INDV_AMNNO", params.get("SRA_INDV_AMNNO"));
				commonDao.insertIndvPostinf(dataMap);
			}
		}
	}
	
	/**
	 * 휴면회원 해제
	 * @param params
	 * @throws Exception
	 */
	@Override
	public void updateDormcUserFhsClear(Map<String, Object> params) throws SQLException, RuntimeException{
		
		this.insertDormBackupData(params);
		this.updateDormBackupData(params);
		this.deleteDormBackupData(params);
		
	}

	private void insertDormBackupData(Map<String, Object> params) throws SQLException, RuntimeException{
		//MBINTGHIS 통합회원 데이터 INSERT
		commonDao.insertMbintgHistoryData(params);
		
		//MI_MWMN 중도매인 이력 테이블 INSERT, 농가는 없음
		if("01".equals(params.get("MB_INTG_GB"))) {
			commonDao.insertMwmnHistoryData(params);
		}
	}

	private void updateDormBackupData(Map<String, Object> params) throws SQLException, RuntimeException{
		//BK_DORM_MBINTG -> MM_MBINTG 로 백업 데이터 UPDATE 하기
		commonDao.updateDormMbintgBackupData(params);
		
		if("01".equals(params.get("MB_INTG_GB"))) {
			//BK_DORM_MWMN -> MM_MWMN 
			commonDao.updateDormMwmnBackupData(params);
		}else {
			//BK_DORM_FHS -> MM_FHS
			commonDao.updateDormFhsBackupData(params);
		}
		
	}
	
	private void deleteDormBackupData(Map<String, Object> params) throws SQLException, RuntimeException{
		//BK 테이블 삭제하기
		commonDao.deleteDormMbintgBackupData(params);
		
		if("01".equals(params.get("MB_INTG_GB"))) {
			commonDao.deleteDormMwmnBackupData(params);
		}else {
			commonDao.deleteDormFhsBackupData(params);
		}
	}
	
	/**
	 * Map 데이터 확인용
	 * @param map
	 */
	public void paramLog(Map<String, Object> map) {
		Iterator<String> keys = map.keySet().iterator();
		while(keys.hasNext()) {
			String key = keys.next();
			log.debug("{} : {}", key, map.getOrDefault(key, "").toString().trim());
		}
	}


	public void callIndvAiakInfo(String barcode) throws SQLException {		
		if(barcode != null && barcode.length() == 15) {
			barcode = barcode.substring(3);
		}	
		Map<String,Object> aiakInfo = httpUtils.callApiAiakMap(barcode);
		if(aiakInfo != null) this.updateIndvAiakInfo(aiakInfo);
	}

	private void updateIndvAiakInfo(Map<String, Object> map) throws SQLException {
		List<Map<String,Object>> postArr = (List<Map<String, Object>>) map.get("postInfo");
		List<Map<String,Object>> sibArr = (List<Map<String, Object>>) map.get("sibInfo");
		commonDao.updatetIndvAiakInfo(map);
		for(Map<String,Object> postMap : postArr) {
			commonDao.updatetIndvAiakPostInfo(postMap);			
		}
		for(Map<String,Object> sibMap : sibArr) {
			commonDao.updatetIndvAiakSibInfo(sibMap);	
		}
		commonDao.updateIndvSibMatime(map);
		commonDao.updateIndvPostMatime(map);
	}
	
	public List<Map<String, Object>> selectBloodInfo(Map<String, Object> params) throws SQLException{
		return commonDao.selectBloodInfo(params);
	}

	public List<Map<String, Object>> selectIndvPost(Map<String, Object> params) throws SQLException{
		return commonDao.selectIndvPost(params);		
	}

	public List<Map<String, Object>> selectIndvSib(Map<String, Object> params) throws SQLException{
		return commonDao.selectIndvSib(params);		
	}
}
