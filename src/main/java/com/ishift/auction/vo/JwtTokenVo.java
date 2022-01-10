package com.ishift.auction.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtTokenVo {
	
	// 이름
	@Deprecated
	private String userName;
	
	// 생년월일
	@Deprecated
	private String userBirthdate;
	
	// 참가번호
	@Deprecated
	private int entryNum;
	
	// 조합코드
	private String auctionHouseCode;
	
	// 디바이스 아이디
	@Deprecated
	private String deviceUUID;
	
	// 회원번호(중도메인코드)
	private String userMemNum;
	
	// 회원 권한 - 응찰(BIDDER), 농가(FARM), 일반(NORMAL), 관리자(ADMIN)
	// TODO :: enum 생성
	private String userRole;
	
	// 경매구분 - 큰소(C100), 일반송아지(C200), 현통송아지(C300), 기타(C400)
	@Deprecated
	private String auctionClass;
	
	// 단일/일괄 경매 구분
	@Deprecated
	private String auctionType;
	
	@Builder
	public JwtTokenVo(String userName, String userBirthdate, int entryNum
					, String auctionHouseCode, String deviceUUID, String userMemNum
					, String userRole, String auctionClass, String auctionType) {
//		this.userName = userName;
//		this.userBirthdate = userBirthdate;
//		this.entryNum = entryNum;
		this.auctionHouseCode = auctionHouseCode;
//		this.deviceUUID = deviceUUID;
		this.userMemNum = userMemNum;
		this.userRole = userRole;
//		this.auctionClass = auctionClass;
//		this.auctionType = auctionType;
	}	
}
