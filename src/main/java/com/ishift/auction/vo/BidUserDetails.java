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

/**
 * 중도매인 정보 ( TB_LA_IS_MM_MWMM )
 * @author Yuchan
 */
@Getter
@ToString
@NoArgsConstructor
public class BidUserDetails implements UserDetails{

	private static final long serialVersionUID = -246165231898300898L;

	private String naBzplc;				// 경제 통합 사업장 코드
	private Long trmnAmnno;				// 거래인 관리 번호
	private String mwmnNaTrplC;			// 중도매인 경제 통합 거래처 코드
	private String sraMwmnnm;			// 축산 중도매인명
	private String frlno;				// 실명번호 앞자리
	private String zip;					// 우편번호
	private String dongup;				// 기본 주소(동 이상)
	private String dongbw;				// 상세 주소(동 이하)
	private String ohseTelno;			// 자택 전화번호
	private String cusMpno;				// 고객 휴대전화 번호
	private String macoYn;				// 조합원 여부
	private String jrdwoDsc;			// 관내외 구분 코드
	private String psnInfOfrAgrYn;		// 개인정보 제공 동의 여부
	private String tmsYn;				// 전송 여부
	private String delYn;				// 삭제 여부
	private String rmkCntn;				// 비고내용
	private String cusRlno;				// 고객 실명번호
	private String fsrgmnEno;			// 최초 등록자 개인번호
	private String lsCmeno;				// 최종 변경자 개인번호
	private LocalDateTime fsrgDtm;		// 최초 등록 일시
	private LocalDateTime lschgDtm;		// 최종 변경 일시
	private String place;				// 조합 번호(TB_LA_IS_BM_BZLOC 참조)

	@Builder
	public BidUserDetails(String naBzplc, Long trmnAmnno, String mwmnNaTrplC, String sraMwmnnm
						, String frlno, String zip, String dongup, String dongbw
						, String ohseTelno, String cusMpno, String macoYn, String jrdwoDsc
						, String psnInfOfrAgrYn, String tmsYn, String delYn, String rmkCntn
						, String cusRlno, String fsrgmnEno, String lsCmeno
						, LocalDateTime fsrgDtm, LocalDateTime lschgDtm, String place) {
		this.naBzplc = naBzplc;
		this.trmnAmnno = trmnAmnno;
		this.mwmnNaTrplC = mwmnNaTrplC;
		this.sraMwmnnm = sraMwmnnm;
		this.frlno = frlno;
		this.zip = zip;
		this.dongup = dongup;
		this.dongbw = dongbw;
		this.ohseTelno = ohseTelno;
		this.cusMpno = cusMpno;
		this.macoYn = macoYn;
		this.jrdwoDsc = jrdwoDsc;
		this.psnInfOfrAgrYn = psnInfOfrAgrYn;
		this.tmsYn = tmsYn;
		this.delYn = delYn;
		this.rmkCntn = rmkCntn;
		this.cusRlno = cusRlno;
		this.fsrgmnEno = fsrgmnEno;
		this.lsCmeno = lsCmeno;
		this.fsrgDtm = fsrgDtm;
		this.lschgDtm = lschgDtm;
		this.place = place;
	}
	
	// 중도매인 기본 역할은 BIDDER
	public Collection<? extends GrantedAuthority> roleList() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		String[] authStrings = {Constants.UserRole.ROLE_BIDDER};
		for(String authString : authStrings) {
			authorities.add(new SimpleGrantedAuthority(authString));
		}
		return authorities;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.roleList();
	}

	/**
	 * 생년월일
	 */
	@Override
	public String getPassword() {
		return this.cusRlno;
	}

	/**
	 * 조합코드 + 중도매인 관리 번호
	 */
	@Override
	public String getUsername() {
		return this.naBzplc + "_" + this.trmnAmnno;
	}

	/**
	 * 삭제 여부가 0인 경우 정상
	 */
	@Override
	public boolean isAccountNonExpired() {
		return "0".equals(this.delYn);
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return this.isAccountNonExpired();
	}

}
