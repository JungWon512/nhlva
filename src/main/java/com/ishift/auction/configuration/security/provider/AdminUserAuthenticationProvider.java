package com.ishift.auction.configuration.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ishift.auction.configuration.security.token.AdminUserAuthenticationToken;
import com.ishift.auction.service.admin.user.AdminUserDetailsService;
import com.ishift.auction.vo.AdminUserDetails;

import lombok.extern.slf4j.Slf4j;

/**
 * 관리자 인증에 사용할 provider
 * @author Yuchan
 */
@Slf4j
@Component
public class AdminUserAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private AdminUserDetailsService adminUserDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Value("${spring.profiles.service-name:nhlva}")
	private String serviceName;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		
		if (authentication == null) {
			throw new InternalAuthenticationServiceException("아이디와 패스워드를 입력하세요.");
		}
		
		if (authentication.getName() == null || "".equals(authentication.getName())) {
			throw new BadCredentialsException("아이디를 입력해주세요.");
		}
		
		if (authentication.getCredentials() == null || "".equals(authentication.getCredentials())) {
			throw new BadCredentialsException("패스워드를 입력해주세요.");
		}

		AdminUserDetails adminUserDetails = null;

		final String password = authentication.getCredentials() == null ? "" : authentication.getCredentials().toString();		
		if ("tibero".equals(serviceName)) {
			adminUserDetails = (AdminUserDetails)adminUserDetailsService.loadUserByUsername(authentication.getName());
		
		if (adminUserDetails == null) {
			throw new InternalAuthenticationServiceException("아이디 또는 패스워드를 확인해주세요.");
		}
		
			log.debug("encode pw : {}", passwordEncoder.encode(password));
			if (!passwordEncoder.matches(password, adminUserDetails.getPw())) {
				throw new BadCredentialsException("아이디 또는 패스워드를 확인해주세요.");
			}
		}
		else {
			adminUserDetails = (AdminUserDetails)adminUserDetailsService.loadUserByUsername(authentication.getName(),password);
			if (adminUserDetails == null) {
				throw new InternalAuthenticationServiceException("아이디 또는 패스워드를 확인해주세요.");
			}
		}
		
		// 인증 완료된 토큰을 리턴한다.
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(adminUserDetails, null, adminUserDetails.getAuthorities());
		auth.setDetails(authentication.getDetails());
		return auth;
	}

	/**
	 * AdminUserAuthenticationToken class로 요청을 한 경우만 이 provider를 사용한다.
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return AdminUserAuthenticationToken.class.equals(authentication);
	}
	
}