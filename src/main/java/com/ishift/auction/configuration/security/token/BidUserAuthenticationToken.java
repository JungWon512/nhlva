package com.ishift.auction.configuration.security.token;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 중도매인 인증 요청 토큰
 * @author Yuchan
 */
@Deprecated
public class BidUserAuthenticationToken extends AbstractAuthenticationToken {

	private static final long serialVersionUID = 5017911139532470282L;

	private String username;			// 중도매인 이름
	private String bzPlace;				// 조합 번호
	private String password;			// 중도매인 생년월일

	public BidUserAuthenticationToken(String username
									, String bzPlace
									, String password
									, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.username = username;
		this.bzPlace = bzPlace;
		this.password = password;
		setAuthenticated(false);
	}

	public BidUserAuthenticationToken(Collection<? extends GrantedAuthority> authorities) {
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
		return this.username + "_" + bzPlace;
	}

}
