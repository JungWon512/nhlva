package com.ishift.auction.service.login;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.service.auction.AuctionDAO;
import com.ishift.auction.service.common.CommonDAO;
import com.ishift.auction.service.common.CommonService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.HttpUtils;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.util.SessionUtill;
import com.ishift.auction.util.Util;
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
	
	@Autowired
	private CommonService commonService;
	
	@Autowired
	private CommonDAO commonDao;
	
	@Autowired
	private SessionUtill sessionUtil;
	
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
		String mb_intg_no = "";
		
		// 로그인 유형 > 0 : 중도매인, 1 : 출하주
		final String type = params.getOrDefault("type", "0").toString();
		
		// 통합회원 구분 코드 > 01 : 중도매인, 02 : 출하주(농가)
		final String intg_gubun = "0".equals(type) ? "01" : "02";
				
		// 중도매인, 출하주 검색 (같은 사업장에 이름과 생년월일이 같은 사람이 있을지도?)
		final List<Map<String, Object>> list = "0".equals(type) ? this.selectWholesalerList(params) : this.selectFarmUserList(params);
		
		if (list.size() > 0) {
			
			//[s] gypark : 회원통합 확인 로직
			params.put("regUsrid", "SYSTEM");
			params.put("MB_INTG_GB", intg_gubun);	//통합회원구분
			params.put("MB_INTG_NM", "01".equals(intg_gubun) ? list.get(0).get("SRA_MWMNNM") : list.get(0).get("FTSNM"));	//회원명
			params.put("MB_RLNO", "01".equals(intg_gubun) ? list.get(0).get("CUS_RLNO") : list.get(0).get("BIRTH"));	//생년월일
			params.put("OHSE_TELNO", list.get(0).get("OHSE_TELNO"));		//자택전화번호
			params.put("MB_MPNO", list.get(0).get("CUS_MPNO"));		//휴대전화번호
			// 통합회원 테이블에서 이름, 생년월일, 휴대전화번호로 통합회원 정보 조회
			final Map<String, Object> mbIntgNoInfo = commonDao.getIntgNoInfo(params);
			
			if(list.get(0).get("MB_INTG_NO") != null && !"".equals(list.get(0).get("MB_INTG_NO"))) {
				mb_intg_no = list.get(0).get("MB_INTG_NO").toString();
				params.put("MB_INTG_NO", mb_intg_no);
				final Map<String, Object> intgNumInfo = commonDao.getIntgNoInfoForNum(params);
				//휴면회원이면
				if("1".equals(intgNumInfo.get("DORMACC_YN").toString()) && "0".equals(intgNumInfo.get("DELACC_YN").toString())){
					//휴면해제 프로세스
					commonService.updateDormcUserFhsClear(params);
				}
				
				//최종접속일자, 휴면예정일자 update
				loginDAO.updateMbintgConDormInfo(params);
				
			}else {
				//중도매인 이름, 생년월일 or 출하주 이름, 연락처로 통합회원 조회 했을 때도 데이터가 없는 경우 => 신규가입
				if(mbIntgNoInfo == null || mbIntgNoInfo.isEmpty()) {
					//회원 통합 데이터 insert
					if (!"".equals(params.get("MB_INTG_NM")) && !"".equals(params.get("MB_RLNO")) && !"".equals(params.get("MB_MPNO"))) {
						int mb_intg_no_i = commonDao.insertIntgInfo(params);
						mb_intg_no = String.valueOf(mb_intg_no_i);
					}
				}
				else {  //다른 조합에서 가입한 통합회원 정보가 있으면 해당 회원통합번호만 업데이트 
					mb_intg_no = mbIntgNoInfo.get("MB_INTG_NO").toString();
				}
				
				//중도매인 or 출하주 회원통합번호 update
				if(!"".equals(mb_intg_no)) {
					params.put("MB_INTG_NO", mb_intg_no);
					if("01".equals(params.get("MB_INTG_GB"))) {
						loginDAO.updateMmMwmnMbintgNo(params);
					}else {
						loginDAO.updateMmFhsMbintgNo(params);
					}
					
					//최종접속일자, 휴면예정일자 update
					loginDAO.updateMbintgConDormInfo(params);
				}
			}
			//[e] gypark : 회원통합 확인 로직
			
			JwtTokenVo jwtTokenVo = JwtTokenVo.builder()
												.auctionHouseCode(list.get(0).get("NA_BZPLC").toString())
												.userMemNum(list.get(0).get("TRMN_AMNNO").toString())
												.userRole("0".equals(type) ? Constants.UserRole.BIDDER : Constants.UserRole.FARM)
												.mbIntgNo(mb_intg_no)			//회원통합번호 추가
												.build();

			token = jwtTokenUtil.generateToken(jwtTokenVo, Constants.JwtConstants.ACCESS_TOKEN);
			returnMap.put("success", true);
			returnMap.put("info", jwtTokenVo);
			returnMap.put("token", token);
			returnMap.put("returnUrl", "0".equals(type) ? "/main" : "/my/entry");
			returnMap.put("type", type);
			returnMap.put("place", params.get("place"));
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
	 * 인증번호 조회 (중도매인)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectAuthNumberInfo(final Map<String, Object> params) throws SQLException {
		return loginDAO.selectAuthNumberInfo(params);
	}

	/**
	 * 인증번호 조회 (출하주)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public Map<String, Object> selectAuthFhsNumberInfo(Map<String, Object> params) throws SQLException{
		return loginDAO.selectAuthFhsNumberInfo(params);
	}
	
	/**
	 * 새로 발급한 인증번호 저장 (중도매인)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int updateAuthNumber(final Map<String, Object> params) throws SQLException {
		return loginDAO.updateAuthNumber(params);
	}
	
	/**
	 * 새로 발급한 인증번호 저장 (출하주)
	 * @param params
	 * @return
	 * @throws SQLException
	 */
	@Override
	public int updateAuthFhsNumber(final Map<String, Object> params) throws SQLException {
		return loginDAO.updateAuthFhsNumber(params);
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
		final String type = params.getOrDefault("type", "0").toString();
		final String token = params.getOrDefault("token", "").toString();
		params.put("naBzplc", jwtTokenUtil.getValue(token, Constants.JwtConstants.JWT_CLAIM_AUCTION_HOUSE_CODE));
		
		String loginId = jwtTokenUtil.getValue(token, Constants.JwtConstants.JWT_CLAIM_USER_MEM_NUM);
		if(Constants.UserRole.BIDDER.equals(jwtTokenUtil.getValue(token, Constants.JwtConstants.JWT_CLAIM_USER_ROLE))) {
			params.put("trmnAmnno", loginId);
		}else {
			params.put("naBzplcno", params.get("place"));
			params.put("fhsIdNo", loginId.split("-")[0]);
			params.put("farmAmnno", loginId.split("-")[1]);
		}

		final Map<String, Object> branchInfo = adminService.selectOneJohap(params);
		final List<Map<String, Object>> auctionList = auctionDAO.selectAuctQcnForToday();
		final Map<String, Object> userInfo = "0".equals(type) ? this.selectWholesaler(params) : this.selectFarmUser(params);
		
		// TB_LA_IS_MM_MWMN에 오늘 발급받은 인증번호가 있는지 조회
		final Map<String, Object> authInfo = "0".equals(type) ? this.selectAuthNumberInfo(params) : this.selectAuthFhsNumberInfo(params);	
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
				if("0".equals(type)) {
					this.updateAuthNumber(params);
				}else {
					this.updateAuthFhsNumber(params);
				}
			}
		}
		
		String aucObjDsc = "0";
		if (auctionList.size() > 0) aucObjDsc = auctionList.get(0).getOrDefault("AUC_OBJ_DSC", "0").toString();
		// SMS 발송 정보 저장
		params.put("aucObjDsc", aucObjDsc);
		params.put("rmsMnName", "0".equals(type) ? userInfo.get("SRA_MWMNNM") : userInfo.get("FTSNM"));
		params.put("smsRmsMpno", userInfo.get("CUS_MPNO"));
		params.put("ioTrmsnm", branchInfo.get("CLNTNM"));
		params.put("smsTrmsTelno", branchInfo.get("TEL_NO"));
		
		StringBuffer sb = new StringBuffer();
		sb.append("경매참가 인증번호 [").append(smsNo).append("] 입니다.");
		params.put("smsFwdgCntn", sb.toString());

		if ("production".equals(profile)) {
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
					
					if("1".equals(type)) {
						params.put("trmnAmnno", params.get("fhsIdNo"));
					}
					loginDAO.sendSms(params);
					returnMap.put("success", true);
					returnMap.put("message", "인증번호 발송에 성공했습니다.");
				}
				else {
					returnMap.put("success", false);
					returnMap.put("message", "인증번호 발송에 실패했습니다.");
				}
			} catch (Exception e1) {
				returnMap.put("success", false);
				returnMap.put("message", "인증번호 발송에 실패했습니다.");
			}
		}
		else {
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
		String loginId = jwtTokenUtil.getValue(token, Constants.JwtConstants.JWT_CLAIM_USER_MEM_NUM);
		if(Constants.UserRole.BIDDER.equals(jwtTokenUtil.getValue(token, Constants.JwtConstants.JWT_CLAIM_USER_ROLE))) {
			params.put("trmnAmnno", loginId);
		}else {
			params.put("naBzplcno", params.get("place"));
			params.put("fhsIdNo", loginId.split("-")[0]);
			params.put("farmAmnno", loginId.split("-")[1]);
		}
		
		final Map<String, Object> authInfo = "0".equals(params.get("type")) ? this.selectAuthNumberInfo(params) : this.selectAuthFhsNumberInfo(params);	
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

	/**
	 * 로그인 이력 저장하기
	 * @param request, params, inOutGb
	 * @return
	 * @throws SQLException
	 * @throws RuntimeException
	 */
	@Override
	public void insertLoginConnHistory(HttpServletRequest request, Map<String, Object> params) throws SQLException, RuntimeException {
		
		final JwtTokenVo tokenVo = jwtTokenUtil.getTokenVo(params.get("token").toString());
		String inOutGb = params.get("inOutGb").toString();		//앞에서 들어옴
		
		//TODO : 경제지주, 축산담당 -> 대시보드 로그인 때 들어올 가능성 있음, 추후 작업 필요하면 추가할 예정 
		//접속자구분 : CONN_GBCD (중도매인 : 1, 농가 : 2, 경제지주 : 3, 축산담당 : 4, 기타 : 5)
		switch(tokenVo.getUserRole()) {
			case Constants.UserRole.BIDDER : 
				params.put("connGbcd", "1");		
				break;
			case Constants.UserRole.FARM : 
				params.put("connGbcd", "2");	
				break;
			default :
				params.put("connGbcd", "5");	
				break;
		}
		
		params.put("mbIntgNo", tokenVo.getMbIntgNo());		//회원통합번호 
		params.put("loginId", tokenVo.getUserMemNum());		//LOGIN_ID (중도매인 : TRMN_AMNNO, 농가 : FHS_ID_NO, 축산/경제지주 : 사번 또는 부여코드)
		params.put("naBzPlc", tokenVo.getAuctionHouseCode());	//NA_BZPLC
		params.put("inOutGb", inOutGb);	//로그인, 로그아웃 구분코드 (로그인 : 1, 로그아웃 : 2)
		params.put("connIP", Util.getClientIP(request));		//접속자 IP
		loginDAO.insertMmConnHistory(params);
	}

}
