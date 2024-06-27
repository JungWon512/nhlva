package com.ishift.auction.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.service.common.CommonService;
import com.ishift.auction.util.HttpUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class FrontController {
	
	@Autowired
	AuctionService auctionService;

	@Autowired
	private HttpUtils httpUtils;
	
	@Autowired
	private CommonService commonService;
	
	@RequestMapping(value = "/", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView init() throws Exception {
		// 전국지도 - 8개군에서 선택
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/home");
		return mav;
	}

	@RequestMapping(value = "/home", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView main(HttpServletRequest req) throws Exception {
		// 전국지도 - 8개군에서 선택
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<>();
		try {
			Map<String,Object> bannerJson = commonService.readJsonFile("home");
			mav.addObject("bannerInfo", bannerJson);
			commonService.callRenderingAdsLog("home",httpUtils.getClientIp(req));
		}catch (Exception e) {
			log.error(e.getMessage()+"|| 배너광고 로그 등록중 err : {}",e);
		}
		mav.addObject("bizList", auctionService.selectBizList(map));
		mav.setViewName("front/user/home");
		return mav;
	}
	
	@RequestMapping(value = "/district", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView area(@RequestParam(name = "loc", required = false) String loc,
							 @RequestParam(name = "auctingYn", required = false) String auctingYn,HttpServletRequest req) throws Exception {
		// 전국지도 - 시/군 선택
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<>();
		map.put("naBzPlcLoc", loc);
		map.put("auctingYn", auctingYn);
		List<Map<String, Object>> list = auctionService.selectBizLocList(map);
		try {
			Map<String,Object> adsMap = new HashMap<>();
			commonService.callRenderingAdsLog("district",httpUtils.getClientIp(req));
		}catch (Exception e) {
			log.error(e.getMessage()+"|| 배너광고 로그 등록중 err : {}",e);
		}
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

	@RequestMapping(value = "/privacy{date}", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView privacy(@PathVariable String date) {
		ModelAndView mav = new ModelAndView();
		if(date == null || "".equals(date)) {
			mav.setViewName("pop/privacy");			
		}else {
			mav.setViewName("pop/privacy"+date);			
		}
		return mav;
	}

	// 이용약관
	@RequestMapping(value = "/agreement", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView agreement() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("pop/agreement"); // 팝업용
		return mav;
	}
	
	// 이용약관 new ver
	@RequestMapping(value =  {"/agreement/{date}"}, method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView agreement(@PathVariable String date) {
		ModelAndView mav = new ModelAndView();
		if("new".equals(date)) {
			mav.setViewName("pop/agreement"); // 이용약관동의
		} else {
			mav.setViewName("front/user/agreement"+date);  // 현재 개인정보이용방침
			mav.addObject("subheaderTitle","개인정보 처리방침"); // 뒤로가기용
		}
		return mav;
	}

	// 제3자 이용약관
	@RequestMapping(value = "/thirdAgreement", method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView thirdAgreement() {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("pop/thirdAgreement"); // 팝업용
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
	
	@RequestMapping(value = "/test/aiak", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String,Object> testAiak(@RequestParam Map<String, Object> params) throws Exception {
		// 전국지도 - 8개군에서 선택
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);		
		try {
			String barcode ="";
			if(params.get("barcode") != null) barcode = (String)params.get("barcode");
			resultMap.put("result", httpUtils.callApiAiak(barcode));
		}catch(RuntimeException re) {		//SQLException | 
			resultMap.put("success", false);
			resultMap.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return resultMap;
		}
		return resultMap;
	}

	
	@RequestMapping(value = "/home/notice", method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String,Object> homeNoticeList(@RequestParam Map<String, Object> params) throws Exception {
		// 전국지도 - 8개군에서 선택
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("success", true);		
		try {
			resultMap.put("result", auctionService.selectMainPopNoticeList(params));
		}catch(RuntimeException re) {		//SQLException | 
			resultMap.put("success", false);
			resultMap.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return resultMap;
		}
		return resultMap;
	}
}