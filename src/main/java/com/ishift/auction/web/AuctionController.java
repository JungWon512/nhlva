package com.ishift.auction.web;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ishift.auction.util.FormatUtil;
import com.ishift.auction.vo.BidUserDetails;
import com.ishift.auction.vo.JwtTokenVo;

import lombok.extern.slf4j.Slf4j;

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
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.CookieUtil;
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
	private CookieUtil cookieUtil;
	
	@RequestMapping(value = "/results",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView results(
			@RequestParam Map<String, Object> param
			) throws Exception {		
		// 경매결과목록
		
		LOGGER.debug("start of results.do");
		ModelAndView mav = new ModelAndView();
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", param.get("place"));
        
        Map<String, Object> johap = adminService.selectOneJohap(map);

		map.put("naBzPlc", johap.get("NA_BZPLC"));
		map.put("entryType", "A");
		List<Map<String,Object>> datelist=auctionService.selectAucDateList(map);
		if(param.get("searchDate") != null && param != null) {
			map.put("searchDate", param.get("searchDate"));			
		}else {
			map.put("searchDate",datelist.size() > 0 ? datelist.get(0).get("AUC_DT") :null);
		}
		if(param.get("searchOrder") != null) map.put("searchOrder", param.get("searchOrder"));
		if(param.get("searchAucObjDsc") != null) map.put("searchAucObjDsc", param.get("searchAucObjDsc"));
		if(param.get("searchTxt") != null) map.put("searchTxt", param.get("searchTxt"));
		
		List<Map<String,Object>> list=auctionService.entrySelectList(map);
		if(map.get("searchDate") != null) param.put("searchDate", map.get("searchDate"));
		
		mav.addObject("johapData", johap);
		mav.addObject("paramVo", param);
		mav.addObject("dateList",datelist);
		mav.addObject("resultList",list);
		mav.addObject("subheaderTitle","경매결과 조회");
		
		mav.setViewName("auction/results/resultList");
		return mav;
	}

	@RequestMapping(value = "/sales",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView entry(@RequestParam Map<String,Object> param) throws Exception {
		// 경매예정목록
		String place = (String) param.get("place");
		LOGGER.debug("start of sales.do");
		ModelAndView mav = new ModelAndView();
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", place);

		Map<String,Object> johap = adminService.selectOneJohap(map);

		LocalDateTime date = LocalDateTime.now();
		String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));

		map.put("searchDate", today);
		Map<String, Object> nearAucDate =auctionService.selectNearAucDate(map);
		
		map.put("naBzPlc", johap.get("NA_BZPLC"));
		map.put("entryType", "B");
		
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
		map.put("naBzPlcNo", place);
		if(param.get("searchOrder") != null) map.put("searchOrder", param.get("searchOrder"));
		if(param.get("searchTxt") != null) map.put("searchTxt", param.get("searchTxt"));
		if(param.get("searchAucObjDsc") != null) map.put("searchAucObjDsc", param.get("searchAucObjDsc"));
		map.put("loginNo", sessionUtill.getUserId());
		List<Map<String,Object>> list=auctionService.entrySelectList(map);

//		for(Map<String,Object> entry : list) {
//			String birthMonth = this.getConvertBirthDay(this.getStringValue(entry.get("BIRTH")));
//			entry.put("BIRTH_MONTH", birthMonth);
//		}
		if(sessionUtill.getUserId() != null) param.put("loginNo", sessionUtill.getUserId());
		mav.addObject("johapData", johap);
		mav.addObject("subheaderTitle","출장우 조회");
		mav.addObject("dateList",datelist);
		mav.addObject("salesList",list);
		mav.addObject("inputParam", param);
		mav.addObject("subheaderTitle","출장우 조회");
		// mav.addObject("today",date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		mav.setViewName("auction/sales/saleList");
		return mav;
	}

	@RequestMapping(value = "/calendar",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView schedule(@RequestParam Map<String,Object> param) throws Exception {
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
			map.put("yyyymm", yyyyMM);
			List<Map<String,Object>> list = auctionService.selectCalendarList(map);
			mav.addObject("resultList",list);
			param.put("searchYm", yyyyMM);
			param.put("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd")));
			mav.addObject("paramVo",param);
			mav.addObject("title",formatUtil.dateAddDotLenSix(yyyyMM));
		}catch (RuntimeException re) {
			log.error("AuctionController.schedule : {} ",re);
		}catch (SQLException se) {
			log.error("AuctionController.schedule : {} ",se);
		}
		mav.addObject("subheaderTitle","일정안내");
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
		mav.setViewName("auction/watch/watch");
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
	public ModelAndView submain(@RequestParam(name = "place", required = false) String place
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
			//경매진행중
			mav.setViewName("auction/info/noinfo");
			mav.addObject("subheaderTitle","경매안내");
		}
		else if(dateVo != null && today.equals(dateVo.get("AUC_DT")) && aucCnt > 0){
			// 경매진행중인 경우 관전토큰 생성 후 쿠키에 저장
			// 20220210 jjw - Watch token SessionContextInterceptor 생성
			//JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
			//		.auctionHouseCode(johapData.get("NA_BZPLC").toString())
			//		.userMemNum("WATCHER")
			//		.userRole(Constants.UserRole.WATCHER)
			//		.build();
			//String token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
			//Cookie cookie = cookieUtil.createCookie("watch_token", token);
			//res.addCookie(cookie);
			
			//경매진행중
			mav.setViewName("auction/main/main");
			mav.addObject("subheaderTitle","경매참여");
		}
		else {
			//경매
			Map<String,Object> tempMap = new HashMap<>();
			tempMap.put("naBzPlcNo", place);
			tempMap.put("yyyymm", date.format(DateTimeFormatter.ofPattern("yyyyMM")));
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
				mav.setViewName("auction/bid/singleBid");
			}
			// 일괄 경매
			else if ("2".equals(aucDsc)) {
				mav.setViewName("auction/bid/groupBid");
			}
			else {
				return super.makeMessageResult(mav, params, "/main", "", "경매 서비스 준비중입니다.", "pageMove('/main');");
			}
			
			mav.addObject("auctionList", auctionService.entrySelectList(params));
			mav.addObject("johapData",johap);
			mav.addObject("trmnAmnno", sessionUtill.getUserId());
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
	@PostMapping(path = "/pop/entryList", produces = MediaType.APPLICATION_JSON_VALUE)
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
        // params.put("loginNo", sessionUtill.getUserId());
        result.put("success", true);
        try {
        	if(params.get("loginNo") == null || 	"".equals(params.get("loginNo"))) {
            	params.put("loginNo", sessionUtill.getUserId());        		
        	}
            result.put("data", auctionService.insertUpdateZim(params));
            Map<String,Object> map = new HashMap<String, Object>();
            map.put("naBzplc", params.get("naBzPlc"));
            map.put("searchDate", params.get("aucDt"));
            map.put("searchAucObjDsc", params.get("aucObjDsc"));
            map.put("searchAucPrgSq", params.get("aucPrgSq"));
            map.put("loginNo", params.get("loginNo"));
            List<Map<String,Object>> list = auctionService.entrySelectList(map);
            result.put("aucInfo", list.size() > 0 ? list.get(0) : null);
        }catch (RuntimeException | SQLException re) {
            result.put("success", false);
            //result.put("message", re.getMessage());
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
        }catch (RuntimeException | SQLException re) {
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
			else map.put("searchDate", today);
			
			//map.put("searchDate", param.get("date"));
			Map<String,Object> johap=adminService.selectOneJohap(map);
			List<Map<String,Object>> list=auctionService.entrySelectList(map);
			if(param.get("loginNo") != null) map.put("searchTrmnAmnNo", param.get("loginNo"));
			List<Map<String,Object>> soldList = auctionService.entrySelectList(map);
			List<Map<String,Object>> bidList = auctionService.selectBidLogList(map);
			
			mav.addObject("johapData", johap);
			mav.addObject("aucList", list);
			mav.addObject("soldList", soldList);
			mav.addObject("bidList", bidList);
			mav.addObject("bidCnt", auctionService.selectBidLogListCnt(map));
			mav.addObject("buyCnt",auctionService.selectCountEntry(map));
			mav.addObject("totPrice", auctionService.selectTotSoldPrice(map));
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
}
