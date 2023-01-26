
package com.ishift.auction.web;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.RedirectView;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.service.login.LoginService;
import com.ishift.auction.service.mypage.MyPageService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.CookieUtil;
import com.ishift.auction.util.DateUtil;
import com.ishift.auction.util.FormatUtil;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.vo.BidUserDetails;
import com.ishift.auction.vo.FarmUserDetails;
import com.ishift.auction.vo.JwtTokenVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MyPageController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyPageController.class);

	@Autowired
	AuctionService auctionService;
	
	@Autowired
    private AdminService adminService;
	
	@Autowired
	private MyPageService myPageService;
	
	@Autowired
    private SessionUtill sessionUtill;
	
	@Autowired
	private FormatUtil formatUtil;
	
	@Autowired
	private DateUtil dateUtil;
	
	@Autowired
	private LoginService loginService;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private CookieUtil cookieUtil;
	
	/**
	 * 나의 경매내역
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@RequestMapping(value = "/my/buy",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView myresults(@RequestParam Map<String,Object> params) throws Exception {
		ModelAndView mav = new ModelAndView("mypage/buy/buy");
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
		BidUserDetails userVo = (BidUserDetails) sessionUtill.getUserVo();
		if(userVo != null) params.put("loginNo", userVo.getTrmnAmnno());
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("naBzPlcNo", params.get("place").toString());

		Map<String,Object> johap = adminService.selectOneJohap(paramMap);
		paramMap.put("naBzPlc", johap.get("NA_BZPLC"));
		
		// 20230113 :: 최근 4주이내 경매일만 검색하도록 변경
		paramMap.put("entryType", "W");
		
		List<Map<String,Object>> datelist= auctionService.selectAucDateList(paramMap);
		paramMap.put("searchDate", datelist.size() > 0 ? datelist.get(0).get("AUC_DT") : null);
		if(userVo != null) {
			paramMap.put("searchTrmnAmnNo", userVo.getTrmnAmnno());
			paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
		}
		paramMap.put("stateFlag", "buy");
		
		// 0. 나의 경매내역 > 구매내역
		mav.addObject("buyCnt",auctionService.selectCountEntry(paramMap));
		mav.addObject("buyList", auctionService.entrySelectList(paramMap));		
		
		// 1. 나의 경매내역 > 응찰내역
		mav.addObject("bidCnt", auctionService.selectBidLogListCnt(paramMap));
		mav.addObject("bidList", auctionService.selectBidLogList(paramMap));
		
		// 2. 나의 경매내역 > 정산서
		paramMap.put("searchDateState", today);
		
		mav.addObject("calendarList", auctionService.selectStateList(paramMap));
		mav.addObject("title",formatUtil.dateAddDotLenSix(today));
		
		//출장우 상세 tab항목 표기
		paramMap.put("simpCGrpSqno", "2");
		mav.addObject("tabList",auctionService.selectListExpitemSet(paramMap));
		
		params.putAll(paramMap);
		
		mav.addObject("johapData", johap);
		mav.addObject("dateList",datelist);
		mav.addObject("inputParam", params);
		mav.addObject("subheaderTitle","나의 경매내역");
		
		return mav;
	}
	/**
	 * 나의 경매내역 - 정산서 리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/auction/api/select/buyList", produces = MediaType.APPLICATION_JSON_VALUE)	
	public Map<String, Object> buyList(@RequestBody final Map<String,Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.putAll(params);
			
			Map<String,Object> johap = adminService.selectOneJohap(paramMap);
			
			// 검색할 날짜 계산처리
			String yyyyMM = "";
			DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMM");
			String searchYm = paramMap.get("searchDateState").toString();
			LocalDate date = ( searchYm == null || searchYm.isEmpty()) ? LocalDate.now() : LocalDate.parse(searchYm+"01",DateTimeFormatter.ofPattern("yyyyMMdd"));
			if ("next".equals(paramMap.get("flag"))) {
				yyyyMM = date.plusMonths(1).format(format);
			}
			else if("prev".equals(paramMap.get("flag"))) {
				yyyyMM = date.minusMonths(1).format(format);
			}
			else {
				yyyyMM = date.format(format);
			}
			
			paramMap.put("stateFlag", "buy");
			if((BidUserDetails) sessionUtill.getUserVo() != null) {
				paramMap.put("searchMbIntgNo", ((BidUserDetails) sessionUtill.getUserVo()).getMbIntgNo());
			}
			paramMap.put("searchTrmnAmnNo", sessionUtill.getUserId());
			
			paramMap.put("searchDateState", yyyyMM);
			params.put("searchDateState", yyyyMM);

			List<Map<String,Object>> calendarList = auctionService.selectStateList(paramMap);
			
			if (calendarList != null) {
				result.put("success", true);
				result.put("message", "조회에 성공했습니다.");
				result.put("info", calendarList);
			} else {
				result.put("success", false);
				result.put("message", "정산내역이 없습니다.");
			}
			
			result.put("inputParam",params);
			result.put("johapData", johap);
			result.put("title",formatUtil.dateAddDotLenSix(yyyyMM));
		} 
		catch (SQLException se) {
			log.error("MyPageController.buyList : {}", se);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		catch (Exception e) {
			log.error("MyPageController.buyList : {}", e);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		
		return result;
	}
	/**
	 * 나의 경매내역 - 정산서 상세 페이지
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@RequestMapping(value = "/my/buyInfo", method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView buyInfo(@RequestParam Map<String,Object> params, HttpServletRequest request) throws Exception {
		final ModelAndView mav = new ModelAndView();

		Map<String, Object> flashMap = (Map<String, Object>)RequestContextUtils.getInputFlashMap(request);
		if(!ObjectUtils.isEmpty(flashMap) && flashMap.containsKey("params")) {
			params.putAll((Map<String, Object>)flashMap.get("params"));
		}
		BidUserDetails userVo = (BidUserDetails)sessionUtill.getUserVo();
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("naBzPlcNo", params.get("searchnaBzPlcNo").toString());
		paramMap.put("naBzplc", "");
		
		Map<String,Object> johap = adminService.selectOneJohap(paramMap);
		paramMap.put("naBzPlc", johap.get("NA_BZPLC")); 
		
		if(userVo != null) paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
		paramMap.put("searchTrmnAmnNo", sessionUtill.getUserId());
		paramMap.put("searchDate", params.get("searchDate"));
		paramMap.put("searchAucObjDsc", params.get("searchAucObjDsc"));
		
		params.put("aucDt", dateUtil.addDelimDate(params.get("searchDate").toString()));

		//매수인정산서 flag insert
		params.put("stateFlag", "buy");
		paramMap.put("stateFlag", "buy");
		
		//조합정보 및 계좌정보 가져오기
		mav.addObject("accountInfo", auctionService.selectJohapAccInfo(paramMap));
		//매수인 정보 조회
		mav.addObject("stateInfo", auctionService.selectStateInfo(paramMap));
		//낙찰가 조회
		mav.addObject("stateTotPrice", auctionService.selectTotSoldPriceAndFee(paramMap));
		//낙찰우 두수 조회
		mav.addObject("stateBuyCnt",auctionService.selectCountEntry(paramMap));
		//상세 조회 리스트
		mav.addObject("list", auctionService.entrySelectList(paramMap));
		
		mav.addObject("johapData", johap);
		mav.addObject("inputParam", params);
		mav.addObject("subheaderTitle", "나의 경매내역");
		mav.setViewName("mypage/buy/buy_info");
		
		return mav;
	}
	
	/**
	 * 중도매인 정산 랜딩 페이지
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@PermitAll
	@GetMapping(value = "/state-acc/mwmn/{naBzplc}/{searchDate}/{trmnAmnno}")
	public ModelAndView stateAccMwmn(HttpServletRequest request
									, HttpServletResponse response
									, @PathVariable Map<String, Object> params
									, RedirectAttributes redirect) throws SQLException {
		final ModelAndView mav = new ModelAndView();
//		final String rtnUrl = request.getRequestURI();
//		this.setTempLoginProc("mwmn", params, response);
		final Map<String,Object> johap = adminService.selectOneJohap(params);
		params.put("searchnaBzPlcNo", johap.get("NA_BZPLCNO"));
//		redirect.addFlashAttribute("params", params);
		redirect.mergeAttributes(params);
//		mav.setView(new RedirectView("/my/buyInfo?place=" + johap.get("NA_BZPLCNO") + "&rtnUrl=" + rtnUrl));
		mav.setView(new RedirectView("/my/buyInfo?place=" + johap.get("NA_BZPLCNO")));
		return mav;
	}
	
	/**
	 * 출하주 정산 랜딩 페이지
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@PermitAll
	@GetMapping(value = "/state-acc/fhs/{naBzplc}/{searchDate}/{fhsIdNo}")
	public ModelAndView stateAccFhs(HttpServletRequest request
									, HttpServletResponse response
									, @PathVariable Map<String, Object> params
									, RedirectAttributes redirect) throws SQLException {
		final ModelAndView mav = new ModelAndView();
//		final String rtnUrl = request.getRequestURI();
//		this.setTempLoginProc("fhs", params, response);
		final Map<String,Object> johap = adminService.selectOneJohap(params);
		params.put("searchnaBzPlcNo", johap.get("NA_BZPLCNO"));
//		redirect.addFlashAttribute("params", params);
		redirect.mergeAttributes(params);
//		mav.setView(new RedirectView("/my/entryInfo?place=" + johap.get("NA_BZPLCNO") + "&rtnUrl=" + rtnUrl));
		mav.setView(new RedirectView("/my/entryInfo?place=" + johap.get("NA_BZPLCNO")));
		return mav;
	}

	/**
	 * 정산 페이지 접속을 위한 임시 로그인 처리
	 * @param string
	 * @param params
	 * @throws SQLException 
	 */
	@SuppressWarnings("unused")
	private void setTempLoginProc(String type, Map<String, Object> params, HttpServletResponse response) throws SQLException {
		if ("fhs".equals(type)) {
			String[] fhsIdNo = params.getOrDefault("fhsIdNo", "_").toString().split("_");
			params.put("fhsIdNo", fhsIdNo[0]);
			params.put("farmAmnno", fhsIdNo[1]);
		}
		
		final Map<String, Object> info = "mwmn".equals(type) ? loginService.selectWholesaler(params) : loginService.selectFarmUser(params);
		
		JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
										.auctionHouseCode(info.get("NA_BZPLC").toString())
										.userMemNum(info.get("TRMN_AMNNO").toString())
										.userRole("mwmn".equals(type) ? Constants.UserRole.BIDDER : Constants.UserRole.FARM)
										.mbIntgNo(info.getOrDefault("MB_INTG_NO", "").toString())			//회원통합번호 추가
										.build();
		
		String token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
		Cookie cookie = cookieUtil.createCookie(Constants.JwtConstants.ACCESS_TOKEN, token);
		response.addCookie(cookie);
		response.setHeader(HttpHeaders.AUTHORIZATION, Constants.JwtConstants.BEARER + token);
	}
	/**
	 * 나의 경매내역 - 구매내역 리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(path = "/auction/api/select/myBuyList", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> myBuyList(@RequestBody Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
         params.put("loginNo", sessionUtill.getUserId());
        result.put("success", true);
        try {        	
        	if(params.get("loginNo") != null) params.put("searchTrmnAmnNo", params.get("loginNo"));
        	List<Map<String,Object>> list=auctionService.entrySelectList(params);
        	result.put("totPrice", auctionService.selectTotSoldPrice(params));
        	result.put("buyCnt",auctionService.selectCountEntry(params));
            result.put("data", list);
        }catch (SQLException | RuntimeException re) {
            result.put("success", false);
            result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			log.error("MyPageController.selectMyBuyList : {} ",re);
		}
        return result;
    }
	/**
	 * 나의 경매내역 - 응찰내역 리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(path = "/auction/api/select/myBidList", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> myBidList(@RequestBody Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        try {        	
        	if(params.get("loginNo") != null) params.put("searchTrmnAmnNo", params.get("loginNo"));
        	List<Map<String,Object>> list=auctionService.selectBidLogList(params);
            result.put("data", list);
        }catch (SQLException | RuntimeException re) {
            result.put("success", false);
            result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			log.error("MyPageController.myBidList : {} ",re);
		}
        return result;
    }
	
	/**
	 * 정산서 수수료 상세 팝업
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(path = "/auction/api/select/myFeeStateInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> myFeeStateInfo(@RequestBody Map<String,Object> params) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		try {        	
			Map<String, Object> paramMap = new HashMap<>();
			
			if ("entry".equals(params.get("stateFlag"))) {
				FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
				if(userVo != null) {
					paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
					params.put("searchFhsIdNo", userVo.getFhsIdNo());
					params.put("searchFarmAmnno", userVo.getFarmAmnno());
				}
				paramMap.put("feeFlag", "Y");
			} else {
				BidUserDetails userVo = (BidUserDetails)sessionUtill.getUserVo();
				if(userVo != null) {
					paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
				}
				paramMap.put("searchTrmnAmnno", sessionUtill.getUserId());
			}
			
			paramMap.put("naBzPlcNo", params.get("searchnaBzPlcNo").toString());
			paramMap.put("naBzplc", "");
			
			Map<String,Object> johap = adminService.selectOneJohap(paramMap);
			paramMap.put("naBzPlc", johap.get("NA_BZPLC"));
			paramMap.put("naBzPlcNo", johap.get("NA_BZPLCNO"));
			
			paramMap.putAll(params);

			//수수료 상세 조회
			result.put("feeList", auctionService.myFeeStateList(paramMap));
			
		}catch (SQLException | RuntimeException re) {
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			log.error("MyPageController.myFeeStateInfo : {} ",re);
		}
		return result;
	}
	/**
	 * 나의 경매내역 - 정산서 상세 정보 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(path = "/auction/api/select/myStateInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> myStateInfo(@RequestBody Map<String,Object> params) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		try {     
			if ("entry".equals(params.get("stateFlag"))) {
				FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
				if(userVo != null) {
					params.put("searchMbIntgNo", userVo.getMbIntgNo());
					params.put("searchFhsIdNo", userVo.getFhsIdNo());
					params.put("searchFarmAmnno", userVo.getFarmAmnno());
				}
				params.put("aucYnState", "1");
				params.put("feeFlag", "Y");
			} else {
				BidUserDetails userVo = (BidUserDetails)sessionUtill.getUserVo();
				if(userVo != null) {
					params.put("searchMbIntgNo", userVo.getMbIntgNo());
					params.put("searchTrmnAmnNo", userVo.getTrmnAmnno());
				}
			}
			
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("naBzPlcNo", params.get("searchnaBzPlcNo").toString());
			paramMap.put("naBzplc", "");
			
			Map<String,Object> johap = adminService.selectOneJohap(paramMap);
			paramMap.put("naBzPlc", johap.get("NA_BZPLC"));
			paramMap.put("naBzPlcNo", johap.get("NA_BZPLCNO"));
			
			paramMap.putAll(params);
			
        	//매수인 정보 조회
        	result.put("stateInfo", auctionService.selectStateInfo(paramMap));
        	//낙찰가 조회
        	result.put("stateTotPrice", auctionService.selectTotSoldPriceAndFee(paramMap));
            //낙찰우 두수 조회
        	if ("entry".equals(paramMap.get("stateFlag"))) {
        		result.put("stateBuyCnt",auctionService.selectStateEntryCntFhs(paramMap));
        	} else {
        		result.put("stateBuyCnt",auctionService.selectCountEntry(paramMap));
        	}
            //상세 조회 리스트
            result.put("list", auctionService.entrySelectList(paramMap));
            
        }catch (SQLException | RuntimeException re) {
            result.put("success", false);
            result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			log.error("MyPageController.myStateInfo : {} ",re);
		}
        return result;
    }
	
	/**
	 * My현황 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(path = "/auction/api/select/myMenu", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> myMenu(@RequestBody Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        try {        	
        	Map<String, Object> paramMap = new HashMap<>();
        	
        	// 검색할 날짜 계산처리
			String yyyyMM = "";
			String searchYm = params.get("searchDate").toString();
			DateTimeFormatter format = ("M".equals(params.get("ymFlag").toString())) 
												? DateTimeFormatter.ofPattern("yyyyMM")
												: DateTimeFormatter.ofPattern("yyyy");
			
			LocalDate date = LocalDate.now();
			
			if (searchYm != null && !searchYm.isEmpty()) {
				if (searchYm.length() == 4) {
					date = LocalDate.parse(searchYm+"0101",DateTimeFormatter.ofPattern("yyyyMMdd"));
				} else {
					date = LocalDate.parse(searchYm+"01",DateTimeFormatter.ofPattern("yyyyMMdd"));
				}
			}
			
			if ("M".equals(params.get("ymFlag"))) {
				if ("next".equals(params.get("flag"))) {
					yyyyMM = date.plusMonths(1).format(format);
				}
				else if("prev".equals(params.get("flag"))) {
					yyyyMM = date.minusMonths(1).format(format);	        	
				}
				else {
					yyyyMM = date.format(format);
				}				
			} else {
				if ("next".equals(params.get("flag"))) {
					yyyyMM = date.plusYears(1).format(format);
				}
				else if("prev".equals(params.get("flag"))) {
					yyyyMM = date.minusYears(1).format(format);	        	
				}
				else {
					yyyyMM = date.format(format);
				}
			}
			
			paramMap.putAll(params);
			paramMap.put("searchDateMy", ("M".equals(params.get("ymFlag")) ? yyyyMM : yyyyMM.substring(0, 4)));
			
			// 다른 조합 검색
			paramMap.put("searchNaBzplc", paramMap.get("naBzplc").toString() == params.get("johpCd").toString() ? paramMap.get("naBzplc").toString() : params.get("johpCd").toString());
			Map<String, Object> johapTmpMap = new HashMap<>();
			johapTmpMap.put("naBzplc", paramMap.get("searchNaBzplc"));
			result.put("myJohapData", adminService.selectOneJohap(johapTmpMap));
			
			if ("buy".equals(params.get("stateFlag"))) {
				BidUserDetails userVo = ((BidUserDetails) sessionUtill.getUserVo());
				if(userVo != null) {
					paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
				}
	    		paramMap.put("searchTrmnAmnNo", sessionUtill.getUserId());
	    		
	    		// > 연/월단위 기준 전체 낙찰두수, 송아지, 비육우, 번식우의 낙찰두수
	    		result.put("cowBidCnt", myPageService.selectCowBidCnt(paramMap));
	    		// > 내가 bid한 내역의 숫자와 낙찰 퍼센트 (경매대상별로)
	    		result.put("cowBidPercent", myPageService.selectCowBidPercent(paramMap));
	    		// > 전체 조합의 나의 응찰 현황
	    		result.put("cowBidCntList", myPageService.selectListAucBidCntAll(paramMap));
	    		
			} else {
				FarmUserDetails userVo = ((FarmUserDetails) sessionUtill.getUserVo());
				paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
	    		paramMap.put("searchFhsIdNo", ((FarmUserDetails) sessionUtill.getUserVo()).getFhsIdNo());
	    		paramMap.put("searchFarmAmnno", ((FarmUserDetails) sessionUtill.getUserVo()).getFarmAmnno());
	    		
	    		// > 연/월단위 기준 전체 출장두수, 송아지, 비육우, 번식우의 낙찰두수, 낙찰평균, 평균시세
	    		result.put("cowEntryCnt", myPageService.selectCowEntryCnt(paramMap));
	    		// > 전체 조합의 나의 출장우 현황
	    		result.put("cowEntryCntList", myPageService.selectListAucEntryCntAll(paramMap));
			}
			
    		result.put("inputParam", paramMap);    		
            
        }catch (SQLException | RuntimeException re) {
            result.put("success", false);
            result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			log.error("MyPageController.myMenu : {} ",re);
		}
        return result;
    }

	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@RequestMapping(value = "/my/bid",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView mybidding() throws Exception {
		// 나의응찰내역
		LOGGER.debug("start of bid.do");
		ModelAndView mav = new ModelAndView();
		mav.addObject("subheaderTitle","응찰내역");
		mav.setViewName("mypage/bid/bid");
		return mav;
	}
	
	/**
	 * 나의 출장우
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ROLE_FARM')")
	@RequestMapping(value = "/my/entry",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView entry(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mav = new ModelAndView("mypage/entry/entry");
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
		FarmUserDetails userVo = (FarmUserDetails) sessionUtill.getUserVo();
		if(userVo != null) params.put("loginNo", userVo.getFhsIdNo());
		
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("naBzPlcNo", params.get("place"));

		Map<String,Object> johap = adminService.selectOneJohap(paramMap);
		paramMap.put("naBzPlc", johap.get("NA_BZPLC"));
		
		// 20230113 :: 최근 4주이내 경매일만 검색하도록 변경
		paramMap.put("entryType", "W");
		
		List<Map<String,Object>> datelist= auctionService.selectAucDateList(paramMap);
		paramMap.put("searchDate", datelist.size() > 0 ? datelist.get(0).get("AUC_DT") : null);
		if(userVo != null) {
			paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
			paramMap.put("searchFhsIdNo", userVo.getFhsIdNo());
			paramMap.put("searchFarmAmnno", userVo.getFarmAmnno());
		}
		
		paramMap.putAll(params);
		paramMap.put("stateFlag", "entry");
		
		// 0. 나의 출장우 > 출장우
		mav.addObject("myEntryList", auctionService.entrySelectList(paramMap));
		
		// 1. 나의 출장우 > 정산서
		paramMap.put("searchDateState", today);
		
		mav.addObject("calendarList", auctionService.selectStateList(paramMap));
		
		//출장우 상세 tab항목 표기
		paramMap.put("simpCGrpSqno", "2");
		mav.addObject("tabList",auctionService.selectListExpitemSet(paramMap));
		
		params.putAll(paramMap);
		
 		mav.addObject("johapData", johap);
		mav.addObject("dateList",datelist);
		mav.addObject("inputParam", params);
		mav.addObject("title",formatUtil.dateAddDotLenSix(today));
		mav.addObject("subheaderTitle","나의 출장우");
 		
		return mav;
	}
	
	/**
	 * 나의 출장우 - 출장우 리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(path = "/auction/api/select/myEntryBidList", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> myEntryBidList(@RequestBody Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        try {        	
        	FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
        	if(userVo != null) params.put("searchFhsIdNo", userVo.getFhsIdNo());
    		if(userVo != null) params.put("searchFarmAmnno", userVo.getFarmAmnno());
        	List<Map<String,Object>> list=auctionService.entrySelectList(params);
            result.put("data", list);
        }catch (SQLException | RuntimeException re) {
            result.put("success", false);
            result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			log.error("MyPageController.selectMyBuyList : {} ",re);
		}
        return result;
    }
	
	/**
	 * 나의 출장우 - 정산서 리스트 조회
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/auction/api/select/entryList", produces = MediaType.APPLICATION_JSON_VALUE)	
	public  Map<String, Object> entryList(@RequestBody final Map<String,Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.putAll(params);
			
			Map<String,Object> johap = adminService.selectOneJohap(paramMap);
			
			// 검색할 날짜 계산처리
			String yyyyMM = "";
	        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMM");
	     	String searchYm = paramMap.get("searchDateState").toString();
	     	LocalDate date = ( searchYm == null || searchYm.isEmpty()) ? LocalDate.now() : LocalDate.parse(searchYm+"01",DateTimeFormatter.ofPattern("yyyyMMdd"));
	        if ("next".equals(paramMap.get("flag"))) {
		        yyyyMM = date.plusMonths(1).format(format);
	        }
	        else if("prev".equals(paramMap.get("flag"))) {
		        yyyyMM = date.minusMonths(1).format(format);	        	
	        }
	        else {
		        yyyyMM = date.format(format);
	        }
	        
	        FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
	        paramMap.put("stateFlag", "entry");
	        if(userVo != null) {
	        	paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
	        	paramMap.put("searchFhsIdNo", userVo.getFhsIdNo());
	        	paramMap.put("searchFarmAmnno", userVo.getFarmAmnno());
	        }
			
			paramMap.put("searchDateState", yyyyMM);
			params.put("searchDateState", yyyyMM);
			
			List<Map<String,Object>> calendarList = auctionService.selectStateList(paramMap);
			if (calendarList != null) {
				result.put("success", true);
				result.put("message", "조회에 성공했습니다.");
				result.put("info", calendarList);
			}
			else {
				result.put("success", false);
				result.put("message", "정산 내역이 없습니다.");
			}
			
			result.put("inputParam",params);
			result.put("johapData", johap);
			result.put("title",formatUtil.dateAddDotLenSix(yyyyMM));
		} 
		catch (SQLException se) {
			log.error("MyPageController.entryList : {}", se);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		catch (Exception e) {
			log.error("MyPageController.entryList : {}", e);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		return result;
	}
	
	/**
	 * 나의 출장우 - 정산서 상세 페이지
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@PreAuthorize("hasRole('ROLE_FARM')")
	@RequestMapping(value = "/my/entryInfo",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView entryInfo(@RequestParam Map<String,Object> params, HttpServletRequest request) throws Exception {
		final ModelAndView mav = new ModelAndView();
		
		Map<String, Object> flashMap = (Map<String, Object>)RequestContextUtils.getInputFlashMap(request);
		if(!ObjectUtils.isEmpty(flashMap) && flashMap.containsKey("params")) {
			params.putAll((Map<String, Object>)flashMap.get("params"));
		}
		
		FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
		
		Map<String,Object> map = new HashMap<>();
		map.put("naBzPlcNo", params.get("searchnaBzPlcNo").toString()); 
		map.put("naBzplc", "");

		Map<String,Object> johap = adminService.selectOneJohap(map);
		map.put("naBzPlc", johap.get("NA_BZPLC")); 
		map.put("searchDate", params.get("searchDate"));
		map.put("searchAucObjDsc", params.get("searchAucObjDsc"));
		map.put("aucYnState", "1");
		
		if(userVo != null) {
			map.put("searchMbIntgNo", userVo.getMbIntgNo());
			map.put("searchFhsIdNo", userVo.getFhsIdNo());
			map.put("searchFarmAmnno", userVo.getFarmAmnno());
		}
		
		params.put("aucDt", dateUtil.addDelimDate(params.get("searchDate").toString()));
		//출하우정산서 flag insert
		params.put("stateFlag", "entry");
		map.put("stateFlag", "entry");
		map.put("feeFlag", "Y");
		
		//조합정보 및 계좌정보 가져오기
		mav.addObject("accountInfo", auctionService.selectJohapAccInfo(map));
		//출하우 정보 조회
		mav.addObject("stateInfo", auctionService.selectStateInfo(map));
		//낙찰가 조회
		mav.addObject("stateTotPrice", auctionService.selectTotSoldPriceAndFee(map));
		//낙찰우 두수 조회
		mav.addObject("stateBuyCnt",auctionService.selectStateEntryCntFhs(map));			
		//상세 조회 리스트
		mav.addObject("list", auctionService.entrySelectList(map));

		mav.addObject("johapData", johap);
		mav.addObject("inputParam", params);
		mav.addObject("subheaderTitle", "나의 출장우");
		mav.setViewName("mypage/entry/entry_info");
		return mav;
	}
	
	/**
	 * 1. 단일경매에서 호출시 찜 가격을 조회한다.
	 * 2. 일괄경매에서 호출시 이전 응찰 가격과 찜 가격을 동시에 조회한다. 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@PostMapping(value = "/my/favorite", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> myFavorite(@RequestBody final Map<String, Object> params) throws Exception {
		final Map<String, Object> result = new HashMap<String, Object>();
		try {
			String groupYn = params.getOrDefault("groupYn", "N").toString();
			Map<String, Object> favorite = new HashMap<String, Object>();
			if ("Y".equals(groupYn)) {
				Map<String,Object> map = auctionService.selectNearAtdrAm(params);
				Map<String,Object> zim = auctionService.selectMyZimPrice(params);
				favorite.put("ATDR_AM", map != null ? map.get("ATDR_AM"):0);
				favorite.put("SBID_UPR", zim != null ? zim.get("SBID_UPR"):0);
			}
			else {
				favorite = auctionService.selectMyFavoriteInfo(params);
			}
			
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
		}catch (SQLException | RuntimeException re) {
			log.error("MyPageController.myFavorite : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}

	/**
	 * 나의 정보 페이지 - 로그인 후 접근하는 페이지
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/my/info",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView myInfo(@RequestParam Map<String,Object> params) throws Exception {
		
		LOGGER.debug("start of my/info.do");
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String place = (String) params.get("place");
        mav.addObject("naBzPlcNo", place); // 쿼리스트링 의 지역번호 가져와서 셋팅.
        
        map.put("place", place);
        map.put("flagAplyConfirm", "Y");
        if(sessionUtill.getNaBzplc() != null) map.put("naBzPlc", sessionUtill.getNaBzplc());
		if(sessionUtill.getUserId() != null) map.put("loginNo", sessionUtill.getUserId());
		Map<String, Object> aplyInfo = null;
		
		if("ROLE_BIDDER".equals(sessionUtill.getRoleConfirm())) {
			aplyInfo = auctionService.selectMySecAplyInfo(map);
		}
		
		if (aplyInfo == null) {
			mav.addObject("secAplyPossible", "Y");
		}
		else {
			mav.addObject("secAplyPossible", "N");
		}
		
		//로그인된 역할 확인하여 각 테이블 조회
		Map<String, Object> authNoYmd = new HashMap<String, Object>(); 
		if(sessionUtill.getRoleConfirm() != null) {
			if("ROLE_BIDDER".equals(sessionUtill.getRoleConfirm())) {
				authNoYmd = auctionService.selectMwmnAuthNoYmdInfo(map);
			}
			else if("ROLE_FARM".equals(sessionUtill.getRoleConfirm())) {
				if(sessionUtill.getUserVo() != null) {
					FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
					map.put("farmAmnno", userVo.getFarmAmnno());
				}
				authNoYmd = auctionService.selectFhsAuthNoYmdInfo(map);
			}
		}
		mav.addObject("authNoYmd", authNoYmd);
		
		LocalDateTime date = LocalDateTime.now();
		String today = date.format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
		mav.addObject("today", today);
		mav.addObject("subheaderTitle","나의정보");
		mav.setViewName("mypage/info/myInfo");
		return mav;
	}
	
	/**
	 * 해당 조합에 이용해지 신청이 되어 있는지 체크하는 메소드
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@PostMapping(value = "/my/mySecAplyCheck", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> mySecAplyCheck(@RequestBody final Map<String, Object> params) throws Exception {
		final Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> aplyInfo = new HashMap<String, Object>();

		try {
			if(sessionUtill.getNaBzplc() != null) params.put("naBzPlc", sessionUtill.getNaBzplc());
			if(sessionUtill.getUserId() != null) params.put("loginNo", sessionUtill.getUserId());
			aplyInfo = auctionService.selectMySecAplyInfo(params);
			
			if (aplyInfo == null) {
				result.put("success", true);
				result.put("message", "이용해지 미신청 상태입니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "이미 이용해지 신청을 하신 상태입니다.");
			}
			return result;
			
		}catch (SQLException | RuntimeException re) {
			log.error("MyPageController.mySecAplyCheck : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}
	
	/**
	 * 이용해지 신청 페이지 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@RequestMapping(value = "/my/secAply",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView my_secAply(@RequestParam Map<String,Object> params) throws Exception {
		LOGGER.debug("start of secAply.do");
		ModelAndView mav = new ModelAndView();
		
		String place = (String) params.get("place");
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", place);
		Map<String,Object> johap = adminService.selectOneJohap(map);
		mav.addObject("johapData", johap);
		
		if(sessionUtill.getUserId() != null) params.put("loginNo", sessionUtill.getUserId()); 
		if(sessionUtill.getUserId() != null) map.put("mbIntgNo", ((BidUserDetails) sessionUtill.getUserVo()).getMbIntgNo() ); 
		
		List<Map<String,Object>>  johqpList= auctionService.selectJohqpList(map);
		mav.addObject("johqpList", johqpList);

		
		mav.addObject("inputParam", params);

		mav.addObject("subheaderTitle","이용해지 신청");
 		mav.setViewName("mypage/sec/secAply");
		return mav;
	}
	
	/**
	 * 이용해지 데이터 insert 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@PostMapping(value = "/my/ins_mySecAply", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> ins_mySecAply(@RequestBody final Map<String, Object> params) throws Exception {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			BidUserDetails userVo = (BidUserDetails) sessionUtill.getUserVo();
			params.put("naBzPlc", StringUtils.isEmpty(params.get("johpCd").toString()) ? sessionUtill.getNaBzplc() : params.get("johpCd"));
			params.put("loginNo", StringUtils.isEmpty(params.get("loginNo").toString()) ? sessionUtill.getUserId() : params.get("loginNo"));
			if(userVo != null) {
				params.put("mbIntgNo", userVo.getMbIntgNo());
			}
			
			auctionService.insertMySecAplyInfo(params);
			result.put("success", true);
			result.put("message", "이용해지 신청이 완료되었습니다.<br/>해당 조합 관리자의 확인 후 최종적으로 이용해지가 이뤄질 예정입니다.");
			
			return result;
			
		}catch (SQLException | RuntimeException re) {
			log.error("MyPageController.mySecAplyCheck : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}
	
	/**
	 * 이용해지 철회가 가능한 상태인지 확인하는 메소드
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@PostMapping(value = "/my/mySecWithdrawCheck", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> mySecWithdrawCheck(@RequestBody final Map<String, Object> params) throws Exception {
		final Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> aplyInfo = new HashMap<String, Object>();

		try {
			if(sessionUtill.getNaBzplc() != null) params.put("naBzPlc", sessionUtill.getNaBzplc());
			if(sessionUtill.getUserId() != null) params.put("loginNo", sessionUtill.getUserId());
			aplyInfo = auctionService.selectMySecAplyInfo(params);
			
			if (aplyInfo == null) {
				result.put("success", false);
				result.put("message", "이용해지 미신청 상태입니다.");
			}
			else {
				if(!"0".equals(aplyInfo.get("MGR_APPR_YN"))) {
					result.put("success", false);
					result.put("message", "이미 이용해지 신청이 완료되어 철회 불가능한 상태입니다.");
				}else {
					result.put("success", true);
					result.put("message", "이용해지 철회 가능한 상태입니다.");
				}
			}
			return result;
			
		}catch (SQLException | RuntimeException re) {
			log.error("MyPageController.mySecAplyCheck : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}
	
	/**
	 * 이용해지 신청 철회 페이지 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@RequestMapping(value = "/my/secWithdraw",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView secWithdraw(@RequestParam Map<String,Object> params) throws Exception {
		LOGGER.debug("start of secWithdraw.do");
		ModelAndView mav = new ModelAndView();
		
		String place = (String) params.get("place");
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", place);
		Map<String,Object> johap = adminService.selectOneJohap(map);
		mav.addObject("johapData", johap);
		
		if(sessionUtill.getUserId() != null) params.put("loginNo", sessionUtill.getUserId());
		mav.addObject("inputParam", params);

		mav.addObject("subheaderTitle","이용해지 철회");
 		mav.setViewName("mypage/sec/secWithdraw");
		return mav;
	}
	
	/**
	 * 이용해지 신청 철회기능 구현을 위해
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@PostMapping(value = "/my/del_mySecAply", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> del_mySecAply(@RequestBody final Map<String, Object> params) throws Exception {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			BidUserDetails userVo = (BidUserDetails) sessionUtill.getUserVo();
			params.put("naBzPlc", StringUtils.isEmpty(params.get("johpCd").toString()) ? sessionUtill.getNaBzplc() : params.get("johpCd"));
			params.put("loginNo", StringUtils.isEmpty(params.get("loginNo").toString()) ? sessionUtill.getUserId() : params.get("loginNo")); 
			params.put("mbIntgNo", userVo.getMbIntgNo());
			
			Map<String, Object> aplyInfo = auctionService.selectMySecAplyInfo(params);
			
			if (aplyInfo == null) {
				result.put("success", false);
				result.put("message", "이용해지 미신청 상태입니다.");
			}
			else {
				if(!"0".equals(aplyInfo.get("MGR_APPR_YN"))) {
					result.put("success", false);
					result.put("message", "이미 이용해지 신청이 완료되어 철회 불가능한 상태입니다.");
				}else {
					//delete 해야함
					auctionService.deleteMySecAplyInfo(params);
					result.put("success", true);
					result.put("message", "해지 신청 철회가 완료되었습니다.");
				}
			}
			
			return result;
			
		}catch (SQLException | RuntimeException re) {
			log.error("MyPageController.del_mySecAply : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}
	
	/**
	 * 키오스크 인증번호 발급하기
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(value = "/my/myAuthNumIssue", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> myAuthNumIssue(@RequestBody final Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			result = auctionService.myAuthNumIssue(params);
			return result;
			
		}catch (SQLException | RuntimeException re) {
			log.error("MyPageController.myAuthNumIssue : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
	}
}
