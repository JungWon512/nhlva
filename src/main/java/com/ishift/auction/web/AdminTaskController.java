package com.ishift.auction.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.vo.AdminUserDetails;

import lombok.extern.slf4j.Slf4j;

/**
 * @author iShift
 * 경매 업무 처리를 위한 컨트롤러
 */
@Slf4j
@Controller
public class AdminTaskController {
	
	@Autowired
	private AuctionService auctionService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private SessionUtill sessionUtill;

	/**
	 * 관리자 > 경매업무 > 메인화면
	 * @return
	 */
	@GetMapping("/admin/task/main")
	public ModelAndView main() {
		final ModelAndView mav = new ModelAndView("admin/task/main");

		try {
			final Map<String,Object> map = new HashMap<>();
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			map.put("naBzPlc", userVo.getNaBzplc());
			map.put("entryType", "B");
			map.put("naBzPlcNo", userVo.getPlace());

			final List<Map<String,Object>> dateList = auctionService.selectAucDateList(map);
			mav.addObject("dateList", dateList);
			mav.addObject("johapData", adminService.selectOneJohap(map));
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		mav.addObject("subheaderTitle", "경매업무");
		return mav;
	}
	
	/**
	 * 관리자 > 경매업무 > 작업선택
	 * @return
	 */
	@PostMapping("/admin/task/select")
	public ModelAndView select(@RequestParam final Map<String, Object> params) {
		final ModelAndView mav = new ModelAndView("admin/task/select");
		try {
			final Map<String,Object> map = new HashMap<>();
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			map.put("naBzPlcNo", userVo.getPlace());
			mav.addObject("johapData", adminService.selectOneJohap(map));
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		mav.addObject("params", params);
		mav.addObject("subheaderTitle", "작업선택");
		return mav;
	}
	
	/**
	 * 관리자 > 경매업무 > 중량, 최저가, 계류대 입력
	 * @param params
	 * @return
	 */
	@PostMapping("/admin/task/entry")
	public ModelAndView entry(@RequestParam final Map<String, Object> params) {
		final ModelAndView mav = new ModelAndView("admin/task/entry");
		try {
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			params.put("naBzPlcNo", userVo.getPlace());
			mav.addObject("johapData", adminService.selectOneJohap(params));
			
			params.put("naBzplc", userVo.getNaBzplc());
			params.put("searchDate", params.get("aucDt"));
			params.put("searchAucObjDsc", params.get("aucObjDsc"));
			mav.addObject("entryList", auctionService.entrySelectList(params));
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
		mav.addObject("params", params);
		mav.addObject("subheaderTitle", "작업선택");
		return mav;
	}
	
	/**
	 * 중량, 최저가, 계류대 수정을 위한 출장우 정보
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/admin/task/cowInfo"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> selectCowInfo(@RequestBody final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();
		try {
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			params.put("naBzplc", userVo.getNaBzplc());
			final Map<String, Object> cowInfo = auctionService.selectCowInfo(params);
			if (cowInfo == null) {
				result.put("success", false);
				result.put("message", "출장우 정보가 없습니다.");
			}
			else {
				result.put("success", true);
				result.put("message", "조회에 성공했습니다.");
				result.put("cowInfo", cowInfo);
			}
		}
		catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
			return result;
		}
		result.put("params", params);
		return result;
	}
	
	/**
	 * 중량, 최저가, 계류대 정보 저장
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PutMapping(value = "/admin/task/cowInfo"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> saveCowInfo(@RequestBody final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();
		try {

		}
		catch (Exception e) {
			result.put("success", false);
			result.put("message", e.getMessage());
			return result;
		}
		return result;
	}
	
}
