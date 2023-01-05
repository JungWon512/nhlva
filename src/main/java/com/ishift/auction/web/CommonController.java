package com.ishift.auction.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.service.common.CommonService;

public class CommonController {
	
	@Autowired
	private CommonService commonService;
	
	/**
	 * 에러 페이지
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
	
	/**
	 * 공통코드 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	protected List<Map<String, Object>> getCommonCode(final Map<String, Object> params) throws SQLException {
		return commonService.getCommonCode(params);
	}
	
	/**
	 * 공통코드 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	protected List<Map<String, Object>> getCommonCode(final String simpTpc, final String simpCGrpSqno) throws SQLException {
		final Map<String, Object> params = new HashMap<String, Object>();
		params.put("simpTpc", simpTpc);
		params.put("simpCGrpSqno", simpCGrpSqno);
		return commonService.getCommonCode(params);
	}

}
