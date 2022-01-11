package com.ishift.auction.web;


import com.ishift.auction.configuration.security.token.AdminUserAuthenticationToken;
import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.CookieUtil;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.vo.AdminUserDetails;
import com.ishift.auction.vo.JwtTokenVo;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Generated ishift
 * @Date: 2021-06-28 09:17:05.673
 * @Description:
 **/
@Slf4j
@RestController
public class AdminController {

	@Resource(name = "adminService")
	private AdminService adminService;
	
	@Autowired
	private AuctionService auctionService;

	@Autowired
	private SessionUtill sessionUtill;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private CookieUtil cookieUtil;

	/**
	 * request path가 admin인 경우 관리자 main으로 redirect
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/office")
	public ModelAndView init() throws Exception {
		final ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/office/main");
		return mav;
	}
	
	/**
	 * 관리자 > 메인메뉴
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/office/main")
	public ModelAndView adminMain() {
		final ModelAndView mav = new ModelAndView("admin/main/main");
		
		try {
			final Map<String,Object> map = new HashMap<>();
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			if(userVo != null) map.put("naBzPlcNo", userVo.getPlace());
			mav.addObject("johapData", adminService.selectOneJohap(map));
		}catch (RuntimeException re) {
			log.error("AdminController.adminMain : {} ",re);
		} catch (SQLException se) {
			log.error("AdminController.adminMain : {} ",se);
		}
		mav.addObject("subheaderTitle", "메인");
		return mav;
	}
	
	/**
	 * 관리자 > 경매실시간방송 송출 > 라이브 송출 (Kakao i Conenct)
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/office/broad/cast")
	public ModelAndView adminBroadCast() {
		final ModelAndView mav = new ModelAndView("admin/broad/cast");
		try {
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();

			final Map<String, Object> params = new HashMap<>();
			if(userVo != null) params.put("naBzPlcNo", userVo.getPlace());

			final Map<String, Object> bizInfo = adminService.selectOneJohap(params);
			mav.addObject("johapData", bizInfo);
			mav.addObject("naBzplc", bizInfo.get("NA_BZPLC"));
		}catch (RuntimeException re) {
			log.error("AdminController.adminBroadCast : {} ",re);
		} catch (SQLException se) {
			log.error("AdminController.adminBroadCast : {} ",se);
		}
		mav.addObject("subheaderTitle", "영상송출");
		return mav;
	}

	/**
	 * 관리자 > 응찰모니터링
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/office/auction/monster")
	public ModelAndView adminGhost(@RequestParam(name = "place", required = false) final String place) {
		
		final ModelAndView mav = new ModelAndView("admin/auction/monster/monster");
		
		try {
			final Map<String,Object> map = new HashMap<>();
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			map.put("delYn", "0");
			if(userVo != null) {
				map.put("naBzPlcNo", userVo.getPlace());
				map.put("naBzplc", userVo.getNaBzplc());
			}
			Map<String,Object> johapData = adminService.selectOneJohap(map);
			mav.addObject("johapData", johapData);

			final LocalDateTime date = LocalDateTime.now();
			final String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			map.put("searchDate", today);
			if(userVo != null) map.put("naBzPlcNo", userVo.getPlace());

			final Map<String,Object> count =auctionService.selectCountEntry(map);
			String usrid = userVo.getUsrid();
			mav.addObject("auctCount",count);
			mav.addObject("bidderCnt", adminService.selectBidderCnt(map));
			mav.addObject("userId", usrid);
			mav.addObject("today",date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
			String naBzPlc = userVo.getNaBzplc();
			final String tokenNm = "monster_"+(new Date()).getTime();
			final JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
													.auctionHouseCode(naBzPlc)
													.userMemNum(tokenNm)
													.userRole(Constants.UserRole.WATCHER)
													.build();
			final String token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);

			mav.addObject("tokenNm", tokenNm);
			mav.addObject("token", token);
		}catch (RuntimeException re) {
			log.error("AdminController.adminGhost : {} ",re);
		} catch (SQLException se) {
			log.error("AdminController.adminGhost : {} ",se);
		}
		mav.addObject("subheaderTitle", "모니터링");
		return mav;
	}
	
	/**
	 * 관리자 > 멀티전광판
	 * @return
	 */
	@GetMapping("/office/auction/board")
	public ModelAndView adminBoard(final HttpServletResponse response
								 , @RequestParam final Map<String,Object> params) {

		final ModelAndView mav = new ModelAndView();

		try {
			boolean loginChk = false;
			if(params.get("usrid") != null && params.get("pw") != null) {
				loginChk = this.adminUserLoginProc(response, params);				
			}else if(params.get("ea") != null && params.get("eb") != null){
				String decUsrId = new String(Base64.getDecoder().decode((String)params.getOrDefault("ea","")));
				String decPw = new String(Base64.getDecoder().decode((String)params.getOrDefault("eb","")));
				params.put("usrid",decUsrId);
				params.put("pw",decPw);
				loginChk = this.adminUserLoginProc(response, params);				
			}
			
			
			
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();

			if(userVo != null || loginChk) {
				Map<String,Object> map = new HashMap<>();
				map.put("delYn", "0");
				if(userVo.getPlace() != null) map.put("naBzPlcNo", userVo.getPlace());
				String usrid = userVo.getUsrid();
				Map<String,Object> johap = adminService.selectOneJohap(map);
				String aucGubun = (String) johap.getOrDefault("AUC_DSC","");
				
				if("1".equals(aucGubun)) {
					mav.setViewName("admin/auction/board/singleBoard");					
				}else if("2".equals(aucGubun)) {
			        LocalDateTime date = LocalDateTime.now();
			        Map<String,Object> temp = new HashMap<String,Object>();
			        String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			        temp.put("searchDate", today);
			        temp.put("naBzPlcNo", johap.get("NA_BZPLCNO"));
			        
					List<Map<String,Object>> list=auctionService.entrySelectList(temp);
					Map<String,Object> count =auctionService.selectCountEntry(temp);
					mav.addObject("cowTotCnt", count);
					mav.addObject("list", list);
					mav.setViewName("admin/auction/board/groupBoard");					
				}else {
					mav.setViewName("redirect:/office/main");			
				}
				mav.addObject("userId", usrid);
				mav.addObject("johapData", johap);
				
			}
			else {
				mav.setViewName("redirect:/office/main");
				return mav;
			}
		}catch (RuntimeException re) {
			log.error("AdminController.adminBoard : {} ",re);
			mav.setViewName("redirect:/office/main");
		} catch (SQLException se) {
			log.error("AdminController.adminBoard : {} ",se);
			mav.setViewName("redirect:/office/main");
		}
		mav.addObject("subheaderTitle", "멀티비전");
		return mav;
	}
	@RequestMapping(value = "/office/getCowList" ,method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getCowList(@RequestParam Map<String, Object> params) {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		try {
	        LocalDateTime date = LocalDateTime.now();
	        Map<String,Object> temp = new HashMap<String,Object>();
	        String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	        temp.put("searchDate", today);
	        temp.put("stAucNo", params.get("stAucNo"));
	        temp.put("edAucNo", params.get("edAucNo"));
	        temp.put("naBzplc", params.get("naBzplc"));
	        temp.put("searchAucObjDsc", params.get("aucObjDsc"));
	        
			List<Map<String,Object>> list=auctionService.entrySelectList(temp);
			
			result.put("success", true);
			result.put("message", "조회에 성공했습니다.");
			result.put("list", list);
		}catch (SQLException | RuntimeException re) {
			log.error("AdminController.getCowList : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		return result;
	}

	@ResponseBody
	@RequestMapping(value = "/office/getAbsentCowList" ,method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getAbsentCowList(@RequestParam Map<String, Object> params) {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		try {
			params.put("absentYn", "Y");
			List<Map<String, Object>> list = auctionService.selectCowList(params);
			if (list != null) {
				for (final Map<String, Object> vo : list) {
					sb.append(this.getStringValue(vo.get("AUC_PRG_SQ")).replace("|", ",")).append('|');	// 계류대 번호
				}
			}
			
			result.put("success", true);
			result.put("message", "조회에 성공했습니다.");
			result.put("entry", sb.toString());
		}catch (SQLException | RuntimeException re) {
			log.error("ApiController.selectAbsentCowList : {} ",re);
			result.put("success", false);
			result.put("entry", sb.toString());
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/office/report/list")
	public ModelAndView reportList() throws Exception{
		final ModelAndView mav = new ModelAndView();
		final Map<String,Object> map = new HashMap<>();
		map.put("delYn", "0");

		final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
		if(userVo != null) map.put("naBzPlcNo", userVo.getPlace());

		mav.addObject("johapData", adminService.selectOneJohap(map));
		mav.addObject("totalCnt", adminService.selectVisitTotalCnt(map));
		mav.setViewName("admin/auction/visit/visit");
		mav.addObject("subheaderTitle", "접속현황");
		return mav;
	}
	
	@RequestMapping(value = "/office/getReportList" ,method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getReportList(@RequestParam Map<String,Object> param) throws Exception{
		final Map<String,Object> map = new HashMap<>();
		final Map<String,Object> result = new HashMap<>();

		try {
			final String flag = (String) param.get("searchFlag");
			if(null == param.get("searchDate") || "".equals(param.get("searchDate"))) {
				final LocalDateTime date = LocalDateTime.now();
				final String today = date.format(DateTimeFormatter.ofPattern("yyyyMM"));
				param.put("searchDate",today);
			}
			map.put("delYn", "0");
			map.put("pageSize", param.get("length"));
			map.put("searchDate", param.get("searchDate"));
			if("true".equals(flag)) {
				map.put("pagePerSize", 0);
			}
			else {
				map.put("pagePerSize", param.get("start"));
			}
			
			int totCnt = adminService.selectVisitTotalCnt(map);
			result.put("data", adminService.selectListVisit(map));
			result.put("draw", param.get("draw"));
			result.put("recordsTotal",totCnt) ;
			result.put("recordsFiltered", totCnt);
			result.put("inputParam", map);
			result.put("success", true);
		}catch (RuntimeException re) {
			result.put("success", false);
			log.error("AdminController.adminBoard : {} ",re);
		} catch (SQLException se) {
			result.put("success", false);
			log.error("AdminController.adminBoard : {} ",se);
		}
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/office/auction/stream" ,method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView streamPage() throws Exception{
		final ModelAndView mav = new ModelAndView();
		final Map<String,Object> map = new HashMap<>();
		map.put("delYn", "0");
		final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
        String today = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        
		if(userVo != null) map.put("naBzPlcNo", userVo.getPlace());
		
		Map<String,Object> johap  = adminService.selectOneJohap(map);
        JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
				.auctionHouseCode(johap.get("NA_BZPLC").toString())
				.userMemNum("WATCHER")
				.userRole(Constants.UserRole.WATCHER)
				.build();
        String token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);

		map.put("searchDate", today);
		Map<String,Object> count =auctionService.selectCountEntry(map);

		mav.addObject("johapData", johap);
        mav.addObject("token",token);
        mav.addObject("count",count);
		mav.setViewName("admin/auction/stream/stream");
		mav.addObject("subheaderTitle", "방송");
		return mav;
	}
	
	private boolean adminUserLoginProc(final HttpServletResponse response
									 , final Map<String,Object> params) {

		final boolean result = true;
		String token = "";

		try {
			final Authentication authentication = authenticationManager.authenticate(
															new AdminUserAuthenticationToken(
																	params.getOrDefault("usrid", "").toString()
																	, params.getOrDefault("pw", "").toString()
																	, null));
			
			SecurityContextHolder.getContext().setAuthentication(authentication);

			AdminUserDetails adminUserDetails = (AdminUserDetails)authentication.getPrincipal();
			String usrid = adminUserDetails.getUsrid();
			String naBzPlc = adminUserDetails.getNaBzplc();
			if (adminUserDetails != null) {
				final JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
														.userMemNum(usrid)
														.auctionHouseCode(naBzPlc)
														.userRole(Constants.UserRole.ADMIN)
														.build();
				token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
				final Cookie cookie = cookieUtil.createCookie(Constants.JwtConstants.ACCESS_TOKEN, token);
				response.addCookie(cookie);
			}
		}catch (RuntimeException re) {
			log.error("AdminController.adminUserLoginProc : {} ",re);
			return false;
		}
		return result;
	}
	private String getStringValue(Object value) {
		return value == null ? "" : value.toString();
	}
	
	@ResponseBody
	@RequestMapping(value = "/office/monster/getBidderCnt" ,method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getBidderCnt(@RequestParam Map<String,Object> param) throws Exception{
		final Map<String,Object> map = new HashMap<>();
		final Map<String,Object> result = new HashMap<>();
		try {
			if(null == param.get("searchDate") || "".equals(param.get("searchDate"))) {
				final LocalDateTime date = LocalDateTime.now();
				final String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
				param.put("searchDate",today);
			}
						
			result.put("data", adminService.selectBidderCnt(param));
			result.put("success", true);
		}catch (RuntimeException re) {
			result.put("success", false);
			log.error("AdminController.adminBoard : {} ",re);
		} catch (SQLException se) {
			result.put("success", false);
			log.error("AdminController.adminBoard : {} ",se);
		}
		return result;
	}
	@RequestMapping(value = "/office/getStnInfo" ,method = { RequestMethod.GET, RequestMethod.POST })
	public Map<String, Object> getStnInfo(@RequestParam Map<String, Object> params) {
		
		final Map<String, Object> result = new HashMap<String, Object>();
		StringBuffer sb = new StringBuffer();
		try {
	        LocalDateTime date = LocalDateTime.now();
	        Map<String,Object> temp = new HashMap<String,Object>();
	        String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
	        temp.put("searchDate", today);
	        temp.put("naBzplc", params.get("naBzplc"));
	        temp.put("selStsDsc", "21");
	        
			Map<String,Object> info=auctionService.getStnInfo(temp);
			
			result.put("success", true);
			result.put("message", "조회에 성공했습니다.");
			result.put("info", info);
		}catch (SQLException | RuntimeException re) {
			log.error("AdminController.getStnInfo : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
		}
		return result;
	}
}
