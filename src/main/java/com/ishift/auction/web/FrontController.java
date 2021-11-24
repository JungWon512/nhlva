package com.ishift.auction.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.service.auction.AuctionService;

@RestController
public class FrontController {
	private static final Logger LOGGER = LoggerFactory.getLogger(FrontController.class);
	@Autowired
	AuctionService auctionService;
	
	@RequestMapping(value="/",method = { RequestMethod.GET, RequestMethod.POST }) 	
	public ModelAndView init() throws Exception {
		// 전국지도 - 8개군에서 선택 
		LOGGER.debug("start of main.do");
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/home");		
		return mav;
	}	
	
	@RequestMapping(value = "/home",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView main() throws Exception {
		// 전국지도 - 8개군에서 선택 
		LOGGER.debug("start of main.do");
		ModelAndView mav = new ModelAndView();
		Map<String,Object>  map = new HashMap<>();
		mav.addObject("bizList", auctionService.selectBizList(map));
		mav.setViewName("front/user/home");
		return mav;
	}
	
	@RequestMapping(value = "/district",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView area(
		@RequestParam(name = "loc", required = false) String loc
		,@RequestParam(name = "auctingYn", required = false) String auctingYn
	) throws Exception {
		// 전국지도 - 시/군 선택
		LOGGER.debug("start of district.do");
		ModelAndView mav = new ModelAndView();
		Map<String,Object>  map = new HashMap<>();
		map.put("naBzPlcLoc", loc);
		map.put("auctingYn", auctingYn);
		List<Map<String,Object>> list = auctionService.selectBizLocList(map);
		mav.addObject("locList", list);
		mav.setViewName("front/user/district");
		return mav;
	}
	
    @RequestMapping(value="/index",method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView login() {
        LOGGER.debug("start of index");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("front/user/index");
        return mav;
    }
	
    @RequestMapping(value="/privacy",method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView privacy() {
        LOGGER.debug("start of index");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("pop/privacy");
        return mav;
    }
	
    @RequestMapping(value="/agreement",method = { RequestMethod.GET, RequestMethod.POST })
    public ModelAndView agreement() {
        LOGGER.debug("start of index");
        ModelAndView mav = new ModelAndView();
        mav.setViewName("pop/agreement");
        return mav;
    }
}
