package com.ishift.auction.web;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.service.common.CommonService;
import com.ishift.auction.service.dashboard.DashBoardService;
import com.ishift.auction.util.DateUtil;
import com.ishift.auction.util.HttpUtils;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.vo.AdminUserDetails;

@RestController
public class DashboardController {

	private static Logger log = LoggerFactory.getLogger(DashboardController.class);
	
	@Autowired
	AuctionService auctionService;
	
	@Autowired
	DashBoardService dashboardService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private SessionUtill sessionUtill;

	@Autowired
	private HttpUtils httpUtils;
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * 대시보드 기본 월 설정하는 메소드
	 * 오늘 날짜가 월의 10일 이전이면 전월 데이터를 기본으로 보여주기
	 * 10일 이후면 현재 월 데이터 보여주기
	 * @param params
	 */
	private void setDayToSearchMonth(Map<String, Object> params) {
		String searchMonth = params.getOrDefault("searchMonth", "").toString();
		if("".equals(searchMonth)) {
			int dayOfMonth = LocalDate.now().getDayOfMonth();
			if(dayOfMonth < 10) {	//오늘 날짜가 월의 10일 이전이면 전월 데이터를 기본으로 보여주기
				searchMonth = LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
			}else {
				searchMonth = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
			}
			params.put("searchMonth", searchMonth);
		}
		else {	//param으로 넘어올 때
			String pnFlag = params.get("searchFlag") == null ? "" : params.get("searchFlag").toString();
			LocalDate date = LocalDate.parse(searchMonth+"01", DateTimeFormatter.ofPattern("yyyyMMdd"));
			if("prev".equals(pnFlag)) {
				searchMonth = date.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
			}else if("next".equals(pnFlag)) {
				searchMonth = date.plusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
			}
			params.put("searchMonth", searchMonth);
		}
		
		LocalDate currMonth = LocalDate.parse(searchMonth+"01", DateTimeFormatter.ofPattern("yyyyMMdd"));
		String beforeMonth = currMonth.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
		String before2Month = currMonth.minusMonths(2).format(DateTimeFormatter.ofPattern("yyyyMM"));
		params.put("beforeMonth", beforeMonth);		//증감율 계산을 위한 전월
		params.put("before2Month", before2Month);		//출장우 조회화면을 위한 전전월
		
		params.put("beforeYear", currMonth.minusYears(1).format(DateTimeFormatter.ofPattern("yyyy")));
		params.put("searchYear", currMonth.format(DateTimeFormatter.ofPattern("yyyy")));
		params.put("searchMonTxt", searchMonth.substring(0, 4) + "년 "  + searchMonth.substring(4, 6) + "월");
	}
	
