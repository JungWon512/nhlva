package com.ishift.auction.service.kiosk;

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
	public Map<String, Object> mwmnAuthNumProc(Map<String, Object> params) throws SQLException {
		final Map<String, Object> reMap = new HashMap<String, Object>();

		params.put("naBzplc", sessionUtil.getNaBzplc());
		params.put("usrid", sessionUtil.getUserId());
		// 오늘 일자로 authNum, naBzplc로 발급된 데이터가 있는지 확인
		final Map<String, Object> authNoInfo = kioskApiDAO.selectAuthNoInfo(params);
		if (ObjectUtils.isEmpty(authNoInfo)) {
			reMap.put("status", HttpStatus.NO_CONTENT.value());
			reMap.put("code", "C" + HttpStatus.NO_CONTENT.value());
			reMap.put("message", "인증번호를 확인하세요.");
			return reMap;
		}
		
		// 인증번호가 있는 경우 3분짜리 토큰 발급
		JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
				.auctionHouseCode(authNoInfo.get("NA_BZPLC").toString())
				.userMemNum(authNoInfo.get("TRMN_AMNNO").toString())
				.userRole(Constants.UserRole.BIDDER)
				.mbIntgNo(authNoInfo.get("MB_INTG_NO").toString())
				.build();

		String token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);

		reMap.put("status", HttpStatus.OK.value());
		reMap.put("code", "C" + HttpStatus.OK.value());
		reMap.put("message", "인증에 성공했습니다.");
		
		if(!"".equals(token)) {
			final Map<String, Object> data = new HashMap<String, Object>();
			data.put("token", token);
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
			reMap.put("message", "토큰 인증에 실패했습니다.[필수인자 누락]");
			return reMap;
		}
		
		final JwtTokenVo tokenVo = jwtTokenUtil.getTokenVo(params.get("token").toString());
		// 토큰의 정보가 없는 경우 (보통 유효기간 만료) 또는 토큰의 사용자가 중도매인(BIDDER)이 아닌 경우
		if (ObjectUtils.isEmpty(tokenVo) || !Constants.UserRole.BIDDER.equals(tokenVo.getUserRole())) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", "토큰 인증에 실패했습니다.[토큰정보 만료]");
			return reMap;
		}

		params.put("naBzplc", tokenVo.getAuctionHouseCode());
		params.put("trmnAmnno", tokenVo.getUserMemNum());
		log.debug("params : {}", params);
		// 오늘 날짜로 등록된 참가 번호가 있는지 확인
		final List<Map<String, Object>> entryList = kioskApiDAO.selectEntryList(params);
		log.debug("entryList : {}", entryList);
		if (!ObjectUtils.isEmpty(entryList)) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", String.format("이미 참가등록된 번호입니다.[참가번호 : %s]", entryList.get(0).get("LVST_AUC_PTC_MN_NO")));
			return reMap;
		}
		
		int insertNum = kioskApiDAO.insertEntryInfo(params);
		reMap.put("insertNum", insertNum);
		
		return reMap;
	}

	/**
	 * 중도매인 정산내역
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectStateAccInfo(Map<String, Object> params) throws SQLException {
		final Map<String, Object> reMap = new HashMap<String, Object>();
		
		if (ObjectUtils.isEmpty(params.get("token"))) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", "토큰 인증에 실패했습니다.[필수인자 누락]");
			return reMap;
		}
		
		final JwtTokenVo tokenVo = jwtTokenUtil.getTokenVo(params.get("token").toString());
		// 토큰의 정보가 없는 경우 (보통 유효기간 만료) 또는 토큰의 사용자가 중도매인(BIDDER)이 아닌 경우
		if (ObjectUtils.isEmpty(tokenVo) ||!Constants.UserRole.BIDDER.equals(tokenVo.getUserRole())) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", "토큰 인증에 실패했습니다.");
			return reMap;
		}
		
		params.put("naBzplc", tokenVo.getAuctionHouseCode());
		params.put("trmnAmnno", tokenVo.getUserMemNum());
		
		// TODO :: 조합 정보
		// TODO :: 참가 중도매인 정보
		// TODO :: 상세 리스트
		// TODO :: 낙찰통계리스트 
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
