package com.ishift.auction.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
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

import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadResult;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.UploadPartRequest;
import com.amazonaws.services.s3.model.UploadPartResult;
import com.amazonaws.util.IOUtils;
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
public class AdminTaskController extends CommonController {
	
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
		final Map<String, String> titleMap = new HashMap<String, String>() {{put("LW", "하한가/중량 등록");put("N", "계류대 변경");put("AW", "중량 일괄 등록");put("AL", "하한가 일괄 등록");put("SB", "낙찰결과 조회");put("SCOW", "출장우 리스트");put("SMCOW", "미감정 임신우");}};
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
				params.put("simpTpc", "PPGCOW_FEE_DSC");
				result.put("ppgcowList", auctionService.selectCodeList(params));
				params.put("simpTpc", "INDV_SEX_C");
				result.put("indvList", auctionService.selectCodeList(params));
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
			
			String regType = (String)params.getOrDefault("regType", "");
			if("N".equals(regType)) {
				auctionService.updateCowInfoForModlNo(params);
			}

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
	
	@RequestMapping(value = "/office/task/uploadImage", method = {RequestMethod.GET, RequestMethod.POST})
	public ModelAndView uploadImage(@RequestParam final Map<String, Object> params) {
		final ModelAndView mav = new ModelAndView("admin/task/uploadImage");
		return mav;
	}
	
	@ResponseBody
	@RequestMapping(value = "/office/task/uploadImageAjax", method = {RequestMethod.GET, RequestMethod.POST})
	public Map<String, Object> uploadImageAjax(@RequestBody final Map<String, Object> params) throws IOException {
		final Map<String, Object> result = new HashMap<String, Object>();
		final String endPoint = "https://kr.object.ncloudstorage.com";
		final String regionName = "kr-standard";
		final String accessKey = "loqHvgq2A4WGx0D7feer";
		final String secretKey = "yrmIJmsF37g1BExQXk5CIhrMn1EG1h32qJyaTvzF";

//		// S3 client
		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
												 .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(endPoint, regionName))
												 .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
												 .build();

		String bucketName = "test-tt12";
		String folderName = "sample-folder2/";
		
		String imgUplad = params.getOrDefault("imgUpload", ",").toString();
		String[] base64Arr = imgUplad.split(",");
		byte[] imgByte = Base64.decodeBase64(base64Arr[1]);
		InputStream bis = new ByteArrayInputStream(imgByte);
		
		ObjectMetadata imageObjectMetadata = new ObjectMetadata();
		imageObjectMetadata.setContentLength(imgByte.length);
		imageObjectMetadata.setContentType(MediaType.IMAGE_PNG_VALUE);
		PutObjectRequest imgputObjectRequest = new PutObjectRequest(bucketName, folderName + "test.png", bis, imageObjectMetadata);

		try {
			s3.putObject(imgputObjectRequest);
		} catch (AmazonS3Exception e) {
			e.printStackTrace();
		} catch(SdkClientException e) {
			e.printStackTrace();
		}

		return result;
	}
	
}
