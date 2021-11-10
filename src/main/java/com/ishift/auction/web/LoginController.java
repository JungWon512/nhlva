package com.ishift.auction.web;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.login.LoginService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.CookieUtil;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.util.PasswordEncoding;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created com.tirablue.generator.login by photoprogrammer on 04/02/2019
 */
@Slf4j
@RestController
public class LoginController {

	@Autowired
	LoginService loginService;

	@Autowired
	PasswordEncoding passwordEncoding;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@Autowired
	CookieUtil cookieUtil;

	@Autowired
	AdminService adminService;

	/**
	 * 로그인 페이지
	 * @param request
	 * @param response
	 * @param map
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/user/login")
	public ModelAndView login(HttpServletRequest request
							, HttpServletResponse response
							, @RequestParam final Map<String,Object> params
							, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("front/user/login");
		// 로그인 페이지에 다시 접근 할 경우 access_token 삭제
//		response.addCookie(cookieUtil.deleteCookie(request, Constants.JwtConstants.ACCESS_TOKEN));
		
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", params.get("place"));
        mav.addObject("subheaderTitle","로그인");
        mav.addObject("johapData", adminService.selectOneJohap(map));
		mav.addAllObjects(params);
		return mav;
	}
	
	/**
	 * 토큰 payload 정보를 전달한다.
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/user/getTokenInfo")
	@ApiOperation(value = "토큰 payload 정보", tags = "getTokenInfo")
	Map<String, Object> getTokenInfo(@RequestBody final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<>();
		try {
			final String token = params.get("token").toString();
			if (jwtTokenUtil.isValidToken(token)) {
				result.put("auctionCode", jwtTokenUtil.getValue(token, Constants.JwtConstants.JWT_CLAIM_AUCTION_HOUSE_CODE));
				result.put("userNum", jwtTokenUtil.getValue(token, Constants.JwtConstants.JWT_CLAIM_USER_MEM_NUM));
				params.put("naBzplc", jwtTokenUtil.getValue(token, Constants.JwtConstants.JWT_CLAIM_AUCTION_HOUSE_CODE));
				Map<String, Object> branchInfo = adminService.selectOneJohap(params);
				result.put("auctionCodeName", branchInfo.get("CLNTNM"));
				result.put("success", true);
			}
			else {
				result.put("success", false);
			}
			return result;
		}catch (RuntimeException re) {
			log.error("LoginController.getTokenInfo : {} ",re);
			result.put("success", false);
			return result;
		}catch (SQLException se) {
			log.error("LoginController.getTokenInfo : {} ",se);
			result.put("success", false);
			return result;
		} 
	}

	/**
	 * 로그인 프로세스
	 * @param request 
	 * @param response
	 * @param map
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(value="/user/loginProc", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> loginProc(HttpServletRequest request
										, HttpServletResponse response
										, @RequestBody final Map<String,Object> params
										, ModelMap model) throws Exception {

		final Map<String, Object> result = loginService.loginProc(params);
		String token = "";

		if ((boolean)result.get("success")) {
			token = result.get("token").toString();
			Cookie cookie = cookieUtil.createCookie(Constants.JwtConstants.ACCESS_TOKEN, token);
			response.addCookie(cookie);
			response.setHeader(HttpHeaders.AUTHORIZATION, Constants.JwtConstants.BEARER + token);
			result.put(Constants.JwtConstants.ACCESS_TOKEN, token);
		}
		return result;
	}
	
	/**
	 * @param request
	 * @param response
	 * @return
	 */
	@GetMapping(path = "/user/logoutProc")
	public void logoutProc(HttpServletRequest request
						 , HttpServletResponse response) {
		try {
			response.addCookie(cookieUtil.deleteCookie(request, Constants.JwtConstants.ACCESS_TOKEN));
			response.sendRedirect("/home");
		}catch (RuntimeException re) {
			log.error("LoginController.logoutProc : {} ",re);
		} catch (IOException ie) {
			log.error("LoginController.logoutProc : {} ",ie);
		}
	}

}
