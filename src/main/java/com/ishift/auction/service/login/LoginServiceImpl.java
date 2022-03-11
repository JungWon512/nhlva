package com.ishift.auction.service.login;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;

import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.auction.AuctionDAO;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.HttpUtils;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.vo.JwtTokenVo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("LoginService")
public class LoginServiceImpl implements LoginService {

	@Resource(name = "loginDAO")
	private LoginDAO loginDAO;
	
	@Resource(name = "auctionDAO")
	private AuctionDAO auctionDAO;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private HttpUtils httpUtils;
	
	@Autowired
	private AdminService adminService;
	
	@Value("${spring.profiles.active}")
	private String profile;

	/**
	 * 로그인 중도매인 검색
	 * @throws SQLException 
	 */
	@Override
	public List<Map<String, Object>> selectWholesalerList(final Map<String, Object> params) throws SQLException {
		return loginDAO.selectWholesalerList(params);
	}

	/**
	 * 조합코드(NA_BZPLC)와 거래인 관리번호(TRMN_AMNNO)로 중도매인 조회
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectWholesaler(final Map<String, Object> params) throws SQLException {
		return loginDAO.selectWholesaler(params);
	}

	/**
	 * 로그인 출하주 검색
	 * @param params
	 * @return
	 */
	@Override
	public List<Map<String, Object>> selectFarmUserList(final Map<String, Object> params) throws SQLException {
		return loginDAO.selectFarmUserList(params);
	}
	
	/**
	 * 조합코드(NA_BZPLC)와 농가 식별번호(FHS_ID_NO)로 출하주 조회
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> selectFarmUser(final Map<String, Object> params) throws SQLException {
		return loginDAO.selectFarmUser(params);
	}
	
	/**
	 * 방문로그 저장
	 * @throws SQLException
	 */
	@Override
	public void insertVisitor(final Map<String, Object> vo) throws SQLException{
		loginDAO.insertVisitor(vo);
	}

