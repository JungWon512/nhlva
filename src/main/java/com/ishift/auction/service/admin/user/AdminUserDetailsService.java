package com.ishift.auction.service.admin.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.ishift.auction.vo.AdminUserDetails;

/**
 * @author Yuchan
 */
public interface AdminUserDetailsService extends UserDetailsService {

	public UserDetails loadUserByUsername(String userNum) throws UsernameNotFoundException;

	public AdminUserDetails loadUserByUsername(String name, String password) throws UsernameNotFoundException;

}
