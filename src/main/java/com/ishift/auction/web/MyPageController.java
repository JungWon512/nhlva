package com.ishift.auction.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.vo.FarmUserDetails;

@RestController
public class MyPageController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyPageController.class);

	@Autowired
	AuctionService auctionService;
	@Autowired
    private AdminService adminService;
	@Autowired
    private SessionUtill sessionUtill;
	
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@RequestMapping(value = "/my/buy")	
	public ModelAndView myresults(@RequestParam Map<String,Object> param) throws Exception {
		// 나의낙찰내역
		LOGGER.debug("start of myBuy.do");
		ModelAndView mav = new ModelAndView();
		String place = (String) param.get("place");
		
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", place);

		Map<String,Object> johap = adminService.selectOneJohap(map);

		map.put("naBzPlc", johap.get("NA_BZPLC"));
		map.put("entryType", "A");
		List<Map<String,Object>> datelist=auctionService.selectAucDateList(map);
		if(param != null && param.get("searchDateBuy") != null) {
			map.put("searchDate", param.get("searchDateBuy"));			
		}else {
			map.put("searchDate",datelist.size() > 0 ? datelist.get(0).get("AUC_DT") :null);
		}
		param.put("searchDateBuy", map.get("searchDate"));
		
		map.put("naBzPlcNo", place);
		map.put("searchTxt", param.get("searchTxtBuy"));
		map.put("searchAucObjDsc", param.get("searchAucObjDscBuy"));
		map.put("searchTrmnAmnNo", sessionUtill.getUserId());
		List<Map<String,Object>> list=auctionService.entrySelectList(map);

		param.put("loginNo", sessionUtill.getUserId());
		mav.addObject("johapData", johap);
		mav.addObject("subheaderTitle","경매예정조회");
		mav.addObject("dateList",datelist);
		mav.addObject("buyList",list);
		mav.addObject("inputParam", param);
		mav.addObject("subheaderTitle","나의 구매내역");
		mav.setViewName("mypage/buy/buy");
		return mav;
	}

	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@RequestMapping(value = "/my/bid")
	public ModelAndView mybidding() throws Exception {
		// 나의응찰내역
		LOGGER.debug("start of bid.do");
		ModelAndView mav = new ModelAndView();
		mav.addObject("subheaderTitle","응찰내역");
		mav.setViewName("mypage/bid/bid");
		return mav;
	}
	
	@PreAuthorize("hasRole('ROLE_FARM')")
	@RequestMapping(value = "/my/entry")
	public ModelAndView myEntry(@RequestParam Map<String, Object> param) throws Exception {
		// 나의출장우내역(출하주)
		LOGGER.debug("start of entry.do");
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
		FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
		map.put("searchOrder", param.get("searchOrder"));
		map.put("searchAucObjDsc", param.get("searchAucObjDsc"));
		map.put("searchTxt", param.get("searchTxt"));
		map.put("searchFhsIdNo", userVo.getFhsIdNo());
		map.put("searchFarmAmnno", userVo.getFarmAmnno());
		
		List<Map<String,Object>> list=auctionService.entrySelectList(map);
		param.put("searchDate", map.get("searchDate"));
		
		mav.addObject("johapData", johap);
		mav.addObject("paramVo", param);		
		mav.addObject("dateList",datelist);
		mav.addObject("myEntryList",list);
		mav.addObject("subheaderTitle","나의 출장우");
		mav.setViewName("mypage/entry/entry");
		return mav;
	}
	
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@PostMapping(value = "/my/favorite", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> myFavorite(@RequestBody final Map<String, Object> params) throws Exception {
		final Map<String, Object> result = new HashMap<String, Object>();
		try {
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
			result.put("message", e.getMessage());
			return result;
		}
	}

}
