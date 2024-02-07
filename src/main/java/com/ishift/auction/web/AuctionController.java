package com.ishift.auction.web;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ishift.auction.util.FormatUtil;
import com.ishift.auction.util.HttpUtils;
import com.ishift.auction.vo.BidUserDetails;
import com.ishift.auction.vo.FarmUserDetails;
import com.ishift.auction.vo.JwtTokenVo;

import lombok.extern.slf4j.Slf4j;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.service.common.CommonService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.util.SessionUtill;

@Slf4j
@RestController 
public class AuctionController extends CommonController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuctionController.class);
	
	@Autowired
	private AuctionService auctionService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private SessionUtill sessionUtill;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private FormatUtil formatUtil;
	
	@Autowired
	HttpUtils httpUtils;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value = "/results",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView results(@RequestParam Map<String, Object> param) throws Exception {		
		// 경매결과목록
		LOGGER.debug("start of results.do");
		ModelAndView mav = new ModelAndView();
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", param.get("place"));
        Map<String, Object> johap = adminService.selectOneJohap(map);
		map.put("naBzPlc", johap.get("NA_BZPLC"));
		map.put("naBzplc", johap.get("NA_BZPLC"));
		map.put("entryType", "A");
		
		LocalDateTime date = LocalDateTime.now();
		String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")); 

		/* 20221125 jjw : 출장우 조회시(경매 예정)
		 * 최대일자 현재일자 +7에서 벗어나지않도록수정
		 */
		map.put("searchDate", today); 
		map.put("entryType", "A");
		Map<String, Object> nearAucDate =auctionService.selectNearAucDate(map);
  
		List<Map<String,Object>> datelist=auctionService.selectAucDateList(map); 
		if(param.get("searchDate") != null && param != null) {
			map.put("searchDate", param.get("searchDate"));
		}
//		else if(nearAucDate != null){ 
//			map.put("searchDate", nearAucDate.get("AUC_DT"));
//		}
		else {
			map.put("searchDate",datelist.size() > 0 ? datelist.get(0).get("AUC_DT") :null);
		}
		
		if(map.get("searchDate") != null) param.put("searchDate", map.get("searchDate")); 
		
		if(param.get("searchLowsAm") != null) map.put("searchLowsAm", param.get("searchLowsAm"));
		if(param.get("searchBidAm") != null) map.put("searchBidAm", param.get("searchBidAm"));
		if(param.get("searchBirthDay") != null) map.put("searchBirthDay", param.get("searchBirthDay"));
		
		map.putAll(param);		
	
		List<Map<String,Object>> list=auctionService.entrySelectList(map);
		
		mav.addObject("inputParam", param);
		mav.addObject("johapData", johap);
		mav.addObject("dateList",datelist);
		mav.addObject("resultList",list); 
		mav.addObject("resultList",list);
		mav.addObject("buyCnt",auctionService.selectCountEntry(map));
		mav.addObject("subheaderTitle","경매결과 조회");
		
		mav.setViewName("auction/results/resultList");
		return mav;
	}

	@RequestMapping(value = "/sales",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView sales(@RequestParam Map<String,Object> param) throws Exception {
		// 경매예정목록
		String place = (String) param.get("place"); 
		LOGGER.debug("start of sales.do"); 
		ModelAndView mav = new ModelAndView();
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", place); 

		Map<String,Object> johap = adminService.selectOneJohap(map);

		LocalDateTime date = LocalDateTime.now();
		String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd")); 

		/* 20221125 jjw : 출장우 조회시(경매 예정)
		 * 최대일자 현재일자 +7에서 벗어나지않도록수정
		 */
		map.put("searchDate", today); 
		map.put("entryType", "B");
		Map<String, Object> nearAucDate =auctionService.selectNearAucDate(map);
  
		map.put("naBzPlc", johap.get("NA_BZPLC")); 
		map.put("naBzplc", johap.get("NA_BZPLC"));
		
		//sysdate로 쿼리 조회하기에 searchDate바라보지 않음.
		//searchDate기준 검색시 쿼리수정 필요
		List<Map<String,Object>> datelist=auctionService.selectAucDateList(map); 
		if(param.get("searchDate") != null && param != null) {
			map.put("searchDate", param.get("searchDate"));
		}else if(nearAucDate != null){ 
			map.put("searchDate", nearAucDate.get("AUC_DT"));  
		}else {
			map.put("searchDate",datelist.size() > 0 ? datelist.get(0).get("AUC_DT") :null);
		}
		
		if(map.get("searchDate") != null) param.put("searchDate", map.get("searchDate")); 
		
		if(param.get("searchLowsAm") != null) map.put("searchLowsAm", param.get("searchLowsAm"));
		if(param.get("searchBidAm") != null) map.put("searchBidAm", param.get("searchBidAm"));
		if(param.get("searchBirthDay") != null) map.put("searchBirthDay", param.get("searchBirthDay"));
		
		// 출장우 조회 파라미터
		map.putAll(param); 
		
		map.put("loginNo", sessionUtill.getUserId()); 
		List<Map<String,Object>> list=auctionService.entrySelectList(map);

//		for(Map<String,Object> entry : list) {
//			String birthMonth = this.getConvertBirthDay(this.getStringValue(entry.get("BIRTH")));
//			entry.put("BIRTH_MONTH", birthMonth);
//		} 
		if(sessionUtill.getUserId() != null) param.put("loginNo", sessionUtill.getUserId());
		param.put("authRole", sessionUtill.getRoleConfirm());
		mav.addObject("johapData", johap);
		mav.addObject("subheaderTitle","출장우 조회");
		mav.addObject("dateList",datelist); 
		mav.addObject("salesList",list);
		mav.addObject("inputParam", param);
		mav.addObject("buyCnt",auctionService.selectCountEntry(map));
		mav.addObject("subheaderTitle","출장우 조회");
		// mav.addObject("today",date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		mav.setViewName("auction/sales/saleList");
		return mav;
	}

	@RequestMapping(value = "/calendar",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView calendar(@RequestParam Map<String,Object> param) throws Exception {
		// 경매일정
		LOGGER.debug("start of calendar.do");        
		ModelAndView mav = new ModelAndView();
		try {
	        String yyyyMM="";
	        DateTimeFormatter format =DateTimeFormatter.ofPattern("yyyyMM");
        	String searchYm = (String) param.get("searchYm");
        	LocalDate date = (searchYm == null || searchYm.isEmpty())?LocalDate.now():LocalDate.parse(searchYm+"01",DateTimeFormatter.ofPattern("yyyyMMdd"));
	        if("next".equals(param.get("flag"))) {
		        yyyyMM = date.plusMonths(1).format(format);
	        }else if("prev".equals(param.get("flag"))) {
		        yyyyMM = date.minusMonths(1).format(format);
	        }else {
		        yyyyMM = date.format(format);
	        }
			Map<String,Object> map = new HashMap<>();
			map.put("naBzPlcNo", param.get("place"));
			map.put("searchAucDt", yyyyMM);
			List<Map<String,Object>> list = auctionService.selectCalendarList(map);
			mav.addObject("resultList",list);
			param.put("searchYm", yyyyMM);
			param.put("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
			mav.addObject("paramVo",param);
			mav.addObject("title",formatUtil.dateAddDotLenSix(yyyyMM));
		}catch (RuntimeException re) {
			log.error("AuctionController.calendar : {} ",re);
		}catch (SQLException se) {
			log.error("AuctionController.calendar : {} ",se);
		}
		mav.addObject("subheaderTitle","경매일정");
		mav.setViewName("auction/calendar/calendar");
		return mav;
	}
	
	@RequestMapping(value = "/guide",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView guide() throws Exception {
		// 경매안내
		LOGGER.debug("start of guide.do");
		ModelAndView mav = new ModelAndView();
		mav.addObject("subheaderTitle","이용안내");
		mav.setViewName("auction/guide/guide");
		return mav;
	}

	@RequestMapping(value = "/watch",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView watch(@RequestParam(name = "place", required = false) String place) throws Exception {
		// 경매관전
		LOGGER.debug("start of watch.do");
		ModelAndView mav = new ModelAndView();
		try {
	        LocalDateTime date = LocalDateTime.now();
	        Map<String,Object> map = new HashMap<String,Object>();
	        String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	        map.put("searchDate", today);
	        map.put("naBzPlcNo", place);
	        
	        Map<String,Object> johap = adminService.selectOneJohap(map);
			List<Map<String,Object>> list=auctionService.entrySelectList(map);
			Map<String,Object> count =auctionService.selectCountEntry(map);
			//Map<String,Object> calendar =auctionService.selectCalendarList(map).get(0);
	
	        JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
					.auctionHouseCode(johap.get("NA_BZPLC").toString())
					.userMemNum("WATCHER")
					.userRole(Constants.UserRole.WATCHER)
					.build();
	        String token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);

	        mav.addObject("johapData",johap);
        	// mav.addObject("dateVo",calendar);
	        mav.addObject("dateVo",auctionService.selectNearAucDate(map));
	        mav.addObject("watchList",list);
	        mav.addObject("watchCount",count);
	        mav.addObject("watchToken",token);
	        mav.addObject("subheaderTitle","경매관전");
	        mav.addObject("today",date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		}catch (RuntimeException re) {
			log.error("AuctionController.watch : {} ",re);
		}catch (SQLException se) {
			log.error("AuctionController.watch : {} ",se);
		}
		mav.setViewName("auction/watch/watch"+"_agora");
		return mav;
	}
	
	/**
	 * App 관전 페이지 ( 방송송출 영역 없이 출장 리스트만 노출 )
	 * @param place
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/watchApp",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView watchApp(@RequestParam(name = "place", required = false) String place) throws Exception {
		// 경매관전
		LOGGER.debug("start of watchApp.do");
		ModelAndView mav = new ModelAndView();
		try {
			LocalDateTime date = LocalDateTime.now();
			Map<String,Object> map = new HashMap<String,Object>();
			String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			map.put("searchDate", today);
			map.put("naBzPlcNo", place);
			
			Map<String,Object> johap = adminService.selectOneJohap(map);
			List<Map<String,Object>> list=auctionService.entrySelectList(map);
			Map<String,Object> count =auctionService.selectCountEntry(map);
			
			mav.addObject("johapData",johap);
			mav.addObject("dateVo",auctionService.selectNearAucDate(map));
			mav.addObject("watchList",list);
			mav.addObject("watchCount",count);
			mav.addObject("today",date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		}
		catch (RuntimeException | SQLException e) {
			log.error("AuctionController.watchApp : {} ", e);
		}
		mav.setViewName("auction/watch/watchApp");
		return mav;
	}
	
	@RequestMapping(value = "/notice",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView notice(@RequestParam(name = "place", required = false) String place) throws Exception {
		// 경매안내
		LOGGER.debug("start of notice.do");
		ModelAndView mav = new ModelAndView();
		mav.addObject("subheaderTitle","공지사항");

        Map<String,Object> map = new HashMap<>();
        map.put("johpCd", place);
        map.put("naBzPlcNo", place);
        Map<String,Object> johap = adminService.selectOneJohap(map);
        mav.addObject("johapData", johap);
        map.put("johpCd", johap.get("NA_BZPLC"));
        map.put("delYn", 0);
        mav.addObject("noticeList", auctionService.noticeSelectList(map));
        mav.addObject("subheaderTitle","공지사항");
		mav.setViewName("auction/notice/notice");
		return mav;
	}
	
	
	@RequestMapping(value = "/main",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView main(@RequestParam(name = "place", required = false) String place
							   , HttpServletRequest req
							   , HttpServletResponse res) throws Exception {
		// 조합메인(main)
		LOGGER.debug("start of main.do");
		LocalDateTime date = LocalDateTime.now();
		String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		ModelAndView mav = new ModelAndView();
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", place);
        final Map<String, Object> johapData = adminService.selectOneJohap(map);
        mav.addObject("johapData", johapData);
		
        map.put("searchDate", today);
		Map<String, Object> dateVo = auctionService.selectNearAucDate(map);
		
		map.put("searchDate", dateVo == null ? today : dateVo.get("AUC_DT"));
		List<Map<String, Object>> list = auctionService.selectCalendarList(map);
        mav.addObject("aucDate", list);

		List<Map<String,Object>> bizList = auctionService.selectBizLocList(map);
		mav.addObject("bizList",bizList);
		long aucYn = bizList.size() < 1 ? 0 :Integer.parseInt((String)bizList.get(0).get("AUC_YN"));
		long aucCnt = bizList.size() < 1 ? 0 : Long.parseLong(bizList.get(0).getOrDefault("AUC_CNT", "0").toString());
		
		//전광판 개발 테스트를 위한 back
		if(sessionUtill.getUserVo() instanceof BidUserDetails && list.size() > 0) {
			BidUserDetails loginUser = (BidUserDetails)sessionUtill.getUserVo();
			final Map<String, Object> params = new HashMap<String, Object>();
			params.put("naBzplc", johapData.get("NA_BZPLC"));
			String aucObjDsc = (String)list.get(0).get("AUC_OBJ_DSC");
			params.put("aucObjDsc", aucObjDsc.split(","));
			if(loginUser.getTrmnAmnno() != null) params.put("trmnAmnno", loginUser.getTrmnAmnno());
			mav.addObject("entryList", auctionService.selectMyEntryList(params));
		}
		
		if(bizList.size() <= 0 || aucYn < 1 ) {
			//경매를 안하는 곳이면
			mav.setViewName("auction/info/noinfo");
			mav.addObject("subheaderTitle","경매안내");
		}
		else if(dateVo != null && today.equals(dateVo.get("AUC_DT")) && aucCnt > 0){
			//경매진행중
			mav.setViewName("auction/main/main");
			mav.addObject("subheaderTitle","경매참여");
			
			//로그인된 역할 확인하여 각 테이블 조회
			Map<String, Object> authNoYmd = new HashMap<String, Object>(); 
			if(sessionUtill.getNaBzplc() != null) map.put("naBzPlc", sessionUtill.getNaBzplc());
			if(sessionUtill.getUserId() != null) map.put("loginNo", sessionUtill.getUserId());
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
			mav.addObject("naBzPlcNo", place);
			String todayHms = date.format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
			mav.addObject("today", todayHms);
		}
		else {
			//경매는 하는 조합이지만 오늘이 아닌 경우
			Map<String,Object> tempMap = new HashMap<>();
			tempMap.put("naBzPlcNo", place);
			tempMap.put("searchAucDt", date.format(DateTimeFormatter.ofPattern("yyyyMM")));
			List<Map<String,Object>> dateList = auctionService.selectCalendarList(tempMap);
			mav.addObject("dateList",dateList);
			
			mav.addObject("today", today);
			mav.setViewName("auction/info/info");
			mav.addObject("subheaderTitle","경매안내");
		}
		return mav;
	}	

	/**
	 * Web 경매 페이지
	 * @param place
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/bid")
	public ModelAndView bid(@RequestParam(name = "place", required = false) String place) throws Exception {
		ModelAndView mav = new ModelAndView();
		final Map<String,Object> params = new HashMap<String,Object>();

		try {
			// 1. Web 경매 진행을 위해 조합 정보를 가져온다.
			LocalDateTime date = LocalDateTime.now();
			String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			params.put("searchDate", today);
			params.put("naBzPlcNo", place);
			params.put("aucDscFlag", "Y");
			Map<String,Object> johap = adminService.selectOneJohap(params);
			
			
			if (johap == null) {
				return makeMessageResult(mav, params, "/main", "", "조합 정보가 없습니다.", "pageMove('/main');");
			}

			// 2. 경매 진행 여부 체크
			final String aucYn = johap.getOrDefault("AUC_YN", "0").toString();
			if (!"1".equals(aucYn)) {
				return makeMessageResult(mav, params, "/main", "", "경매 서비스 준비중입니다.", "pageMove('/main');");
			}
			
			// 3. 경매 유형(단일, 일괄) 체크
			final String aucDsc = johap.getOrDefault("AUC_DSC", "9").toString();
			
			// 단일 경매
			if ("1".equals(aucDsc)) {
				mav.setViewName("auction/bid/singleBid"+"_agora");
			}
			// 일괄 경매
			else if ("2".equals(aucDsc)) {
				mav.setViewName("auction/bid/groupBid"+"_agora");
			}
			else {
				return super.makeMessageResult(mav, params, "/main", "", "경매 서비스 준비중입니다.", "pageMove('/main');");
			}
			
			mav.addObject("auctionList", auctionService.entrySelectList(params));
			mav.addObject("johapData",johap);
			mav.addObject("trmnAmnno", sessionUtill.getUserId());
			mav.addObject("authRole", sessionUtill.getRoleConfirm());
			mav.addObject("subheaderTitle",johap.get("CLNTNM"));
		}catch (RuntimeException re) {
			return super.makeMessageResult(mav, params, "/main", "", "경매 서비스 준비중입니다.", "pageMove('/main');");
		}catch (SQLException se) {
			return super.makeMessageResult(mav, params, "/main", "", "경매 서비스 준비중입니다.", "pageMove('/main');");
		}

		return mav;
	}
	
	/**
	 * 경매 페이지 > 예정, 결과, 응찰 내역 조회 팝업
	 * @param param
	 * @return
	 * @throws Exception
	 */
	@PostMapping(path = "/pop/entryList")
	public ModelAndView entryListPopUp(@RequestBody final Map<String, Object> params) {
		ModelAndView mav = new ModelAndView("pop/modal/entryList");
		List<Map<String,Object>> list = null;
		try {
			LocalDateTime date = LocalDateTime.now();
			String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			//params.put("searchDate", "20211111");
			params.put("searchDate", today);
			
	        if(params.get("naBzplc") != null) params.put("naBzplc", params.get("naBzPlc"));
			if(sessionUtill.getUserId() != null) params.put("loginNo", sessionUtill.getUserId());
			
			list = auctionService.entrySelectList(params);
			if(sessionUtill.getUserId() != null) params.put("searchTrmnAmnNo", sessionUtill.getUserId());
			List<Map<String,Object>> soldList = auctionService.entrySelectList(params);
			params.put("aucYn", "1");
			List<Map<String,Object>> bidList = auctionService.selectBidLogList(params);
			mav.addObject("aucList", list);
			mav.addObject("soldList", soldList);
			mav.addObject("bidList", bidList);
			mav.addObject("johapData", adminService.selectOneJohap(params));
			mav.addObject("bidCnt", auctionService.selectBidLogListCnt(params));
			mav.addObject("buyCnt",auctionService.selectCountEntry(params));
			mav.addObject("totPrice", auctionService.selectTotSoldPrice(params));
			mav.addObject("params", params);
		}catch (RuntimeException re) {
			log.error("AuctionController.entryListPopUp : {} ",re);
		}catch (SQLException se) {
			log.error("AuctionController.entryListPopUp : {} ",se);
		}
		return mav;
	}
	
	@ResponseBody
	@PostMapping(path = "/auction/api/inserttZimPrice", produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> insertUpdateZim(@RequestBody Map<String,Object> params) {
		Map<String, Object> result = new HashMap<>();
		try {
			if(params.get("loginNo") == null || "".equals(params.get("loginNo"))) {
				params.put("loginNo", sessionUtill.getUserId());
			}
			int cnt = auctionService.insertUpdateZim(params);
			result.put("data", cnt);
			result.put("success", (cnt > 0));
			result.put("message", (cnt > 0) ? "찜가격 등록에 성공했습니다." : "찜가격을 확인하세요.");
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("naBzplc", params.get("naBzPlc"));
			map.put("searchDate", params.get("aucDt"));
			map.put("searchAucObjDsc", params.get("aucObjDsc"));
			map.put("searchAucPrgSq", params.get("aucPrgSq"));
			map.put("loginNo", params.get("loginNo"));
			List<Map<String,Object>> list = auctionService.entrySelectList(map);
			result.put("aucInfo", list.size() > 0 ? list.get(0) : null);
		}
		catch (RuntimeException | SQLException re) {
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			log.error("AuctionController.insertUpdateZim : {} ",re);
		}
		return result;
	}

	@ResponseBody
	@PostMapping(path = "/auction/api/deleteZimPrice", produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> deleteZimPrice(@RequestBody Map<String,Object> params) {
		Map<String, Object> result = new HashMap<>();
		// params.put("loginNo", sessionUtill.getUserId());
		result.put("success", true);
		try {
			if(params.get("loginNo") == null || "".equals(params.get("loginNo"))) {
				params.put("loginNo", sessionUtill.getUserId());
			}
			result.put("data", auctionService.deleteZimPrice(params));
			
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("naBzplc", params.get("naBzPlc"));
			map.put("searchDate", params.get("aucDt"));
			map.put("searchAucObjDsc", params.get("aucObjDsc"));
			map.put("searchAucPrgSq", params.get("aucPrgSq"));
			map.put("loginNo", params.get("loginNo"));
			List<Map<String,Object>> list = auctionService.entrySelectList(map);
			result.put("aucInfo", list.size() > 0 ? list.get(0) : null);
		}
		catch (RuntimeException | SQLException re) {
			result.put("success", false);
			//result.put("message", re.getMessage());
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			log.error("AuctionController.deleteZimPrice : {} ",re);
		}
		return result;
	}

	@RequestMapping(value = "/auction/api/entryListApi",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView entryListApiPopUp(@RequestParam Map<String,Object> param) throws Exception {
		// 경매관전
		log.debug("start of pop_entryList.do");
		ModelAndView mav = new ModelAndView();
		try {	        
			LocalDateTime date = LocalDateTime.now();
			String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	        Map<String,Object> map = new HashMap<String,Object>();
	        if(param.get("naBzplc") != null) map.put("naBzplc", param.get("naBzplc"));
	        if(param.get("naBzplc") != null) map.put("naBzPlc", param.get("naBzplc"));
			if(param.get("loginNo") != null)map.put("loginNo", param.get("loginNo"));
			if(param.get("date") != null) map.put("searchDate", param.get("date"));
			else {
				param.put("date", today);
				map.put("searchDate", today);
			}
			//map.put("searchDate", param.get("date"));
			Map<String,Object> johap=adminService.selectOneJohap(map);
			//List<Map<String,Object>> list=auctionService.entrySelectList(map);
			//if(param.get("loginNo") != null) map.put("searchTrmnAmnNo", param.get("loginNo"));
			//List<Map<String,Object>> soldList = auctionService.entrySelectList(map);
			//map.put("aucYn", "1");
			//List<Map<String,Object>> bidList = auctionService.selectBidLogList(map);
			map.put("entryType", "A");
			List<Map<String,Object>> datelist=auctionService.selectAucDateList(map);
			
			mav.addObject("dateList", datelist);
			mav.addObject("johapData", johap);
			//mav.addObject("aucList", list);
			//mav.addObject("soldList", soldList);
			//mav.addObject("bidList", bidList);
			//mav.addObject("bidCnt", auctionService.selectBidLogListCnt(map));
			//mav.addObject("buyCnt",auctionService.selectCountEntry(map));
			//mav.addObject("totPrice", auctionService.selectTotSoldPrice(map));
			mav.addObject("inputParam", param);
		}catch (RuntimeException re) {
			log.error("AuctionController.entryListApiPopUp : {} ",re);
		}catch (SQLException se) {
			log.error("AuctionController.entryListApiPopUp : {} ",se);
		} 
		mav.setViewName("pop/entryListApi");
		return mav;
	}
	
	@RequestMapping(value = "/buyStat", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView buyStat(@RequestParam final Map<String, Object> params) {
		ModelAndView mav = new ModelAndView("auction/stat/buyStat");
		try {
			List<Map<String, Object>> list = auctionService.selectMyBuyList(params);
			mav.addObject("list", list);
			mav.addObject("params", params);
		}catch (RuntimeException re) {
			log.error("AuctionController.buyStat : {} ",re);
		}catch (SQLException se) {
			log.error("AuctionController.buyStat : {} ",se);
		} 
		return mav;
	}
	
	@RequestMapping(value = "/bidStat", method = {RequestMethod.POST, RequestMethod.GET})
	public ModelAndView bidStat(@RequestParam final Map<String, Object> params) {
		ModelAndView mav = new ModelAndView("auction/stat/bidStat");
		try {
			List<Map<String, Object>> list = auctionService.selectMyBuyList(params);
			mav.addObject("list", list);
			mav.addObject("params", params);
		}catch (RuntimeException re) {
			log.error("AuctionController.bidStat : {} ",re);
		}catch (SQLException se) {
			log.error("AuctionController.bidStat : {} ",se);
		} 
		return mav;
	}

	@ResponseBody
	@RequestMapping(value = "/auction/select/nearAtdrAm", method = {RequestMethod.POST, RequestMethod.GET})
	public Map<String, Object> selectNearAtdrAm(@RequestParam final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();
		final Map<String, Object> temp = new HashMap<String, Object>();

		try {
			Map<String,Object> map = auctionService.selectNearAtdrAm(params);
			Map<String,Object> zim = auctionService.selectMyZimPrice(params);

			temp.put("bidPrice", map != null ?map.get("ATDR_AM"):0);
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
	

	@RequestMapping(value = "/log",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView log() throws Exception {
		// 경매관전
		ModelAndView mav = new ModelAndView();
		try {	
		}catch (RuntimeException re) {
			log.error("AuctionController.watch : {} ",re);
		}
		mav.setViewName("log");
		return mav;
	}

	@ResponseBody
	@PostMapping(path = "/auction/api/getWatchToken", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> getWatchToken(@RequestBody Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("success", true);
        try {        	
        	JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
        			.auctionHouseCode((String)params.get("naBzPlc"))
        			.userMemNum("WATCHER")
        			.userRole(Constants.UserRole.WATCHER)
        			.build();
        	String token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
            result.put("token", token);            
            
        }catch (RuntimeException re) {
            result.put("success", false);
            result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			log.error("AuctionController.getWatchToken : {} ",re);
		}
        return result;
    }
	
	@ResponseBody
	@PostMapping(path = "/auction/api/updateNoticeReadCnt", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> updateNoticeReadCnt(@RequestBody Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
        try {
			params.put("johpCd", params.get("naBzplc"));
			result.put("updateRow", auctionService.updateNoticeReadCnt(params));
			result.put("data", auctionService.selectOneNotice(params));
		}catch (SQLException | RuntimeException se) {
			log.debug("ApiController.updateNoticeReadCnt : {} ",se);
			result.put("success", false);
			//result.put("message", se.getMessage());
            result.put("message", "작업중 에러가 발생하였습니다.");
		}
        return result;
    }
	@RequestMapping(value = "/front/getCowList" ,method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getCowList(@RequestParam Map<String, Object> params) {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		try {

	        LocalDateTime date = LocalDateTime.now();
	        Map<String,Object> temp = new HashMap<String,Object>();
	        String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			
	        temp.put("searchDate", today);
	        temp.put("naBzplc", params.get("naBzplc"));
	        temp.put("searchAucObjDsc", params.get("aucObjDsc"));

	        
	        if("2".equals(params.get("aucDsc"))) {
		        temp.put("rgSqno", params.get("rgSqno"));
		        temp.put("aucObjDsc", params.get("aucObjDsc"));
		        
				Map<String,Object> info=auctionService.getStnInfo(temp);
				
				if(info == null || info.isEmpty()) {		
					result.put("success", false);
					result.put("message", "경매 차수 구간이 없습니다.");
					return result;
				}		
		        temp.put("searchAucObjDsc", info.get("AUC_OBJ_DSC"));
		        temp.put("stAucNo", info.get("ST_AUC_NO"));
		        temp.put("edAucNo", info.get("ED_AUC_NO"));	        	
	        }	        
	        
			List<Map<String,Object>> list=auctionService.entrySelectList(temp);
			
			result.put("success", true);
			result.put("message", "조회에 성공했습니다.");
			result.put("list", list);
		}catch (SQLException | RuntimeException re) {
			log.error("AdminController.getCowList : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		return result;
	}
	

	@RequestMapping(value = "/cowDetail",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView cowDetail(@RequestParam Map<String, Object> param) throws Exception {		
		LOGGER.debug("start of cowDetail.do");
		ModelAndView mav = new ModelAndView();
        Map<String,Object> map = new HashMap<>();
		Map<String, Object> cowInfo = null;
        map.put("naBzPlcNo", param.get("place"));
        
        //Map<String, Object> johap = adminService.selectOneJohap(map);
        map.put("naBzplc", param.get("naBzplc"));
        //map.put("searchAucObjDsc", param.get("aucObjDsc"));
        //map.put("searchAucPrgSq", param.get("aucPrgSq"));
        //map.put("searchDate", param.get("aucDt"));
        //map.put("sraIndvAmnno", param.get("sraIndvAmnno"));
		//List<Map<String,Object>> list=auctionService.entrySelectList(map);
		//if(list != null && list.size() >0) {
		//	cowInfo  = list.get(0);
		//}

		//출장우 상세 tab항목 표기
        map.put("simpCGrpSqno", "1");
		List<Map<String,Object>> tabList =auctionService.selectListExpitemSet(map);

		//mav.addObject("infoData",cowInfo);
		mav.addObject("tabList",tabList);
		
		//mav.addObject("johapData",johap);
		mav.addObject("subheaderTitle","출장우 상세");
		//mav.setViewName("auction/sales/cowDetail");
		mav.addObject("inputParam", param);		
		mav.setViewName("pop/cowDetail");
		return mav;
	}	

	@RequestMapping(value = "/info/getCowInfo",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView getCowInfo(@RequestParam Map<String, Object> param) throws Exception {		
		LOGGER.debug("start of results.do");

		LocalDateTime date = LocalDateTime.now();
		String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		
		String tabId = (String) param.getOrDefault("tabId","");
		ModelAndView mav = new ModelAndView("pop/modal/info/cowInfo_tab_"+tabId);
		Map<String, Object> cowInfo = null;
		Map<String, Object> paramMap = new HashMap<String,Object>();
		//String naBzplc = (String) param.get("naBzplc");
		paramMap.put("naBzplc", param.get("naBzplc"));
		paramMap.put("naBzPlcNo", param.get("place"));
        paramMap.put("searchAucObjDsc", param.get("aucObjDsc"));
        paramMap.put("searchAucPrgSq", param.get("aucPrgSq"));
        paramMap.put("searchDate", param.get("aucDt"));
        //20240206 : 반영필요 : outofmemory 대응용 소스
        if("0".equals(tabId)) paramMap.put("cowDetailYn", "Y");        
		List<Map<String,Object>> cowList = auctionService.entrySelectList(paramMap);
		if(cowList != null && cowList.size() >0) {
			cowInfo  = cowList.get(0);
		}
		mav.addObject("infoData",cowInfo);
		mav.addObject("inputParam", param);		
		try {
			switch(tabId) {
				case "0":
					paramMap.put("naBzplc", param.get("naBzplc"));
					paramMap.put("sraIndvAmnno", param.get("sraIndvAmnno"));
					paramMap.put("simpCGrpSqno", "1");
					List<Map<String,Object>> moveList = auctionService.selectListIndvMove(paramMap);
					List<Map<String,Object>> childBirthList = auctionService.selectListIndvChildBirth(paramMap);
					List<Map<String,Object>> tabList =auctionService.selectListExpitemSet(paramMap);
					mav.addObject("childBirthList",childBirthList);
					mav.addObject("moveList",moveList);
					mav.addObject("tabList", tabList);
				break;
				case "1":
				case "2":					
					paramMap.put("naBzplc", param.get("naBzplc"));
					paramMap.put("sraIndvAmnno", param.get("sraIndvAmnno"));
					Map<String,Object> bloodInfo = auctionService.selectIndvBloodInfo(paramMap);
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
					
					String aucDt = (String) param.get("aucDt");
					Date aucDate = formatter.parse(aucDt);					
					
					if(bloodInfo == null || bloodInfo.isEmpty()) {
						
						commonService.callIndvAiakInfo((String)param.get("sraIndvAmnno"));
						bloodInfo = auctionService.selectIndvBloodInfo(paramMap);
					}else {
						String lschgDt = (String)bloodInfo.get("LSCHG_DT");
						Date lschgDate = formatter.parse(lschgDt);
						if((today.equals(aucDt) && lschgDate.before(aucDate) )) {							
							commonService.callIndvAiakInfo((String)param.get("sraIndvAmnno"));
							bloodInfo = auctionService.selectIndvBloodInfo(paramMap);							
						}
					}
					mav.addObject("bloodInfo",bloodInfo);
					if("1".equals(tabId)) {
						mav.addObject("sibList",auctionService.selectListIndvSib(paramMap));
						mav.addObject("postList",auctionService.selectListIndvPost(paramMap));						
					}
				break;
				case "3": //thumnail만 조회
					paramMap.put("naBzplc", param.get("naBzplc"));
					paramMap.put("sraIndvAmnno", param.get("sraIndvAmnno"));
					paramMap.put("aucObjDsc", param.get("aucObjDsc"));
					paramMap.put("aucDt", param.get("aucDt"));
					paramMap.put("oslpNo", param.get("oslpNo"));
					paramMap.put("ledSqNo", param.get("ledSqno"));
					List<Map<String,Object>> imgList = auctionService.selectListCowImg(paramMap);
					mav.addObject("imgList",imgList);
				break;
			}			
		}catch(SQLException |RuntimeException  e) {
			log.error("AuctionController.getCowInfo : {} ",e);			
		}
		return mav;
	}

	@RequestMapping(value = "/info/cowImageDetail",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView cowImageDetail(@RequestParam Map<String, Object> param) throws Exception {
		//이미지 원본만 조회
		LOGGER.debug("start of cowImageDetail");
		String tabId = (String) param.getOrDefault("tabId","");
		ModelAndView mav = new ModelAndView("pop/cowImageDetail");
		Map<String, Object> paramMap = new HashMap<String,Object>();
		try {
			paramMap.put("naBzplc", param.get("naBzplc"));
			paramMap.put("sraIndvAmnno", param.get("sraIndvAmnno"));
			paramMap.put("aucObjDsc", param.get("aucObjDsc"));
			paramMap.put("aucDt", param.get("aucDt"));
			paramMap.put("oslpNo", param.get("oslpNo"));
			paramMap.put("ledSqNo", param.get("ledSqno"));
			List<Map<String,Object>> imgList = auctionService.selectListCowImg(paramMap);
			mav.addObject("imgList",imgList);			
		}catch(SQLException |RuntimeException  e) {
			log.error("AuctionController.cowImageDetail : {} ",e);			
		}
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value = "/inf/getMcaInfo",method = { RequestMethod.GET, RequestMethod.POST })	
	public Map<String, Object> getMcaInfo(@RequestParam Map<String, Object> param) throws Exception {		
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		String ctgrmCd = (String) param.getOrDefault("ctgrmCd","");
		String recvMsg = httpUtils.sendPostJson(param, ctgrmCd);
		log.info("recvMsg : " + recvMsg);

		result.put("data", recvMsg);
		if(recvMsg != null && "".equals(recvMsg)) {
			result.put("success", false);		
		}		
		return result;
	}

	@RequestMapping(value = "/info/getBidPopupDetail",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView bidPopupDetail(@RequestParam Map<String, Object> param) throws Exception {		
		LOGGER.debug("start of bidPopupDetail.do");
		String tabId = (String) param.getOrDefault("tabId","");
		ModelAndView mav = new ModelAndView("pop/modal/detail/bidPop_tab_"+tabId);
		Map<String, Object> cowInfo = null;
		Map<String, Object> paramMap = new HashMap<String,Object>();
		LocalDateTime date = LocalDateTime.now();
		String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));		
		param.put("entryType", "A");		
		if(param.get("loginNo") != null) param.put("searchTrmnAmnNo", param.get("loginNo"));

		List<Map<String,Object>> datelist=auctionService.selectAucDateList(param);
		if(param != null && param.get("searchDate") != null &&  !"".equals(param.get("searchDate"))) {
			param.put("searchDate", param.get("searchDate"));			
		}else {
			String tempDate= datelist.size() > 0 ? (String)datelist.get(0).get("AUC_DT") :null;
			param.put("searchDate",tempDate);
		}
		try {
			switch(tabId) {
				case "auc":
					//중도매인번호 초기화(출장우내역시에 미사용)
					param.put("searchTrmnAmnNo", "");
					mav.addObject("inputParam", param);
					//param.put("authRole", sessionUtill.getRoleConfirm());
					List<Map<String,Object>> list = auctionService.entrySelectList(param);
					mav.addObject("aucList", list);
				break;
				case "sold":
					mav.addObject("inputParam", param);
					List<Map<String,Object>> soldList = auctionService.entrySelectList(param);
					mav.addObject("soldList", soldList);
					mav.addObject("buyCnt",auctionService.selectCountEntry(param));
				break;
				case "bid": 
					param.put("aucYn", "1");
					mav.addObject("inputParam", param);
					List<Map<String,Object>> bidList = auctionService.selectBidLogList(param);
					mav.addObject("bidList", bidList);
					mav.addObject("bidCnt", auctionService.selectBidLogListCnt(param));
				break;
			}
			mav.addObject("dateList", datelist);
		}catch(SQLException |RuntimeException  e) {
			log.error("AuctionController.bidPopupDetail : {} ",e);			
		}
		return mav;
	}
	
	//DB 데이터를 조회할것인지 인터페이스후 값만 뿌릴것인지?
	@RequestMapping(value = "/cowDetailFull",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView cowDetailFull(@RequestParam Map<String, Object> param) throws Exception {		
		LOGGER.debug("start of cowDetailFull.do");
		ModelAndView mav = new ModelAndView();
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", param.get("place"));        
        Map<String, Object> johap = adminService.selectOneJohap(map);
        map.put("naBzplc", param.get("naBzplc"));
        map.put("sraIndvAmnno", param.get("sraIndvAmnno"));
		//Map<String,Object> indvData=auctionService.selectIndvDataInfo(map);
		//map.put("sraIndvAmnno", param.get("sraIndvAmnno"));
		
		//List<Map<String,Object>> moveList = auctionService.selectListIndvMove(map);
		//List<Map<String,Object>> postList = auctionService.selectListIndvPost(map);
		//List<Map<String,Object>> sibList = auctionService.selectListIndvSib(map);
		
		//mav.addObject("moveList",moveList);
		commonService.callIndvAiakInfo((String)param.get("sraIndvAmnno"));
		//bloodInfo = auctionService.selectIndvBloodInfo(map);
		Map<String,Object> bloodInfo = auctionService.selectIndvBloodInfo(map);
		
		mav.addObject("bloodInfo",bloodInfo);
		mav.addObject("sibList",auctionService.selectListIndvSib(map));
		mav.addObject("postList",auctionService.selectListIndvPost(map));

		//출장우 상세 tab항목 표기
        map.put("simpCGrpSqno", "1");
        map.put("indvPopYn", "Y");
		mav.addObject("tabList",auctionService.selectListExpitemSet(map));

		//mav.addObject("infoData",indvData);		
		mav.addObject("johapData",johap);
		mav.addObject("subheaderTitle",(param.get("title"))+"개체 상세");
		mav.addObject("inputParam", param);		
		mav.setViewName("pop/cowDetailFull");
		return mav;
	}
	
	@RequestMapping(value = "/cowDetailFull_Temp",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView cowDetailFull_Temp(@RequestParam Map<String, Object> param) throws Exception {		
		LOGGER.debug("start of cowDetailFull.do");
		ModelAndView mav = new ModelAndView();
        Map<String,Object> map = new HashMap<>();
                
        map.put("SRA_INDV_AMNNO", param.get("sraIndvAmnno"));
		Map<String, Object> indvData = httpUtils.sendPostJsonToMap(map, "4700");
		mav.addObject("infoData",indvData);
		
		Map<String,Object> sibList = httpUtils.sendPostJsonToMap(map, "4900");
		mav.addObject("sibList",sibList);

		Map<String,Object> openData = httpUtils.getOpenApiAnimalTraceToMap(map);
		mav.addObject("moveList",openData.get("moveList"));
		mav.addObject("vacnInfo",openData.get("vacnInfo"));
		
		map.put("SRA_INDV_AMNNO", indvData.get("MCOW~"));
		Map<String,Object> postList = httpUtils.sendPostJsonToMap(map, "4900");
		mav.addObject("postList",postList);

		//출장우 상세 tab항목 표기
        map.put("simpCGrpSqno", "1");
        map.put("indvPopYn", "Y");
		mav.addObject("tabList",auctionService.selectListExpitemSet(map));

		mav.addObject("subheaderTitle",(param.get("title"))+"개체 상세");
		mav.addObject("inputParam", param);		
		mav.setViewName("pop/cowDetailFull");
		return mav;
	}
	@ResponseBody
	@PostMapping(path = "/auction/api/getInfMca", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> getInfCowDetailFull(@RequestBody Map<String,Object> param) {
        Map<String, Object> result = new HashMap<>();
        Map<String,Object> map = new HashMap<>();
        try {
        	result.put("success", true);
        	String cgtrmCd = (String)param.get("cgtrmCd");
	        //map.put("SRA_INDV_AMNNO", param.get("sraIndvAmnno"));
        	result.put("data", httpUtils.sendPostJson(param, cgtrmCd));
		}
        catch (RuntimeException re) {
        	log.debug("ApiController.getInfCowDetailFull : {} ",re);
        	result.put("success", false);
        	result.put("message", "작업중 에러가 발생하였습니다.");
        }
        catch (Exception se) {
			log.debug("ApiController.getInfCowDetailFull : {} ",se);
			result.put("success", false);
            result.put("message", "작업중 에러가 발생하였습니다.");
		}
        return result;
    }
	
	@ResponseBody
	@RequestMapping(value = "/info/getIndvInfo",method = { RequestMethod.GET, RequestMethod.POST })	
	public Map<String, Object> getIndvInfo(@RequestParam Map<String, Object> param) throws Exception {		
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		Map<String,Object> indvMap = auctionService.selectIndvDataInfo(param);
		result.put("data", indvMap);
		if(indvMap == null || indvMap.isEmpty()) {
			result.put("success", false);		
		}		
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/info/getAiakInfo", method = { RequestMethod.GET, RequestMethod.POST })
	public String testAiak(@RequestParam Map<String, Object> params) throws Exception {
		String rHtml= "";
		try {
			if(params.get("barcode") == null) {
				throw new Exception("귀표번호를 입력해주시기 바랍니다.");
			}
			String barcode ="";
			if(params.get("barcode") != null) barcode = (String)params.get("barcode");
			rHtml = httpUtils.callApiAiak(barcode);
		}catch(RuntimeException re) {		//SQLException |
			rHtml = "작업중 오류가 발생했습니다. 관리자에게 문의하세요.";
		}
		return rHtml;
	}
}
