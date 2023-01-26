package com.ishift.auction.web;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.admin.task.AdminTaskService;
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
public class AdminTaskController extends CommonController {
	
	@Autowired
	private AuctionService auctionService;
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private AdminTaskService adminTaskService;
	
	@Autowired
	private SessionUtill sessionUtill;

	/**
	 * 관리자 > 경매업무 > 메인화면
	 * @return
	 */
//	@GetMapping(value = "/office/task/main")
	@RequestMapping(value = "/office/task/main", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView main() {
		final ModelAndView mav = new ModelAndView("admin/task/main");

		try {
			final Map<String,Object> map = new HashMap<>();
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			if(userVo != null) map.put("naBzPlc", userVo.getNaBzplc());
			map.put("entryType", "B");
			if(userVo != null) map.put("naBzPlcNo", userVo.getPlace());

			final List<Map<String,Object>> dateList = adminTaskService.selectAucDtList(map);
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
//	@PostMapping("/office/task/select")
	@RequestMapping(value = "/office/task/select", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView select(@RequestParam final Map<String, Object> params) {
		final ModelAndView mav = new ModelAndView("admin/task/select");
		try {
			final Map<String,Object> map = new HashMap<>();
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			if(userVo != null) map.put("naBzPlcNo", userVo.getPlace());
			mav.addObject("johapData", adminService.selectOneJohap(map));
		}catch (RuntimeException re) {
			log.error("AdminTaskController.select : {} ",re);
			return makeMessageResult(mav, params, "/office/task/main", "", "경매 서비스 준비중입니다.", "pageMove('/office/task/main');");
		} catch (SQLException se) {
			log.error("AdminTaskController.select : {} ",se);
			return makeMessageResult(mav, params, "/office/task/main", "", "경매 서비스 준비중입니다.", "pageMove('/office/task/main');");
		}
		mav.addObject("params", params);
		mav.addObject("subheaderTitle", "작업선택");
		return mav;
	}
	
	@RequestMapping(value = "/office/task/menu",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView menu(@RequestParam final Map<String, Object> params) {
		final ModelAndView mav = new ModelAndView("admin/task/menu");
		try {
			final Map<String,Object> map = new HashMap<>();
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			if(userVo != null) map.put("naBzPlcNo", userVo.getPlace());
			mav.addObject("johapData", adminService.selectOneJohap(map));
		}catch (RuntimeException re) {
			log.error("AdminTaskController.select : {} ",re);
			return makeMessageResult(mav, params, "/office/task/main", "", "경매 서비스 준비중입니다.", "pageMove('/office/task/main');");
		} catch (SQLException se) {
			log.error("AdminTaskController.select : {} ",se);
			return makeMessageResult(mav, params, "/office/task/main", "", "경매 서비스 준비중입니다.", "pageMove('/office/task/main');");
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
	@SuppressWarnings("serial")
//	@PostMapping("/office/task/entry")
	@RequestMapping(value = "/office/task/entry", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView entry(@RequestParam final Map<String, Object> params) {
		final ModelAndView mav = new ModelAndView("admin/task/entry");
		final Map<String, String> titleMap = new HashMap<String, String>() {
			{
					put("LW", "중량/예정가 등록");
					put("CM", "출장우 관리");
					put("N", "계류대 변경");
					put("AW", "중량 일괄 등록");
					put("AL", "예정가 일괄 등록");
					put("AWL", "일괄 등록");
					put("SB", "낙찰결과 조회");
					put("SCOW", "출장우 리스트");
					put("SMCOW", "미감정 임신우");
			}
		};
		try {			
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			if(userVo != null) params.put("naBzPlcNo", userVo.getPlace());
			mav.addObject("johapData", adminService.selectOneJohap(params));
			
			if(userVo != null) params.put("naBzplc", userVo.getNaBzplc());
			if(params.get("aucDt") != null) params.put("searchDate", params.get("aucDt"));
			if(params.get("aucObjDsc") != null) params.put("searchAucObjDsc", params.get("aucObjDsc"));
			if(params.get("regType") != null && "SB".equals(params.get("regType"))) {
				params.put("searchSelStsDsc", "22");
			}
			if(params.get("regType") != null && "SMCOW".equals(params.get("regType"))) {
				params.put("mCowYn", "Y");
			}
			
			mav.addObject("entryList", auctionService.entrySelectList(params));
		}catch (RuntimeException re) {
			log.error("AdminTaskController.entry : {} ",re);
			return makeMessageResult(mav, params, "/office/task/main", "", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.", "pageMove('/office/task/main');");
		} catch (SQLException se) {
			log.error("AdminTaskController.entry : {} ",se);
			return makeMessageResult(mav, params, "/office/task/main", "", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.", "pageMove('/office/task/main');");
		}
		mav.addObject("params", params);
		mav.addObject("subheaderTitle", titleMap.get(params.getOrDefault("regType", "")));
		return mav;
	}
	
	@ResponseBody
	@PostMapping(value = "/office/task/entryInfo"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> entryInfo(@RequestBody final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();
		try {
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			if(userVo != null) params.put("naBzPlcNo", userVo.getPlace());
			result.put("johapData", adminService.selectOneJohap(params));
			
			if(userVo != null) params.put("naBzplc", userVo.getNaBzplc());

			List<Map<String, Object>> entryList = auctionService.entrySelectList(params);
			
			result.put("entryList", entryList);
		}
		catch (RuntimeException | SQLException re) {
			log.error("AdminTaskController.entry_ajax : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		result.put("params", params);
		return result;
	}
	
	/**
	 * 중량, 최저가, 계류대 수정을 위한 출장우 정보
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/office/task/cowInfo"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> selectCowInfo(@RequestBody final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();
		try {
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			if(userVo != null) params.put("naBzplc", userVo.getNaBzplc());

			final Map<String, Object> cowInfo = auctionService.selectCowInfo(params);

			if (cowInfo == null) {
				result.put("success", false);
				result.put("message", "출장우 정보가 없습니다.");
			}
			else {
				result.put("success", true);
				result.put("message", "조회에 성공했습니다.");
				result.put("cowInfo", cowInfo);
				
				result.put("ppgcowList", this.getCommonCode("PPGCOW_FEE_DSC", "1"));
				result.put("indvList", this.getCommonCode("INDV_SEX_C", "1"));
				result.put("vetList", auctionService.selectVetList(params));	// 수의사 리스트
			}
		}
		catch (RuntimeException | SQLException re) {
			log.error("AdminTaskController.selectCowInfo : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
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
	@PostMapping(value = "/office/task/saveCowInfo"
				, consumes = MediaType.APPLICATION_JSON_VALUE
				, produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> saveCowInfo(@RequestBody final Map<String, Object> params) {
		final Map<String, Object> result = new HashMap<String, Object>();
		try {
			final AdminUserDetails userVo = (AdminUserDetails)sessionUtill.getUserVo();
			if(userVo != null) params.put("regUserId", userVo.getEno());
			if(userVo != null) params.put("naBzplc", userVo.getNaBzplc());
			if(params.get("aucDt") != null) params.put("searchDate", params.get("aucDt"));
			
			
			//계류대 번호 변경
			String regType = (String)params.getOrDefault("regType", "");
			if("N".equals(regType)) {
				auctionService.updateCowInfoForModlNo(params);
			}

			//참가번호 변경
			String selStsDsc = (String)params.getOrDefault("selStsDsc", "");
			if("S".equals(regType) && "22".equals(selStsDsc)) {
				Map<String,Object> temp = new HashMap<>();
				temp.putAll(params);
				temp.put("aucObjDsc", params.get("qcnAucObjDsc"));
				Map<String,Object> mwmn =  auctionService.selectAuctMwmn(temp);
				if(mwmn == null || mwmn.isEmpty()) {
					result.put("success", false);
					result.put("message", "입력한 참가번호가 없습니다.");
					return result;
				}
				params.put("trmnAmnno", mwmn.get("TRMN_AMNNO"));
			}
			
			//정보 변경
			final int cnt = auctionService.updateCowInfo(params);
			if (cnt > 0) {
				result.put("success", true);
				result.put("message", "수정되었습니다.");
				result.put("params", params);
				if (!"I".equals(params.get("regType")) && !"S".equals(params.get("regType"))) {
					result.put("entryList", auctionService.entrySelectList(params));
				}
			}
			else {
				result.put("success", false);
				result.put("message", "출장우 정보가 없습니다.");
			}
		}catch (SQLException | RuntimeException re) {
			log.error("AdminTaskController.saveCowInfo : {} ",re);
			result.put("success", false);
			result.put("message", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	/**
	 * 관리자 > 출장우 조회
	 * @param params
	 * @return
	 */
	@RequestMapping(value = "/office/task/cowList", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView cowList(@RequestParam final Map<String, Object> params) {
		final ModelAndView mav = new ModelAndView("admin/task/cowList");
		try {			
			if(params.get("aucDt") != null) params.put("searchDate", params.get("aucDt"));
			if(params.get("aucObjDsc") != null) params.put("searchAucObjDsc", params.get("aucObjDsc"));
			
			mav.addObject("entryList", auctionService.entrySelectList(params));
		}
		catch (RuntimeException re) {
			log.error("AdminTaskController.entry : {} ",re);
			return makeMessageResult(mav, params, "/office/task/main", "", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.", "pageMove('/office/task/main');");
		}
		catch (SQLException se) {
			log.error("AdminTaskController.entry : {} ",se);
			return makeMessageResult(mav, params, "/office/task/main", "", "작업중 오류가 발생했습니다. 관리자에게 문의하세요.", "pageMove('/office/task/main');");
		}
		mav.addObject("params", params);
		mav.addObject("subheaderTitle", "출장우 조회");
		return mav;
	}

	/* ---------------------------------------------------------- 출장우 간편 등록 [s] ---------------------------------------------------------- */
	
	/**
	 * 관리자 > 출장우 조회 > 개체 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@PostMapping(value = "/office/task/searchIndv")
	public ModelAndView searchIndv(@RequestParam final Map<String, Object> params) throws SQLException {
		final ModelAndView mav = new ModelAndView("admin/task/searchIndv");
		mav.addObject("aucObjDscList", this.getCommonCode("AUC_OBJ_DSC", "1"));
		mav.addObject("dateList", adminTaskService.selectAucDtList(params));
		mav.addObject("params", params);
		mav.addObject("subheaderTitle", "개체 조회");
		return mav;
	}
	
	/**
	 * 관리자 > 출장우 조회 > 개체 조회 > 개체 정보 조회(mca i/f)
	 * @param params
	 * @return
	 */
	@ResponseBody
	@PostMapping(value = "/office/task/searchIndvAjax")
	public Map<String, Object> searchIndvAjax(@RequestBody final Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = adminTaskService.searchIndvAmnno(params);
		}
		catch(SQLException se) {
			log.error("SQLException::AdminTaskController.searchIndvAjax : {} ", se);
			result.put("success", false);
			result.put("message", "개체 정보 조회중 오류가 발생했습니다.<br/>관리자에게 문의하세요.");
			return result;
		}
		catch (Exception e) {
			log.error("Exception::AdminTaskController.searchIndvAjax : {} ", e);
			result.put("success", false);
			result.put("message", "개체 정보 조회중 오류가 발생했습니다.<br/>관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	/**
	 * 관리자 > 출장우 조회 > 개체 조회 > 경매 차수 등록여부 확인
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@ResponseBody
	@PostMapping(value = "/office/task/checkAucDt")
	public Map<String, Object> checkAucDt(@RequestBody final Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = adminTaskService.checkAucDt(params);
		}
		catch(SQLException se) {
			log.error("SQLException::AdminTaskController.checkAucDt : {} ", se);
			result.put("success", false);
			result.put("message", "경매차수 조회중 오류가 발생했습니다.<br/>관리자에게 문의하세요.");
			return result;
		}
		catch (Exception e) {
			log.error("Exception::AdminTaskController.checkAucDt : {} ", e);
			result.put("success", false);
			result.put("message", "경매차수 조회중 오류가 발생했습니다.<br/>관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	/**
	 * 관리자 > 출장우 조회 > 출장우 간편 등록
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@PostMapping(value = "/office/task/registCow")
	public ModelAndView registCow(@RequestParam final Map<String, Object> params) throws SQLException {
		final ModelAndView mav = new ModelAndView("admin/task/registCow");
		
		mav.addObject("aucObjDscList", this.getCommonCode("AUC_OBJ_DSC", "1"));				// 경매대상 구분코드
		mav.addObject("indvSexCList", this.getCommonCode("INDV_SEX_C", ""));				// 개체성별 코드
		mav.addObject("rgDscList", this.getCommonCode("SRA_INDV_BRDSRA_RG_DSC", ""));		// 등록구분 코드
		mav.addObject("ppgcowFeeDscList", this.getCommonCode("PPGCOW_FEE_DSC", ""));		// 번식우수수료 구분코드
		mav.addObject("sogCowInfo", adminTaskService.selectSogCowInfo(params));
		mav.addObject("qcnInfo", adminTaskService.selectQcnInfo(params));
		mav.addObject("params", params);
		mav.addObject("subheaderTitle", "출장우 간편 등록");
		return mav;
	}
	
	/**
	 * 관리자 > 출장우 조회 > 출장우 간편 등록 > 출장우 정보 저장
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@ResponseBody
	@PostMapping(value = "/office/task/registCowAjax")
	public Map<String, Object> registCowAjax(@RequestBody final Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = adminTaskService.registSogCow(params);
		}
		catch(SQLException se) {
			log.error("SQLException::AdminTaskController.registCowAjax : {} ", se);
			result.put("success", false);
			result.put("message", "저장중 오류가 발생했습니다.<br/>관리자에게 문의하세요.");
			return result;
		}
		catch (Exception e) {
			log.error("Exception::AdminTaskController.registCowAjax : {} ", e);
			result.put("success", false);
			result.put("message", "저장중 오류가 발생했습니다.<br/>관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	/* ---------------------------------------------------------- 출장우 간편 등록 [e] ---------------------------------------------------------- */
	
	/* ---------------------------------------------------------- 경매내역 변경 [s] ---------------------------------------------------------- */
	
	/**
	 * 관리자 > 출장우 조회 > 출장우 간편 등록 > 경매내역 변경을 위한 출장우/출장우 로그 조회
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@ResponseBody
	@PostMapping(value = "/office/task/searchSogCowAjax")
	public Map<String, Object> searchSogCowAjax(@RequestBody final Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = adminTaskService.searchSogCow(params);
		}
		catch(SQLException se) {
			log.error("SQLException::AdminTaskController.searchSogCowAjax : {} ", se);
			result.put("success", false);
			result.put("message", "저장중 오류가 발생했습니다.<br/>관리자에게 문의하세요.");
			return result;
		}
		catch (Exception e) {
			log.error("Exception::AdminTaskController.searchSogCowAjax : {} ", e);
			result.put("success", false);
			result.put("message", "저장중 오류가 발생했습니다.<br/>관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	/**
	 * 관리자 > 출장우 조회 > 출장우 간편 등록 > 변경된 경매내역 저장
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@ResponseBody
	@PostMapping(value = "/office/task/updateResultAjax")
	public Map<String, Object> updateResultAjax(@RequestBody final Map<String, Object> params) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = adminTaskService.updateResult(params);
		}
		catch(SQLException se) {
			log.error("SQLException::AdminTaskController.updateResultAjax : {} ", se);
			result.put("success", false);
			result.put("message", "저장중 오류가 발생했습니다.<br/>관리자에게 문의하세요.");
			return result;
		}
		catch (Exception e) {
			log.error("Exception::AdminTaskController.updateResultAjax : {} ", e);
			result.put("success", false);
			result.put("message", "저장중 오류가 발생했습니다.<br/>관리자에게 문의하세요.");
			return result;
		}
		return result;
	}
	
	
	/* ---------------------------------------------------------- 경매내역 변경 [e] ---------------------------------------------------------- */
	
	/* ---------------------------------------------------------- 출장우 이미지 업로드 [s] ---------------------------------------------------------- */
	/**
	 * 관리자 > 경매업무 > 출장우 간편 등록 페이지
	 * @param params
	 * @return
	 * @throws SQLException 
	 */
	@RequestMapping(value = "/office/task/uploadImage", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView uploadImage(@RequestParam final Map<String, Object> params) throws SQLException {
		final ModelAndView mav = new ModelAndView("admin/task/uploadImage");
		mav.addObject("params", params);
		mav.addObject("sogCowInfo", adminTaskService.selectSogCowInfo(params));
		mav.addObject("imgList", adminTaskService.selectCowImg(params));
		mav.addObject("subheaderTitle", "이미지 등록");
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value = "/office/task/uploadImageAjax", method = {RequestMethod.POST})
	public Map<String, Object> uploadImageAjax(@RequestBody final Map<String, Object> params) throws SQLException, KeyManagementException, NoSuchAlgorithmException {
		return adminTaskService.uploadImageProc(params);
	}
	/* ---------------------------------------------------------- 출장우 이미지 업로드 [e] ---------------------------------------------------------- */

}
