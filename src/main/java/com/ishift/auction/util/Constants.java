package com.ishift.auction.util;

/**
 * 프로젝트 상수 정의
 * @author Yuchan
 *
 */
public interface Constants {
	
	/**
	 * JWT 인증 관련
	 * @author Yuchan
	 *
	 */
	class JwtConstants {
		public static final String NH_AUCTION_CERT_CREATE_KEY = "NHAUCTIONishiftNHAUCTIONishiftNHAUCTIONishiftNHAUCTIONishiftNHAUCTIONishiftNHAUCTIONishiftNHAUCTIONishiftNHAUCTIONishiftNHAUCTIONishiftNHAUCTIONishift";

		public static final String SUBJECT = "user_auth";
		public static final String ISSUER = "IShift";
		public static final String AUDIENCE = "client";
		public static String SPLIT_TOKEN_SEPARATOR = "\\.";
		public static String BEARER = "Bearer ";
		public static String ACCESS_TOKEN = "access_token";
		public static String REFRESH_TOKEN = "refresh_token";

		// Claim 관련
		public static final String JWT_CLAIM_USER_MEM_NUM		= "userMemNum";				// 회원번호
		public static final String JWT_CLAIM_AUCTION_HOUSE_CODE	= "auctionHouseCode";		// 조합코드
		public static final String JWT_CLAIM_USER_ROLE			= "userRole";				// 사용자 권한(응찰, 농가, 일반, 관리자)

		// 1차에서 제외 [s]
		@Deprecated
		public static final String JWT_CLAIM_DEVICE_UUID		= "deviceUUID";				// DEVICE UUID
		@Deprecated
		public static final String JWT_CLAIM_USER_NAME			= "userName";				// 이름
		@Deprecated
		public static final String JWT_CLAIM_USER_BIRTHDATE		= "userBirthdate";			// 생년월일
		@Deprecated
		public static final String JWT_CLAIM_ENTRY_NUM			= "entryNum";				// 참가 번호
		@Deprecated
		public static final String JWT_CLAIM_AUCTION_CLASS		= "auctionClass";			// 경매 구분(큰소, 번식우, 송아지)
		@Deprecated
		public static final String JWT_CLAIM_AUCTION_TYPE		= "auctionType";			// 단일/일괄 경매 구분
		// 1차에서 제외 [e]
	}
	
	class UserRole {
		public static final String ROLE_BIDDER = "ROLE_BIDDER";
		public static final String ROLE_ADMIN = "ROLE_ADMIN";
		public static final String ROLE_FARM = "ROLE_FARM";
		public static final String ROLE_NORMAL = "ROLE_NORMAL";
		
		public static final String BIDDER = "BIDDER";
		public static final String ADMIN = "ADMIN";
		public static final String FARM = "FARM";
		public static final String NORMAL = "NORMAL";
		public static final String WATCHER = "WATCHER";
	}

}
