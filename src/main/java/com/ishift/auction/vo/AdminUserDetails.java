package com.ishift.auction.vo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ishift.auction.util.Constants;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class AdminUserDetails implements UserDetails {

	private static final long serialVersionUID = 3527990713559597472L;

	private String naBzplc;				// 경제통합사업장코드
	private String usrid;				// 사용자ID
	private String usrnm;				// 사용자명
	private String pw;					// 비밀번호
	private String eno;					// 우편번호
	private String mpno;				// 우편번호
	private String strgYn;				// 동이하주소
	private int pwerrNt;				// 자택전화번호
	private LocalDateTime fsrgDtm;		// 최초등록일시
	private String fsrgmnEno;			// 최초등록자개인번호
	private LocalDateTime lschgDtm;		// 최종변경일시
	private String lsCmeno;				// 최초변경자개인번호
	private String place;
	private String naBzplNm;
	private String grpC;
	
	@Builder
	public AdminUserDetails(String naBzplc, String usrid, String usrnm, String pw
						, String eno, String mpno, String strgYn, int pwerrNt
						, String fsrgmnEno, String lsCmeno, LocalDateTime fsrgDtm, LocalDateTime lschgDtm
						, String place
						, String naBzplNm
						, String grpC) {
		this.naBzplc = naBzplc;
		this.usrid = usrid;
		this.usrnm = usrnm;
		this.pw = pw;
		this.eno = eno;
		this.mpno = mpno;
		this.strgYn = strgYn;
		this.pwerrNt = pwerrNt;
		this.fsrgmnEno = fsrgmnEno;
		this.lsCmeno = lsCmeno;
		this.fsrgDtm = fsrgDtm;
		this.lschgDtm = lschgDtm;
		this.place = place;
		this.naBzplNm = naBzplNm;
		this.grpC = grpC;
	}
	
	// 관리자 기본역할 ADMIN
	public Collection<? extends GrantedAuthority> roleList() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		String[] authStrings = {Constants.UserRole.ROLE_ADMIN};
		for(String authString : authStrings) {
			authorities.add(new SimpleGrantedAuthority(authString));
		}
		return authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roleList();
	}

	@Override
	public String getPassword() {
		return this.pw;
	}

	@Override
	public String getUsername() {
		return this.usrid;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.pwerrNt < 10;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.isAccountNonLocked();
	}

}
