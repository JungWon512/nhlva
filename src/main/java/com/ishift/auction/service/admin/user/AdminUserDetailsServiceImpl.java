package com.ishift.auction.service.admin.user;

import java.sql.SQLException;
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
										.naBzplNm(admin.get("NA_BZPLNM").toString())
										.grpC(admin.get("GRP_C").toString())
										.build();
				}
			else {
				throw new UsernameNotFoundException("[" + username + "] 1.로그인 정보를 찾을 수 없습니다.");
			}
		}catch (RuntimeException re) {
			log.error("RuntimeException: AdminUserDetailsServiceImpl ");			
			throw new UsernameNotFoundException("[" + username + "] 로그인 정보를 찾을 수 없습니다.");
		} catch (SQLException se) {
			log.error("SQLException: AdminUserDetailsServiceImpl ");			
			throw new UsernameNotFoundException("[" + username + "] 로그인 정보를 찾을 수 없습니다.");
		}
		
		log.debug("##### AdminUserDetailsServiceImpl.loadUserByUsername [e]");
		return userVo;
	}

	public AdminUserDetails loadUserByUsername(String username, String password) throws UsernameNotFoundException{
		log.debug("##### AdminUserDetailsServiceImpl.loadUserByUsername [s]");
		log.debug("arguments : {}", username);

		AdminUserDetails userVo = null;

		try {
			Map<String,Object> params = new HashMap<>();
			params.put("usrid", username);
			params.put("pw", password);
			Map<String,Object> admin = adminloginService.selectAdminInfoWherePw(params);
			
			if(admin != null) {
				userVo = AdminUserDetails.builder()
										.naBzplc(admin.get("NA_BZPLC").toString())
										.pw(admin.get("PW").toString())
										.usrnm(admin.get("USRNM").toString())
										.usrid(admin.get("USRID").toString())
										.place(admin.get("NA_BZPLCNO").toString())
										.eno(admin.get("ENO").toString())
										.naBzplNm(admin.get("NA_BZPLNM").toString())
										.grpC(admin.get("GRP_C").toString())
										.build();
				}
			else {
				throw new UsernameNotFoundException(" 1.로그인 정보를 찾을 수 없습니다.");
			}
		}catch (RuntimeException re) {
			log.error("RuntimeException: AdminUserDetailsServiceImpl ");			
			throw new UsernameNotFoundException(" 로그인 정보를 찾을 수 없습니다.");
		}catch (SQLException se) {
			log.error("SQLException: AdminUserDetailsServiceImpl ");			
			throw new UsernameNotFoundException("로그인 정보를 찾을 수 없습니다.");
		}
		
		log.debug("##### AdminUserDetailsServiceImpl.loadUserByUsername2 [e]");
		return userVo;		
	}
}
