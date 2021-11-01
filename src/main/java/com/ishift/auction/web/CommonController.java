package com.ishift.auction.web;

import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

public class CommonController {
	
	/**
	 * @param mav
	 * @param params
	 * @param returnUrl
	 * @param returnParam
	 * @param message
	 * @param script
	 * @return
	 */
	protected ModelAndView makeMessageResult (final ModelAndView mav
											, final Map<String, Object> params
											, final String returnUrl
											, final String returnParam
											, final String message
											, final String script) {
		mav.addObject("returnParam", returnParam);
		mav.addObject("returnUrl", returnUrl);
		mav.addObject("message", message);
		mav.addObject("script", script);
		mav.addObject("reqVo", params);
		mav.setViewName("front/common/message");
		return mav;
	}

}
