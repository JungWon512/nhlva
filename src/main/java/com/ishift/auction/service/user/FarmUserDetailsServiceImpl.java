package com.ishift.auction.service.user;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ishift.auction.service.login.LoginService;
import com.ishift.auction.vo.FarmUserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FarmUserDetailsServiceImpl implements FarmUserDetailsService {

	@Autowired
	private LoginService loginService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("##### FarmUserDetailsServiceImpl.loadUserByUsername [s]");
		log.debug("arguments : {}", username);

		FarmUserDetails FarmdUserVo = null;

		try {
			Map<String,Object> params = new HashMap<>();
			params.put("fhsIdNo", (username.split("_")[0]).split("-")[0]);
			params.put("farmAmnno", (username.split("_")[0]).split("-")[1]);
			params.put("naBzplc", username.split("_")[1]);
			params.put("naBzplcno", username.split("_")[2]);
			Map<String,Object> farmUser = loginService.selectFarmUser(params);
			
			if(farmUser != null) {
				FarmdUserVo = FarmUserDetails.builder()
											 .naBzplc(farmUser.get("NA_BZPLC").toString())
											 .fhsIdNo(farmUser.get("FHS_ID_NO").toString())
											 .farmAmnno(Long.parseLong(farmUser.get("FARM_AMNNO").toString()))
											 .ftsnm(farmUser.get("FTSNM").toString())
											 .place(farmUser.get("NA_BZPLCNO").toString())
											 .build();
			}
			else {
				throw new UsernameNotFoundException("[" + username + "] 1.로그인 정보를 찾을 수 없습니다.");
			}
		}catch (RuntimeException re) {
			log.error("RuntimeException: FarmUserDetailsServiceImpl ",re);			
			throw new UsernameNotFoundException("[" + username + "] 로그인 정보를 찾을 수 없습니다.");
		} catch (SQLException se) {
			log.error("SQLException: FarmUserDetailsServiceImpl ",se);			
			throw new UsernameNotFoundException("[" + username + "] 로그인 정보를 찾을 수 없습니다.");
		}
		
		log.debug("##### FarmUserDetailsServiceImpl.loadUserByUsername [e]");
		return FarmdUserVo;
	}

}
