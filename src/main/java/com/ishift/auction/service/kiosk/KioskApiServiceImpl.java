package com.ishift.auction.service.kiosk;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import com.ishift.auction.util.Constants;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.vo.JwtTokenVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class KioskApiServiceImpl implements KioskApiService {

	@Resource(name = "kioskApiDAO")
	private KioskApiDAO kioskApiDAO;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private SessionUtill sessionUtil;
	
	/**
	 * 출장우 정보 리스트
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public List<Map<String, Object>> selectSogCowList(Map<String, Object> params) throws SQLException {
		return kioskApiDAO.selectSogCowList(params);
	}

	/**
	 * 중도매인 인증번호 확인 프로세스
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> authNumChkProc(Map<String, Object> params) throws SQLException {
		final Map<String, Object> reMap = new HashMap<String, Object>();

		params.put("naBzplc", sessionUtil.getNaBzplc());
		params.put("usrid", sessionUtil.getUserId());
		// 오늘 일자로 authNum, naBzplc로 발급된 데이터가 있는지 확인
		final Map<String, Object> authNoInfo = (Constants.UserRole.BIDDER.equals(params.get("userRole"))) ? kioskApiDAO.selectAuthNoInfo(params) : kioskApiDAO.selectFhsAuthNoInfo(params);
		if (ObjectUtils.isEmpty(authNoInfo)) {
			reMap.put("status", HttpStatus.NO_CONTENT.value());
			reMap.put("code", "C" + HttpStatus.NO_CONTENT.value());
			reMap.put("message", "인증번호를 확인하세요.");
			return reMap;
		}
		
		// 인증번호가 일치 하는 경우 3분짜리 토큰 발급
		JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
											.auctionHouseCode(authNoInfo.get("NA_BZPLC").toString())
											.userMemNum(authNoInfo.get("USER_MEM_NUM").toString())
											.userRole(params.get("userRole").toString())
											.mbIntgNo(authNoInfo.get("MB_INTG_NO").toString())
										  .build();

//		String token = jwtTokenUtil.generateToken(jwtTokenVo, "");
		String token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
		log.info("tokenVo : {}", jwtTokenVo);
		log.info("token : {}", token);
		reMap.put("status", HttpStatus.OK.value());
		reMap.put("code", "C" + HttpStatus.OK.value());
		reMap.put("message", "인증에 성공했습니다.");
		
		if(!"".equals(token)) {
			final Map<String, Object> data = new HashMap<String, Object>();
			data.put("TOKEN", token);
			reMap.put("data", data);
		}
		
		return reMap;
	}

	/**
	 * 중도매인 경매 참가등록 프로세스
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> mwmnRegEntryProc(Map<String, Object> params) throws SQLException {
		final Map<String, Object> reMap = new HashMap<String, Object>();
		
		if (ObjectUtils.isEmpty(params.get("token"))) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", "인증에 실패했습니다.[필수인자 누락]");
			return reMap;
		}
		
		final JwtTokenVo tokenVo = jwtTokenUtil.getTokenVo(params.get("token").toString());
		// 토큰의 정보가 없는 경우 (보통 유효기간 만료) 또는 토큰의 사용자가 중도매인(BIDDER)이 아닌 경우
		if (ObjectUtils.isEmpty(tokenVo) || !Constants.UserRole.BIDDER.equals(tokenVo.getUserRole())) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", "인증에 실패했습니다.");
			return reMap;
		}

		params.put("naBzplc", tokenVo.getAuctionHouseCode());
		params.put("trmnAmnno", tokenVo.getUserMemNum());
		
		// 오늘 날짜로 등록된 참가 번호가 있는지 확인
		final List<Map<String, Object>> entryList = kioskApiDAO.selectEntryList(params);
		if (!ObjectUtils.isEmpty(entryList)) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", String.format("이미 참가 등록된 번호입니다.[참가번호 : %s]", entryList.get(0).get("LVST_AUC_PTC_MN_NO")));
			return reMap;
		}
		
		int insertNum = kioskApiDAO.insertEntryInfo(params);
		reMap.put("insertNum", insertNum);
		reMap.put("status", HttpStatus.OK.value());
		reMap.put("code", "C" + HttpStatus.OK.value());
		reMap.put("message", String.format("참가 등록에 성공했습니다.[참가번호 : %s]", params.get("lvstAucPtcMnNo")));
		
		return reMap;
	}

	/**
	 * 중도매인 정산내역
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectMwmnStateAccInfo(Map<String, Object> params) throws SQLException {
		final Map<String, Object> reMap = new HashMap<String, Object>();
		final Map<String, Object> dataMap = new HashMap<String, Object>();
		
		if (ObjectUtils.isEmpty(params.get("token"))) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", "인증에 실패했습니다.[필수인자 누락]");
			return reMap;
		}
		
		final JwtTokenVo tokenVo = jwtTokenUtil.getTokenVo(params.get("token").toString());
		log.debug("tokenVo : {}", tokenVo);
		// 토큰의 정보가 없는 경우 (보통 유효기간 만료) 또는 토큰의 사용자가 중도매인(BIDDER)이 아닌 경우
		if (ObjectUtils.isEmpty(tokenVo) ||!Constants.UserRole.BIDDER.equals(tokenVo.getUserRole())) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", "인증에 실패했습니다.");
			return reMap;
		}
		
		params.put("naBzplc", tokenVo.getAuctionHouseCode());
		params.put("trmnAmnno", tokenVo.getUserMemNum());
		
		// 경매차수 정보
		final Map<String, Object> aucQcnInfo = kioskApiDAO.selectAucQcnInfo(params);
		if (ObjectUtils.isEmpty(aucQcnInfo)) {
			reMap.put("status", HttpStatus.NO_CONTENT.value());
			reMap.put("code", "C" + HttpStatus.NO_CONTENT.value());
			reMap.put("message", "경매 차수 정보가 없습니다.");
			return reMap;
		}
		dataMap.put("AUC_QCN_INFO", aucQcnInfo);
		
		// 낙찰 목록 조회
		final List<Map<String, Object>> buyList = kioskApiDAO.selectBuyList(params);
		if (ObjectUtils.isEmpty(buyList)) {
			reMap.put("status", HttpStatus.NO_CONTENT.value());
			reMap.put("code", "C" + HttpStatus.NO_CONTENT.value());
			reMap.put("message", "낙찰 내역이 없습니다.");
			return reMap;
		}
		dataMap.put("DETAIL_LIST", buyList);

		final Map<String, Object> wmcInfo = kioskApiDAO.selectWmcInfo(params);
		if (!ObjectUtils.isEmpty(wmcInfo) && !StringUtils.isEmpty(wmcInfo.get("SEAL_IMG_CNTN"))) {
			byte arr[] = com.ishift.auction.base.utils.StringUtils.blobToBytes((Blob)wmcInfo.get("SEAL_IMG_CNTN"));
			if(arr != null && arr.length > 0) {
				String base64Encode = "data:image/png;base64," + com.ishift.auction.base.utils.StringUtils.byteToBase64(arr);
				wmcInfo.put("SEAL_IMG_CNTN", base64Encode);
			}
		}
		// 가축시장 정보
		dataMap.put("WMC_INFO", wmcInfo);
		
		// 참가 중도매인 정보
		dataMap.put("ENTR_INFO", kioskApiDAO.selectEntrInfo(params));
		// 낙찰 정산 상세 정보
		dataMap.put("STAT_INFO", kioskApiDAO.selectStatInfo(params));
		
		reMap.put("data", dataMap);
		
		return reMap;
	}

	/**
	 * 출하자 정산내역
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectFhsStateAccInfo(Map<String, Object> params) throws SQLException {
		log.debug("params : {}", params);
		final Map<String, Object> reMap = new HashMap<String, Object>();
		final Map<String, Object> dataMap = new HashMap<String, Object>();
		
		if (ObjectUtils.isEmpty(params.get("token"))) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", "인증에 실패했습니다.[필수인자 누락]");
			return reMap;
		}
		
		final JwtTokenVo tokenVo = jwtTokenUtil.getTokenVo(params.get("token").toString());
		// 토큰의 정보가 없는 경우 (보통 유효기간 만료) 또는 토큰의 사용자가 중도매인(BIDDER)이 아닌 경우
		if (ObjectUtils.isEmpty(tokenVo) ||!Constants.UserRole.FARM.equals(tokenVo.getUserRole())) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", "인증에 실패했습니다.");
			return reMap;
		}
		
		params.put("naBzplc", tokenVo.getAuctionHouseCode());
		params.put("fhsIdNo", tokenVo.getUserMemNum().split("-")[0]);
		params.put("farmAmnno", tokenVo.getUserMemNum().split("-")[1]);
		
		// 출하 유형 조회
		final List<Map<String, Object>> selObjList = kioskApiDAO.selectAucObjDecList(params);
		if (ObjectUtils.isEmpty(selObjList)) {
			reMap.put("status", HttpStatus.NO_CONTENT.value());
			reMap.put("code", "C" + HttpStatus.NO_CONTENT.value());
			reMap.put("message", "출하 내역이 없습니다.");
			return reMap;
		}
		
		final List<Map<String, Object>> accList = new ArrayList<Map<String, Object>>();
		// 출하 유형별 상세, 통계 데이터 조회
		for (Map<String, Object> objInfo : selObjList) {
			Map<String, Object> accInfo = new HashMap<String, Object>();
			params.put("aucObjDsc", objInfo.get("AUC_OBJ_DSC"));
			accInfo.put("FHS_INFO", objInfo);
			accInfo.put("DETAIL_LIST", kioskApiDAO.selectSelList(params));
			accInfo.put("STAT_INFO", kioskApiDAO.selectSelStatInfo(params));
			accList.add(accInfo);
		}
		
		// 정산 상세 정보
		dataMap.put("ACC_LIST", accList);

		reMap.put("data", dataMap);
		
		return reMap;
	}
	
	/****************************************************** 공통함수 [s] ******************************************************/
	/**
	 * 요청 파라이터 list로 변환
	 * @param params
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> createListRequestParam(Map<String, Object> params) {
		if (params.get("data") instanceof List) {
			return (List<Map<String, Object>>)params.get("data");
		}
		
		if (params.get("data") instanceof Map) {
			List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();
			rtnList.add((Map<String, Object>)params.get("data"));
			return rtnList;
		}
		return null;
	}
	
	/******************************************************* 공통함수 [e] ******************************************************/
	

	/****************************************************** Deprecate [s] ******************************************************/
	/**
	 * 키오스크 중도매인 로그인(인증번호 발송) - 사용X
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@Deprecated
	@Override
	public Map<String, Object> mwmnLoginProc(Map<String, Object> params) throws SQLException {
		final Map<String, Object> rtnMap = new HashMap<String, Object>();
		// 필수인자 체크
		if (StringUtils.isEmpty(params.get("naBzplc"))
		 || StringUtils.isEmpty(params.get("sraMwmnnm"))
		 || StringUtils.isEmpty(params.get("cusRlno"))
		 || StringUtils.isEmpty(params.get("cusMpno")))
		{
			rtnMap.put("status", HttpStatus.BAD_REQUEST.value());
			rtnMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			rtnMap.put("message", "입력 정보를 확인하세요.");
			return rtnMap;
		}
		
		final Map<String, Object> info = kioskApiDAO.selectMwmnInfo(params);
		// 정보가 없는 경우
		if (ObjectUtils.isEmpty(info)) {
			rtnMap.put("status", HttpStatus.BAD_REQUEST.value());
			rtnMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			rtnMap.put("message", "로그인 정보가 없습니다.");
			return rtnMap;
		}
		
		// 휴면회원인 경우
		if ("1".equals(info.get("DORMACC_YN"))) {
			rtnMap.put("status", HttpStatus.BAD_REQUEST.value());
			rtnMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			rtnMap.put("message", "휴면회원입니다.관리자에게 문의하세요.");
			return rtnMap;
		}
		
		// 3분짜리 토큰 발급
		JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
										.auctionHouseCode(info.get("NA_BZPLC").toString())
										.userMemNum(info.get("TRMN_AMNNO").toString())
										.userRole(Constants.UserRole.BIDDER)
										.mbIntgNo(info.get("MB_INTG_NO").toString())
										.build();

		String token = jwtTokenUtil.generateToken(jwtTokenVo, "");
		
		// TODO :: 인증번호 발송하기...
		
		rtnMap.put("status", HttpStatus.OK.value());
		rtnMap.put("code", "C" + HttpStatus.OK.value());
		rtnMap.put("message", "인증번호 발신 성공했습니다.");
		
		final Map<String, Object> data = new HashMap<String, Object>();
		data.put("token", token);
		rtnMap.put("data", data);
		return rtnMap;
	}
	/******************************************************* Deprecate [e] ******************************************************/


}
