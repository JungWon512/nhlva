package com.ishift.auction.web;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.login.LoginService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.CookieUtil;
import com.ishift.auction.util.JwtTokenUtil;

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
 * 로그인 관련 프로세스를 처리하는 컨트롤러
 * @author ishift
 */
@Slf4j
@RestController
public class LoginController {

	@Autowired
	private LoginService loginService;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private CookieUtil cookieUtil;

	@Autowired
	private AdminService adminService;

	/**
	 * 로그인 페이지
	 * @param request
	 * @param response
	 * @param map
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/user/login",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView login(HttpServletRequest request
							, HttpServletResponse response
							, @RequestParam final Map<String,Object> params
							, ModelMap model) throws SQLException {
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
			final String token = params.getOrDefault("token", "").toString();
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
		}
		catch (RuntimeException | SQLException re) {
			log.error("LoginController.getTokenInfo : {} ",re);
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
	@SuppressWarnings("unchecked")
	@PostMapping(value="/user/loginProc", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> loginProc(HttpServletRequest request
										, HttpServletResponse response
										, @RequestBody final Map<String,Object> params
										, ModelMap model) throws RuntimeException, SQLException {

		final Map<String, Object> result = loginService.loginProc(params);
		final String type = params.getOrDefault("type", "0").toString();
		String token = "";
		if ((boolean)result.get("success")) {
			final Map<String, Object> branchInfo = (Map<String, Object>)result.get("branchInfo");
			
			// 로그인 유형이 중도매인이고 조합에서 SMS인증을 사용하는 경우 SMS를 발송한다.
			if ("0".equals(type) && "1".equals(branchInfo.get("SMS_AUTH_YN"))) {
				final Map<String, Object> sendResult = loginService.sendSmsProc(result);
				return sendResult;
			}
			else {
				token = result.get("token").toString();
				Cookie cookie = cookieUtil.createCookie(Constants.JwtConstants.ACCESS_TOKEN, token);
				response.addCookie(cookie);
				response.setHeader(HttpHeaders.AUTHORIZATION, Constants.JwtConstants.BEARER + token);
				result.put(Constants.JwtConstants.ACCESS_TOKEN, token);
			}
		}
		return result;
	}
	
	/**
	 * 인증번호 확인 프로세스
	 * @param response
	 * @param params
	 * @return
	 * @throws RuntimeException
	 * @throws SQLException
	 */
	@ResponseBody
	@SuppressWarnings("serial")
	@PostMapping(value = "/user/loginAuthProc", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> loginAuthProc(HttpServletResponse response
											, @RequestBody final Map<String, Object> params) {
		try {
			final Map<String, Object> chkResult = loginService.loginAuthProc(params);
			if ((boolean)chkResult.getOrDefault("success", false)) {
				final String token = chkResult.get("token").toString();
				Cookie cookie = cookieUtil.createCookie(Constants.JwtConstants.ACCESS_TOKEN, token);
				response.addCookie(cookie);
				response.setHeader(HttpHeaders.AUTHORIZATION, Constants.JwtConstants.BEARER + token);
				chkResult.put(Constants.JwtConstants.ACCESS_TOKEN, token);
			}
			return chkResult;
		}
		catch(RuntimeException | SQLException e) {
			log.error("LoginController.loginAuthProc : {} ", e);
			return new HashMap<String, Object>(){{put("success", false);put("message", "작업중 오류가 발생했습니다.");}};
		}
	}
	
	/**
	 * 인증번호 확인 프로세스
	 * @param response
	 * @param params
	 * @return
	 * @throws RuntimeException
	 * @throws SQLException
	 */
	@ResponseBody
	@SuppressWarnings("serial")
	@PostMapping(value = "/user/resendSms", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> resendSms(HttpServletResponse response
			, @RequestBody final Map<String, Object> params) throws RuntimeException, SQLException {
		try {
			final Map<String, Object> sendResult = loginService.sendSmsProc(params);
			return sendResult;
		}
		catch(RuntimeException | SQLException e) {
			log.error("LoginController.loginAuthProc : {} ", e);
			return new HashMap<String, Object>(){{put("success", false);put("message", "작업중 오류가 발생했습니다.");}};
		}
	}
	
	/**
	 * 로그아웃 프로세스
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
		} catch (IOException | RuntimeException re) {
			log.error("LoginController.logoutProc : {} ", re);
		}
	}

}
