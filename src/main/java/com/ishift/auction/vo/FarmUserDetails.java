package com.ishift.auction.vo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ishift.auction.util.Constants;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class FarmUserDetails implements UserDetails {

	private static final long serialVersionUID = 683459936054791622L;

	private String naBzplc;				// 경제통합사업장코드
	private String fhsIdNo;				// 농가식별번호
	private Long farmAmnno;				// 농장관리번호
	private String naTrplC;				// 경제통합거래처코드
	private String ftsnm;				// 농가명
	private String zip;					// 우편번호
	private String dongup;				// 동이상주소
	private String dongbw;				// 동이하주소
	private String ohseTelno;			// 자택전화번호
	private String cusMpno;				// 고객휴대전화번호
	private String macoYn;				// 조합원여부
	private String rmkCntn;				// 비고내용
	private String jrdwoDsc;			// 관내외구분코드
	private String delYn;				// 삭제여부
	private String anwYn;				// 신규여부
	private String hdwkRgYn;			// 수기등록여부
	private String sraFarmAcno;			// 축산농장계좌번호
	private String newSraFarmAcno;		// 뉴축산농장계좌번호
	private String sraFedSpyYn;			// 축산사료사용여부
	private String birth;				// 생년월일
	private LocalDateTime fsrgDtm;		// 최초등록일시
	private String fsrgmnEno;			// 최초등록자개인번호
	private LocalDateTime lschgDtm;		// 최종변경일시
	private String lsCmeno;				// 최초변경자개인번호
	private String place;				// 조합 번호(TB_LA_IS_BM_BZLOC 참조)
	
	@Builder
	public FarmUserDetails(String naBzplc, String fhsIdNo, Long farmAmnno, String naTrplC
						, String ftsnm, String zip, String dongup, String dongbw
						, String ohseTelno, String cusMpno, String macoYn, String rmkCntn
						, String jrdwoDsc, String delYn, String anwYn, String hdwkRgYn
						, String sraFarmAcno, String newSraFarmAcno, String sraFedSpyYn, String birth
						, String fsrgmnEno, String lsCmeno, LocalDateTime fsrgDtm, LocalDateTime lschgDtm, String place) {
		this.naBzplc = naBzplc;
		this.fhsIdNo = fhsIdNo;
		this.farmAmnno = farmAmnno;
		this.naTrplC = naTrplC;
		this.ftsnm = ftsnm;
		this.zip = zip;
		this.dongup = dongup;
		this.dongbw = dongbw;
		this.ohseTelno = ohseTelno;
		this.cusMpno = cusMpno;
		this.macoYn = macoYn;
		this.rmkCntn = rmkCntn;
		this.jrdwoDsc = jrdwoDsc;
		this.delYn = delYn;
		this.anwYn = anwYn;
		this.hdwkRgYn = hdwkRgYn;
		this.sraFarmAcno = sraFarmAcno;
		this.newSraFarmAcno = newSraFarmAcno;
		this.sraFedSpyYn = sraFedSpyYn;
		this.birth = birth;
		this.fsrgmnEno = fsrgmnEno;
		this.lsCmeno = lsCmeno;
		this.fsrgDtm = fsrgDtm;
		this.lschgDtm = lschgDtm;
		this.place = place;
	}

	// 중도매인 기본 역할은 BIDDER
	public Collection<? extends GrantedAuthority> roleList() {
		List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
		String[] authStrings = {Constants.UserRole.ROLE_FARM};
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
		return this.ohseTelno + "_" + this.cusMpno;
	}

	/**
	 * 조합코드 + 농가식별번호
	 */
	@Override
	public String getUsername() {
		return this.naBzplc + "_" + this.fhsIdNo;
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
