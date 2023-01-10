package com.ishift.auction.web;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.login.LoginService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.CookieUtil;
import com.ishift.auction.util.HttpUtils;
import com.ishift.auction.util.JsonUtils;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.util.PasswordEncoding;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.login.LoginService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.CookieUtil;
import com.ishift.auction.util.JwtTokenUtil;

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

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
	private PasswordEncoding passwordEncoding;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private CookieUtil cookieUtil;

	@Autowired
	private AdminService adminService;

	@Autowired
	private AuctionController auctionController;

	@Autowired
	private HttpUtils httpUtils;

	/**
	 * 로그인 페이지
	 * @param request
	 * @param response
	 * @param map
	 * @param model
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/user/oauth",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView oauth(HttpServletRequest request
							, HttpServletResponse response
							, @RequestParam final Map<String,Object> params
							, ModelMap model) throws Exception {
		ModelAndView mav = new ModelAndView();
		mav.setViewName("front/user/oauth");
		
		String code = (String)params.getOrDefault("code","");
		String state = (String)params.getOrDefault("state","");
		
		String kakaoRefreshToken = cookieUtil.getCookieValue(request, "nhlva_k_r_token");
		String redirect_url = "http://localhost:8080/user/oauth";
		String url = "/oauth/token";
		String tempParm = "&grant_type=authorization_code&code="+code+"&redirect_uri="+redirect_url;
		String apiResult="";
		JSONObject json=null;
		if(!StringUtils.isEmpty(kakaoRefreshToken)) {
			tempParm="&grant_type=refresh_token&refresh_token="+kakaoRefreshToken;		
			apiResult = httpUtils.callApiKauth(url, "POST", "client_id=005918c16a55cafd5746f248a883e97e"+tempParm);
			if(apiResult != null) {
				json = JsonUtils.getJsonObjectFromString(apiResult);
				if(json.get("error_code") != null) {
					apiResult = httpUtils.callApiKauth(url, "POST", "client_id=005918c16a55cafd5746f248a883e97e"+"&grant_type=authorization_code&redirect_uri="+redirect_url+"&code="+code);
					json = JsonUtils.getJsonObjectFromString(apiResult);
					
					String aToken = (String) json.get("access_token");
					String rToken = (String) json.get("refresh_token");
					log.info("KAKAO ACCESS : TOKEN : "+aToken);
					log.info("KAKAO REFRESH : TOKEN : "+rToken);
					String tokenData= httpUtils.callApiKakaoTokenInfo("GET", aToken);
					if(tokenData != null) {
						//mav.addObject("tokenData",tokenData);
						String place="";
						String[] stateArr = ((String)params.get("state")).split("&");
						for(String param : stateArr) {
							String key = param.split("=")[0];
							String value = param.split("=")[1];
							if("place".equals(key)){
								place = value;
								break;
							}
						}
						//loginService.upd
						params.put("userName", "홍길동1");
						params.put("birthDate", "090101");
						params.put("place", place);
						params.put("type", "0");
						Map<String, Object> result = this.loginProc(request, response, params, model);
						if((boolean) result.get("success")) {
							mav.addObject("loginResult",result);
							//this.login(request,response,params,mav.getModelMap());
							//return auctionController.submain(place, request, response,mav.getModelMap());
							mav.setViewName("redirect:/main?"+state);						
						}else {
							request.getSession().setAttribute("kkoLoginResult", result.get("success"));
							request.getSession().setAttribute("kkoLoginResultMsg", result.get("message"));
							mav.setViewName("redirect:/user/login?"+state);
						}
						return mav;
					}
				}
			}
		}else{
			apiResult = httpUtils.callApiKauth(url, "POST", "client_id=005918c16a55cafd5746f248a883e97e"+tempParm);
			if(apiResult != null) {
				json = JsonUtils.getJsonObjectFromString(apiResult);
				
				String aToken = (String) json.get("access_token");
				String rToken = (String) json.get("refresh_token");
				log.info("KAKAO ACCESS : TOKEN : "+aToken);
				log.info("KAKAO REFRESH : TOKEN : "+rToken);
				String tokenData= httpUtils.callApiKakaoTokenInfo("GET", aToken);
				if(tokenData != null) {
					//mav.addObject("tokenData",tokenData);
					String place="";
					String[] stateArr = ((String)params.get("state")).split("&");
					for(String param : stateArr) {
						String key = param.split("=")[0];
						String value = param.split("=")[1];
						if("place".equals(key)){
							place = value;
							break;
						}
					}
					//loginService.upd
					params.put("userName", "홍길동1");
					params.put("birthDate", "090101");
					params.put("place", place);
					params.put("type", "0");
					Map<String, Object> result = this.loginProc(request, response, params, model);
					if((boolean) result.get("success")) {
						mav.addObject("loginResult",result);
						//this.login(request,response,params,mav.getModelMap());
						//return auctionController.submain(place, request, response,mav.getModelMap());
						mav.setViewName("redirect:/main?"+state);						
					}else {
						request.getSession().setAttribute("kkoLoginResult", result.get("success"));
						request.getSession().setAttribute("kkoLoginResultMsg", result.get("message"));
						mav.setViewName("redirect:/user/login?"+state);
					}
					return mav;
				}
			}
		}		
		mav.setViewName("redirect:/user/login?"+state);

//		mav.addObject("state", state);
//		mav.addObject("tokenData", tokenData);
//		System.out.println(obj);
		// 로그인 페이지에 다시 접근 할 경우 access_token 삭제
//		response.addCookie(cookieUtil.deleteCookie(request, Constants.JwtConstants.ACCESS_TOKEN));
//		if(params.get("error") != null) mav.setViewName("redirect:/user/login"+state);
//		else mav.setViewName("redirect:/main"+state);
		return mav;
	}
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
        mav.addObject("kkoLoginResult", request.getSession().getAttribute("kkoLoginResult"));
        mav.addObject("kkoLoginResultMsg", request.getSession().getAttribute("kkoLoginResultMsg"));
		request.getSession().setAttribute("kkoLoginResult", "");
		request.getSession().setAttribute("kkoLoginResultMsg", "");
        
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
		String token = "";
		if ((boolean)result.get("success")) {
			final Map<String, Object> branchInfo = (Map<String, Object>)result.get("branchInfo");
			
			// 로그인 유형이 중도매인이고 조합에서 SMS인증을 사용하는 경우 SMS를 발송한다.
			if ("1".equals(branchInfo.get("SMS_AUTH_YN"))) {		/*"0".equals(type) && */
				final Map<String, Object> sendResult = loginService.sendSmsProc(result);
				return sendResult;
			}
			else {
				token = result.get("token").toString();
				Cookie cookie = cookieUtil.createCookie(Constants.JwtConstants.ACCESS_TOKEN, token);
				response.addCookie(cookie);
				response.setHeader(HttpHeaders.AUTHORIZATION, Constants.JwtConstants.BEARER + token);
				result.put(Constants.JwtConstants.ACCESS_TOKEN, token);
				
				params.put("token", token);
				params.put("inOutGb", "1");
				loginService.insertLoginConnHistory(request, params);		//로그인 이력 남기기
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
				
				HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getRequest();
				params.put("token", token);
				params.put("inOutGb", "1");
				loginService.insertLoginConnHistory(request, params);		//로그인 이력 남기기
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
	 * @throws SQLException 
	 */
	@GetMapping(path = "/user/logoutProc")
	public void logoutProc(HttpServletRequest request
						 , HttpServletResponse response) throws IOException {
		try {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("connChannel", request.getParameter("connChannel"));
			params.put("token", request.getParameter("token"));
			params.put("inOutGb", "2");
			loginService.insertLoginConnHistory(request, params);		//로그아웃 이력 남기기
			
			response.addCookie(cookieUtil.deleteCookie(request, Constants.JwtConstants.ACCESS_TOKEN));
			response.sendRedirect("/home");
			
		} catch (RuntimeException | SQLException re) {
			log.error("LoginController.logoutProc : {} ", re);
		}
	}

}
