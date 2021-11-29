package com.ishift.auction.web;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.ishift.auction.configuration.security.token.AdminUserAuthenticationToken;
import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.service.login.LoginService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.JsonUtils;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.vo.AdminUserDetails;
import com.ishift.auction.vo.JwtTokenVo;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Yuchan
 *
 */
@Slf4j
@RestController
@SuppressWarnings("unchecked")
public class ApiController {

	@Resource(name = "auctionService")
	private AuctionService auctionService;

	@Autowired
	private LoginService loginService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	SessionUtill sessionUtill;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Value("${spring.profiles.active}")
	private String profile;

	@PostMapping(value = "/api/test", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> test(@RequestBody final Map<String, Object> map) {
		Map<String, Object> result = new HashMap<String, Object>();
		JwtTokenVo jwtTokenVo = JwtTokenVo.builder().auctionHouseCode(map.get("auctionHouseCode").toString())
													.userMemNum(map.get("userName").toString())
													.userRole(map.get("userRole").toString())
													.build();
		final String token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
		result.put("token", token);
		return result;
	}
	
	/**
	 * 응찰 서버로부터 전달받은 경매 결과를 업데이트한다.
	 * @param params
	 * @return 업데이트 결과
	 */
	@ApiOperation(value = "경매 결과 업데이트 API", tags = "result")
	@PostMapping(value = "/api/{version}/auction/result"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> updataAuctionResultForJson(@PathVariable(name = "version") String version
														, @RequestBody final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();
		final List<Map<String, Object>> failList = new ArrayList<Map<String, Object>>();

		try {
			if ("v2".equals(version)) {
				if(params.get("list") == null) {
					result.put("success", false);
					result.put("message", "필수 인자가 없습니다.");
					return result;
				}

				final List<Map<String,Object>> list = JsonUtils.getListMapFromJsonString(params.get("list").toString());
				if (list.size() < 1) {
					result.put("success", false);
					result.put("message", "경매 결과 정보가 없습니다.");
					return result;
				}
				
				for (Map<String, Object> info : list) {
					info.put("version", version);
					try {
						Map<String, Object> resultMap = auctionService.updateAuctionResultMap(info);
						if (resultMap != null) {
							failList.add(resultMap);
						}
					}catch (RuntimeException re) {
						log.debug("ApiController.updataAuctionResultForJson : {} ",re);
						failList.add(info);
					}catch (SQLException se) {
						log.debug("ApiController.updataAuctionResultForJson : {} ",se);
						failList.add(info);
					}
				}

				result.put("success", true);
				result.put("message", "정상적으로 변경되었습니다.");
				if (failList.size() > 0) {
					result.put("failList", failList);
				}
			}
			else {
				int cnt = auctionService.updateAuctionResult(params);
				
				if (cnt > 0) {
					result.put("success", true);
					result.put("message", "정상적으로 변경되었습니다.");
				}
				else {
					result.put("success", false);
					result.put("message", "출하우 정보가 없습니다.");
				}
			}
		}catch (SQLException | RuntimeException | JsonProcessingException re) {
			log.debug("ApiController.updataAuctionResultForJson : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	/**
	 * 응찰 서버로부터 전달받은 경매 결과를 업데이트한다.
	 * @param params
	 * @return 업데이트 결과
	 */
	@ApiOperation(value = "경매 결과 업데이트 API", tags = "result")
	@PostMapping(value = "/api/{version}/auction/result"
				, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> updataAuctionResultForForm(@PathVariable(name = "version") String version
														, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();
		final List<Map<String, Object>> failList = new ArrayList<Map<String, Object>>();

		try {
			if ("v2".equals(version)) {
				if(params.get("list") == null) {
					result.put("success", false);
					result.put("message", "필수 인자가 없습니다.");
					return result;
				}

				final List<Map<String,Object>> list = JsonUtils.getListMapFromJsonString(params.get("list").toString());
				if (list.size() < 1) {
					result.put("success", false);
					result.put("message", "경매 결과 정보가 없습니다.");
					return result;
				}
				
				for (Map<String, Object> info : list) {
					info.put("version", version);
					try {
						Map<String, Object> resultMap = auctionService.updateAuctionResultMap(info);
						if (resultMap != null) {
							failList.add(resultMap);
						}
					}catch (RuntimeException re) {
						log.debug("ApiController.updataAuctionResultForForm : {} ",re);
						info.put("message", re.getMessage());
						failList.add(info);
					}catch (SQLException se) {
						log.debug("ApiController.updataAuctionResultForForm : {} ",se);
						info.put("message", se.getMessage());
						failList.add(info);
					}
				}
				
				result.put("success", true);
				result.put("message", "정상적으로 변경되었습니다.");
				if (failList.size() > 0) {
					result.put("failList", failList);
				}
			}
			else {
				int cnt = auctionService.updateAuctionResult(params);
				
				if (cnt > 0) {
					result.put("success", true);
					result.put("message", "정상적으로 변경되었습니다.");
				}
				else {
					result.put("success", false);
					result.put("message", "출하우 정보가 없습니다.");
				}
			}
		}catch (SQLException | RuntimeException | JsonProcessingException re) {
			log.debug("ApiController.updataAuctionResultForForm : {} ",re);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			result.put("success", false);
		}
		return result;
	}

	/**
	 * 경매 출품 리스트 api
	 * @param version > api 버전
	 * @param naBzplc > 조합코드
	 * @param searchDate > 검색일자
	 * @return 경매 출품 리스트
	 */
	@ResponseBody
	@GetMapping(value = "/api/{version}/auction/{naBzplc}/entry")
	Map<String, Object> auctionEntry(@PathVariable(name = "version") final String version
									, @PathVariable(name = "naBzplc") final String naBzplc
									, @RequestParam(name = "date", required = false) final String searchDate) {
		final Map<String, Object> result = new HashMap<String, Object>();
		final List<String> entryList = new ArrayList<String>();
		try {
			
			if ("".equals(naBzplc)) {
				result.put("success", false);
				result.put("message", "조합구분코드[naBzplc]를 확인하세요.");
				return result;
			}

			final Map<String, Object> params = new HashMap<String, Object>();
			params.put("naBzplc", naBzplc);
			params.put("searchDate", searchDate);
			final List<Map<String, Object>> list = auctionService.selectAuctionEntry(params);
			if (list != null) {
				for (final Map<String, Object> vo : list) {
					StringBuffer sb = new StringBuffer();
					sb.append("SC").append('|')
					  .append(this.getStringValue(vo.get("NA_BZPLC")).replace("|", ",")).append('|')
//					  .append(this.getStringValue(vo.get("NA_BZPLC")).replace("|", "｜")).append('|')
					  .append(this.getStringValue(vo.get("AUC_PRG_SQ")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("QCN")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("AUC_OBJ_DSC")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("SRA_INDV_AMNNO")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("SRA_SRS_DSC")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("FHS_ID_NO")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("FARM_AMNNO")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("FTSNM")).replace("|", ",")).append('|')
//					  .append(this.getStringValue(vo.get("SRA_PDMNM")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("BRANDNM")).replace("|", ",")).append('|')
//					  .append(this.getConvertBirthDay(this.getStringValue(vo.get("BIRTH")).replace("|", ","))).append('|')
					  .append(this.getStringValue(vo.get("BIRTH_FMT")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("KPN_NO")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("INDV_SEX_NAME")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("MCOW_DSC_NM")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("MCOW_SRA_INDV_AMNNO")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("MATIME")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("PRNY_MTCN")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("SRA_INDV_PASG_QCN")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("INDV_ID_NO")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("SRA_INDV_BRDSRA_RG_NO")).replace("|", ",")).append('|')
//					  .append(this.getStringValue(vo.getOrDefault("RG_DSC_NM")).append('|')
					  .append(this.getStringValue(vo.get("RG_DSC")).replace("|", ",")).append('|')
					  .append(this.getRgnName(vo.get("SRA_PD_RGNNM")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("DNA_YN")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("ANW_YN")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("COW_SOG_WT")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("FIR_LOWS_SBID_LMT_AM")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("LOWS_SBID_LMT_AM")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("RMK_CNTN")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("SEL_STS_DSC")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("LVST_AUC_PTC_MN_NO")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("SRA_SBID_AM")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("ATDR_DTM")).replace("|", ",")).append('|')
					  .append('N').append('|')
					  .append(this.getStringValue(vo.get("MODL_NO")).replace("|", ",")).append('|')	// 계류대 번호
					  .append("N");								// 초과 줄장우 여부 

					entryList.add(sb.toString());
				}
			}
			
			result.put("success", true);
			result.put("message", "조회에 성공했습니다.");
			result.put("entry", entryList);
		}catch (SQLException | RuntimeException re) {
			log.error("ApiController.auctionEntry : {} ",re);
			result.put("success", false);
			result.put("entry", entryList);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		return result;
	}

	/**
	 * 조합 직원 로그인 api
	 * @param version > api 버전
	 * @param naBzplc > 조합코드
	 * @param params > 로그인 정보
	 * @return access_token
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auth/login"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> authLoginForJson(@PathVariable(name = "version") final String version
										, @RequestBody final Map<String, Object> params) {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		String token = "";

		try {
			final Authentication authentication = authenticationManager.authenticate(
																		new AdminUserAuthenticationToken(
																				params.getOrDefault("usrid", "").toString()
																				, params.getOrDefault("pw", "").toString()
																				, null));

			final AdminUserDetails adminUserDetails = (AdminUserDetails)authentication.getPrincipal();
			
			if (adminUserDetails != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				final JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
														.userMemNum(adminUserDetails.getUsrid())
														.auctionHouseCode(adminUserDetails.getNaBzplc())
														.userRole(Constants.UserRole.ADMIN)
														.build();
				
				token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
				result.put("accessToken", token);
				result.put("naBzplc", adminUserDetails.getNaBzplc());
				result.put("success", true);
				result.put("message", "토큰 발급 성공했습니다.");
				return result;
			}
			else {
				result.put("message", "입력하신 정보가 없습니다.");
				result.put("success", false);
				return result;
			}
			
		}catch (RuntimeException re) {
			log.error("ApiController.auctionEntry : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}
	
	/**
	 * 조합 직원 로그인 api
	 * @param version > api 버전
	 * @param naBzplc > 조합코드
	 * @param params > 로그인 정보
	 * @return access_token
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auth/login"
				, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> authLoginForForm(@PathVariable(name = "version") final String version
										, @RequestParam final Map<String, Object> params) {

		final Map<String, Object> result = new HashMap<String, Object>();
		String token = "";
		
		try {
			final Authentication authentication = authenticationManager.authenticate(
					new AdminUserAuthenticationToken(
							params.getOrDefault("usrid", "").toString()
							, params.getOrDefault("pw", "").toString()
							, null));

			final AdminUserDetails adminUserDetails = (AdminUserDetails)authentication.getPrincipal();

			if (adminUserDetails != null) {
				SecurityContextHolder.getContext().setAuthentication(authentication);
				final JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
						.userMemNum(adminUserDetails.getUsrid())
						.auctionHouseCode(adminUserDetails.getNaBzplc())
						.userRole(Constants.UserRole.ADMIN)
						.build();
				
				token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
				result.put("naBzplc", adminUserDetails.getNaBzplc());
				result.put("accessToken", token);
				result.put("success", true);
				result.put("message", "토큰 발급 성공했습니다.");
				return result;
			}
			else {
				result.put("message", "입력하신 정보가 없습니다.");
				result.put("success", false);
				return result;
			}
			
		}catch (RuntimeException re) {
			log.debug("ApiController.authLoginForForm : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}
	
	/**
	 * 찜 정보 조회 api
	 * @param version > api 버전
	 * @param naBzplc > 조합코드
	 * @param aucDt > 경매일자
	 * @param aucObjDsc > 경매구분코드
	 * @param trmnAmnno > 회원번호
	 * @param aucPrgSq > 출품번호
	 * @return
	 */
	@ResponseBody
	@GetMapping(value = "/api/{version}/my/{naBzplc}/favorite")
	Map<String, Object> myFavorite(@PathVariable(name = "version") final String version
								 , @PathVariable(name = "naBzplc") final String naBzplc
								 , @RequestParam(name = "date", required = false) final String aucDt
								 , @RequestParam(name = "aucClass", required = true) final String aucObjDsc
								 , @RequestParam(name = "userMemNum", required = true) final String trmnAmnno
								 , @RequestParam(name = "aucSeq", required = true) final String aucPrgSq) {
		final Map<String, Object> result = new HashMap<String, Object>();
		try {
			final Map<String, Object> params = new HashMap<String, Object>();
			params.put("naBzplc", naBzplc);
			params.put("trmnAmnno", trmnAmnno);
			params.put("aucPrgSq", aucPrgSq);
			params.put("aucDt", aucDt);
			params.put("aucObjDsc", aucObjDsc);
			
			final Map<String, Object> favorite = auctionService.selectMyFavoriteInfo(params);
			if (favorite != null) {
				result.put("success", true);
				result.put("message", "조회에 성공했습니다.");
				result.put("info", favorite);
			}
			else {
				result.put("success", false);
				result.put("message", "등록한 찜 정보가 없습니다.");
			}
			return result;
		}catch (RuntimeException re) {
			log.error("ApiController.myFavorite : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}catch (SQLException se) {
			log.error("ApiController.myFavorite : {} ",se);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}

	/**
	 * 경매 참가 App 업데이트를 위한 app version 체크 api
	 * @param osType > os유형
	 * @return
	 */
	@ResponseBody
	@GetMapping(value = "/api/appversion")
	Map<String, Object> appVersion(@RequestParam(name = "osType", required = true) final String osType) {
		final Map<String, Object> result = new HashMap<String, Object>();
		try {
			final Map<String, Object> params = new HashMap<String, Object>();
			params.put("osType", osType);
			
			final Map<String, Object> appVersion = auctionService.selectAppVersionInfo(params);
			if (appVersion != null) {
				result.put("success", true);
				result.put("message", "조회에 성공했습니다.");
				result.put("info", appVersion);
			}
			else {
				result.put("success", false);
				result.put("message", "조회에 실패했습니다.");
			}
			return result;
		}catch (RuntimeException re) {
			log.error("ApiController.appVersion : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}catch (SQLException se) {
			log.error("ApiController.appVersion : {} ",se);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}
	
	/**
	 * 경매 가능한 지역 조합 리스트 api
	 * @param version > api버전
	 * @return
	 */
	@ResponseBody
	@GetMapping(value = "/api/{version}/biz/info")
	Map<String, Object> bizInfo(@PathVariable(name = "version") final String version) {
		final Map<String, Object> result = new HashMap<String, Object>();
		try {
			final List<Map<String, Object>> bizList = auctionService.selectAuctionBizList(result);
			if (bizList == null || bizList.size() < 1) {
				result.put("success", false);
				result.put("message", "조회에 실패했습니다.");
			}
			else {
				result.put("success", true);
				result.put("message", "조회에 성공했습니다.");
				result.put("bizList", bizList);
			}
		}catch (RuntimeException re) {
			log.error("ApiController.bizInfo : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}catch (SQLException se) {
			log.error("ApiController.bizInfo : {} ",se);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	
	/**
	 * 현재 좌표 기준으로 가장 가까운 조합 검색
	 * @param version > api버전
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/biz/nearest")
	@ApiOperation(value = "근처 조합 검색", tags = "bizNearest")
	Map<String, Object> bizNearest(@PathVariable(name = "version") final String version
								 , @RequestBody final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<>();
		try {
			if (!"production".equals(profile)) {
				params.put("distance", 250);
			}
			List<Map<String, Object>> list = auctionService.selectNearestBranchInfo(params);
			if (list != null && list.size() > 0) {
				result.put("success", true);
				result.put("message", "조회에 성공했습니다.");
				result.put("branchInfo", list.get(0));
			}
			else {
				result.put("success", false);
				result.put("message", "경매 참여 가능한 지점이 없습니다.");
			}
			return result;
		}catch (RuntimeException re) {
			log.error("ApiController.bizNearest : {} ",re);
			log.error("error - getNearestBranchInfo : {}", re.getMessage());
			result.put("success", false);
			return result;
		}catch (SQLException se) {
			log.error("ApiController.bizNearest : {} ",se);
			log.error("error - getNearestBranchInfo : {}", se.getMessage());
			result.put("success", false);
			return result;
		}
	}
	
	/**
	 * 카카오 커넥트 라이브 서비스ID, Key 조회 api
	 * @param version > api버전
	 * @param naBzplc > 조합코드
	 * @return
	 */
	@ResponseBody
	@GetMapping(value = "/api/{version}/biz/{naBzplc}/kakao")
	@ApiOperation(value = "카카오 커넥트 라이브 서비스ID, Key 조회", tags = "bizServiceId")
	Map<String, Object> bizKakao(@PathVariable(name = "version") final String version
									,@PathVariable(name = "naBzplc") final String naBzplc) {
		final Map<String, Object> result = new HashMap<>();
		try {
			final Map<String, Object> params = new HashMap<>();
			params.put("naBzplc", naBzplc);
			Map<String, Object> kkoInfo = auctionService.selectKakaoServiceInfo(params);
			if (kkoInfo != null && kkoInfo.get("KKO_SVC_ID") != null && kkoInfo.get("KKO_SVC_KEY") != null) {
				result.put("success", true);
				result.put("message", "조회에 성공했습니다.");
				result.put("kkoInfo", kkoInfo);
			}
			else {
				result.put("success", false);
				result.put("message", "등록된 정보가 없습니다.");
			}
			return result;
		}catch (RuntimeException re) {
			log.error("error - bizServiceId : {}", re.getMessage());
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}catch (SQLException se) {
			log.error("error - bizServiceId : {}", se.getMessage());
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}
	
	/**
	 * 문자열 Null, Empty, Length 유효성 확인 함수
	 * @param str 확인 문자열
	 * @return boolean true : 유효 문자, false : 무효 문자
	 */
	private boolean isValidString(String str) {
		if (str == null || str.equals("") || str.isEmpty()) {
			return false;
		}

		if (str.trim().length() <= 0) {
			return false;
		}

		return true;
	}
	
	/**
	 * 출하 지역명 가져오기
	 * @param rgnNm
	 * @return
	 */
	private String getRgnName(Object rgnNm) {
		if (rgnNm == null) return "";
		try {
			String[] splitAddr = rgnNm.toString().split("\\s+");
			if (splitAddr.length > 2) {
				String tmpAddr = splitAddr[2].trim();
				if (tmpAddr.length() == 3) {
					String subAddr = tmpAddr.substring(0, 2);
					if (this.isValidString(subAddr)) {
						return subAddr;
					}
					else {
						return "";
					}
				}
				else {
					return tmpAddr;
				}
			}
			else {
				return splitAddr[0];
			}
		}catch (RuntimeException re) {
			return "";
		}
	}
	
	private String getStringValue(Object value) {
		return value == null ? "" : value.toString();
	}
	
	/**
	 * 카카오 커넥트 라이브 서비스ID, Key 조회 api
	 * @param version > api버전
	 * @param naBzplc > 조합코드
	 * @return
	 */
	@ResponseBody
	@GetMapping(value = "/api/{version}/biz/token")
	Map<String, Object> userToken(@PathVariable(name = "version") final String version
								 ,@RequestParam Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<>();
		try {
			List<Map<String, Object>> userList = auctionService.selectTestUserList(params);
			
			List<Map<String, Object>> tokenList = new ArrayList<>();
			for (Map<String, Object> userVo : userList) {
				Map<String, Object> tempVo = new HashMap<String, Object>();
				JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
													.auctionHouseCode(userVo.get("NA_BZPLC").toString())
													.userMemNum(userVo.get("TRMN_AMNNO").toString())
													.userRole(Constants.UserRole.BIDDER)
													.build();
				
				tempVo.put("trmnAmnno", userVo.get("TRMN_AMNNO").toString());
				tempVo.put("sraMwmnnm", userVo.get("SRA_MWMNNM").toString());
				tempVo.put("token", jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.REFRESH_TOKEN));
				tokenList.add(tempVo);
			}
			result.put("tokenList", tokenList);
			return result;
		}catch (RuntimeException re) {
			log.error("error - userToken : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}catch (SQLException se) {
			log.error("error - userToken : {}", se);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}
	
	/**
	 * 경매 회차 정보 검색
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/select/qcn"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> sealectAuctQcn(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			Map<String,Object> map = auctionService.sealectAuctQcn(params);
			
			if (map != null) {
				result.put("success", true);
				result.put("data", map);
				result.put("message", "정상적으로 조회되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "경매회차 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - sealectAuctQcn : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	/**
	 * 출장우 데이터 수 조회
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/select/cowcnt"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> sealectAuctCowCnt(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			String stnYn = params.get("stnYn") == null? "" :(String)params.get("stnYn");
			if("Y".equals(stnYn)) {
				Map<String, Object> map = auctionService.selectAuctStn(params);
				if (map == null) {
					result.put("success", false);
					result.put("message", "일괄경매회차정보가 없습니다.");
					return result;
				}
				params.put("stAucNo", map.get("ST_AUC_NO"));
				params.put("edAucNo", map.get("ED_AUC_NO"));
			}
			int cnt = auctionService.sealectAuctCowCnt(params);

			if (cnt > 0) {
				result.put("success", true);
				result.put("data", cnt);
				result.put("message", "정상적으로 조회되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "조회된 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - sealectAuctCowCnt : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	/**
	 * 출장우 데이터 조회 (단일)
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/select/cowinfo"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> selectAuctCowInfo(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			String stnYn = params.get("stnYn") == null? "" :(String)params.get("stnYn");
			if("Y".equals(stnYn)) {
				Map<String, Object> map = auctionService.selectAuctStn(params);
				if (map == null) {
					result.put("success", false);
					result.put("message", "일괄경매회차정보가 없습니다.");
					return result;					
				}
				params.put("stAucNo", map.get("ST_AUC_NO"));
				params.put("edAucNo", map.get("ED_AUC_NO"));
			}
			List<Map<String, Object>> list = auctionService.selectAuctCowList(params);
			
			for (Map<String, Object> info : list) {
				info.put("SRA_PD_RGNNM_FMT", this.getRgnName(info.get("SRA_PD_RGNNM")));
			}
			
			if (list != null && list.size() > 0) {
				result.put("success", true);
				result.put("data", list);
				result.put("message", "정상적으로 조회되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "출하우 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - selectAuctCowList : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}

	/**
	 * 최저가 변경 (단일)
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/update/lowsbidamt"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> updateLowSbidAmt(@PathVariable(name = "version") String version
			, @RequestParam Map<String,Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();
		try {
			//int cnt = 0;
			if(params.get("list") == null) {
				result.put("success", false);
				result.put("message", "필수 인자가 없습니다.");
			}
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse((String) (params.get("list")));
			List<Map<String,Object>> temp = new ArrayList<>();
			for(int i=0;i<array.size();i++) {
				JSONObject json = (JSONObject) array.get(i);
				Map<String,Object> map = new HashMap<String, Object>();
				Set<String> jsonKeys = json.keySet();
				for(String jsonKey : jsonKeys) {
					map.put(jsonKey, json.get(jsonKey));
				}
				temp.add(map);
			}
			params.put("list", temp);
			if(temp.size() <= 0 ) {
				result.put("success", false);
				result.put("message", "변경된 정보가 없습니다.");				
			}
			int cnt = auctionService.updateLowSbidAmt(params);

			result.put("success", true);
			result.put("data", cnt);
			result.put("message", "정상적으로 변경되었습니다.");
			
		}catch (SQLException | RuntimeException | ParseException re) {
			log.error("error - updateLowSbidAmt : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	/**
	 * 경매상태변경(보류)
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/update/cowst"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> updateAuctCowSt(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			int cnt = auctionService.updateAuctCowSt(params);
			
			if (cnt > 0) {
				result.put("success", true);
				result.put("data", cnt);
				result.put("message", "정상적으로 변경되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "변경된 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - updateAuctCowSt : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	/**
	 * 경매결과저장
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/update/cowresult"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> updateAuctCowResult(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			int cnt = auctionService.updateAuctCowResult(params);
			
			if (cnt > 0) {
				result.put("success", true);
				result.put("data", cnt);
				result.put("message", "정상적으로 변경되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "변경된 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - updateAuctCowResult : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}

	/**
	 * 응찰내역카운트
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/select/bidlogcnt"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> selectBidLogCnt(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			int cnt = auctionService.selectBidLogCnt(params);
			
			if (cnt > 0) {
				result.put("success", true);
				result.put("data", cnt);
				result.put("message", "정상적으로 조회되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "조회된 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - selectBidLogCnt : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	/**
	 * 응찰자 리스트
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/select/bidentry"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> selectBidLog(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			List<Map<String, Object>> list = auctionService.selectBidEntryList(params);
			
			if (list.size() > 0) {
				result.put("success", true);
				result.put("data", list);
				result.put("message", "정상적으로 조회되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "응찰 내역이 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - selectBidLog : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}

	/**
	 * 다음 응츨번호 조회
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/select/nextbid"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> selectNextBidNum(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			Map<String, Object> map = auctionService.selectNextBidNum(params);
			
			if (map != null) {
				result.put("success", true);
				result.put("data", map);
				result.put("message", "정상적으로 조회되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "조회된 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - selectAuctCowInfo : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}

	/**
	 * 응찰로그 저장
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/insert/bidlog"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> insertBidLog(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			
			if ("0".equals(params.get("rgSqno")) || "99999999".equals(params.get("rgSqno"))) {
				int logCnt = auctionService.selectBidLogCnt(params);
				if(logCnt > 0) {
					result.put("success", false);
					result.put("message", "이미 등록된 데이터입니다.");
					return result;
				}
			}
			
			int cnt = auctionService.insertBidLog(params);
			
			if (cnt > 0) {
				result.put("success", true);
				result.put("data", cnt);
				result.put("message", "정상적으로 변경되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "변경된 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - selectAuctCowInfo : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}

	/**
	 * TODO : Deprecate 예정
	 * 수수료 조회
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/select/fee"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> selectFeeInfo(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			List<Map<String, Object>> map = auctionService.selectFeeInfo(params);
			
			if (map != null && map.size() > 0) {
				result.put("success", true);
				result.put("data", map);
				result.put("message", "정상적으로 조회되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "조회된 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - selectFeeInfo : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}

	/**
	 * TODO : Deprecate 예정
	 * 수수료내역 삭제
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/delete/fee"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> deleteFeeInfo(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			int cnt = auctionService.deleteFeeInfo(params);
			
			if ( cnt > 0) {
				result.put("success", true);
				result.put("data", cnt);
				result.put("message", "정상적으로 변경되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "변경된 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - deleteFeeInfo : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}

	/**
	 * TODO : Deprecate 예정
	 * 수수료 내역저장
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/insert/fee"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> insertFeeLog(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			if(params.get("list") == null) {
				result.put("success", false);
				result.put("message", "필수 인자가 없습니다.");
			}
			JSONParser parser = new JSONParser();
			JSONArray array = (JSONArray) parser.parse((String) (params.get("list")));
			List<Map<String,Object>> temp = new ArrayList<>();
			for(int i=0;i<array.size();i++) {
				JSONObject json = (JSONObject) array.get(i);
				Map<String,Object> map = new HashMap<String, Object>();
				Set<String> jsonKeys = json.keySet();
				for(String jsonKey : jsonKeys) {
					map.put(jsonKey, json.get(jsonKey));
				}
				temp.add(map);
			}
			params.put("list", temp);
			int cnt = auctionService.insertFeeLog(params);
			
			if ( cnt > 0) {
				result.put("success", true);
				result.put("data", cnt);
				result.put("message", "정상적으로 변경되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "변경된 정보가 없습니다.");
			}
		}catch (SQLException | ParseException | RuntimeException re) {
			log.error("error - insertFeeLog : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	/**
	 * 경매참가사용자조회
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/select/bidnum"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> selectAuctBidNum(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			Map<String, Object> map = auctionService.selectAuctBidNum(params);
			
			if ( map != null) {
				result.put("success", true);
				result.put("data", map);
				result.put("message", "정상적으로 조회되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "조회된 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - selectAuctBidNum : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}

	/**
	 * 일괄경매 상태 변경
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@SuppressWarnings("serial")
	@PostMapping(value = "/api/{version}/auction/status"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> auctStatus(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {

		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			if(StringUtils.isEmpty(params.get("naBzPlc"))
			|| StringUtils.isEmpty(params.get("aucObjDsc"))
			|| StringUtils.isEmpty(params.get("aucDt"))
			|| StringUtils.isEmpty(params.get("rgSqno"))
			|| StringUtils.isEmpty(params.get("status"))
			) {
				result.put("success", false);
				result.put("message", "필수 인자값이 없습니다.");
				return result;
			}
			
			params.put("naBzplc", params.get("naBzPlc"));
			String status = params.getOrDefault("status", "").toString();

			final Map<String, Object> aucStn = auctionService.selectAuctStn(params);

			if(aucStn == null) {
				result.put("success", false);
				result.put("message", "경매차수가 없습니다.");
				return result;
			}

			final Map<String, Object> temp = new HashMap<String, Object>() {{putAll(params);}};
			Map<String, Object> returnMap = new HashMap<String, Object>();

			// SEL_STS_DSC 상태 : 11.송장등록, 21.경매진행, 22.낙찰, 23.보류(유찰)
			if(!"".equals(status)) {
				switch(status) {
					case "start":
						// 현재 차수(TB_LA_IS_MH_AUC_STN의 RG_SQNO)가 이미 진행중인 경우 실패 RETURN
						if("21".equals(aucStn.get("SEL_STS_DSC"))) {
							result.put("success", false);
							result.put("message", "이미 진행중인 경매입니다.");
							return result;
						}
						
						// 경매 시작
						returnMap = auctionService.auctionStart(aucStn, temp);
						if (returnMap != null && (Boolean)returnMap.get("success")) {
							result.put("success", true);
							result.put("message", "경매 시작이 정상 처리되었습니다.");
							
							params.put("stAucNo", aucStn.get("ST_AUC_NO"));
							params.put("edAucNo", aucStn.get("ED_AUC_NO"));
							params.put("aucYn", "1");
							List<Map<String, Object>> entryList = auctionService.selectAuctCowList(params);
							for (Map<String, Object> info : entryList) {
								info.put("SRA_PD_RGNNM_FMT", this.getRgnName(info.get("SRA_PD_RGNNM")));
							}
							result.put("entryList", entryList);
						}
						else {
							result.put("success", false);
							result.put("message", "작업 중 오류가 발생했습니다.");
						}
						
						break;
					case "pause":
						// 현재 차수(TB_LA_IS_MH_AUC_STN의 RG_SQNO)가 이미 중지된 경우 실패 RETURN
						if("23".equals(aucStn.get("SEL_STS_DSC"))) {
							result.put("success", false);
							result.put("message", "이미 중지된 경매입니다.");
							return result;
						}
						// 현재 차수(TB_LA_IS_MH_AUC_STN의 RG_SQNO)가 이미 종료 경우 실패 RETURN
						if("22".equals(aucStn.get("SEL_STS_DSC"))) {
							result.put("success", false);
							result.put("message", "이미 종료된 경매입니다.");
							return result;
						}
						
						// 경매 중지
						returnMap = auctionService.auctionPause(aucStn, temp);
						if (returnMap != null && (Boolean)returnMap.get("success")) {
							result.put("success", true);
							result.put("message", "경매 중지가 정상 처리되었습니다.");
						}
						else {
							result.put("success", false);
							result.put("message", "작업 중 오류가 발생했습니다.");
						}
						break;
					case "finish":
						// 현재 차수(TB_LA_IS_MH_AUC_STN의 RG_SQNO)가 이미 종료 경우 실패 RETURN
						if("22".equals(aucStn.get("SEL_STS_DSC"))) {
							result.put("success", false);
							result.put("message", "이미 종료된 경매입니다.");
							return result;
						}
						
						// 경매 종료
						returnMap = auctionService.auctionFinish(aucStn, temp);
						if (returnMap != null && (Boolean)returnMap.get("success")) {
							result.put("success", true);
							result.put("message", "경매 종료가 정상 처리되었습니다.");
						}
						else {
							result.put("success", false);
							result.put("message", "작업 중 오류가 발생했습니다.");
						}

						break;
					default:
						result.put("success", false);
						result.put("message", "경매 상태를 확인하세요.");
						break;
				}
			}
			else {
				result.put("success", false);
				result.put("message", "필수 인자값이 없습니다.");
			}				
		}catch (SQLException | RuntimeException re) {
			log.error("error - auctStatus : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	/**
	 * 조합원/비조합원 구분
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/auction/select/macoYn"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> selectMacoYn(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			Map<String, Object> map = loginService.selectWholesaler(params);
			
			if (map != null) {
				result.put("success", true);
				result.put("macoYn", map.get("MACO_YN"));
				result.put("message", "정상적으로 조회되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "중도매인 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - selectMacoYn : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	/**
	 * 금일 경매 차수 조회
	 * @param version
	 * @return
	 */
	@ResponseBody
	@GetMapping(value = "/api/{version}/host/select/auctQcnForToday")
	public Map<String, Object> selectAuctQcnForToday(@PathVariable(name = "version") String version) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			List<Map<String, Object>> list = auctionService.selectAuctQcnForToday();
			
			if (list != null && list.size() > 0) {
				result.put("success", true);
				result.put("data", list);
				result.put("message", "정상적으로 조회되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "회차 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("error - selectAuctQcnForToday : {}", re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	/**
	 * 응찰 서버 PORT 번호 업데이트
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@GetMapping(value = "/api/{version}/host/update/netPort")
	public Map<String, Object> updateNetPort(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			int cnt = auctionService.updateNetPort(params);
			
			if (cnt > 0) {
				result.put("success", true);
				result.put("message", "정상적으로 변경되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "변경된 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.debug("ApiController.updateNetPort : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		return result;
	}

	/**
	 * 최근 응찰 가격, 찜 가격 조회
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@GetMapping(value = "/api/{version}/my/select/nearAtdrAm")
	public Map<String, Object> selectNearAtdrAm(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();
		final Map<String, Object> temp = new HashMap<String, Object>();

		try {
			Map<String,Object> map = auctionService.selectNearAtdrAm(params);
			Map<String,Object> zim = auctionService.selectMyZimPrice(params);

			temp.put("bidPrice", map != null ? map.get("ATDR_AM"):0);
			temp.put("zimPrice", zim != null ? zim.get("SBID_UPR"):0);
			result.put("success", true);
			result.put("data", temp);
			result.put("message", "정상적으로 조회되었습니다.");
		}catch (SQLException | RuntimeException re) {
			log.debug("ApiController.selectNearAtdrAm : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		return result;
	}
	
	
	/**
	 * APP 일괄 경매용 출하우 정보 API
	 * @param version
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/api/{version}/my/select/cowList")
	public Map<String, Object> selectCowList(@PathVariable(name = "version") String version
											, @RequestParam final Map<String, Object> params) {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			Map<String, Object> map = auctionService.selectAuctStn(params);
			if (map == null) {
				result.put("success", false);
				result.put("message", "회차 정보가 없습니다.");
				return result;
			}
			params.put("stAucNo", map.get("ST_AUC_NO"));
			params.put("edAucNo", map.get("ED_AUC_NO"));
			List<Map<String, Object>> list = auctionService.selectCowList(params);
			
			for (Map<String, Object> info : list) {
				info.put("SRA_PD_RGNNM_FMT", this.getRgnName(info.get("SRA_PD_RGNNM")));
			}
			
			if (list != null && list.size() > 0) {
				result.put("success", true);
				result.put("data", list);
				result.put("message", "정상적으로 조회되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "출하우 정보가 없습니다.");
			}
		}
		catch (SQLException | RuntimeException re) {
			log.debug("ApiController.selectCowList : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		return result;
	}
	
	@ResponseBody
	@GetMapping(value = "/api/{version}/auction/{naBzplc}/absentCowList")
	public Map<String, Object> selectAbsentCowList(@PathVariable(name = "version") String version
											, @PathVariable(name = "naBzplc") final String naBzplc
											, @RequestParam final Map<String, Object> params) {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		try {			
			params.put("naBzplc", naBzplc);
			params.put("aucDt", params.get("date"));
			
			Map<String, Object> map = auctionService.selectAuctStn(params);
			if (map == null) {
				result.put("success", false);
				result.put("message", "회차 정보가 없습니다.");
				return result;
			}
			params.put("stAucNo", map.get("ST_AUC_NO"));
			params.put("edAucNo", map.get("ED_AUC_NO"));
			params.put("absentYn", "Y");
			List<Map<String, Object>> list = auctionService.selectCowList(params);
			if (list != null) {
				for (final Map<String, Object> vo : list) {
					sb.append(this.getStringValue(vo.get("AUC_PRG_SQ")).replace("|", ",")).append('|');	// 계류대 번호
				}
			}
			
			result.put("success", true);
			result.put("message", "조회에 성공했습니다.");
			result.put("entry", sb.toString());
		}catch (SQLException | RuntimeException re) {
			log.error("ApiController.selectAbsentCowList : {} ",re);
			result.put("success", false);
			result.put("entry", sb.toString());
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		return result;
	}
	
}
