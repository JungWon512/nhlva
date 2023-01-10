
package com.ishift.auction.web;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.auction.AuctionService;
import com.ishift.auction.service.mypage.MyPageService;
import com.ishift.auction.util.DateUtil;
import com.ishift.auction.util.FormatUtil;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.vo.BidUserDetails;
import com.ishift.auction.vo.FarmUserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class MyPageController {
	private static final Logger LOGGER = LoggerFactory.getLogger(MyPageController.class);

	@Autowired
	AuctionService auctionService;
	
	@Autowired
    private AdminService adminService;
	
	@Autowired
	private MyPageService myPageService;
	
	@Autowired
    private SessionUtill sessionUtill;
	
	@Autowired
	private FormatUtil formatUtil;
	
	@Autowired
	private DateUtil dateUtil;
	
	/**
	 * �굹�쓽 寃쎈ℓ�궡�뿭
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@RequestMapping(value = "/my/buy",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView myresults(@RequestParam Map<String,Object> params) throws Exception {
		ModelAndView mav = new ModelAndView("mypage/buy/buy");
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
		BidUserDetails userVo = (BidUserDetails) sessionUtill.getUserVo();
		params.put("loginNo", userVo.getTrmnAmnno());
		
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("naBzPlcNo", params.get("place").toString());

		Map<String,Object> johap = adminService.selectOneJohap(paramMap);
		paramMap.put("naBzPlc", johap.get("NA_BZPLC"));
		
		List<Map<String,Object>> datelist= auctionService.selectAucDateList(paramMap);
		paramMap.put("searchDate", datelist.size() > 0 ? datelist.get(0).get("AUC_DT") : null);
		paramMap.put("searchTrmnAmnNo", userVo.getTrmnAmnno());
		paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
		paramMap.put("stateFlag", "buy");
		
		// 0. �굹�쓽 寃쎈ℓ�궡�뿭 > 援щℓ�궡�뿭
		mav.addObject("buyCnt",auctionService.selectCountEntry(paramMap));
		mav.addObject("buyList", auctionService.entrySelectList(paramMap));		
		
		// 1. �굹�쓽 寃쎈ℓ�궡�뿭 > �쓳李곕궡�뿭
		mav.addObject("bidCnt", auctionService.selectBidLogListCnt(paramMap));
		mav.addObject("bidList", auctionService.selectBidLogList(paramMap));
		
		// 2. �굹�쓽 寃쎈ℓ�궡�뿭 > �젙�궛�꽌
		paramMap.put("searchDateState", today);
		
		mav.addObject("calendarList", auctionService.selectStateList(paramMap));
		mav.addObject("title",formatUtil.dateAddDotLenSix(today));
		
		//異쒖옣�슦 �긽�꽭 tab�빆紐� �몴湲�
		paramMap.put("simpCGrpSqno", "2");
		mav.addObject("tabList",auctionService.selectListExpitemSet(paramMap));
		
		params.putAll(paramMap);
		
 		mav.addObject("johapData", johap);
		mav.addObject("dateList",datelist);
		mav.addObject("inputParam", params);
		mav.addObject("subheaderTitle","�굹�쓽 寃쎈ℓ�궡�뿭");
 		
		return mav;
	}
	/**
	 * �굹�쓽 寃쎈ℓ�궡�뿭 - �젙�궛�꽌 由ъ뒪�듃 議고쉶
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/auction/api/select/buyList", produces = MediaType.APPLICATION_JSON_VALUE)	
	public Map<String, Object> buyList(@RequestBody final Map<String,Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.putAll(params);
			
			Map<String,Object> johap = adminService.selectOneJohap(paramMap);
			
			// 寃��깋�븷 �궇吏� 怨꾩궛泥섎━
			String yyyyMM = "";
	        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMM");
	     	String searchYm = paramMap.get("searchDateState").toString();
	     	LocalDate date = ( searchYm == null || searchYm.isEmpty()) ? LocalDate.now() : LocalDate.parse(searchYm+"01",DateTimeFormatter.ofPattern("yyyyMMdd"));
	        if ("next".equals(paramMap.get("flag"))) {
		        yyyyMM = date.plusMonths(1).format(format);
	        }
	        else if("prev".equals(paramMap.get("flag"))) {
		        yyyyMM = date.minusMonths(1).format(format);	        	
	        }
	        else {
		        yyyyMM = date.format(format);
	        }
	        
			paramMap.put("stateFlag", "buy");
			paramMap.put("searchMbIntgNo", ((BidUserDetails) sessionUtill.getUserVo()).getMbIntgNo());
			paramMap.put("searchTrmnAmnNo", sessionUtill.getUserId());
			
			paramMap.put("searchDateState", yyyyMM);
			params.put("searchDateState", yyyyMM);
						
			List<Map<String,Object>> calendarList = auctionService.selectStateList(paramMap);
			
			if (calendarList != null) {
				result.put("success", true);
				result.put("message", "議고쉶�뿉 �꽦怨듯뻽�뒿�땲�떎.");
				result.put("info", calendarList);
			} else {
				result.put("success", false);
				result.put("message", "�젙�궛�궡�뿭�씠 �뾾�뒿�땲�떎.");
			}
			
			result.put("inputParam",params);
			result.put("johapData", johap);
			result.put("title",formatUtil.dateAddDotLenSix(yyyyMM));
		} catch (Exception e) {
			log.error("MyPageController.buyList : {}", e);
			result.put("success", false);
			result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
		}
		
		return result;
	}
	/**
	 * �굹�쓽 寃쎈ℓ�궡�뿭 - �젙�궛�꽌 �긽�꽭 �럹�씠吏�
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/my/buyInfo", method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView buyInfo(@RequestParam Map<String,Object> params) throws Exception {
		ModelAndView mav = new ModelAndView();
		BidUserDetails userVo = (BidUserDetails) sessionUtill.getUserVo();
		
		Map<String,Object> paramMap = new HashMap<>();
		paramMap.put("naBzPlcNo", params.get("searchnaBzPlcNo").toString());
		paramMap.put("naBzplc", "");
		
		Map<String,Object> johap = adminService.selectOneJohap(paramMap);
		paramMap.put("naBzPlc", johap.get("NA_BZPLC")); 
		
		paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
		paramMap.put("searchTrmnAmnNo", sessionUtill.getUserId());
		paramMap.put("searchDate", params.get("searchDate"));
		paramMap.put("searchAucObjDsc", params.get("searchAucObjDsc"));
		
		params.put("aucDt", dateUtil.addDelimDate(params.get("searchDate").toString()));

		//留ㅼ닔�씤�젙�궛�꽌 flag insert
		params.put("stateFlag", "buy");
		paramMap.put("stateFlag", "buy");
		
		//議고빀�젙蹂� 諛� 怨꾩쥖�젙蹂� 媛��졇�삤湲�
		mav.addObject("accountInfo", auctionService.selectJohapAccInfo(paramMap));		
		//留ㅼ닔�씤 �젙蹂� 議고쉶
		mav.addObject("stateInfo", auctionService.selectStateInfo(paramMap));
		//�굺李곌� 議고쉶
		mav.addObject("stateTotPrice", auctionService.selectTotSoldPriceAndFee(paramMap));
		//�굺李곗슦 �몢�닔 議고쉶
		mav.addObject("stateBuyCnt",auctionService.selectCountEntry(paramMap));
		//�긽�꽭 議고쉶 由ъ뒪�듃
		mav.addObject("list", auctionService.entrySelectList(paramMap));
		
		mav.addObject("johapData", johap);
		mav.addObject("inputParam", params);
		mav.addObject("subheaderTitle", "�굹�쓽 寃쎈ℓ�궡�뿭");
		mav.setViewName("mypage/buy/buy_info");
		
		return mav;
	}
	/**
	 * �굹�쓽 寃쎈ℓ�궡�뿭 - 援щℓ�궡�뿭 由ъ뒪�듃 議고쉶
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(path = "/auction/api/select/myBuyList", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> myBuyList(@RequestBody Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
         params.put("loginNo", sessionUtill.getUserId());
        result.put("success", true);
        try {        	
        	if(params.get("loginNo") != null) params.put("searchTrmnAmnNo", params.get("loginNo"));
        	List<Map<String,Object>> list=auctionService.entrySelectList(params);
        	result.put("totPrice", auctionService.selectTotSoldPrice(params));
        	result.put("buyCnt",auctionService.selectCountEntry(params));
            result.put("data", list);
        }catch (SQLException | RuntimeException re) {
            result.put("success", false);
            result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
			log.error("MyPageController.selectMyBuyList : {} ",re);
		}
        return result;
    }
	/**
	 * �굹�쓽 寃쎈ℓ�궡�뿭 - �쓳李곕궡�뿭 由ъ뒪�듃 議고쉶
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(path = "/auction/api/select/myBidList", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> myBidList(@RequestBody Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        try {        	
        	if(params.get("loginNo") != null) params.put("searchTrmnAmnNo", params.get("loginNo"));
        	List<Map<String,Object>> list=auctionService.selectBidLogList(params);
            result.put("data", list);
        }catch (SQLException | RuntimeException re) {
            result.put("success", false);
            result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
			log.error("MyPageController.selectMyBuyList : {} ",re);
		}
        return result;
    }
	
	/**
	 * �젙�궛�꽌 �닔�닔猷� �긽�꽭 �뙘�뾽
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(path = "/auction/api/select/myFeeStateInfo", produces = MediaType.APPLICATION_JSON_VALUE)
	Map<String, Object> myFeeStateInfo(@RequestBody Map<String,Object> params) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		try {        	
			Map<String, Object> paramMap = new HashMap<>();
			
			if ("entry".equals(params.get("stateFlag"))) {
				FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
				paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
				params.put("searchFhsIdNo", userVo.getFhsIdNo());
				params.put("searchFarmAmnno", userVo.getFarmAmnno());
				paramMap.put("feeFlag", "Y");
			} else {
				BidUserDetails userVo = (BidUserDetails)sessionUtill.getUserVo();
				paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
				paramMap.put("searchTrmnAmnno", sessionUtill.getUserId());
			}
			
			paramMap.put("naBzPlcNo", params.get("searchnaBzPlcNo").toString());
			paramMap.put("naBzplc", "");
			
			Map<String,Object> johap = adminService.selectOneJohap(paramMap);
			paramMap.put("naBzPlc", johap.get("NA_BZPLC"));
			paramMap.put("naBzPlcNo", johap.get("NA_BZPLCNO"));
			
			paramMap.putAll(params);

			//�닔�닔猷� �긽�꽭 議고쉶
			result.put("feeList", auctionService.myFeeStateList(paramMap));
			
		}catch (SQLException | RuntimeException re) {
			result.put("success", false);
			result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
			log.error("MyPageController.myFeeStateInfo : {} ",re);
		}
		return result;
	}
	/**
	 * �굹�쓽 寃쎈ℓ�궡�뿭 - �젙�궛�꽌 �긽�꽭 �젙蹂� 議고쉶
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(path = "/auction/api/select/myStateInfo", produces = MediaType.APPLICATION_JSON_VALUE)
    Map<String, Object> myStateInfo(@RequestBody Map<String,Object> params) {
		Map<String, Object> result = new HashMap<>();
		result.put("success", true);
		try {     
			if ("entry".equals(params.get("stateFlag"))) {
				FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
				params.put("searchMbIntgNo", userVo.getMbIntgNo());
				params.put("searchFhsIdNo", userVo.getFhsIdNo());
				params.put("searchFarmAmnno", userVo.getFarmAmnno());
				params.put("aucYnState", "1");
				params.put("feeFlag", "Y");
			} else {
				BidUserDetails userVo = (BidUserDetails)sessionUtill.getUserVo();
				params.put("searchMbIntgNo", userVo.getMbIntgNo());
				params.put("searchTrmnAmnNo", userVo.getTrmnAmnno());
			}
			
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.put("naBzPlcNo", params.get("searchnaBzPlcNo").toString());
			paramMap.put("naBzplc", "");
			
			Map<String,Object> johap = adminService.selectOneJohap(paramMap);
			paramMap.put("naBzPlc", johap.get("NA_BZPLC"));
			paramMap.put("naBzPlcNo", johap.get("NA_BZPLCNO"));
			
			paramMap.putAll(params);
			
        	//留ㅼ닔�씤 �젙蹂� 議고쉶
        	result.put("stateInfo", auctionService.selectStateInfo(paramMap));
        	//�굺李곌� 議고쉶
        	result.put("stateTotPrice", auctionService.selectTotSoldPriceAndFee(paramMap));
            //�굺李곗슦 �몢�닔 議고쉶
        	if ("entry".equals(paramMap.get("stateFlag"))) {
        		result.put("stateBuyCnt",auctionService.selectStateEntryCntFhs(paramMap));
        	} else {
        		result.put("stateBuyCnt",auctionService.selectCountEntry(paramMap));
        	}
            //�긽�꽭 議고쉶 由ъ뒪�듃
            result.put("list", auctionService.entrySelectList(paramMap));
            
        }catch (SQLException | RuntimeException re) {
            result.put("success", false);
            result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
			log.error("MyPageController.myStateInfo : {} ",re);
		}
        return result;
    }
	
	/**
	 * My�쁽�솴 議고쉶
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(path = "/auction/api/select/myMenu", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> myMenu(@RequestBody Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        try {        	
        	Map<String, Object> paramMap = new HashMap<>();
        	
        	// 寃��깋�븷 �궇吏� 怨꾩궛泥섎━
			String yyyyMM = "";
			String searchYm = params.get("searchDate").toString();
			DateTimeFormatter format = ("M".equals(params.get("ymFlag").toString())) 
												? DateTimeFormatter.ofPattern("yyyyMM")
												: DateTimeFormatter.ofPattern("yyyy");
			
			LocalDate date = LocalDate.now();
			
			if (searchYm != null && !searchYm.isEmpty()) {
				if (searchYm.length() == 4) {
					date = LocalDate.parse(searchYm+"0101",DateTimeFormatter.ofPattern("yyyyMMdd"));
				} else {
					date = LocalDate.parse(searchYm+"01",DateTimeFormatter.ofPattern("yyyyMMdd"));
				}
			}
			
			if ("M".equals(params.get("ymFlag"))) {
				if ("next".equals(params.get("flag"))) {
					yyyyMM = date.plusMonths(1).format(format);
				}
				else if("prev".equals(params.get("flag"))) {
					yyyyMM = date.minusMonths(1).format(format);	        	
				}
				else {
					yyyyMM = date.format(format);
				}				
			} else {
				if ("next".equals(params.get("flag"))) {
					yyyyMM = date.plusYears(1).format(format);
				}
				else if("prev".equals(params.get("flag"))) {
					yyyyMM = date.minusYears(1).format(format);	        	
				}
				else {
					yyyyMM = date.format(format);
				}
			}
			
			paramMap.putAll(params);
			paramMap.put("searchDateMy", ("M".equals(params.get("ymFlag")) ? yyyyMM : yyyyMM.substring(0, 4)));
			
			// �떎瑜� 議고빀 寃��깋
			paramMap.put("searchNaBzplc", paramMap.get("naBzplc").toString() == params.get("johpCd").toString() ? paramMap.get("naBzplc").toString() : params.get("johpCd").toString());
			Map<String, Object> johapTmpMap = new HashMap<>();
			johapTmpMap.put("naBzplc", paramMap.get("searchNaBzplc"));
			result.put("myJohapData", adminService.selectOneJohap(johapTmpMap));
			
			if ("buy".equals(params.get("stateFlag"))) {
				BidUserDetails userVo = ((BidUserDetails) sessionUtill.getUserVo());
				paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
	    		paramMap.put("searchTrmnAmnNo", sessionUtill.getUserId());
	    		
	    		// > �뿰/�썡�떒�쐞 湲곗� �쟾泥� �굺李곕몢�닔, �넚�븘吏�, 鍮꾩쑁�슦, 踰덉떇�슦�쓽 �굺李곕몢�닔
	    		result.put("cowBidCnt", myPageService.selectCowBidCnt(paramMap));
	    		// > �궡媛� bid�븳 �궡�뿭�쓽 �닽�옄�� �굺李� �띁�꽱�듃 (寃쎈ℓ���긽蹂꾨줈)
	    		result.put("cowBidPercent", myPageService.selectCowBidPercent(paramMap));
	    		// > �쟾泥� 議고빀�쓽 �굹�쓽 �쓳李� �쁽�솴
	    		result.put("cowBidCntList", myPageService.selectListAucBidCntAll(paramMap));
	    		
			} else {
				FarmUserDetails userVo = ((FarmUserDetails) sessionUtill.getUserVo());
				paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
	    		paramMap.put("searchFhsIdNo", ((FarmUserDetails) sessionUtill.getUserVo()).getFhsIdNo());
	    		paramMap.put("searchFarmAmnno", ((FarmUserDetails) sessionUtill.getUserVo()).getFarmAmnno());
	    		
	    		// > �뿰/�썡�떒�쐞 湲곗� �쟾泥� 異쒖옣�몢�닔, �넚�븘吏�, 鍮꾩쑁�슦, 踰덉떇�슦�쓽 �굺李곕몢�닔, �굺李고룊洹�, �룊洹좎떆�꽭
	    		result.put("cowEntryCnt", myPageService.selectCowEntryCnt(paramMap));
	    		// > �쟾泥� 議고빀�쓽 �굹�쓽 異쒖옣�슦 �쁽�솴
	    		result.put("cowEntryCntList", myPageService.selectListAucEntryCntAll(paramMap));
			}
			
    		result.put("inputParam", paramMap);    		
            
        }catch (SQLException | RuntimeException re) {
            result.put("success", false);
            result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
			log.error("MyPageController.myMenu : {} ",re);
		}
        return result;
    }

	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@RequestMapping(value = "/my/bid",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView mybidding() throws Exception {
		// �굹�쓽�쓳李곕궡�뿭
		LOGGER.debug("start of bid.do");
		ModelAndView mav = new ModelAndView();
		mav.addObject("subheaderTitle","�쓳李곕궡�뿭");
		mav.setViewName("mypage/bid/bid");
		return mav;
	}
	
	/**
	 * �굹�쓽 異쒖옣�슦
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ROLE_FARM')")
	@RequestMapping(value = "/my/entry",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView entry(@RequestParam Map<String, Object> params) throws Exception {
		ModelAndView mav = new ModelAndView("mypage/entry/entry");
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
		FarmUserDetails userVo = (FarmUserDetails) sessionUtill.getUserVo();
		params.put("loginNo", userVo.getFhsIdNo());
		
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("naBzPlcNo", params.get("place"));

		Map<String,Object> johap = adminService.selectOneJohap(paramMap);
		paramMap.put("naBzPlc", johap.get("NA_BZPLC"));
		
		List<Map<String,Object>> datelist= auctionService.selectAucDateList(paramMap);
		paramMap.put("searchDate", datelist.size() > 0 ? datelist.get(0).get("AUC_DT") : null);
		paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
		paramMap.put("searchFhsIdNo", userVo.getFhsIdNo());
		paramMap.put("searchFarmAmnno", userVo.getFarmAmnno());
		
		paramMap.putAll(params);
		paramMap.put("stateFlag", "entry");
		
		// 0. �굹�쓽 異쒖옣�슦 > 異쒖옣�슦
		mav.addObject("myEntryList", auctionService.entrySelectList(paramMap));
		
		// 1. �굹�쓽 異쒖옣�슦 > �젙�궛�꽌
		paramMap.put("searchDateState", today);
		
		mav.addObject("calendarList", auctionService.selectStateList(paramMap));
		
		//異쒖옣�슦 �긽�꽭 tab�빆紐� �몴湲�
		paramMap.put("simpCGrpSqno", "2");
		mav.addObject("tabList",auctionService.selectListExpitemSet(paramMap));
		
		params.putAll(paramMap);
		
 		mav.addObject("johapData", johap);
		mav.addObject("dateList",datelist);
		mav.addObject("inputParam", params);
		mav.addObject("title",formatUtil.dateAddDotLenSix(today));
		mav.addObject("subheaderTitle","�굹�쓽 異쒖옣�슦");
 		
		return mav;
	}
	
	/**
	 * �굹�쓽 異쒖옣�슦 - 異쒖옣�슦 由ъ뒪�듃 議고쉶
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(path = "/auction/api/select/myEntryBidList", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, Object> myEntryBidList(@RequestBody Map<String,Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        try {        	
        	FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
        	if(userVo != null) params.put("searchFhsIdNo", userVo.getFhsIdNo());
    		if(userVo != null) params.put("searchFarmAmnno", userVo.getFarmAmnno());
        	List<Map<String,Object>> list=auctionService.entrySelectList(params);
            result.put("data", list);
        }catch (SQLException | RuntimeException re) {
            result.put("success", false);
            result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
			log.error("MyPageController.selectMyBuyList : {} ",re);
		}
        return result;
    }
	
	/**
	 * �굹�쓽 異쒖옣�슦 - �젙�궛�꽌 由ъ뒪�듃 議고쉶
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PostMapping(value = "/auction/api/select/entryList", produces = MediaType.APPLICATION_JSON_VALUE)	
	public  Map<String, Object> entryList(@RequestBody final Map<String,Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		
		try {
			Map<String,Object> paramMap = new HashMap<>();
			paramMap.putAll(params);
			
			Map<String,Object> johap = adminService.selectOneJohap(paramMap);
			
			// 寃��깋�븷 �궇吏� 怨꾩궛泥섎━
			String yyyyMM = "";
	        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMM");
	     	String searchYm = paramMap.get("searchDateState").toString();
	     	LocalDate date = ( searchYm == null || searchYm.isEmpty()) ? LocalDate.now() : LocalDate.parse(searchYm+"01",DateTimeFormatter.ofPattern("yyyyMMdd"));
	        if ("next".equals(paramMap.get("flag"))) {
		        yyyyMM = date.plusMonths(1).format(format);
	        }
	        else if("prev".equals(paramMap.get("flag"))) {
		        yyyyMM = date.minusMonths(1).format(format);	        	
	        }
	        else {
		        yyyyMM = date.format(format);
	        }
	        
	        FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
	        paramMap.put("stateFlag", "entry");
			paramMap.put("searchMbIntgNo", userVo.getMbIntgNo());
			paramMap.put("searchFhsIdNo", userVo.getFhsIdNo());
			paramMap.put("searchFarmAmnno", userVo.getFarmAmnno());
			
			paramMap.put("searchDateState", yyyyMM);
			params.put("searchDateState", yyyyMM);
			
			List<Map<String,Object>> calendarList = auctionService.selectStateList(paramMap);
			if (calendarList != null) {
				result.put("success", true);
				result.put("message", "議고쉶�뿉 �꽦怨듯뻽�뒿�땲�떎.");
				result.put("info", calendarList);
			}
			else {
				result.put("success", false);
				result.put("message", "�젙�궛 �궡�뿭�씠 �뾾�뒿�땲�떎.");
			}
			
			result.put("inputParam",params);
			result.put("johapData", johap);
			result.put("title",formatUtil.dateAddDotLenSix(yyyyMM));
		} catch (Exception e) {
			log.error("MyPageController.buyList : {}", e);
			result.put("success", false);
			result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
		}
		return result;
	}
	
	/**
	 * �굹�쓽 異쒖옣�슦 - �젙�궛�꽌 �긽�꽭 �럹�씠吏�
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/my/entryInfo",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView entryInfo(@RequestParam Map<String,Object> params) throws Exception {
		ModelAndView mav = new ModelAndView();
		FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
		
		Map<String,Object> map = new HashMap<>();
		map.put("naBzPlcNo", params.get("searchnaBzPlcNo").toString()); 
		map.put("naBzplc", "");

		Map<String,Object> johap = adminService.selectOneJohap(map);
		map.put("naBzPlc", johap.get("NA_BZPLC")); 
		map.put("searchDate", params.get("searchDate"));
		map.put("searchAucObjDsc", params.get("searchAucObjDsc"));
		map.put("aucYnState", "1");
		
		map.put("searchMbIntgNo", userVo.getMbIntgNo());
		map.put("searchFhsIdNo", userVo.getFhsIdNo());
		map.put("searchFarmAmnno", userVo.getFarmAmnno());
		
		params.put("aucDt", dateUtil.addDelimDate(params.get("searchDate").toString()));
		//異쒗븯�슦�젙�궛�꽌 flag insert
		params.put("stateFlag", "entry");
		map.put("stateFlag", "entry");
		map.put("feeFlag", "Y");
		
		//議고빀�젙蹂� 諛� 怨꾩쥖�젙蹂� 媛��졇�삤湲�
		mav.addObject("accountInfo", auctionService.selectJohapAccInfo(map));
		//異쒗븯�슦 �젙蹂� 議고쉶
		mav.addObject("stateInfo", auctionService.selectStateInfo(map));
		//�굺李곌� 議고쉶
		mav.addObject("stateTotPrice", auctionService.selectTotSoldPriceAndFee(map));
		//�굺李곗슦 �몢�닔 議고쉶
		mav.addObject("stateBuyCnt",auctionService.selectStateEntryCntFhs(map));			
		//�긽�꽭 議고쉶 由ъ뒪�듃
		mav.addObject("list", auctionService.entrySelectList(map));

		mav.addObject("johapData", johap);
		mav.addObject("inputParam", params);
		mav.addObject("subheaderTitle", "�굹�쓽 異쒖옣�슦");
		mav.setViewName("mypage/entry/entry_info");
		return mav;
	}
	
	/**
	 * 1. �떒�씪寃쎈ℓ�뿉�꽌 �샇異쒖떆 李� 媛�寃⑹쓣 議고쉶�븳�떎.
	 * 2. �씪愿꾧꼍留ㅼ뿉�꽌 �샇異쒖떆 �씠�쟾 �쓳李� 媛�寃⑷낵 李� 媛�寃⑹쓣 �룞�떆�뿉 議고쉶�븳�떎. 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@PostMapping(value = "/my/favorite", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> myFavorite(@RequestBody final Map<String, Object> params) throws Exception {
		final Map<String, Object> result = new HashMap<String, Object>();
		try {
			String groupYn = params.getOrDefault("groupYn", "N").toString();
			Map<String, Object> favorite = new HashMap<String, Object>();
			if ("Y".equals(groupYn)) {
				Map<String,Object> map = auctionService.selectNearAtdrAm(params);
				Map<String,Object> zim = auctionService.selectMyZimPrice(params);
				favorite.put("ATDR_AM", map != null ? map.get("ATDR_AM"):0);
				favorite.put("SBID_UPR", zim != null ? zim.get("SBID_UPR"):0);
			}
			else {
				favorite = auctionService.selectMyFavoriteInfo(params);
			}
			
			if (favorite != null) {
				result.put("success", true);
				result.put("message", "議고쉶�뿉 �꽦怨듯뻽�뒿�땲�떎.");
				result.put("info", favorite);
			}
			else {
				result.put("success", false);
				result.put("message", "�벑濡앺븳 李� �젙蹂닿� �뾾�뒿�땲�떎.");
			}
			return result;
		}catch (SQLException | RuntimeException re) {
			log.error("MyPageController.myFavorite : {} ",re);
			result.put("success", false);
			result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
			return result;
		}
	}

	/**
	 * �굹�쓽 �젙蹂� �럹�씠吏� - 濡쒓렇�씤 �썑 �젒洹쇳븯�뒗 �럹�씠吏�
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/my/info",method = { RequestMethod.GET, RequestMethod.POST })
	public ModelAndView myInfo(@RequestParam Map<String,Object> params) throws Exception {
		
		LOGGER.debug("start of my/info.do");
		ModelAndView mav = new ModelAndView();
		Map<String, Object> map = new HashMap<String, Object>();
		
		String place = (String) params.get("place");
        mav.addObject("naBzPlcNo", place);
        
        map.put("place", place);
        map.put("flagAplyConfirm", "Y");
        if(sessionUtill.getNaBzplc() != null) map.put("naBzPlc", sessionUtill.getNaBzplc());
		if(sessionUtill.getUserId() != null) map.put("loginNo", sessionUtill.getUserId());
		Map<String, Object> aplyInfo = null;
		
		if("ROLE_BIDDER".equals(sessionUtill.getRoleConfirm())) {
			aplyInfo = auctionService.selectMySecAplyInfo(map);
		}
		
		if (aplyInfo == null) {
			mav.addObject("secAplyPossible", "Y");
		}
		else {
			mav.addObject("secAplyPossible", "N");
		}
		
		//濡쒓렇�씤�맂 �뿭�븷 �솗�씤�븯�뿬 媛� �뀒�씠釉� 議고쉶
		Map<String, Object> authNoYmd = new HashMap<String, Object>(); 
		if(sessionUtill.getRoleConfirm() != null) {
			if("ROLE_BIDDER".equals(sessionUtill.getRoleConfirm())) {
				authNoYmd = auctionService.selectMwmnAuthNoYmdInfo(map);
			}
			else if("ROLE_FARM".equals(sessionUtill.getRoleConfirm())) {
				if(sessionUtill.getUserVo() != null) {
					FarmUserDetails userVo = (FarmUserDetails)sessionUtill.getUserVo();
					map.put("farmAmnno", userVo.getFarmAmnno());
				}
				authNoYmd = auctionService.selectFhsAuthNoYmdInfo(map);
			}
		}
		mav.addObject("authNoYmd", authNoYmd);
		
		LocalDateTime date = LocalDateTime.now();
		String today = date.format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
		mav.addObject("today", today);
		mav.addObject("subheaderTitle","�굹�쓽�젙蹂�");
		mav.setViewName("mypage/info/myInfo");
		return mav;
	}
	
	/**
	 * �빐�떦 議고빀�뿉 �씠�슜�빐吏� �떊泥��씠 �릺�뼱 �엳�뒗吏� 泥댄겕�븯�뒗 硫붿냼�뱶
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@PostMapping(value = "/my/mySecAplyCheck", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> mySecAplyCheck(@RequestBody final Map<String, Object> params) throws Exception {
		final Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> aplyInfo = new HashMap<String, Object>();

		try {
			if(sessionUtill.getNaBzplc() != null) params.put("naBzPlc", sessionUtill.getNaBzplc());
			if(sessionUtill.getUserId() != null) params.put("loginNo", sessionUtill.getUserId());
			aplyInfo = auctionService.selectMySecAplyInfo(params);
			
			if (aplyInfo == null) {
				result.put("success", true);
				result.put("message", "�씠�슜�빐吏� 誘몄떊泥� �긽�깭�엯�땲�떎.");
			}
			else {
				result.put("success", false);
				result.put("message", "�씠誘� �씠�슜�빐吏� �떊泥��쓣 �븯�떊 �긽�깭�엯�땲�떎.");
			}
			return result;
			
		}catch (SQLException | RuntimeException re) {
			log.error("MyPageController.mySecAplyCheck : {} ",re);
			result.put("success", false);
			result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
			return result;
		}
	}
	
	/**
	 * �씠�슜�빐吏� �떊泥� �럹�씠吏� 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@RequestMapping(value = "/my/secAply",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView my_secAply(@RequestParam Map<String,Object> params) throws Exception {
		LOGGER.debug("start of secAply.do");
		ModelAndView mav = new ModelAndView();
		
		String place = (String) params.get("place");
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", place);
		Map<String,Object> johap = adminService.selectOneJohap(map);
		mav.addObject("johapData", johap);
		
		if(sessionUtill.getUserId() != null) params.put("loginNo", sessionUtill.getUserId());
		mav.addObject("inputParam", params);

		mav.addObject("subheaderTitle","�씠�슜�빐吏� �떊泥�");
 		mav.setViewName("mypage/sec/secAply");
		return mav;
	}
	
	/**
	 * �씠�슜�빐吏� �뜲�씠�꽣 insert 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@PostMapping(value = "/my/ins_mySecAply", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> ins_mySecAply(@RequestBody final Map<String, Object> params) throws Exception {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			BidUserDetails userVo = (BidUserDetails) sessionUtill.getUserVo();
			params.put("naBzPlc", StringUtils.isEmpty(params.get("johpCd").toString()) ? sessionUtill.getNaBzplc() : params.get("johpCd"));
			params.put("loginNo", StringUtils.isEmpty(params.get("loginNo").toString()) ? sessionUtill.getUserId() : params.get("loginNo")); 
			params.put("mbIntgNo", userVo.getMbIntgNo());
			
			auctionService.insertMySecAplyInfo(params);
			result.put("success", true);
			result.put("message", "�씠�슜�빐吏� �떊泥��씠 �셿猷뚮릺�뿀�뒿�땲�떎.<br/>�빐�떦 議고빀 愿�由ъ옄�쓽 �솗�씤 �썑 理쒖쥌�쟻�쑝濡� �씠�슜�빐吏�媛� �씠琉꾩쭏 �삁�젙�엯�땲�떎.");
			
			return result;
			
		}catch (SQLException | RuntimeException re) {
			log.error("MyPageController.mySecAplyCheck : {} ",re);
			result.put("success", false);
			result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
			return result;
		}
	}
	
	/**
	 * �씠�슜�빐吏� 泥좏쉶媛� 媛��뒫�븳 �긽�깭�씤吏� �솗�씤�븯�뒗 硫붿냼�뱶
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@PostMapping(value = "/my/mySecWithdrawCheck", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> mySecWithdrawCheck(@RequestBody final Map<String, Object> params) throws Exception {
		final Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> aplyInfo = new HashMap<String, Object>();

		try {
			if(sessionUtill.getNaBzplc() != null) params.put("naBzPlc", sessionUtill.getNaBzplc());
			if(sessionUtill.getUserId() != null) params.put("loginNo", sessionUtill.getUserId());
			aplyInfo = auctionService.selectMySecAplyInfo(params);
			
			if (aplyInfo == null) {
				result.put("success", false);
				result.put("message", "�씠�슜�빐吏� 誘몄떊泥� �긽�깭�엯�땲�떎.");
			}
			else {
				if(!"0".equals(aplyInfo.get("MGR_APPR_YN"))) {
					result.put("success", false);
					result.put("message", "�씠誘� �씠�슜�빐吏� �떊泥��씠 �셿猷뚮릺�뼱 泥좏쉶 遺덇��뒫�븳 �긽�깭�엯�땲�떎.");
				}else {
					result.put("success", true);
					result.put("message", "�씠�슜�빐吏� 泥좏쉶 媛��뒫�븳 �긽�깭�엯�땲�떎.");
				}
			}
			return result;
			
		}catch (SQLException | RuntimeException re) {
			log.error("MyPageController.mySecAplyCheck : {} ",re);
			result.put("success", false);
			result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
			return result;
		}
	}
	
	/**
	 * �씠�슜�빐吏� �떊泥� 泥좏쉶 �럹�씠吏� 
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@RequestMapping(value = "/my/secWithdraw",method = { RequestMethod.GET, RequestMethod.POST })	
	public ModelAndView secWithdraw(@RequestParam Map<String,Object> params) throws Exception {
		LOGGER.debug("start of secWithdraw.do");
		ModelAndView mav = new ModelAndView();
		
		String place = (String) params.get("place");
        Map<String,Object> map = new HashMap<>();
        map.put("naBzPlcNo", place);
		Map<String,Object> johap = adminService.selectOneJohap(map);
		mav.addObject("johapData", johap);
		
		if(sessionUtill.getUserId() != null) params.put("loginNo", sessionUtill.getUserId());
		mav.addObject("inputParam", params);

		mav.addObject("subheaderTitle","�씠�슜�빐吏� 泥좏쉶");
 		mav.setViewName("mypage/sec/secWithdraw");
		return mav;
	}
	
	/**
	 * �씠�슜�빐吏� �떊泥� 泥좏쉶湲곕뒫 援ы쁽�쓣 �쐞�빐
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PreAuthorize("hasRole('ROLE_BIDDER')")
	@PostMapping(value = "/my/del_mySecAply", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> del_mySecAply(@RequestBody final Map<String, Object> params) throws Exception {
		final Map<String, Object> result = new HashMap<String, Object>();

		try {
			BidUserDetails userVo = (BidUserDetails) sessionUtill.getUserVo();
			params.put("naBzPlc", StringUtils.isEmpty(params.get("johpCd").toString()) ? sessionUtill.getNaBzplc() : params.get("johpCd"));
			params.put("loginNo", StringUtils.isEmpty(params.get("loginNo").toString()) ? sessionUtill.getUserId() : params.get("loginNo")); 
			params.put("mbIntgNo", userVo.getMbIntgNo());
			
			Map<String, Object> aplyInfo = auctionService.selectMySecAplyInfo(params);
			
			if (aplyInfo == null) {
				result.put("success", false);
				result.put("message", "�씠�슜�빐吏� 誘몄떊泥� �긽�깭�엯�땲�떎.");
			}
			else {
				if(!"0".equals(aplyInfo.get("MGR_APPR_YN"))) {
					result.put("success", false);
					result.put("message", "�씠誘� �씠�슜�빐吏� �떊泥��씠 �셿猷뚮릺�뼱 泥좏쉶 遺덇��뒫�븳 �긽�깭�엯�땲�떎.");
				}else {
					//delete �빐�빞�븿
					auctionService.deleteMySecAplyInfo(params);
					result.put("success", true);
					result.put("message", "�빐吏� �떊泥� 泥좏쉶媛� �셿猷뚮릺�뿀�뒿�땲�떎.");
				}
			}
			
			return result;
			
		}catch (SQLException | RuntimeException re) {
			log.error("MyPageController.del_mySecAply : {} ",re);
			result.put("success", false);
			result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
			return result;
		}
	}
	
	/**
	 * �궎�삤�뒪�겕 �씤利앸쾲�샇 諛쒓툒�븯湲�
	 * @param params
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@PostMapping(value = "/my/myAuthNumIssue", produces = MediaType.APPLICATION_JSON_VALUE)
	public Map<String, Object> myAuthNumIssue(@RequestBody final Map<String, Object> params) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();

		try {
			result = auctionService.myAuthNumIssue(params);
			return result;
			
		}catch (SQLException | RuntimeException re) {
			log.error("MyPageController.myAuthNumIssue : {} ",re);
			result.put("success", false);
			result.put("message", "�옉�뾽以� �삤瑜섍� 諛쒖깮�뻽�뒿�땲�떎. 愿�由ъ옄�뿉寃� 臾몄쓽�븯�꽭�슂.");
			return result;
		}
	}
}
