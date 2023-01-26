package com.ishift.auction.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ishift.auction.configuration.security.token.AdminUserAuthenticationToken;
import com.ishift.auction.service.kiosk.KioskApiService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.vo.AdminJwtTokenVo;
import com.ishift.auction.vo.AdminUserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/kiosk/api")
@SuppressWarnings({"unused", "unchecked"})
public class KioskApiController<T> {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private KioskApiService kioskApiService;
	
	/**
	 * 관리자 로그인
	 * @param version
	 * @param params
	 * @return
	 */
	@PostMapping(value = "/{version}/auth/login"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> adminLogin(@PathVariable(name = "version") final String version
									,@RequestBody Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			final Authentication authentication = authenticationManager.authenticate(
																		new AdminUserAuthenticationToken(
																				params.getOrDefault("usrid", "").toString()
																				, params.getOrDefault("pw", "").toString()
																				, null));

			final AdminUserDetails adminUserDetails = (AdminUserDetails)authentication.getPrincipal();
			
			if (ObjectUtils.isEmpty(adminUserDetails)) {
				result.put("status", HttpStatus.BAD_REQUEST.value());
				result.put("code", "C" + HttpStatus.BAD_REQUEST.value());
				result.put("message", "입력하신 정보가 없습니다.");
				return result;
			}

			SecurityContextHolder.getContext().setAuthentication(authentication);

			final AdminJwtTokenVo jwtTokenVo = AdminJwtTokenVo.builder()
																.userMemNum(adminUserDetails.getUsername())
																.auctionHouseCode(adminUserDetails.getNaBzplc())
																.userRole(Constants.UserRole.ADMIN)
																.userId(adminUserDetails.getUsername())
																.password(passwordEncoder.encode(adminUserDetails.getUsername()))
																.eno(adminUserDetails.getEno())
																.userCusName(adminUserDetails.getUsrnm())
																.na_bzplc(adminUserDetails.getNaBzplc())
																.security("security")
																.na_bzplnm(adminUserDetails.getNaBzplNm())
																.grp_c(adminUserDetails.getGrpC())
																.build();

			final Map<String, Object> data = new HashMap<String, Object>();

			result.put("status", HttpStatus.OK.value());
			result.put("code", "C" + HttpStatus.OK.value());
			result.put("message", "토큰 발급 성공했습니다.");

			data.put("ACCESS_TOKEN", jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN));
			data.put("NA_BZPLC", adminUserDetails.getNaBzplc());
			result.put("data", data);
			return result;
		}
		catch (RuntimeException re) {
			result.put("status", HttpStatus.BAD_REQUEST.value());
			result.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			result.put("message", re.getMessage());
			return result;
		}
	}
	
	/**
	 * 출장우 리스트 조회 ( 사용자 인증 필요 x )
	 * @param params
	 * @return
	 */
	@GetMapping(value = "/{version}/sog-cow")
	public Map<String, Object> sogCow(@RequestParam Map<String, Object> params) {
		try {
			final List<Map<String, Object>> list = kioskApiService.selectSogCowList(params);
			return this.createResultSetListData(list);
		}
		catch (RuntimeException | SQLException e) {
			return this.createResultSetListData(null);
		}
	}


	/**
	 * 중도매인 인증번호(6자리) 확인
	 * @param params
	 * @return
	 */
	@PostMapping(value = "/{version}/mwmn/auth-num"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> mwmnAuthNum(@RequestBody Map<String, Object> params) {
		try {
			params.put("userRole", Constants.UserRole.BIDDER);
			final Map<String, Object> result = kioskApiService.authNumChkProc(params);
			return this.createResultCUD(result);
		}
		catch (RuntimeException | SQLException e) {
			return this.createResultCUD(null);
		}
	}
	
	/**
	 * 중도매인 참가번호 등록
	 * @param params
	 * @return
	 */
	@PostMapping(value = "/{version}/mwmn/entry"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> mwmnEntry(@RequestBody Map<String, Object> params) {
		try {
			final Map<String, Object> result = kioskApiService.mwmnRegEntryProc(params);
			return this.createResultCUD(result);
		}
		catch (RuntimeException | SQLException e) {
			return this.createResultCUD(null);
		}
	}
	
	/**
	 * 중도매인 정산 정보
	 * @param params
	 * @return
	 */
	@PostMapping(value = "/{version}/mwmn/state-acc"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> mwmnStateAcc(@RequestBody Map<String, Object> params) {
		try {
			final Map<String, Object> info = kioskApiService.selectMwmnStateAccInfo(params);
			return this.createResultSetMapData(info);
		}
		catch (RuntimeException | SQLException e) {
			e.printStackTrace();
			return this.createResultSetMapData(null);
		}
	}
	
	/**
	 * 출하주 인증번호(6자리) 확인
	 * @param params
	 * @return
	 */
	@PostMapping(value = "/{version}/fhs/auth-num"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> fhsAuthNum(@RequestBody Map<String, Object> params) {
		try {
			params.put("userRole", Constants.UserRole.FARM);
			final Map<String, Object> result = kioskApiService.authNumChkProc(params);
			return this.createResultCUD(result);
		}
		catch (RuntimeException | SQLException e) {
			return this.createResultCUD(null);
		}
	}
	
	/**
	 * 출하주 정산 정보
	 * @param params
	 * @return
	 */
	@PostMapping(value = "/{version}/fhs/state-acc"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> fhsStateAcc(@RequestBody Map<String, Object> params) {
		try {
			final Map<String, Object> info = kioskApiService.selectFhsStateAccInfo(params);
			return this.createResultSetMapData(info);
		}
		catch (RuntimeException | SQLException e) {
			e.printStackTrace();
			return this.createResultSetMapData(null);
		}
	}
	
	/************************************************************************ 공통 함수 [s] ************************************************************************/
	
	/**
	 * 조회 API 응답 생성
	 * @param list
	 * @return
	 */
	public Map<String, Object> createResultSetListData(List<Map<String, Object>> list){
		//데이터 암호화해서 result 추가, 상태코드 추가, 조회 count 추가
		final Map<String, Object> reMap = new HashMap<String, Object>();
		
		if (list == null) {
			reMap.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
			reMap.put("code", "C" + HttpStatus.INTERNAL_SERVER_ERROR.value());
			reMap.put("message", "조회에 실패했습니다. 관리자에게 문의하세요.");
			return reMap;
		}
		
		// 조회 결과가 0건일 경우 201 리턴
		if(list.size() < 1) {
			reMap.put("status", HttpStatus.NO_CONTENT.value());
			reMap.put("code", "C" + HttpStatus.NO_CONTENT.value());
			reMap.put("message", "조회된 내역이 없습니다.");
			return reMap;
		}
		
		reMap.put("status", HttpStatus.OK.value());
		reMap.put("code", "C" + HttpStatus.OK.value());
		reMap.put("message", "조회에 성공했습니다.");
		reMap.put("data", list);
		
		return reMap;
	}
	
	/**
	 * 조회 API 응답 생성
	 * @param list
	 * @return
	 */
	public Map<String, Object> createResultSetMapData(Map<String, Object> map){
		//데이터 암호화해서 result 추가, 상태코드 추가, 조회 count 추가
		final Map<String, Object> reMap = new HashMap<String, Object>();
		
		if (ObjectUtils.isEmpty(map)) {
			reMap.put("status", HttpStatus.NO_CONTENT.value());
			reMap.put("code", "C" + HttpStatus.NO_CONTENT.value());
			reMap.put("message", "조회된 내역이 없습니다.");
			return reMap;
		}
		
		if (map.containsKey("status") && map.containsKey("code") && map.containsKey("message")) {
			reMap.put("status", map.get("status"));
			reMap.put("code", map.get("code"));
			reMap.put("message", map.get("message"));
			if (!ObjectUtils.isEmpty(map.get("data"))) {
				reMap.put("data", map.get("data"));
			}
			return reMap;
		}
		
		reMap.put("status", HttpStatus.OK.value());
		reMap.put("code", "C" + HttpStatus.OK.value());
		reMap.put("message", "조회에 성공했습니다.");
		if (!ObjectUtils.isEmpty(map.get("data"))) {
			reMap.put("data", map.get("data"));
		}
		
		return reMap;
	}

	/**
	 * 저장 API 응답 생성
	 * @param map
	 * @return
	 */
	public Map<String, Object> createResultCUD(Map<String, Object> map) {
		final Map<String, Object> reMap = new HashMap<String, Object>();
		if(ObjectUtils.isEmpty(map)) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", "저장된 내역이 없습니다");
			return reMap;
		}
		
		if (map.containsKey("status") && map.containsKey("code") && map.containsKey("message")) {
			reMap.put("status", map.get("status"));
			reMap.put("code", map.get("code"));
			reMap.put("message", map.get("message"));
			if (!ObjectUtils.isEmpty(map.get("data"))) {
				reMap.put("data", map.get("data"));
			}
			return reMap;
		}

		if(!map.containsKey("insertNum") && !map.containsKey("updateNum") && !map.containsKey("deleteNum")) {
			reMap.put("status", HttpStatus.NO_CONTENT.value());
			reMap.put("code", "C" + HttpStatus.NO_CONTENT.value());
			reMap.put("message", map.getOrDefault("message", "저장된 내역이 없습니다"));
			return reMap;
		}

		//service에서 insertNum, updateNum, deleteNum 값 put 해줌
		if(map.containsKey("insertNum")) {
			reMap.put("insertNum", map.get("insertNum"));
			reMap.put("status", HttpStatus.OK.value());
			reMap.put("code", "C" + 201);
			reMap.put("message", map.getOrDefault("message", "저장에 성공했습니다."));
			return reMap;
		}
		
		if(map.containsKey("updateNum")) {
			reMap.put("updateNum", map.get("updateNum"));
			reMap.put("status", HttpStatus.OK.value());
			reMap.put("code", "C" + HttpStatus.OK.value());
			reMap.put("message", map.getOrDefault("message", "수정에 성공했습니다."));
			return reMap;
		}
		
		if(map.containsKey("deleteNum")) {
			reMap.put("deleteNum", map.get("deleteNum"));
			reMap.put("status", HttpStatus.OK.value());
			reMap.put("code", "C" + HttpStatus.OK.value());
			reMap.put("message", map.getOrDefault("message", "삭제에 성공했습니다."));
			return reMap;
		}
		
		return reMap;
		
	}
	
	/**
	 * 조회 API 응답 생성
	 * @param list
	 * @return
	 */
	public ResponseEntity<T> createResponseEntityListData(List<Map<String, Object>> list){
		//데이터 암호화해서 result 추가, 상태코드 추가, 조회 count 추가
		final Map<String, Object> reMap = new HashMap<String, Object>();
		
		if (list == null) {
			reMap.put("message", "조회된 내역이 없습니다.");
			return (ResponseEntity<T>)ResponseEntity.status(HttpStatus.NO_CONTENT);
		}
		
		if (list.size() < 1) {
			reMap.put("message", "조회된 내역이 없습니다.");
			return (ResponseEntity<T>)ResponseEntity.status(HttpStatus.NO_CONTENT);
		}
		
		reMap.put("message", "조회에 성공했습니다.");
		reMap.put("data", list);
		return (ResponseEntity<T>)ResponseEntity.status(HttpStatus.OK).body(reMap);
	}
	
	/**
	 * 저장 API 응답 생성
	 * @param map
	 * @return
	 */
	public ResponseEntity<T> createResponseEntityCUD(Map<String, Object> map) {
		final Map<String, Object> reMap = new HashMap<String, Object>();
		if(ObjectUtils.isEmpty(map)) {
			reMap.put("message", "저장된 내역이 없습니다");
			return (ResponseEntity<T>)ResponseEntity.status(HttpStatus.NO_CONTENT);
		}
		
		if(!map.containsKey("insertNum") && !map.containsKey("updateNum") && !map.containsKey("deleteNum")) {
			reMap.put("message", "저장된 내역이 없습니다");
			return (ResponseEntity<T>)ResponseEntity.status(HttpStatus.NO_CONTENT);
		}
		
		//service에서 insertNum, updateNum, deleteNum 값 put 해줌
		if(map.containsKey("insertNum")) {
			reMap.put("insertNum", map.get("insertNum"));
			reMap.put("message", "저장에 성공했습니다.");
			return (ResponseEntity<T>)ResponseEntity.status(HttpStatus.CREATED).body(reMap);
		}
		
		if(map.containsKey("updateNum")) {
			reMap.put("updateNum", map.get("updateNum"));
			reMap.put("message", "저장에 성공했습니다.");
			return (ResponseEntity<T>)ResponseEntity.status(HttpStatus.CREATED).body(reMap);
		}
		
		if(map.containsKey("deleteNum")) {
			reMap.put("deleteNum", map.get("deleteNum"));
			reMap.put("message", "삭제에 성공했습니다.");
			return (ResponseEntity<T>)ResponseEntity.status(HttpStatus.CREATED).body(reMap);
		}
		
		return (ResponseEntity<T>)ResponseEntity.status(HttpStatus.CREATED).body(reMap);
		
	}
	
	/************************************************************************ 공통 함수 [e] ************************************************************************/
	
	/************************************************************************ Deprecated [e] ************************************************************************/
	
	/**
	 * 중도매인 로그인 + 휴대전화 인증번호 발송(1회용)
	 * @param params
	 * @return
	 */
	@Deprecated
	@PostMapping(value = "/{version}/mwmn/login"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> mwmnLogin(@RequestBody Map<String, Object> params) {
		try {
			final Map<String, Object> login = kioskApiService.mwmnLoginProc(params);
			return this.createResultCUD(login);
		}
		catch (SQLException se) {
			return this.createResultCUD(null);
		}
		catch (Exception e) {
			return this.createResultCUD(null);
		}
	}
	/************************************************************************ Deprecated [e] ************************************************************************/
}
