package com.ishift.auction.web;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.util.SessionUtill;

@Slf4j
@RestController
public class AuctionController extends CommonController {
	private static final Logger LOGGER = LoggerFactory.getLogger(AuctionController.class);
	
	@Autowired
	AuctionService auctionService;
	@Autowired
    private AdminService adminService;
	@Autowired
	SessionUtill sessionUtill;
    @Autowired
    JwtTokenUtil jwtTokenUtil;
	@Autowired
	private FormatUtil formatUtil;
	
	@RequestMapping(value = "/results")	
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
		map.put("searchOrder", param.get("searchOrder"));
		map.put("searchAucObjDsc", param.get("searchAucObjDsc"));
		map.put("searchTxt", param.get("searchTxt"));
		
		List<Map<String,Object>> list=auctionService.entrySelectList(map);
		param.put("searchDate", map.get("searchDate"));
		
		mav.addObject("johapData", johap);
		mav.addObject("paramVo", param);
		//mav.addObject("param", map);
		mav.addObject("dateList",datelist);
//		for(Map<String,Object> entry : list) {
//			String birthMonth = this.getConvertBirthDay(this.getStringValue(entry.get("BIRTH")));
//			entry.put("BIRTH_MONTH", birthMonth);
//		}
		mav.addObject("resultList",list);
		mav.addObject("subheaderTitle","경매결과 조회");
		
		mav.setViewName("auction/results/resultList");
		return mav;
	}

	@RequestMapping(value = "/sales")
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
		
		param.put("searchDate", map.get("searchDate"));
		map.put("naBzPlcNo", place);
		map.put("searchOrder", param.get("searchOrder"));
		map.put("searchTxt", param.get("searchTxt"));
		map.put("searchAucObjDsc", param.get("searchAucObjDsc"));
		map.put("loginNo", sessionUtill.getUserId());
		List<Map<String,Object>> list=auctionService.entrySelectList(map);

//		for(Map<String,Object> entry : list) {
//			String birthMonth = this.getConvertBirthDay(this.getStringValue(entry.get("BIRTH")));
//			entry.put("BIRTH_MONTH", birthMonth);
//		}
		param.put("loginNo", sessionUtill.getUserId());
		mav.addObject("johapData", johap);
		mav.addObject("subheaderTitle","경매예정조회");
		mav.addObject("dateList",datelist);
		mav.addObject("salesList",list);
		mav.addObject("inputParam", param);
		mav.addObject("subheaderTitle","출장우 조회");
		// mav.addObject("today",date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
		mav.setViewName("auction/sales/saleList");
		return mav;
	}

	@RequestMapping(value = "/calendar")
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
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		mav.addObject("subheaderTitle","일정안내");
		mav.setViewName("auction/calendar/calendar");
		return mav;
	}
	
	@RequestMapping(value = "/guide")
	public ModelAndView guide() throws Exception {
		// 경매안내
		LOGGER.debug("start of guide.do");
		ModelAndView mav = new ModelAndView();
		mav.addObject("subheaderTitle","이용안내");
		mav.setViewName("auction/guide/guide");
		return mav;
	}

	@RequestMapping(value = "/watch")
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
		}catch (Exception e) {
			// TODO: handle exception
		}
		mav.setViewName("auction/watch/watch");
		return mav;
	}
	
	@RequestMapping(value = "/notice")
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
	
	
	@RequestMapping(value = "/main")	
	public ModelAndView submain(@RequestParam(name = "place", required = false) String place) throws Exception {
		// 조합메인(main)
		LOGGER.debug("start of result.do");
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
		long aucYn = bizList.size() < 1?0:Integer.parseInt((String)bizList.get(0).get("AUC_YN"));
//		long aucCnt = bizList.size() < 1?0:(Long)bizList.get(0).get("AUC_CNT");
		long aucCnt = bizList.size() < 1?0:((BigDecimal)bizList.get(0).get("AUC_CNT")).longValue();
		
		log.debug("instanceof : {}", sessionUtill.getUserVo() instanceof BidUserDetails);
		
		//전광판 개발 테스트를 위한 back
		if(sessionUtill.getUserVo() instanceof BidUserDetails && list.size() > 0) {
			BidUserDetails loginUser = (BidUserDetails)sessionUtill.getUserVo();
			final Map<String, Object> params = new HashMap<String, Object>();
			params.put("naBzplc", johapData.get("NA_BZPLC"));
//			params.put("aucObjDsc", (list.get(0).get("AUC_OBJ_DSC") == null ? "" : list.get(0).get("AUC_OBJ_DSC").toString()).split(","));
			String aucObjDsc = (String)list.get(0).get("AUC_OBJ_DSC");
			params.put("aucObjDsc", aucObjDsc.split(","));
			params.put("trmnAmnno", loginUser.getTrmnAmnno());
			mav.addObject("entryList", auctionService.selectMyEntryList(params));
		}
		
		if(bizList.size() <= 0 || aucYn < 1 ) {
			//경매진행중
			mav.setViewName("auction/info/noinfo");
			mav.addObject("subheaderTitle","경매안내");
		}else if(dateVo != null && today.equals(dateVo.get("AUC_DT")) && aucCnt > 0 ){
			//경매진행중
			mav.setViewName("auction/main/main");
			mav.addObject("subheaderTitle","경매참여");
		}else {
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
		}
		catch (Exception e) {
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
//			params.put("searchDate", "20210813");
			params.put("searchDate", today);
			params.put("loginNo", sessionUtill.getUserId());
			list = auctionService.entrySelectList(params);
			mav.addObject("aucList", list);
			log.debug("params.loginNo > {}", params.get("loginNo"));
			log.debug("sessionUtill.getUserId() > {}", sessionUtill.getUserId());
			mav.addObject("params", params);
		}
		catch (Exception e) {
			log.error(e.getMessage());
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
        } catch (Exception e){
            result.put("success", false);
            result.put("message", e.getMessage());
            log.debug(e.getMessage());
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
        } catch (Exception e){
            result.put("success", false);
            result.put("message", e.getMessage());
            log.debug(e.getMessage());
        }
        return result;
    }

	@RequestMapping(value = "/auction/api/entryListApi")
	public ModelAndView entryListApiPopUp(@RequestParam Map<String,Object> param) throws Exception {
		// 경매관전
		log.debug("start of pop_entryList.do");
		ModelAndView mav = new ModelAndView();
		try {	        
			LocalDateTime date = LocalDateTime.now();
			String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	        Map<String,Object> map = new HashMap<String,Object>();
			map.put("naBzplc", param.get("naBzplc"));
			map.put("loginNo", param.get("loginNo"));
			map.put("searchDate", today);		
			Map<String,Object> johap=adminService.selectOneJohap(map);
			//map.put("loginNo", sessionUtill.getUserId());
			List<Map<String,Object>> list=auctionService.entrySelectList(map);
			mav.addObject("johapData", johap);
			mav.addObject("aucList", list);
			mav.addObject("inputParam", param);
		}
		catch (Exception e) {
			// TODO: handle exception
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
		}
		catch (Exception e) {
			e.printStackTrace();
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return mav;
	}

	private String getStringValue(Object value) {
		return value == null ? "" : value.toString();
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
}
