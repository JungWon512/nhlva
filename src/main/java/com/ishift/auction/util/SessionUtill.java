package com.ishift.auction.util;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.ishift.auction.util.Constants.UserRole;
import com.ishift.auction.vo.AdminUserDetails;
import com.ishift.auction.vo.BidUserDetails;
import com.ishift.auction.vo.FarmUserDetails;

@Component
public class SessionUtill {

	public String getUserId() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth == null) return null;
		
		Collection<? extends GrantedAuthority> auths = auth.getAuthorities();
		if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_BIDDER))) {
			return ((BidUserDetails)auth.getPrincipal()).getTrmnAmnno().toString();
		}
		else if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_ADMIN))){
			return ((AdminUserDetails)auth.getPrincipal()).getUsrid();
		}
		else if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_FARM))){
			return ((FarmUserDetails)auth.getPrincipal()).getFhsIdNo();
		}
		return null;
	}
	
	public String getEno() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth == null) return null;
		
		Collection<? extends GrantedAuthority> auths = auth.getAuthorities();
		if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_BIDDER))) {
			return ((BidUserDetails)auth.getPrincipal()).getTrmnAmnno().toString();
		}
		else if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_ADMIN))){
			return ((AdminUserDetails)auth.getPrincipal()).getEno();
		}
		else if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_FARM))){
			return ((FarmUserDetails)auth.getPrincipal()).getFhsIdNo();
		}
		return null;
	}
	
	public String getNaBzplc() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth == null) return null;
		
		Collection<? extends GrantedAuthority> auths = auth.getAuthorities();
		if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_BIDDER))) {
			return ((BidUserDetails)auth.getPrincipal()).getNaBzplc();
		}
		else if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_ADMIN))){
			return ((AdminUserDetails)auth.getPrincipal()).getNaBzplc();
		}
		else if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_FARM))){
			return ((FarmUserDetails)auth.getPrincipal()).getNaBzplc();
		}
		return null;
	}

	public Object getUserVo() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth == null) return null;

		Collection<? extends GrantedAuthority> auths = auth.getAuthorities();
		if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_BIDDER))
				|| auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_ADMIN))
				|| auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_FARM))
				) {
			return auth.getPrincipal();
		}
		return null;
	}
	
	public String getRoleConfirm() {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth == null) return null;
		
		Collection<? extends GrantedAuthority> auths = auth.getAuthorities();
		if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_BIDDER))) {
			return "ROLE_BIDDER";
		}
		else if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_ADMIN))){
			return "ROLE_ADMIN";
		}
		else if(auths.contains(new SimpleGrantedAuthority(UserRole.ROLE_FARM))){
			return "ROLE_FARM";
		}
		return null;
	}
}
