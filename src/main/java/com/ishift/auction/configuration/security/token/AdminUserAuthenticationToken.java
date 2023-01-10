package com.ishift.auction.configuration.security.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 관리자 인증 요청 토큰
 * @author Yuchan
 */
public class AdminUserAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 2546919953564501480L;

	private String username;			// 관리자 아이디
	private String password;			// 관리자 비밀번호

	public AdminUserAuthenticationToken(String username
										, String password
										, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.username = username;
		this.password = password;
		setAuthenticated(false);
	}

	public AdminUserAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
	}

	@Override
	public void eraseCredentials() {
		super.eraseCredentials();
		this.password = null;
	}
	
	@Override
	public Object getCredentials() {
		return this.password;
	}

	@Override
	public Object getPrincipal() {
		return this.username;
	}

}
