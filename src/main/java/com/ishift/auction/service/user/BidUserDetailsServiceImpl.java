package com.ishift.auction.service.user;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ishift.auction.service.login.LoginService;
import com.ishift.auction.vo.BidUserDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class BidUserDetailsServiceImpl implements BidUserDetailsService {

	@Autowired
	private LoginService loginService;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		log.debug("##### BidUserDetailsService.loadUserByUsername [s]");
		log.debug("arguments : {}", username);

		BidUserDetails biddUserVo = null;

		try {
			Map<String,Object> params = new HashMap<>();
			params.put("trmnAmnno", Long.parseLong(username.split("_")[0]));
			params.put("naBzplc", username.split("_")[1]);
			params.put("naBzplcno", username.split("_")[2]);
			Map<String,Object> wholesaler = loginService.selectWholesaler(params);
			
			if(wholesaler != null) {
				biddUserVo = BidUserDetails.builder()
										 .naBzplc(wholesaler.get("NA_BZPLC").toString())
										 .cusRlno(wholesaler.get("CUS_RLNO").toString())
										 .sraMwmnnm(wholesaler.get("SRA_MWMNNM").toString())
										 .trmnAmnno(Long.parseLong(wholesaler.get("TRMN_AMNNO").toString()))
										 .place(wholesaler.get("NA_BZPLCNO").toString())
										 .build();
			}
			else {
				throw new UsernameNotFoundException("[" + username + "] 1.로그인 정보를 찾을 수 없습니다.");
			}
		}catch (RuntimeException re) {
			log.error("RuntimeException: BidUserDetailsService ",re);			
			throw new UsernameNotFoundException("[" + username + "] 로그인 정보를 찾을 수 없습니다.");
		} catch (SQLException se) {
			log.error("SQLException: BidUserDetailsService ",se);			
			throw new UsernameNotFoundException("[" + username + "] 로그인 정보를 찾을 수 없습니다.");
		}
		
		log.debug("##### BidUserDetailsService.loadUserByUsername [e]");
		return biddUserVo;
	}

}
