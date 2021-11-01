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

import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
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
	@RequestMapping(value="/admin")
	public ModelAndView init() throws Exception {
		final ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/admin/main");
		return mav;
	}
	
	/**
	 * 관리자 > 메인메뉴
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/main")
	public ModelAndView adminMain() {
		final ModelAndView mav = new ModelAndView("admin/main/main");
		
		try {
			final Map<String,Object> map = new HashMap<>();
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			map.put("naBzPlcNo", userVo.getPlace());
			mav.addObject("johapData", adminService.selectOneJohap(map));
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		mav.addObject("subheaderTitle", "메인");
		return mav;
	}

	/**
	 * 관리자 공지사항
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/admin/auction/aucNotice")
	public ModelAndView aucNotice() throws Exception{
		final ModelAndView mav = new ModelAndView();
		final Map<String,Object> map = new HashMap<>();

		AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();

		map.put("delYn", "0");
		map.put("naBzPlcNo", userVo.getPlace());
		mav.addObject("johapData", adminService.selectOneJohap(map));

		map.put("order", " ORDER BY SEQ_NO DESC");
		mav.addObject("noticeData", adminService.selectListNotice(map));
		mav.addObject("subheaderTitle", "공지사항");
		mav.setViewName("admin/auction/notice/noticeList");
		return mav;
	}

	/**
	 * 관리자 공지사항 리스트
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/admin/auction/aucNotice.get", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "공지사항 listAPI", tags = "aucnotice")
	public Map<String, Object> list(@RequestBody final Map<String,Object> vo) throws Exception {
		final Map<String, Object> result = new HashMap<>();
		try {
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();

			vo.put("delYn", "0");
			vo.put("naBzPlcNo", userVo.getPlace());

			result.put("success", true);
			result.put("data", adminService.selectListNotice(vo));
			result.put("searchCount", adminService.countNotice(vo));
		}
		catch (Exception e){
			result.put("success", false);
			result.put("message", e.getMessage());
			log.error(e.getMessage());
		}
		return result;
	}

	/**
	 * 관리자 공지사항 등록/수정
	 * @param naBzplc
	 * @param seqNo
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/admin/auction/aucNotice/{naBzplc}/{seqNo}")
	public ModelAndView viewPage(@PathVariable final String naBzplc
								, @PathVariable final String seqNo) throws Exception{

		final ModelAndView mav = new ModelAndView();
		
		final Map<String,Object> map = new HashMap<>();
		map.put("naBzplc", naBzplc);
		map.put("seqNo", seqNo);
		map.put("delYn", "0");

		final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();

		final Map<String,Object> temp = new HashMap<>();
		temp.put("naBzPlcNo", userVo.getPlace());
		mav.addObject("johapData", adminService.selectOneJohap(temp));
		mav.addObject("data", adminService.selectOneNotice(map));
		
		final Map<String,Object> vo = new HashMap<>();
		vo.put("naBzplc",naBzplc);
		vo.put("seqNo", Long.parseLong(seqNo));
		
		mav.addObject("paramVO", vo);
		mav.addObject("subheaderTitle","공지사항 등록/수정");
		mav.setViewName("admin/auction/notice/noticeView");
		return mav;
	}
	
	/**
	 * 관리자 공지사항 저장
	 * @param aucNoticeVO
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = "/admin/auction/aucNotice.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "공지사항 insertAPI", tags = "aucnotice")
	public Map<String, Object> insert(@RequestBody final Map<String,Object> aucNoticeVO) throws Exception {
		final Map<String, Object> result = new HashMap<>();
		final Map<String,Object> paramMap = new HashMap<>();

		paramMap.put("johpCd",aucNoticeVO.get("johpCd").toString());
		final Map<String,Object> returnMap = adminService.selectOneMaxVO(paramMap);

		Integer returnNextSeq =0 ;
		if(returnMap ==null) {
			returnNextSeq = 1;
		}
		else {
			returnNextSeq =  Integer.parseInt(returnMap.get("seqNo").toString());
		}
		aucNoticeVO.put("seqNo",returnNextSeq);
		
		try {
			result.put("success", true);
			result.put("data", adminService.insertNotice(aucNoticeVO));
		}
		catch (Exception e){
			result.put("success", false);
			result.put("message", e.getMessage());
			log.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 관리자 공지사항 상세 api
	 * @param johpCd
	 * @param seqNo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/admin/auction/aucNotice/{johpCd}/{seqNo}.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "공지사항 detailAPI", tags = "aucnotice")
	public Map<String, Object> get(@PathVariable final String johpCd
								 , @PathVariable final String seqNo) throws Exception {
		final Map<String, Object> result = new HashMap<>();
		final Map<String, Object> map = new HashMap<>();
		map.put("johpCd", johpCd);
		map.put("seqNo", seqNo);

		try {
			result.put("success", true);
			result.put("data", adminService.selectOneNotice(map));
		}
		catch (Exception e){
			result.put("success", false);
		}
		return result;
	}
	
	/**
	 * 관리자 공지사항 저장
	 * @param johpCd
	 * @param seqNo
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PutMapping(value = "/admin/auction/aucNotice/{johpCd}/{seqNo}.json", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> update(@PathVariable final String johpCd
									, @PathVariable final Long seqNo
									, @RequestBody final Map<String,Object> vo) throws Exception {
		final Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		
		try {
			vo.put("johpCd",johpCd);
			vo.put("seqNo",String.valueOf(seqNo));
			result.put("noticeData", adminService.updateNotice(vo));
		}
		catch (Exception e){
			result.put("success", false);
			result.put("message", e.getMessage());
			log.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 관리자 공지사항 삭제
	 * @param johpCd
	 * @param seqNo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@DeleteMapping(value = "/admin/auction/aucNotice/{johpCd}/{seqNo}.json", produces = MediaType.APPLICATION_JSON_VALUE)
	@ApiOperation(value = "공지사항 deleteAPI", tags = "aucnotice")
	public Map<String, Object> delete(@PathVariable final String johpCd
									, @PathVariable final Long seqNo) throws Exception {
		final Map<String, Object> result = new HashMap<>();

		final Map<String, Object> map = new HashMap<>();
		map.put("johpCd",johpCd);
		map.put("seqNo",seqNo);

		try {
			result.put("success", true);
			result.put("data", adminService.deleteNotice(map));
		}
		catch (Exception e){
			result.put("success", false);
			result.put("message", e.getMessage());
			log.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 관리자 공지사항 카운트
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/admin/auction/aucNotice/count.json", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST})
	@ApiOperation(value = "공지사항 countAPI", tags = "aucnotice")
	public Map<String, Object> count(@RequestBody final Map<String,Object> vo) throws Exception {
		final Map<String, Object> result = new HashMap<>();

		try {
			result.put("success", true);
			result.put("data", adminService.countNotice(vo));
		}
		catch (Exception e){
			result.put("success", false);
			result.put("message", e.getMessage());
			log.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/admin/auction/aucNotice/exist.json", produces = MediaType.APPLICATION_JSON_VALUE, method = {RequestMethod.POST})
	@ApiOperation(value = "공지사항 existAPI", tags = "aucnotice")
	public Map<String, Object> exist(@RequestBody final Map<String,Object> vo) throws Exception {
		final Map<String, Object> result = new HashMap<>();
	
		try {
			result.put("success", true);
			result.put("data", adminService.existNotice(vo));
		}
		catch (Exception e){
			result.put("success", false);
			result.put("message", e.getMessage());
			log.error(e.getMessage());
		}
		return result;
	}
	
	/**
	 * 관리자 > 경매실시간방송 송출 > 라이브 송출 (Kakao i Conenct)
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/broad/cast")
	public ModelAndView adminBroadCast() {
		final ModelAndView mav = new ModelAndView("admin/broad/cast");
		try {
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();

			final Map<String, Object> params = new HashMap<>();
			params.put("naBzPlcNo", userVo.getPlace());

			final Map<String, Object> bizInfo = adminService.selectOneJohap(params);
			mav.addObject("johapData", bizInfo);
			mav.addObject("naBzplc", bizInfo.get("NA_BZPLC"));
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		mav.addObject("subheaderTitle", "영상송출");
		return mav;
	}

	/**
	 * 관리자 > 응찰모니터링
	 * @return
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/admin/auction/monster")
	public ModelAndView adminGhost(@RequestParam(name = "place", required = false) final String place) {
		
		final ModelAndView mav = new ModelAndView("admin/auction/monster/monster");
		
		try {
			final Map<String,Object> map = new HashMap<>();
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			map.put("delYn", "0");
			map.put("naBzPlcNo", userVo.getPlace());
			mav.addObject("johapData", adminService.selectOneJohap(map));

			final LocalDateTime date = LocalDateTime.now();
			final String today = date.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
			map.put("searchDate", today);
			map.put("naBzPlcNo", userVo.getPlace());

			final Map<String,Object> count =auctionService.selectCountEntry(map);
			mav.addObject("auctCount",count);
			mav.addObject("userId", userVo.getUsrid());
			mav.addObject("today",date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

			final String tokenNm = "monster_"+(new Date()).getTime();
			final JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
													.auctionHouseCode(userVo.getNaBzplc())
													.userMemNum(tokenNm)
													.userRole(Constants.UserRole.WATCHER)
													.build();
			final String token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);

			mav.addObject("tokenNm", tokenNm);
			mav.addObject("token", token);
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		mav.addObject("subheaderTitle", "모니터링");
		return mav;
	}
	
	/**
	 * 관리자 > 멀티전광판
	 * @return
	 */
	@GetMapping("/admin/auction/board")
	public ModelAndView adminBoard(final HttpServletResponse response
								 , @RequestParam final Map<String,Object> params) {

		final ModelAndView mav = new ModelAndView();

		try {
			boolean loginChk = this.adminUserLoginProc(response, params);
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();

			if(userVo != null || loginChk) {
				Map<String,Object> map = new HashMap<>();
				map.put("delYn", "0");
				map.put("naBzPlcNo", userVo.getPlace());
				mav.addObject("userId", userVo.getUsrid());
				mav.addObject("johapData", adminService.selectOneJohap(map));
				mav.setViewName("admin/auction/board/board");
			}
			else {
				mav.setViewName("redirect:/admin/main");
				return mav;
			}
		} catch (Exception e) {
			log.error(e.getMessage());
		}
		mav.addObject("subheaderTitle", "멀티비전");
		return mav;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = "/admin/report/list")
	public ModelAndView reportList() throws Exception{
		final ModelAndView mav = new ModelAndView();
		final Map<String,Object> map = new HashMap<>();
		map.put("delYn", "0");

		final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
		map.put("naBzPlcNo", userVo.getPlace());

		mav.addObject("johapData", adminService.selectOneJohap(map));
		mav.addObject("totalCnt", adminService.selectVisitTotalCnt(map));
		mav.setViewName("admin/auction/visit/visit");
		mav.addObject("subheaderTitle", "접속현황");
		return mav;
	}
	
	@RequestMapping(value = "/admin/getReportList")
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
		}
		catch(Exception e){
			result.put("success", false);
		}
		return result;
	}
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/admin/auction/stream")
	public ModelAndView streamPage() throws Exception{
		final ModelAndView mav = new ModelAndView();
		final Map<String,Object> map = new HashMap<>();
		map.put("delYn", "0");

		final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
		map.put("naBzPlcNo", userVo.getPlace());

		mav.addObject("johapData", adminService.selectOneJohap(map));
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

			if (adminUserDetails != null) {
				final JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
														.userMemNum(adminUserDetails.getUsrid())
														.auctionHouseCode(adminUserDetails.getNaBzplc())
														.userRole(Constants.UserRole.ADMIN)
														.build();
				token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
				final Cookie cookie = cookieUtil.createCookie(Constants.JwtConstants.ACCESS_TOKEN, token);
				response.addCookie(cookie);
			}
		}
		catch (Exception e) {
			return false;
		}
		return result;
	}
}
