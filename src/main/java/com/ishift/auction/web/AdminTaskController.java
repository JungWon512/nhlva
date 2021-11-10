package com.ishift.auction.web;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
//	@GetMapping(value = "/admin/task/main")
	@RequestMapping(value = "/admin/task/main", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView main() {
		final ModelAndView mav = new ModelAndView("admin/task/main");

		try {
			final Map<String,Object> map = new HashMap<>();
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			if(userVo.getNaBzplc() != null) map.put("naBzPlc", userVo.getNaBzplc());
			map.put("entryType", "B");
			if(userVo.getPlace() != null) map.put("naBzPlcNo", userVo.getPlace());

			final List<Map<String,Object>> dateList = auctionService.selectAucDateList(map);
			mav.addObject("dateList", dateList);
			mav.addObject("johapData", adminService.selectOneJohap(map));
		}catch (RuntimeException re) {
			log.error("AdminTaskController.main : {} ",re);
		} catch (SQLException se) {
			log.error("AdminTaskController.main : {} ",se);
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
			if(userVo.getPlace() != null) map.put("naBzPlcNo", userVo.getPlace());
			mav.addObject("johapData", adminService.selectOneJohap(map));
		}catch (RuntimeException re) {
			log.error("AdminTaskController.select : {} ",re);
		} catch (SQLException se) {
			log.error("AdminTaskController.select : {} ",se);
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
	@SuppressWarnings("serial")
	public ModelAndView entry(@RequestParam final Map<String, Object> params) {
		final ModelAndView mav = new ModelAndView("admin/task/entry");
		final Map<String, String> titleMap = new HashMap<String, String>() {{put("W", "중량 등록");put("L", "하한가 등록");put("N", "계류대 변경");}};
		try {			
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			if(userVo.getPlace() != null) params.put("naBzPlcNo", userVo.getPlace());
			mav.addObject("johapData", adminService.selectOneJohap(params));
			
			if(userVo.getNaBzplc() != null) params.put("naBzplc", userVo.getNaBzplc());
			if(params.get("aucDt") != null) params.put("searchDate", params.get("aucDt"));
			if(params.get("aucObjDsc") != null) params.put("searchAucObjDsc", params.get("aucObjDsc"));
			mav.addObject("entryList", auctionService.entrySelectList(params));
		}catch (RuntimeException re) {
			log.error("AdminTaskController.entry : {} ",re);
		} catch (SQLException se) {
			log.error("AdminTaskController.entry : {} ",se);
		}
		mav.addObject("params", params);
		mav.addObject("subheaderTitle", titleMap.get(params.getOrDefault("regType", "")));
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
			if(userVo.getNaBzplc() != null) params.put("naBzplc", userVo.getNaBzplc());
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
		}catch (RuntimeException re) {
			log.error("AdminTaskController.selectCowInfo : {} ",re);
			result.put("success", false);
			result.put("message", re.getMessage());
			return result;
		} catch (SQLException se) {
			log.error("AdminTaskController.selectCowInfo : {} ",se);
			result.put("success", false);
			result.put("message", se.getMessage());
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
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			if(userVo.getEno() != null) params.put("regUserId", userVo.getEno());
			
			final int cnt = auctionService.updateCowInfo(params);
			if (cnt > 0) {
				result.put("success", true);
				result.put("message", "수정되었습니다.");
			}
			else {
				result.put("success", false);
				result.put("message", "출장우 정보가 없습니다.");
			}
		}catch (RuntimeException re) {
			log.error("AdminTaskController.saveCowInfo : {} ",re);
			result.put("success", false);
			result.put("message", re.getMessage());
			return result;
		} catch (SQLException se) {
			log.error("AdminTaskController.saveCowInfo : {} ",se);
			result.put("success", false);
			result.put("message", se.getMessage());
			return result;
		}
		return result;
	}
	
}
