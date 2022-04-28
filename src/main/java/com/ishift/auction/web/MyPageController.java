
package com.ishift.auction.web;

import java.sql.SQLException;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.vo.FarmUserDetails;

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
    private SessionUtill sessionUtill;
	
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@RequestMapping(value = "/my/buy",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView myresults(@RequestParam Map<String,Object> params) throws Exception {
		// 나의낙찰내역
		LOGGER.debug("start of myBuy.do");
		ModelAndView mav = new ModelAndView();
		String place = (String) params.get("place");
		
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", place);

		Map<String,Object> johap = adminService.selectOneJohap(map);

		map.put("naBzPlc", johap.get("NA_BZPLC"));
		map.put("entryType", "A");
		List<Map<String,Object>> datelist=auctionService.selectAucDateList(map);
		if(params != null && params.get("searchDate") != null) {
			map.put("searchDate", params.get("searchDateBuy"));
		}else {
			map.put("searchDate",datelist.size() > 0 ? datelist.get(0).get("AUC_DT") :null);
		}
		if(params != null && map.get("searchDate") != null) params.put("searchDate", map.get("searchDate"));
		
		map.put("naBzPlcNo", place);
		if(sessionUtill.getUserId() != null) map.put("searchTrmnAmnNo", sessionUtill.getUserId());
		List<Map<String,Object>> list=auctionService.entrySelectList(map);		
		List<Map<String,Object>> bidList = auctionService.selectBidLogList(map);

		if(sessionUtill.getUserId() != null) params.put("loginNo", sessionUtill.getUserId());
		mav.addObject("johapData", johap);
		mav.addObject("subheaderTitle","경매예정조회");
		mav.addObject("dateList",datelist);
		mav.addObject("buyCnt",auctionService.selectCountEntry(map));
		mav.addObject("buyList",list);
		mav.addObject("bidList",bidList);
		mav.addObject("bidCnt", auctionService.selectBidLogListCnt(map));
		mav.addObject("totPrice", auctionService.selectTotSoldPrice(map));
		mav.addObject("inputParam", params);
		mav.addObject("subheaderTitle","나의 경매내역");
		mav.setViewName("mypage/buy/buy");
		return mav;
	}

	@ResponseBody
	@PostMapping(path = "/auction/api/select/myBuyList", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> selectMyBuyList(@RequestBody Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
        // params.put("loginNo", sessionUtill.getUserId());
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

	@ResponseBody
	@PostMapping(path = "/auction/api/select/myBidList", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> selectBidLogList(@RequestBody Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
        // params.put("loginNo", sessionUtill.getUserId());
        result.put("success", true);
        try {        	
        	if(params.get("loginNo") != null) params.put("searchTrmnAmnNo", params.get("loginNo"));
        	List<Map<String,Object>> list=auctionService.selectBidLogList(params);
            result.put("data", list);
        }catch (SQLException | RuntimeException re) {
            result.put("success", false);
            result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			log.error("MyPageController.selectMyBuyList : {} ",re);
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
	
	@PreAuthorize("hasRole('ROLE_FARM')")
	@RequestMapping(value = "/my/entry",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView myEntry(@RequestParam Map<String, Object> params) throws Exception {
		// 나의출장우내역(출하주)
		LOGGER.debug("start of entry.do");
		ModelAndView mav = new ModelAndView();
		Map<String,Object> map = new HashMap<>();
		map.put("naBzPlcNo", params.get("place"));
		Map<String, Object> johap = adminService.selectOneJohap(map);

		map.put("naBzPlc", johap.get("NA_BZPLC"));
		map.put("entryType", "A");
		List<Map<String,Object>> datelist=auctionService.selectAucDateList(map);
		if(params.get("searchDate") != null && params != null) {
			map.put("searchDate", params.get("searchDate"));			
		}else {
			map.put("searchDate",datelist.size() > 0 ? datelist.get(0).get("AUC_DT") :null);
		}
		FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
		if(params.get("searchOrder") != null) map.put("searchOrder", params.get("searchOrder"));
		if(params.get("searchAucObjDsc") != null) map.put("searchAucObjDsc", params.get("searchAucObjDsc"));
		if(params.get("searchTxt") != null) map.put("searchTxt", params.get("searchTxt"));
		if(userVo != null) map.put("searchFhsIdNo", userVo.getFhsIdNo());
		if(userVo != null) map.put("searchFarmAmnno", userVo.getFarmAmnno());
		
		List<Map<String,Object>> list=auctionService.entrySelectList(map);
		if(params != null && map.get("searchDate") != null) params.put("searchDate", map.get("searchDate"));
		
		mav.addObject("johapData", johap);
		mav.addObject("paramVo", params);		
		mav.addObject("dateList",datelist);
		mav.addObject("myEntryList",list);
		mav.addObject("subheaderTitle","나의 출장우");
		mav.setViewName("mypage/entry/entry");
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

}
