package com.ishift.auction.service.admin.user;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ishift.auction.service.admin.login.AdminLoginService;
import com.ishift.auction.vo.AdminUserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AdminUserDetailsServiceImpl implements AdminUserDetailsService {

	@Autowired
	private AdminLoginService adminloginService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("##### AdminUserDetailsServiceImpl.loadUserByUsername [s]");
		log.debug("arguments : {}", username);

		AdminUserDetails userVo = null;

		try {
			Map<String,Object> params = new HashMap<>();
			params.put("usrid", username);
			Map<String,Object> admin = adminloginService.selectLoginAdminInfo(params);
			
			if(admin != null) {
				userVo = AdminUserDetails.builder()
										.naBzplc(admin.get("NA_BZPLC").toString())
										.pw(admin.get("PW").toString())
										.usrnm(admin.get("USRNM").toString())
										.usrid(admin.get("USRID").toString())
										.place(admin.get("NA_BZPLCNO").toString())
										.eno(admin.get("ENO").toString())
										.build();
				}
			else {
				throw new UsernameNotFoundException("[" + username + "] 1.로그인 정보를 찾을 수 없습니다.");
			}
		}
		catch (Exception e) {
			log.error(e.getMessage());
			throw new UsernameNotFoundException("[" + username + "] 로그인 정보를 찾을 수 없습니다.");
		}
		
		log.debug("##### AdminUserDetailsServiceImpl.loadUserByUsername [e]");
		return userVo;
	}

//	@Override
//	public TbLaIsMmUsr loadUserByUsername(String usrid)
//			throws UsernameNotFoundException {
//		log.debug("##### AdminUserDetailsServiceImpl.loadUserByUsername [s]");
//		log.debug("arguments : {}, {}", usrid, johapCd);
//
//		TbLaIsMmUsr userVo = null;
//
//		try {
//			Map<String,Object> params = new HashMap<>();
//			params.put("usrid", usrid);
//			params.put("naBzplc", johapCd);
//			Map<String,Object> wholesaler = adminloginService.selectAdminInfo(params);
//			
//			if(wholesaler != null) {
//				userVo = TbLaIsMmUsr.builder()
//									.naBzplc(wholesaler.get("NA_BZPLC").toString())
//									.pw(wholesaler.get("PW").toString())
//									.usrnm(wholesaler.get("USRNM").toString())
//									.usrid(wholesaler.get("USRID").toString())
//									.place(wholesaler.get("NA_BZPLCNO").toString())
//									.build();
//				}
//			else {
//				throw new UsernameNotFoundException("[" + usrid + "] 1.로그인 정보를 찾을 수 없습니다.");
//			}
//		}
//		catch (Exception e) {
//			throw new UsernameNotFoundException("[" + usrid + "] 로그인 정보를 찾을 수 없습니다.");
//		}
//		
//		log.debug("##### BidUserDetailsService.loadUserByUsername [e]");
//		return userVo;
//	}

}
