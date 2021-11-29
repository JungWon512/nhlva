package com.ishift.auction.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.configuration.security.token.AdminUserAuthenticationToken;
import com.ishift.auction.service.admin.login.AdminLoginService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.CookieUtil;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.util.PasswordEncoding;
import com.ishift.auction.vo.AdminUserDetails;
import com.ishift.auction.vo.JwtTokenVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AdminLoginController {
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private AdminLoginService adminLoginService;
	
	@Autowired
	private PasswordEncoding passwordEncoding;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private CookieUtil cookieUtil;
	
	/**
	 * 관리자 로그인 페이지
	 * @param map
	 * @return
	 */
	@GetMapping("/office/user/login")
	public ModelAndView adminUserLogin(@RequestParam final Map<String,Object> map) {
		ModelAndView mav = new ModelAndView("admin/user/login");
		try {
			mav.addAllObjects(map);
		}catch (RuntimeException re) {
			log.error("AdminLoginController.adminUserLogin : {} ",re);
		}
		return mav;
	}

	/**
	 * 관리자 로그인 프로세스 - 로그인 인증을 AdminUserAuthenticationProvider에 위임
	 * @param request
	 * @param response
	 * @param params
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(value="/office/user/loginProc", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> adminUserLoginProc(HttpServletRequest request
										, HttpServletResponse response
										, @RequestBody final Map<String,Object> params
										, ModelMap model) throws Exception {

		final Map<String, Object> result = new HashMap<String, Object>();
		String token = "";

		try {
			Authentication authentication = authenticationManager.authenticate(
																		new AdminUserAuthenticationToken(
																				params.getOrDefault("usrid", "").toString()
																				, params.getOrDefault("pw", "").toString()
																				, null));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
			
			AdminUserDetails adminUserDetails = (AdminUserDetails)authentication.getPrincipal();
			
			if (adminUserDetails != null) {
				JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
													.userMemNum(adminUserDetails.getUsrid())
													.auctionHouseCode(adminUserDetails.getNaBzplc())
													.userRole(Constants.UserRole.ADMIN)
													.build();
				
				token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
				final Cookie cookie = cookieUtil.createCookie(Constants.JwtConstants.ACCESS_TOKEN, token);
				response.addCookie(cookie);
				result.put("info", jwtTokenVo);
			}
			else {
				result.put("message", "입력하신 정보가 없습니다.");
				result.put("error", true);
				return result;
			}
			
		}catch (RuntimeException re) {
			log.error("AdminLoginController.adminUserLogin : {} ",re);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			result.put("error", true);
			return result;
		}

		response.setHeader(HttpHeaders.AUTHORIZATION, Constants.JwtConstants.BEARER + token);
		result.put(Constants.JwtConstants.ACCESS_TOKEN, token);
		result.put("message", "성공");
		result.put("returnUrl", "/main");
		result.put("error", false);
		
		return result;
	}

	/**
	 * 관리자 로그인 프로세스
	 * @param request
	 * @param response
	 * @param params
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@Deprecated
//	@PostMapping(value="/office/user/loginProc", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> adminUserLoginProcBack(HttpServletRequest request
										, HttpServletResponse response
										, @RequestBody final Map<String,Object> params
										, ModelMap model) throws Exception {

		final Map<String, Object> result = new HashMap<String, Object>();
		String token = "";

//		// 중도매인 검색 (같은 사업장에 이름과 생년월일이 같은 사람이 있을지도?)
		final Map<String, Object> info = adminLoginService.selectLoginAdminInfo(params);

		final String password = params.getOrDefault("pw", "").toString();
		
		if (info != null
//		&& passwordEncoding.matches(password, info.get("pw").toString())
		) {

			log.debug("input password : {}", password);
			log.debug("saved password : {}", info.get("PW").toString());
			log.debug("match password : {}", passwordEncoding.matches(password, info.get("PW").toString()));

			JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
												.userMemNum(info.get("USRID").toString())
												.auctionHouseCode(info.get("NA_BZPLC").toString())
												.userRole(Constants.UserRole.ADMIN)
//												.userBirthdate(info.get("CUS_RLNO").toString())
//												.userName(info.get("SRA_MWMNNM").toString())
//												.entryNum(1)
//												.deviceUUID("A1234")
//												.auctionClass("C200")
//												.auctionType("C200")
												.build();

			token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
			final Cookie cookie = cookieUtil.createCookie(Constants.JwtConstants.ACCESS_TOKEN, token);
			response.addCookie(cookie);
			result.put("info", jwtTokenVo);
		}
		else {
			result.put("message", "입력하신 정보가 없습니다.");
			result.put("error", true);
			return result;
		}

		response.setHeader(HttpHeaders.AUTHORIZATION, Constants.JwtConstants.BEARER + token);
		result.put(Constants.JwtConstants.ACCESS_TOKEN, token);
		result.put("message", "성공");
		result.put("returnUrl", "/main");
		result.put("error", false);
		
		return result;
	}

}
