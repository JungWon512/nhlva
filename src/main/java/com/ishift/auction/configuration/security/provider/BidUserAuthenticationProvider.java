package com.ishift.auction.configuration.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ishift.auction.configuration.security.token.BidUserAuthenticationToken;
import com.ishift.auction.service.user.BidUserDetailsService;
import com.ishift.auction.vo.BidUserDetails;

import lombok.extern.slf4j.Slf4j;

/**
 * 중도매인 인증에 사용할 provider
 * @author Yuchan
 */
@Slf4j
//@Component
@Deprecated
public class BidUserAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private BidUserDetailsService bidUserDetailsService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

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
		
		final BidUserDetails bidUserDetails = (BidUserDetails)bidUserDetailsService.loadUserByUsername(authentication.getName());
		final String password = authentication.getCredentials() == null ? "" : authentication.getCredentials().toString();
		
		if (bidUserDetails == null) {
			throw new InternalAuthenticationServiceException("아이디 또는 패스워드를 확인해주세요.");
		}
		
		log.debug("encode pw : {}", passwordEncoder.encode(password));
//		if (!passwordEncoder.matches(password, bidUserDetails.getPw())) {
//			throw new BadCredentialsException("아이디 또는 패스워드를 확인해주세요.");
//		}
		
		// 인증 완료된 토큰을 리턴한다.
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(bidUserDetails, null, bidUserDetails.getAuthorities());
		auth.setDetails(authentication.getDetails());
		return auth;
	}

	/**
	 * BidUserAuthenticationToken class로 요청을 한 경우만 이 provider를 사용한다.
	 */
	@Override
	public boolean supports(Class<?> authentication) {
		return BidUserAuthenticationToken.class.equals(authentication);
	}
	
}
