package com.ishift.auction.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.ishift.auction.configuration.security.token.AdminUserAuthenticationToken;
import com.ishift.auction.service.deamon.DaemonApiService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.vo.AdminJwtTokenVo;
import com.ishift.auction.vo.AdminUserDetails;

/**
 * 데몬 호출 api controller
 * @author iShift
 */
@RestController
@RequestMapping("/daemon/api")
public class DaemonApiController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private DaemonApiService daemonApiService;

	/************************************************************************ 조회 API [s] ************************************************************************/

	/**
	 * 관리자 로그인
	 * @param version
	 * @param params
	 * @return
	 */
	@PostMapping(value = "/{version}/auth/login"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> login(@PathVariable(name = "version") final String version
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
	 * TB_LA_IS_MH_AUC_QCN : 경매 차수 정보 리스트
	 * @param naBzplc : 조합코드
	 * @param aucDt : 경매일자
	 * @param timestamp : 타임스탬프
	 * @return
	 * @throws SQLException 
	 */
	@ResponseBody
	@GetMapping("/{version}/auc-qcn/{naBzplc}/{aucDt}")
	public Map<String, Object> aucqcn(@PathVariable final Map<String, Object> params
									, @RequestParam(name = "chgDtm", required = false) String chgDtm) {
		
		try {
			if (!"".equals(chgDtm)) {
				params.put("chgDtm", chgDtm);
			}
			final List<Map<String, Object>> list = daemonApiService.selectAucQcnList(params);
			return this.createResultSetListData(list);
		}
		catch(SQLException e) {
			return this.createResultSetListData(null);
		}
	}
	
	/**
	 * TB_LA_IS_MH_SOG_COW : 출장우 정보 리스트
	 * @param naBzplc : 조합코드
	 * @param aucDt : 경매일자
	 * @param timestamp : 타임스탬프
	 * @return
	 * @throws SQLException 
	 */
	@GetMapping("/{version}/sog-cow/{naBzplc}/{aucDt}")
	public Map<String, Object> sogcow(@PathVariable final Map<String, Object> params
									, @RequestParam(name = "chgDtm", required = false) String chgDtm) {
		
		try {
			if (!"".equals(chgDtm)) {
				params.put("chgDtm", chgDtm);
			}
			final List<Map<String, Object>> list = daemonApiService.selectSogCowList(params);
			return this.createResultSetListData(list);
		}
		catch(SQLException e) {
			return this.createResultSetListData(null);
		}
	}
	
	/**
	 * TB_LA_IS_MH_CALF : 송아지 정보 리스트
	 * @param naBzplc : 조합코드
	 * @param aucDt : 경매일자
	 * @param timestamp : 타임스탬프
	 * @return
	 * @throws SQLException 
	 */
	@GetMapping("/{version}/calf/{naBzplc}/{aucDt}")
	public Map<String, Object> calf(@PathVariable final Map<String, Object> params
								, @RequestParam(name = "chgDtm", required = false) String chgDtm) {
		
		try {
			if (!"".equals(chgDtm)) {
				params.put("chgDtm", chgDtm);
			}
			final List<Map<String, Object>> list = daemonApiService.selectCalfList(params);
			return this.createResultSetListData(list);
		}
		catch(SQLException e) {
			return this.createResultSetListData(null);
		}
	}
	
	/**
	 * TB_LA_IS_MM_INDV : 개체 정보 리스트
	 * @param naBzplc : 조합코드
	 * @param aucDt : 경매일자
	 * @param timestamp : 타임스탬프
	 * @return
	 * @throws SQLException 
	 */
	@GetMapping("/{version}/indv/{naBzplc}/{aucDt}")
	public Map<String, Object> indv(@PathVariable final Map<String, Object> params
								, @RequestParam(name = "chgDtm", required = false) String chgDtm) {
		
		try {
			if (!"".equals(chgDtm)) {
				params.put("chgDtm", chgDtm);
			}
			final List<Map<String, Object>> list = daemonApiService.selectIndvList(params);
			return this.createResultSetListData(list);
		}
		catch(SQLException e) {
			return this.createResultSetListData(null);
		}
	}
	
	/**
	 * TB_LA_IS_MH_AUC_ENTR : 경매 참가자 정보 리스트
	 * @param naBzplc : 조합코드
	 * @param aucDt : 경매일자
	 * @param timestamp : 타임스탬프
	 * @return
	 * @throws SQLException 
	 */
	@GetMapping("/{version}/auc-entr/{naBzplc}/{aucDt}")
	public Map<String, Object> aucentr(@PathVariable final Map<String, Object> params
									, @RequestParam(name = "chgDtm", required = false) String chgDtm) {
		
		try {
			if (!"".equals(chgDtm)) {
				params.put("chgDtm", chgDtm);
			}
			final List<Map<String, Object>> list = daemonApiService.selectAucEntrList(params);
			return this.createResultSetListData(list);
		}
		catch(SQLException e) {
			return this.createResultSetListData(null);
		}
	}
	
	/**
	 * TB_LA_IS_MH_FEE : 경매 수수료 정보 리스트
	 * @param naBzplc : 조합코드
	 * @param aucDt : 경매일자
	 * @param timestamp : 타임스탬프
	 * @return
	 * @throws SQLException 
	 */
	@GetMapping("/{version}/fee/{naBzplc}/{aucDt}")
	public Map<String, Object> fee(@PathVariable final Map<String, Object> params
								, @RequestParam(name = "chgDtm", required = false) String chgDtm) {
		
		try {
			if (!"".equals(chgDtm)) {
				params.put("chgDtm", chgDtm);
			}
			final List<Map<String, Object>> list = daemonApiService.selectFeeList(params);
			return this.createResultSetListData(list);
		}
		catch(SQLException e) {
			return this.createResultSetListData(null);
		}
	}
	
	/**
	 * TB_LA_IS_MM_FHS : 출하주 정보 리스트
	 * @param naBzplc : 조합코드
	 * @param aucDt : 경매일자
	 * @param timestamp : 타임스탬프
	 * @return
	 * @throws SQLException 
	 */
	@GetMapping("/{version}/fhs/{naBzplc}/{aucDt}")
	public Map<String, Object> fhs(@PathVariable final Map<String, Object> params
								, @RequestParam(name = "chgDtm", required = false) String chgDtm) {
		
		try {
			if (!"".equals(chgDtm)) {
				params.put("chgDtm", chgDtm);
			}
			final List<Map<String, Object>> list = daemonApiService.selectFhsList(params);
			return this.createResultSetListData(list);
		}
		catch(SQLException e) {
			return this.createResultSetListData(null);
		}
	}
	
	/**
	 * TB_LA_IS_MM_MWMN : 중도매인 정보 리스트
	 * @param naBzplc : 조합코드
	 * @param aucDt : 경매일자
	 * @param timestamp : 타임스탬프
	 * @return
	 * @throws SQLException 
	 */
	@GetMapping("/{version}/mwmn/{naBzplc}/{aucDt}")
	public Map<String, Object> mwmn(@PathVariable final Map<String, Object> params
								, @RequestParam(name = "chgDtm", required = false) String chgDtm) {
		
		try {
			if (!"".equals(chgDtm)) {
				params.put("chgDtm", chgDtm);
			}
			final List<Map<String, Object>> list = daemonApiService.selectMwmnList(params);
			return this.createResultSetListData(list);
		}
		catch(SQLException e) {
			return this.createResultSetListData(null);
		}
	}
	
	/**
	 * TB_LA_IS_MH_FEE_IMPS : 낙/유찰 수수료 정보 리스트(테스트용)
	 * @param naBzplc : 조합코드
	 * @param aucDt : 경매일자
	 * @param timestamp : 타임스탬프
	 * @return
	 * @throws SQLException 
	 */
	@GetMapping("/{version}/fee-imps/{naBzplc}/{aucDt}")
	public Map<String, Object> feeimps(@PathVariable final Map<String, Object> params
									, @RequestParam(name = "chgDtm", required = false) String chgDtm) {
		
		try {
			if (!"".equals(chgDtm)) {
				params.put("chgDtm", chgDtm);
			}
			final List<Map<String, Object>> list = daemonApiService.selectFeeImpsList(params);
			return this.createResultSetListData(list);
		}
		catch(SQLException e) {
			return this.createResultSetListData(null);
		}
	}
	
	/**
	 * TB_LA_IS_MM_ATDR_LOG : 응찰 정보 리스트(테스트용)
	 * @param naBzplc : 조합코드
	 * @param aucDt : 경매일자
	 * @param timestamp : 타임스탬프
	 * @return
	 * @throws SQLException 
	 */
	@GetMapping("/{version}/atdr-log/{naBzplc}/{aucDt}")
	public Map<String, Object> atdrlog(@PathVariable final Map<String, Object> params
									, @RequestParam(name = "chgDtm", required = false) String chgDtm) {
		
		try {
			if (!"".equals(chgDtm)) {
				params.put("chgDtm", chgDtm);
			}
			final List<Map<String, Object>> list = daemonApiService.selectAtdrLogList(params);
			return this.createResultSetListData(list);
		}
		catch(SQLException e) {
			return this.createResultSetListData(null);
		}
	}
	
	/************************************************************************ 조회 API [e] ************************************************************************/
	
	/************************************************************************ 저장 API [s] ************************************************************************/
	/**
	 * TB_LA_IS_MH_SOG_COW : 출장우 정보 리스트 수정
	 * @param params : 변경된 출장우 정보 리스트
	 * @return
	 * @throws SQLException 
	 */
	@PostMapping("/{version}/sog-cow")
	public Map<String, Object> updSogcow(@PathVariable("version") String version
									, @RequestBody Map<String, Object> params) {
 		try {
			final Map<String, Object> rtnMap = daemonApiService.updSogcow(params);
			return this.createResultCUD(rtnMap);
		}
		catch(SQLException e) {
			return this.createResultCUD(null);
		}
	}
	
	/**
	 * TB_LA_IS_MH_FEE_IMPS : 낙/유찰 수수료 정보 저장
	 * @param params : 낙/유찰 수수료 정보 리스트
	 * @return
	 * @throws SQLException 
	 */
	@PostMapping("/{version}/fee-imps")
	public Map<String, Object> feeImps(@PathVariable("version") String version
									, @RequestBody Map<String, Object> params) {
		try {
			final Map<String, Object> rtnMap = daemonApiService.updFeeImps(params);
			return this.createResultCUD(rtnMap);
		}
		catch(SQLException e) {
			return this.createResultCUD(null);
		}
	}
	
	/**
	 * TB_LA_IS_MH_ATDR_LOG : 응찰 정보 리스트 저장
	 * @param params : 변경된 출장우 정보 리스트
	 * @return
	 * @throws SQLException 
	 */
	@PostMapping("/{version}/atdr-log")
	public Map<String, Object> atdrLog(@PathVariable("version") String version
									, @RequestBody Map<String, Object> params) {
		try {
			final Map<String, Object> rtnMap = daemonApiService.insAtdrLog(params);
			return this.createResultCUD(rtnMap);
		}
		catch(SQLException e) {
			return this.createResultCUD(null);
			
		}
	}
	
	/************************************************************************ 저장 API [e] ************************************************************************/
	
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
	 * 저장 API 응답 생성
	 * @param map
	 * @return
	 */
	public Map<String, Object> createResultCUD(Map<String, Object> map) {
		Map<String, Object> reMap = new HashMap<String, Object>();
		if(ObjectUtils.isEmpty(map)) {
			reMap.put("status", HttpStatus.BAD_REQUEST.value());
			reMap.put("code", "C" + HttpStatus.BAD_REQUEST.value());
			reMap.put("message", "저장된 내역이 없습니다");
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
			reMap.put("message", "저장에 성공했습니다.");
			return reMap;
		}
		
		if(map.containsKey("updateNum")) {
			reMap.put("updateNum", map.get("updateNum"));
			reMap.put("status", HttpStatus.OK.value());
			reMap.put("code", "C" + HttpStatus.OK.value());
			reMap.put("message", "수정에 성공했습니다.");
			return reMap;
		}
		
		if(map.containsKey("deleteNum")) {
			reMap.put("deleteNum", map.get("deleteNum"));
			reMap.put("status", HttpStatus.OK.value());
			reMap.put("code", "C" + HttpStatus.OK.value());
			reMap.put("message", "삭제에 성공했습니다.");
			return reMap;
		}
		
		return reMap;
		
	}
	
	/************************************************************************ 공통 함수 [e] ************************************************************************/
	
}
