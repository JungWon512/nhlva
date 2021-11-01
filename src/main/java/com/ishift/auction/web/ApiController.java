package com.ishift.auction.web;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ishift.auction.configuration.security.token.AdminUserAuthenticationToken;
import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.vo.AdminUserDetails;
import com.ishift.auction.vo.JwtTokenVo;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
public class ApiController {

	@Resource(name = "auctionService")
	private AuctionService auctionService;
	
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
	@PutMapping(value = "/api/{version}/auction/result"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> updataAuctionResultForJson(@PathVariable(name = "version") String version
														, @RequestBody final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
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
		catch (Exception e) {
			log.error("error - updateAuctionStatus : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
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
	@PutMapping(value = "/api/{version}/auction/result"
				, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> updataAuctionResultForForm(@PathVariable(name = "version") String version
														, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();
		
		try {
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
		catch (Exception e) {
			log.error("error - updateAuctionStatus : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
			return result;
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
					  .append(this.getStringValue(vo.get("SRA_PDMNM")).replace("|", ",")).append('|')
//					  .append(this.getStringValue(vo.get("FTSNM")).replace("|", ",")).append('|')
					  .append(this.getStringValue(vo.get("BRANDNM")).replace("|", ",")).append('|')
					  .append(this.getConvertBirthDay(this.getStringValue(vo.get("BIRTH")).replace("|", ","))).append('|')
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
					  //.append(vo.getOrDefault("AUC_PRG_SQ")).append('|')	// 계류대 번호
//					  .append(index++).append('|')				// 계류대 번호 
					  .append(this.getStringValue(vo.get("AUC_PRG_SQ")).replace("|", ",")).append('|')	// 계류대 번호
					  .append("N");								// 초과 줄장우 여부 

					entryList.add(sb.toString());
				}
			}
			
			result.put("success", true);
			result.put("message", "조회에 성공했습니다.");
			result.put("entry", entryList);
		}
		catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("entry", entryList);
			result.put("message", e.getMessage());
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
			
		}
		catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
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
			
		}
		catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
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
		}
		catch (Exception e) {
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
		}
		catch (Exception e) {
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
		}
		catch (Exception e) {
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
		}
		catch(Exception e) {
			log.error("error - getNearestBranchInfo : {}", e.getMessage());
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
		}
		catch (Exception e) {
			log.error("error - bizServiceId : {}", e.getMessage());
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}
	
	/**
	 * 생년월일(개월 수) 변경
	 * @param date
	 * @return
	 */
	private String getConvertBirthDay(Object date) {
		String convertBirthDay = "";
		
		String month = "";
		
		if (date == null) return "";

		if (isValidString(date.toString())) {

			boolean isCheck = isValidationDate(date.toString());

			if (isCheck) {

				try {
					SimpleDateFormat dtFormat = new SimpleDateFormat("yyyyMMdd");
					SimpleDateFormat newDtFormat = new SimpleDateFormat("yy.MM.dd");
					Date formatDate = dtFormat.parse(date.toString());
					convertBirthDay = newDtFormat.format(formatDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
				month = geDiffDateMonth(date.toString(), getTodayYYYYMMDD());
			}else {
				convertBirthDay = date.toString();
				month = "";
			}
		}
		
		if(isValidString(convertBirthDay) && isValidString(convertBirthDay)) {
			return convertBirthDay + "(" + month + "개월)";
		}
		else {
			return "";
		}
	}
	
	/**
	 * 두 날짜 사이에 개월 수 계산
	 * 
	 * @param fromDateStr 20200101
	 * @param toDateStr   20210917
	 * @return
	 */
	private String geDiffDateMonth(String fromDateStr, String toDateStr) {

		String result = "";

		if (!isValidString(toDateStr) || !isValidString(toDateStr)) {
			return result;
		}

		int toDateVal = Integer.parseInt(toDateStr);
		int fromDateVal = Integer.parseInt(fromDateStr);

		if (fromDateVal > toDateVal) {
			return result;
		}

		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");

			Date toDate = format.parse(toDateStr);
			Date fromDate = format.parse(fromDateStr);

			long baseDay = 24 * 60 * 60 * 1000; // 일
			long baseMonth = baseDay * 30; // 월
//			long baseYear = baseMonth * 12; // 년

			// from 일자와 to 일자의 시간 차이를 계산한다.
			long calDate = toDate.getTime() - fromDate.getTime();

			// from 일자와 to 일자의 시간 차 값을 하루기준으로 나눠 준다.
//			long diffDate = calDate / baseDay;
			long diffMonth = (calDate / baseMonth) + 1;
//			long diffYear = calDate / baseYear;

			result = Long.toString(diffMonth);

		} catch (Exception e) {
			System.out.println("[error] : " + e);
			return result;
		}

		return result;
	}
	
	private String getTodayYYYYMMDD() {
		String today = "";
		LocalDate now = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		today = now.format(formatter);
		return today;
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
	 * 날짜 유효성 검사
	 * @param checkDate
	 * @return
	 */
	private boolean isValidationDate(String checkDate) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
			dateFormat.setLenient(false);
			dateFormat.parse(checkDate);
			return true;
		} catch (Exception e) {
			return false;
		}
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
		}
		catch(Exception e) {
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
	Map<String, Object> userToken(@PathVariable(name = "version") final String version) {
		final Map<String, Object> result = new HashMap<>();
		try {
			final Map<String, Object> params = new HashMap<>();
			List<Map<String, Object>> userList = auctionService.selectTestUserList(params);
			
			List<String> tokenList = new ArrayList<String>();
			for (Map<String, Object> userVo : userList) {
				JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
													.auctionHouseCode(userVo.get("NA_BZPLC").toString())
													.userMemNum(userVo.get("TRMN_AMNNO").toString())
													.userRole(Constants.UserRole.BIDDER)
													.build();
				tokenList.add(jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN));
			}
			result.put("success", true);
			result.put("message", "조회에 성공했습니다.");
			result.put("tokenList", tokenList);
			return result;
		}
		catch (Exception e) {
			log.error("error - userToken : {}", e.getMessage());
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
		}
		catch (Exception e) {
			log.error("error - sealectAuctQcn : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
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
		}
		catch (Exception e) {
			log.error("error - sealectAuctCowCnt : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
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
		catch (Exception e) {
			log.error("error - selectAuctCowList : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
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
	@PostMapping(value = "/api/{version}/auction/update/lowsbidamt"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> updateLowSbidAmt(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			int cnt = auctionService.updateLowSbidAmt(params);
			
			if (cnt > 0) {
				result.put("success", true);
				result.put("data", cnt);
				result.put("message", "정상적으로 변경되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "변경된 정보가 없습니다.");
			}
		}
		catch (Exception e) {
			log.error("error - updateLowSbidAmt : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
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
		}
		catch (Exception e) {
			log.error("error - updateAuctCowSt : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
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
		}
		catch (Exception e) {
			log.error("error - updateAuctCowResult : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
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
		}
		catch (Exception e) {
			log.error("error - selectBidLogCnt : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
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
		}
		catch (Exception e) {
			log.error("error - selectAuctCowInfo : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
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
	@PostMapping(value = "/api/{version}/auction/inset/bidlog"
			, consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> insertBidLog(@PathVariable(name = "version") String version
			, @RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
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
		}
		catch (Exception e) {
			log.error("error - selectAuctCowInfo : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
			return result;
		}
		return result;
	}

	/**
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
		}
		catch (Exception e) {
			log.error("error - selectAuctCowInfo : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
			return result;
		}
		return result;
	}

	/**
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
		}
		catch (Exception e) {
			log.error("error - selectAuctCowInfo : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
			return result;
		}
		return result;
	}

	/**
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
		}
		catch (Exception e) {
			log.error("error - selectAuctCowInfo : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
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
		}
		catch (Exception e) {
			log.error("error - selectAuctCowInfo : {}", e.getMessage());
			result.put("success", false);
			result.put("message", e.getMessage());
			return result;
		}
		return result;
	}
}
