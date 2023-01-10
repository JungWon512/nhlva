package com.ishift.auction.web;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.service.front.FrontService;
import com.ishift.auction.util.DateUtil;
import com.ishift.auction.util.FormatUtil;

@RestController
public class FrontController {
	
	@Autowired
	AuctionService auctionService;
	
	@Autowired
	FrontService frontService;
	
	@Autowired
	private FormatUtil formatUtil;
	
	@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView init() throws Exception {
		// 전국지도 - 8개군에서 선택
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/home");
		return mav;
	}

	@RequestMapping(value = "/home", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView main() throws Exception {
		// 전국지도 - 8개군에서 선택
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<>();
		mav.addObject("bizList", auctionService.selectBizList(map));
		mav.setViewName("front/user/home");
		return mav;
	}
	
	@RequestMapping(value = "/district", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView area(@RequestParam(name = "loc", required = false) String loc,
							 @RequestParam(name = "auctingYn", required = false) String auctingYn) throws Exception {
		// 전국지도 - 시/군 선택
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<>();
		map.put("naBzPlcLoc", loc);
		map.put("auctingYn", auctingYn);
		List<Map<String, Object>> list = auctionService.selectBizLocList(map);
		mav.addObject("locList", list);
		mav.setViewName("front/user/district");
		return mav;
	}

	@RequestMapping(value = "/index", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView login() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("front/user/index");
		return mav;
	}

	@RequestMapping(value = "/privacy", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView privacy() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("pop/privacy");
		return mav;
	}

	// 이용약관 old ver
//	@RequestMapping(value = "/agreement", method = { RequestMethod.GET, RequestMethod.POST })
//	public ModelAndView agreement() {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("pop/agreement_bak");
//		return mav;
//	}
	
	// 이용약관 new ver
	@RequestMapping(value =  {"/agreement/{date}"}, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView agreement(@PathVariable String date) {
		ModelAndView mav = new ModelAndView();
		
		if("new".equals(date)) {
			mav.setViewName("front/user/agreement");
			mav.setViewName("pop/agreement"); // 팝업용
		} else {
			mav.setViewName("front/user/agreement"+date);
			mav.addObject("subheaderTitle","이용약관"); // 뒤로가기용
		}
		return mav;
	}
	
	/**
	 * 전국 가축시장 경매안내 페이지
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/schedule", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView schedule(@RequestParam Map<String, Object> params) throws SQLException {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("front/user/schedule");
		String type = params.getOrDefault("type","").toString();
		if("".equals(type)) params.put("type", "today");
		mav.addObject("scheduleList", auctionService.selectcheduleList(params));
		mav.addObject("params", params);
		return mav;
	}
	
	/**
	 * 메인 > 현황 > 대시보드 페이지
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/dashboard",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboard(@RequestParam Map<String, Object> params) throws Exception {
		// 메인화면 현황
		ModelAndView mav = new ModelAndView("front/user/dashboard");
		return mav;
	}
	@PostMapping(value = "/dashboard_ajax", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> dashboard_ajax(@RequestBody Map<String, Object> params) throws Exception {
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);
		try {
			// 기준 날짜 range (초기값 10)
			int range = Integer.parseInt(StringUtils.isEmpty(params.get("searchRaDate")) ? "10" : params.get("searchRaDate").toString().replaceAll("range", "")); 
			
			String searchDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			String searchPreDate = LocalDate.now().minusDays(range).format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			String title = "최근 " + range + "일 (" + DateUtil.addSlashYYYYMMDD(searchPreDate) + " ~ " + DateUtil.addSlashYYYYMMDD(searchDate) + " 까지)";
			
			// 1. 최근 가축시장 시세 조회 
			resultMap.put("cowPriceList", frontService.findCowPriceList(params));
			// 2. 지역별 평균 낙찰가
			resultMap.put("avgPlaceBidAmList", frontService.findAvgPlaceBidAmList(params));
			// 3. 금주의 TOP 3
			resultMap.put("recentDateTopList", frontService.findRecentDateTopList(params));
			
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
	@RequestMapping(value = "/dashboard_btc_price",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboard_btc_price(@RequestParam Map<String, Object> params) throws Exception {
		// 도축장 시세
		ModelAndView mav = new ModelAndView("front/user/dashboard_btc_price");
		mav.addObject("dashheaderTitle", "도축장시세");
		return mav;
	}
	@RequestMapping(value = "/dashboard_cow",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboard_cow(@RequestParam Map<String, Object> params) throws Exception {
		// 출장우 현황
		ModelAndView mav = new ModelAndView("front/user/dashboard_cow");
		mav.addObject("dashheaderTitle", "출장우");
		return mav;
	}
	@RequestMapping(value = "/dashboard_parti_status",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboard_parti_status(@RequestParam Map<String, Object> params) throws Exception {
		// 가축시장 참가현황
		ModelAndView mav = new ModelAndView("front/user/dashboard_parti_status");
		
		this.setDayToSearchMonth(params);
		Map<String, Object> bidderInfo = frontService.findPartiBidderInfo(params);
		List<Map<String, Object>> bidderPerList = frontService.findPartiBidderPerList(params);
		
		mav.addObject("bidderInfo", bidderInfo);
		mav.addObject("bidderPerList", bidderPerList);
		mav.addObject("dashheaderTitle", "참가(응찰)현황");
		mav.addObject("searchMonTxt", params.get("searchMonTxt"));
		mav.addObject("inputParam", params);	
		return mav;
	}
	
	@PostMapping(value = "/dashboard_parti_status_ajax", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> dashboard_parti_status_ajax(@RequestBody Map<String, Object> params) throws Exception{
		// 가축시장 참가현황
		Map<String, Object> resultMap = new HashMap<>();
		this.setDayToSearchMonth(params);
		
		try {
			Map<String, Object> bidderInfo = frontService.findPartiBidderInfo(params);
			List<Map<String, Object>> bidderPerList = frontService.findPartiBidderPerList(params);
			
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
			String pnFlag = params.get("searchFlag").toString();
			LocalDate date = LocalDate.parse(searchMonth+"01", DateTimeFormatter.ofPattern("yyyyMMdd"));
			if("prev".equals(pnFlag)) {
				searchMonth = date.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
			}else if("next".equals(pnFlag)) {
				searchMonth = date.plusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
			}
			params.put("searchMonth", searchMonth);
		}
		params.put("searchMonTxt", searchMonth.substring(0, 4) + "년 "  + searchMonth.substring(4, 6) + "월");
	}

	@RequestMapping(value = "/dashboard_sbid_status",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashboard_sbid_status(@RequestParam Map<String, Object> params) throws Exception {
		// 낙찰시세
		ModelAndView mav = new ModelAndView("front/user/dashboard_sbid_status");
		mav.addObject("dashheaderTitle", "낙찰가 현황");
		return mav;
	}
	
	@RequestMapping(value = "/dashFilter",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashFilter(@RequestParam Map<String, Object> params) throws Exception {
		// 메인화면 > 현황 > 필터
		ModelAndView mav = new ModelAndView("pop/dashFilter");
		mav.addObject("inputParam", params);		
		return mav;
	}
	
	@RequestMapping(value = "/dashTop10",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashTop10(@RequestParam Map<String, Object> params) throws Exception {
		// 메인화면 > 현황 > Top10
		ModelAndView mav = new ModelAndView("pop/dashTop10");
		Map<String, Object> paramMap = new HashMap<>();		
		paramMap.putAll(params);
		// 금주의 TOP 10
		mav.addObject("recentDateTopList", frontService.findRecentDateTopList(paramMap));
		return mav;
	}
	
	@RequestMapping(value = "/dashSbidPop",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView dashSbidPop() throws Exception {
		ModelAndView mav = new ModelAndView("pop/dashSbidPop");
		return mav;
	}
}