	/**
	 * 중도매인 출하주 로그인 프로세스
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> loginProc(final Map<String, Object> params) throws SQLException, RuntimeException {
		final Map<String, Object> returnMap = new HashMap<String, Object>();
		String token = "";
		
		// 로그인 유형 > 0 : 중도매인, 1 : 출하주
		final String type = params.getOrDefault("type", "0").toString();

		// 중도매인, 출하주 검색 (같은 사업장에 이름과 생년월일이 같은 사람이 있을지도?)
		final List<Map<String, Object>> list = "0".equals(type) ? this.selectWholesalerList(params) : this.selectFarmUserList(params);

		if (list.size() > 0) {
			JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
												.auctionHouseCode(list.get(0).get("NA_BZPLC").toString())
												.userMemNum(list.get(0).get("TRMN_AMNNO").toString())
												.userRole("0".equals(type) ? Constants.UserRole.BIDDER : Constants.UserRole.FARM)
												.build();

			token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
			returnMap.put("success", true);
			returnMap.put("info", jwtTokenVo);
			returnMap.put("token", token);
			returnMap.put("returnUrl", "0".equals(type) ? "/main" : "/my/entry");
			params.put("naBzPlcNo", params.get("place"));
			final Map<String, Object> branchInfo = adminService.selectOneJohap(params);
			returnMap.put("branchInfo", branchInfo);
		}
		else {
			returnMap.put("message", "로그인 정보를 확인해주세요.");
			returnMap.put("success", false);
			return returnMap;
		}
		
		return returnMap;
	}

	/**
	 * 인증번호 조회
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectAuthNumberInfo(final Map<String, Object> params) throws SQLException {
		return loginDAO.selectAuthNumberInfo(params);
	}

	/**
	 * 새로 발급한 인증번호 저장
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int updateAuthNumber(final Map<String, Object> params) throws SQLException {
		return loginDAO.updateAuthNumber(params);
	}

	/**
	 * 로그인 인증번호 발송
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> sendSmsProc(final Map<String, Object> params) throws SQLException, RuntimeException {
		final Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.putAll(params);
		String smsNo = "";
		
		final String token = params.getOrDefault("token", "").toString();
		params.put("naBzplc", jwtTokenUtil.getValue(token, Constants.JwtConstants.JWT_CLAIM_AUCTION_HOUSE_CODE));
		params.put("trmnAmnno", jwtTokenUtil.getValue(token, Constants.JwtConstants.JWT_CLAIM_USER_MEM_NUM));

		final Map<String, Object> branchInfo = adminService.selectOneJohap(params);
		final List<Map<String, Object>> auctionList = auctionDAO.selectAuctQcnForToday();
		final Map<String, Object> userInfo = this.selectWholesaler(params);

		// TB_LA_IS_MM_MWMN에 오늘 발급받은 인증번호가 있는지 조회
		final Map<String, Object> authInfo = this.selectAuthNumberInfo(params);
		if (authInfo != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			String now = sdf.format(new Date());
			
			// 1. 인증번호 조회 또는 생성
			// SMS 인증번호 생성일자가 오늘인 경우
			if (now.equals(authInfo.get("SMS_YMD"))) {
				smsNo = authInfo.getOrDefault("SMS_NO", "").toString();
			}
			// - 아닌 경우 4자리 숫자를 랜덤 생성 후 TB_LA_IS_MM_MWMN테이블에 인증번호를 UPDATE해준다.
			else {
				Random random = new Random();
				random.setSeed(System.currentTimeMillis());
				smsNo = String.format("%04d", random.nextInt(10000));
				params.put("smsNo", smsNo);

				// 인증번호 upate
				this.updateAuthNumber(params);
			}
		}
		
		String aucObjDsc = "0";
		if (auctionList.size() > 0) aucObjDsc = auctionList.get(0).getOrDefault("AUC_OBJ_DSC", "0").toString();
		// SMS 발송 정보 저장
		params.put("aucObjDsc", aucObjDsc);
		params.put("rmsMnName", userInfo.get("SRA_MWMNNM"));
		params.put("smsRmsMpno", userInfo.get("CUS_MPNO"));
		params.put("ioTrmsnm", branchInfo.get("CLNTNM"));
		params.put("smsTrmsTelno", branchInfo.get("TEL_NO"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("경매참가 인증번호 [").append(smsNo).append("] 입니다.");
		params.put("smsFwdgCntn", sb.toString());
		
		try {
			
	        String recvMsg = httpUtils.sendPostJson(params);
	        log.info("recvMsg : "+recvMsg);
	        Map<String, Object> map = new HashMap<String, Object>();
	        //받은메시지를 map에 담기
	        if(recvMsg.length()>0) {
	        	try {
	                map = httpUtils.getMapFromJsonObject(recvMsg);              
	        	}catch (JSONException e) {
					map = null;
				}
	        }
	        if(map != null) {
	            params.put("tmsYn", map.getOrDefault("RZTC","0"));
	    		
			loginDAO.sendSms(params);
			returnMap.put("success", true);
			returnMap.put("message", "인증번호 발송에 성공했습니다.");
	        }else {
	    		returnMap.put("success", false);
	    		returnMap.put("message", "인증번호 발송에 실패했습니다.");        	
		}
		} catch (Exception e1) {
    		returnMap.put("success", false);
    		returnMap.put("message", "인증번호 발송에 실패했습니다.");      
		}
		
		if (!"production".equals(profile)) {
			returnMap.put("smsNo", smsNo);
		}
		return returnMap;
	}

	/**
	 * 로그인 인증번호 확인
	 * @param params
	 * @return
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	@Override
	public Map<String, Object> loginAuthProc(final Map<String, Object> params) throws SQLException, RuntimeException {
		final Map<String, Object> returnMap = new HashMap<String, Object>(params);
		final String token = params.getOrDefault("token", "").toString();
		// 토큰이 유효한지 체크
		if (!jwtTokenUtil.isValidToken(token)) {
			returnMap.put("success", false);
			returnMap.put("message", "로그인 정보를 확인해주세요.");
			return returnMap;
		}
		
		// 인증번호 조회
		params.put("trmnAmnno", jwtTokenUtil.getValue(token, Constants.JwtConstants.JWT_CLAIM_USER_MEM_NUM));
		final Map<String, Object> authInfo = this.selectAuthNumberInfo(params);
		if (authInfo == null) {
			returnMap.put("success", false);
			returnMap.put("message", "인증번호 정보가 없습니다.");
			return returnMap;
		}
		
		// 인증번호의 날짜와 인증번호 값을 체크
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String today = sdf.format(new Date());
		if (!today.equals(authInfo.get("SMS_YMD"))
		 || !params.getOrDefault("authNumber", "10000").equals(authInfo.get("SMS_NO"))) {
			returnMap.put("success", false);
			returnMap.put("message", "인증번호가 올바르지 않습니다.");
			return returnMap;
		}
		
		returnMap.put("branchInfo", adminService.selectOneJohap(params));
		returnMap.put("info", jwtTokenUtil.getTokenVo(token));
		returnMap.put("success", true);
		returnMap.put("message", "인증에 성공했습니다.");
		return returnMap;
	}
	
}