	/**
	 * 대시보드 기본 쿼리 캐시 담은 것 clear 하기
	 * @param cacheName
	 * @return
	 * @throws Exception
	 */
	@GetMapping(value = "/dashboard/invalidateCacheMap")
	public Map<String, Object> invalidateCacheMap(@RequestParam(name = "cacheName") String cacheName) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		dashboardService.invalidateCacheMap(cacheName);
		resultMap.put("success", true);
		resultMap.put("message", "캐시 삭제 성공 : " + cacheName);
		return resultMap;
	}
	
	/**
	 * 메인 > 현황 > 대시보드 페이지
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/dashboard/main",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboard(@RequestParam Map<String, Object> params,HttpServletRequest req) throws Exception {
		// 메인화면 현황
		ModelAndView mav = new ModelAndView("dashboard/main");
		// 기준 날짜 range (초기값 10)
		int range = Integer.parseInt(StringUtils.isEmpty(params.get("searchRaDate")) ? "30" : params.get("searchRaDate").toString().replaceAll("range", "")); 
		
		// 데이터는 오늘 -1이 들어오기때문에 minusDays(1) 설정
		String searchDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String searchPreDate = LocalDate.now().minusDays(range).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
		String title = "최근 " + range + "일 (" + DateUtil.addSlashYYMMDD(searchPreDate) + " ~ " + DateUtil.addSlashYYMMDD(searchDate) + " 까지)";
		
		params.putIfAbsent("searchAucObjDsc", "1");		//기본으로 송아지
		
		// 1. 최근 가축시장 시세 조회 
		mav.addObject("cowPriceList", dashboardService.findCowPriceList(params));
		// 2. 지역별 평균 낙찰가
		mav.addObject("avgPlaceBidAmList", dashboardService.findAvgPlaceBidAmList(params));
		// 3. 금주의 TOP 3
		mav.addObject("recentDateTopList", dashboardService.findRecentDateTopList(params));

		// 4.최근 가축시장 시세 기준 조합수
		mav.addObject("cntNaBzplc", dashboardService.selCowBzplcCnt(params));
		
		mav.addObject("range", range);
		mav.addObject("title", title);
		mav.addObject("searchDate", searchDate);
		mav.addObject("searchPreDate", searchPreDate);
		mav.addObject("inputParam", params);	
		try {			
			commonService.callRenderingAdsLog("dashboard/main",httpUtils.getClientIp(req));
		}catch (Exception e) {
			log.error(e.getMessage()+"|| 배너광고 로그 등록중 err : {}",e);
		}	
		return mav;
	}
	@PostMapping(value = "/dashboard/main_ajax", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> dashboard_ajax(@RequestBody Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);
		try {
			// 기준 날짜 range (초기값 10)
			int range = Integer.parseInt(StringUtils.isEmpty(params.get("searchRaDate")) ? "30" : params.get("searchRaDate").toString().replaceAll("range", "")); 
			
			// 데이터는 오늘 -1이 들어오기때문에 minusDays(1) 설정
			String searchDate = LocalDate.now().minusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			String searchPreDate = LocalDate.now().minusDays(range).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			String title = "최근 " + range + "일 (" + DateUtil.addSlashYYMMDD(searchPreDate) + " ~ " + DateUtil.addSlashYYMMDD(searchDate) + " 까지)";
			
			// 1. 최근 가축시장 시세 조회 
			resultMap.put("cowPriceList", dashboardService.findCowPriceList(params));
			// 2. 지역별 평균 낙찰가
			resultMap.put("avgPlaceBidAmList", dashboardService.findAvgPlaceBidAmList(params));
			// 3. 금주의 TOP 3
			resultMap.put("recentDateTopList", dashboardService.findRecentDateTopList(params));

			// 4.최근 가축시장 시세 기준 조합수
			resultMap.put("cntNaBzplc", dashboardService.selCowBzplcCnt(params));
			
			resultMap.put("range", range);
			resultMap.put("title", title);
			resultMap.put("searchDate", searchDate);
			resultMap.put("searchPreDate", searchPreDate);
			resultMap.put("inputParam", params);			
		} catch (SQLException | RuntimeException re) {
			resultMap.put("success", false);
			resultMap.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return resultMap;
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/dashboard/btc_price",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboard_btc_price(@RequestParam Map<String, Object> params) throws Exception {
		// 도축장 시세
		ModelAndView mav = new ModelAndView("dashboard/btc_price");
		this.setDayToSearchMonth(params);
		
		params.putIfAbsent("searchIndvSexC", "1");
		params.putIfAbsent("sortCommand", "DESC");
		Map<String, Object> btcAucDate = dashboardService.getBtcAucDateInfo(params);	//도축장 시세 날짜 로직 가져오기
		if(btcAucDate != null) {
			params.put("SBID_DT", btcAucDate.get("SBID_DT"));		//현재 가장 가까운 도축일자
			params.put("BEFORE_DT", btcAucDate.get("BEFORE_DT"));		//그 이전 도축일자
			
			String basicDt = btcAucDate.get("SBID_DT").toString();
			String basicYm = basicDt.substring(0, 4) + "년 " + basicDt.substring(4, 6)  + "월";
			String basicDay = basicDt.substring(6, 8) + " 일";
			mav.addObject("basicYm", basicYm);
			mav.addObject("basicDay", basicDay);
		}
		
		List<Map<String, Object>> btcAucGrd = dashboardService.getBtcAucGradeList(params);		//성별, 도축일자에 따른 등급 종류
		int grdSize = btcAucGrd == null ? 0 : btcAucGrd.size();
		if(grdSize > 0) {
			params.put("SRA_GRD_DSC", btcAucGrd.get(0).get("SRA_GRD_DSC"));		//도축장 경매 정보 중, 가장 위 등급을 기본으로 셋팅하여 데이터 가져오기
		}
		
		Map<String, Object> btcAucAvg = dashboardService.getBtcAucPriceAvgInfo(params);	//거래가 평균 정보
		List<Map<String, Object>> areaList = dashboardService.getBtcAucAreaAvgList(params);	//지역별 거래가, 낙찰두수 (차트와 목록 같이 사용)
		Map<String, Object> areaSum = dashboardService.getBtcAucAreaAvgSum(params);		//지역별 목록의 총 평균
		
		mav.addObject("btcAucDate", btcAucDate);
		mav.addObject("btcAucGrd", btcAucGrd);
		mav.addObject("btcAucAvg", btcAucAvg);
		mav.addObject("areaList", areaList);
		mav.addObject("areaSum", areaSum);
		mav.addObject("dashheaderTitle", "도축장시세");
		
		return mav;
	}
	
	@PostMapping(value = "/dashboard/btc_price_ajax", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> dashboard_btc_price_ajax(@RequestBody Map<String, Object> params) throws Exception{
		// 가축시장 참가현황
		Map<String, Object> resultMap = new HashMap<>();
		this.setDayToSearchMonth(params);
		
		try {
			params.putIfAbsent("searchIndvSexC", "1");
			
			String addFlag = params.get("addFlag") == null ?  "" : params.get("addFlag").toString();
			String sortCommand = addFlag.equals("next") ? "ASC" : "DESC";
			params.put("sortCommand", sortCommand);
			
			Map<String, Object> btcAucDate = dashboardService.getBtcAucDateInfo(params);	//도축장 시세 날짜 로직 가져오기
			if(btcAucDate != null) {
				params.put("SBID_DT", btcAucDate.get("SBID_DT"));		//현재 가장 가까운 도축일자
				params.put("BEFORE_DT", btcAucDate.get("BEFORE_DT"));		//그 이전 도축일자
				
				String basicDt = btcAucDate.get("SBID_DT").toString();
				String basicYm = basicDt.substring(0, 4) + "년 " + basicDt.substring(4, 6)  + "월";
				String basicDay = basicDt.substring(6, 8) + " 일";
				resultMap.put("basicYm", basicYm);
				resultMap.put("basicDay", basicDay);
			}
			
			List<Map<String, Object>> btcAucGrd = dashboardService.getBtcAucGradeList(params);	//성별,날짜에 따른 등급 종류
			int grdSize = btcAucGrd == null ? 0 : btcAucGrd.size();
			
			if("".equals(params.get("SRA_GRD_DSC"))) {
				if(grdSize > 0) {
					params.put("SRA_GRD_DSC", btcAucGrd.get(0).get("SRA_GRD_DSC"));		//도축장 경매 정보 중, 가장 위 등급을 기본으로 셋팅하여 데이터 가져오기
				}
			}
			Map<String, Object> btcAucAvg = dashboardService.getBtcAucPriceAvgInfo(params);	//거래가 평균 정보
			List<Map<String, Object>> areaList = dashboardService.getBtcAucAreaAvgList(params);	//지역별 거래가, 낙찰두수 (차트와 목록 같이 사용)
			Map<String, Object> areaSum = dashboardService.getBtcAucAreaAvgSum(params);		//지역별 목록의 총 평균
			
			resultMap.put("btcAucDate", btcAucDate);
			resultMap.put("btcAucGrd", btcAucGrd);
			resultMap.put("btcAucAvg", btcAucAvg);
			resultMap.put("areaList", areaList);
			resultMap.put("areaSum", areaSum);
			resultMap.put("dashheaderTitle", "도축장시세");
			
			resultMap.put("success", true);
			resultMap.put("inputParam", params);
			
		}catch(SQLException | RuntimeException re) {
			log.error("DashboardController.dashboard_btc_price_ajax : {} ", re);
			resultMap.put("success", false);
			resultMap.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return resultMap;
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/dashboard/parti_status",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboard_parti_status(@RequestParam Map<String, Object> params) throws Exception {
		// 가축시장 참가현황
		ModelAndView mav = new ModelAndView("dashboard/parti_status");
		
		this.setDayToSearchMonth(params);
		Map<String, Object> bidderInfo = dashboardService.findPartiBidderInfo(params);
		List<Map<String, Object>> bidderPerList = dashboardService.findPartiBidderPerList(params);
		
		mav.addObject("bidderInfo", bidderInfo);
		mav.addObject("bidderPerList", bidderPerList);		
		mav.addObject("dashheaderTitle", "낙찰 현황");
		mav.addObject("searchMonTxt", params.get("searchMonTxt"));
		mav.addObject("inputParam", params);	
		return mav;
	}
	
	@PostMapping(value = "/dashboard/parti_status_ajax", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> dashboard_parti_status_ajax(@RequestBody Map<String, Object> params) throws Exception{
		// 가축시장 참가현황
		Map<String, Object> resultMap = new HashMap<>();
		this.setDayToSearchMonth(params);
		
		try {
			Map<String, Object> bidderInfo = dashboardService.findPartiBidderInfo(params);
			List<Map<String, Object>> bidderPerList = dashboardService.findPartiBidderPerList(params);
			
			resultMap.put("bidderInfo", bidderInfo);
			resultMap.put("bidderPerList", bidderPerList);
			resultMap.put("success", true);
			resultMap.put("inputParam", params);
			
		}catch(SQLException | RuntimeException re) {
			resultMap.put("success", false);
			resultMap.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return resultMap;
		}
		return resultMap;
	}

	/**
	 * 낙찰시세
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/sbid_status",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboard_sbid_status(@RequestParam Map<String, Object> params) throws Exception {
		// 낙찰시세
		ModelAndView mav = new ModelAndView("dashboard/sbid_status");
		this.setDayToSearchMonth(params);
		
		params.putIfAbsent("aucObjDsc", "1");
		Map<String, Object> sbidInfo = dashboardService.findSbidPriceInfo(params);		//가축시장 낙찰가 시세
		List<Map<String, Object>> sbidList = dashboardService.findSbidPriceList(params);		//가축시장 낙찰가 시세
		List<Map<String, Object>> areaSbidList = dashboardService.findAreaSbidList(params);		//지역별 평균 낙찰가
		List<Map<String, Object>> monSbidPriceList = dashboardService.findMonthlySbidPriceList(params);		//월별 평균 낙찰가
		List<Map<String, Object>> monSogCowList = dashboardService.findMonthlySogCowList(params);		//월별 장두수
		
		mav.addObject("sbidInfo", sbidInfo);
		mav.addObject("sbidList", sbidList);
		mav.addObject("areaSbidList", areaSbidList);
		mav.addObject("monSbidPriceList", monSbidPriceList);
		mav.addObject("monSogCowList", monSogCowList);
		mav.addObject("cntNaBzplc", dashboardService.selSbidNaBzplcCnt(params));
		
		mav.addObject("dashheaderTitle", "낙찰시세");
		mav.addObject("searchMonTxt", params.get("searchMonTxt"));
		mav.addObject("inputParam", params);	
		return mav;
	}
	
	/**
	 * 낙찰시세 데이터 가져오기 ajax
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/dashboard/sbid_status_ajax", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> dashboard_sbid_status_ajax(@RequestBody Map<String, Object> params) throws Exception{
		// 가축시장 참가현황
		Map<String, Object> resultMap = new HashMap<>();
		this.setDayToSearchMonth(params);
		
		try {
			Map<String, Object> sbidInfo = dashboardService.findSbidPriceInfo(params);		//가축시장 낙찰가 시세
			List<Map<String, Object>> sbidList = dashboardService.findSbidPriceList(params);		//가축시장 낙찰가 시세
			List<Map<String, Object>> areaSbidList = dashboardService.findAreaSbidList(params);		//지역별 평균 낙찰가
			List<Map<String, Object>> monSbidPriceList = dashboardService.findMonthlySbidPriceList(params);		//월별 평균 낙찰가
			List<Map<String, Object>> monSogCowList = dashboardService.findMonthlySogCowList(params);		//월별 장두수
			
			resultMap.put("sbidInfo", sbidInfo);
			resultMap.put("sbidList", sbidList);
			resultMap.put("areaSbidList", areaSbidList);
			resultMap.put("monSbidPriceList", monSbidPriceList);
			resultMap.put("monSogCowList", monSogCowList);
			resultMap.put("cntNaBzplc", dashboardService.selSbidNaBzplcCnt(params));
			
			resultMap.put("searchMonTxt", params.get("searchMonTxt"));
			resultMap.put("success", true);
			resultMap.put("inputParam", params);
			
		}catch(SQLException | RuntimeException re) {
			resultMap.put("success", false);
			resultMap.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return resultMap;
		}
		return resultMap;
	}
	
	/**
	 * 출장우 현황
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/dashboard/cow_status", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboard_cow(@RequestParam Map<String, Object> params) throws Exception {
		// 낙찰시세
		ModelAndView mav = new ModelAndView("dashboard/cow_status");
		this.setDayToSearchMonth(params);
		
		params.putIfAbsent("aucObjDsc", "1");
		params.putIfAbsent("indvSexC", "1");
		
		Map<String, Object> sogCowInfo = dashboardService.findSogCowInfo(params);		//가축시장 출장우 현황 출장두수 및 증감율
		List<Map<String, Object>> sogCowInfoList = dashboardService.findSogCowInfoList(params);		//가축시장 출장우 현황
		List<Map<String, Object>> areaSbidList = dashboardService.findAreaSbidList(params);		//지역별 평균 예정가
		List<Map<String, Object>> monSogCowList = dashboardService.findMonthlySogCowList(params);		//월별 출장우 현황
		
		mav.addObject("sogCowInfo", sogCowInfo);
		mav.addObject("sogCowInfoList", sogCowInfoList);
		mav.addObject("areaSbidList", areaSbidList);
		mav.addObject("monSogCowList", monSogCowList);
		
		mav.addObject("dashheaderTitle", "출장우 현황");
		mav.addObject("searchMonTxt", params.get("searchMonTxt"));
		mav.addObject("searchMonTxt", params.get("searchMonTxt"));
		mav.addObject("inputParam", params);	
		return mav;
	}
	
	/**
	 * 출장우 현황 데이터 가져오기 ajax
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/dashboard/cow_status_ajax", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> dashboard_cow_ajax(@RequestBody Map<String, Object> params) throws Exception{
		// 가축시장 참가현황
		Map<String, Object> resultMap = new HashMap<>();
		this.setDayToSearchMonth(params);
		
		try {
			Map<String, Object> sogCowInfo = dashboardService.findSogCowInfo(params);		//가축시장 출장우 현황 출장두수 및 증감율
			List<Map<String, Object>> sogCowInfoList = dashboardService.findSogCowInfoList(params);		//가축시장 출장우 현황
			List<Map<String, Object>> areaSbidList = dashboardService.findAreaSbidList(params);		//지역별 평균 예정가
			List<Map<String, Object>> monSogCowList = dashboardService.findMonthlySogCowList(params);		//월별 출장우 현황
			
			resultMap.put("sogCowInfo", sogCowInfo);
			resultMap.put("sogCowInfoList", sogCowInfoList);
			resultMap.put("areaSbidList", areaSbidList);
			resultMap.put("monSogCowList", monSogCowList);
			
			resultMap.put("searchMonTxt", params.get("searchMonTxt"));
			resultMap.put("success", true);
			resultMap.put("inputParam", params);
			
		}catch(RuntimeException re) {
			resultMap.put("success", false);
			resultMap.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return resultMap;
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/dashboard/dashFilter",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashFilter(@RequestParam Map<String, Object> params) throws Exception {
		// 메인화면 > 현황 > 필터
		ModelAndView mav = new ModelAndView("pop/dashFilter");
		mav.addObject("regionList", dashboardService.findFilterRegionList(params));
		mav.addObject("inputParam", params);
		return mav;
	}
	
	@RequestMapping(value = "/dashboard/dashTop10",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashTop10(@RequestParam Map<String, Object> params) throws Exception {
		// 메인화면 > 현황 > Top10
		ModelAndView mav = new ModelAndView("pop/dashTop10");
		Map<String, Object> paramMap = new HashMap<>();		
		paramMap.putAll(params);
		// 3. 금주의 TOP 3
		mav.addObject("recentDateTopList", dashboardService.findRecentDateTopList(params));
		return mav;
	}
	
	@RequestMapping(value = "/dashboard/dashSbidPop",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashSbidPop(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mav = new ModelAndView("pop/dashSbidPop");
		this.setDayToSearchMonth(params);
		
		Map<String, Object> sbidInfo = dashboardService.findSbidPriceInfo(params);		//가축시장 낙찰가 시세

		List<Map<String, Object>> sbidList = dashboardService.findSbidPriceList(params);		//가축시장 낙찰가 시세
		List<Map<String, Object>> areaSbidList = dashboardService.findAreaSbidList(params);		//지역별 평균 낙찰가
		List<Map<String, Object>> monSbidPriceList = dashboardService.findMonthlySbidPriceList(params);		//월별 평균 낙찰가
		List<Map<String, Object>> monSogCowList = dashboardService.findMonthlySogCowList(params);		//월별 장두수
		mav.addObject("cntNaBzplc", dashboardService.selSbidNaBzplcCnt(params));		
		mav.addObject("sbidInfo", sbidInfo);
		mav.addObject("sbidList", sbidList);
		mav.addObject("areaSbidList", areaSbidList);
		mav.addObject("monSbidPriceList", monSbidPriceList);
		mav.addObject("monSogCowList", monSogCowList);
		
		mav.addObject("searchMonTxt", params.get("searchMonTxt"));
		mav.addObject("inputParam", params);	
		return mav;
	}
	
	@RequestMapping(value = "/dashboard/dashSogCowPop",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashSogCowPop(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mav = new ModelAndView("pop/dashSogCowPop");
		this.setDayToSearchMonth(params);
		
		params.put("popupFlag", "P");
		
		Map<String, Object> sogCowInfo = dashboardService.findSogCowInfo(params);		//가축시장 출장우 현황 출장두수 및 증감율
		List<Map<String, Object>> monSogCowList = dashboardService.findMonthlySogCowList(params);		//월별 출장우 현황
		
		mav.addObject("sogCowInfo", sogCowInfo);
		mav.addObject("monSogCowList", monSogCowList);
		
		mav.addObject("searchMonTxt", params.get("searchMonTxt"));
		mav.addObject("inputParam", params);	
		return mav;
	}

	/**
	 * 
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/dashboard/status/staticInfo",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboardStaticInfo(@RequestParam Map<String, Object> params) throws Exception {
		// 메인화면 현황
		ModelAndView mav = new ModelAndView("dashboard/static_info");
		if("ROLE_ADMIN".equals(sessionUtill.getRoleConfirm())) {
			AdminUserDetails userVo = (AdminUserDetails) sessionUtill.getUserVo();
			if(userVo != null) {
				Map<String,Object> temp = new HashMap<String, Object>();
				temp.put("naBzplc", userVo.getNaBzplc());
				Map<String,Object> johap = adminService.selectOneJohap(temp);
				mav.addObject("loginJohapData", johap);
				mav.addObject("loginGrpC", userVo.getGrpC());				
			}			
		}
		mav.addObject("bizList", auctionService.selectBizList(params));
		mav.addObject("dashheaderTitle", "참가자현황");
		return mav;
	}
	@RequestMapping(value = "/dashboard/status/aucBidStaticInfo",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView aucBidStaticInfo(@RequestParam Map<String, Object> params) throws Exception {
		// 메인화면 현황
		ModelAndView mav = new ModelAndView("dashboard/auc_static_info");
		if("ROLE_ADMIN".equals(sessionUtill.getRoleConfirm())) {
			AdminUserDetails userVo = (AdminUserDetails) sessionUtill.getUserVo();
			if(userVo != null) {
				Map<String,Object> temp = new HashMap<String, Object>();
				temp.put("naBzplc", userVo.getNaBzplc());
				Map<String,Object> johap = adminService.selectOneJohap(temp);
				mav.addObject("loginJohapData", johap);				
				mav.addObject("loginGrpC", userVo.getGrpC());
			}			
		}
		mav.addObject("bizList", auctionService.selectBizList(params));
		mav.addObject("dashheaderTitle", "경매 낙찰현황");
		return mav;
	}

	@RequestMapping(value = "/dashboard/status/aucStaticInfo",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView aucCowStaticInfo(@RequestParam Map<String, Object> params) throws Exception {
		// 메인화면 현황
		ModelAndView mav = new ModelAndView("dashboard/auc_cow_static_info");
		if("ROLE_ADMIN".equals(sessionUtill.getRoleConfirm())) {
			AdminUserDetails userVo = (AdminUserDetails) sessionUtill.getUserVo();
			if(userVo != null) {
				Map<String,Object> temp = new HashMap<String, Object>();
				temp.put("naBzplc", userVo.getNaBzplc());
				Map<String,Object> johap = adminService.selectOneJohap(temp);
				mav.addObject("loginJohapData", johap);			
				mav.addObject("loginGrpC", userVo.getGrpC());	
			}			
		}
		mav.addObject("bizList", auctionService.selectBizList(params));
		mav.addObject("dashheaderTitle", "출장우 내역");
		return mav;
	}
	
	@RequestMapping(value = "/dashboard/getBzLocList",method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getBzLocList(@RequestBody Map<String, Object> params) throws Exception{
		// 가축시장 참가현황
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);		
		try {
			resultMap.put("locList", auctionService.selectBizLocList(params));
		}catch(RuntimeException re) {		//SQLException | 
			resultMap.put("success", false);
			resultMap.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return resultMap;
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/dashboard/getStaticInfo",method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getStaticInfo(@RequestBody Map<String, Object> params) throws Exception{
		// 가축시장 참가현황
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);		
		try {			
			resultMap.put("staticInfo", dashboardService.selStaticInfo(params));
		}catch(RuntimeException re) {		//SQLException | 
			resultMap.put("success", false);
			resultMap.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return resultMap;
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/dashboard/getStaticList",method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> selStaticList(@RequestBody Map<String, Object> params) throws Exception{
		// 가축시장 참가현황
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);		
		try {
			resultMap.put("list", dashboardService.selStaticList(params));
		}catch(RuntimeException re) {		//SQLException | 
			resultMap.put("success", false);
			resultMap.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return resultMap;
		}
		return resultMap;
	}

	
	@RequestMapping(value = "/dashboard/getAucStaticInfo",method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getAucStaticInfo(@RequestBody Map<String, Object> params) throws Exception{
		// 가축시장 참가현황
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);		
		try {			
			resultMap.put("staticInfo", dashboardService.selAucStaticInfo(params));
		}catch(RuntimeException re) {		//SQLException | 
			resultMap.put("success", false);
			resultMap.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return resultMap;
		}
		return resultMap;
	}
	
	@RequestMapping(value = "/dashboard/getAucStaticExcelList",method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getAucStaticExcelList(@RequestBody Map<String, Object> params) throws Exception{
		// 가축시장 참가현황
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);		
		try {			
			resultMap.put("list", dashboardService.selMhSogCowRowDataList(params));
		}catch(RuntimeException re) {		//SQLException | 
			resultMap.put("success", false);
			resultMap.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return resultMap;
		}
		return resultMap;
	}
}
