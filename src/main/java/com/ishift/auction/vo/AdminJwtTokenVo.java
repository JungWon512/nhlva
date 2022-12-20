package com.ishift.auction.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AdminJwtTokenVo {	
	// 조합코드
	private String auctionHouseCode;
	
	// 회원번호(중도메인코드)
	private String userMemNum;
	
	// 회원 권한 - 응찰(BIDDER), 농가(FARM), 일반(NORMAL), 관리자(ADMIN)
	// TODO :: enum 생성
	private String userRole;

	private String userId;
	private String password;
	private String eno;
	private String userCusName;
	private String na_bzplc;
	private String security;
	private String apl_ed_dtm;
	private String na_bzplnm;
	private String grp_c;
	
	@Builder
	public AdminJwtTokenVo(String auctionHouseCode, String userMemNum, String userRole
					, String userId, String password
					, String eno, String userCusName, String na_bzplc, String security
					, String apl_ed_dtm, String na_bzplnm, String grp_c
	) {
		this.auctionHouseCode = auctionHouseCode;
		this.userMemNum = userMemNum;
		this.userRole = userRole;

		this.userId = userId;
		this.password = password;
		this.eno = eno;
		this.userCusName = userCusName;
		this.na_bzplc = na_bzplc;
		this.security = security;
		this.apl_ed_dtm = apl_ed_dtm;
		this.na_bzplnm  = na_bzplnm;
		this.grp_c = grp_c;
	}	
}
