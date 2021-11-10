package com.ishift.auction.service.login;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ishift.auction.service.admin.AdminService;
import com.ishift.auction.util.Constants;
import com.ishift.auction.util.JwtTokenUtil;
import com.ishift.auction.vo.JwtTokenVo;

@Service("LoginService")
public class LoginServiceImpl implements LoginService {

	@Resource(name = "loginDAO")
	LoginDAO loginDAO;
	
	@Autowired
	JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	AdminService adminService;

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
	public List<Map<String, Object>> selectFarmUserList(Map<String, Object> params) throws SQLException {
		return loginDAO.selectFarmUserList(params);
	}
	
	/**
	 * 조합코드(NA_BZPLC)와 농가 식별번호(FHS_ID_NO)로 출하주 조회
	 * @param params
	 * @return
	 */
	@Override
	public Map<String, Object> selectFarmUser(Map<String, Object> params) throws SQLException {
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
	public Map<String, Object> loginProc(final Map<String, Object> params) throws SQLException {
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
			Map<String, Object> branchInfo = adminService.selectOneJohap(params);
			returnMap.put("branchInfo", branchInfo);
		}
		else {
			returnMap.put("message", "로그인 정보를 확인해주세요.");
			returnMap.put("success", false);
			return returnMap;
		}
		
		return returnMap;
	}
	
}
