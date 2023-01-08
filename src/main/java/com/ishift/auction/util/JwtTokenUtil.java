package com.ishift.auction.util;


import io.jsonwebtoken.ClaimJwtException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import com.ishift.auction.vo.AdminJwtTokenVo;
import com.ishift.auction.vo.JwtTokenVo;
import java.util.*;

/**
 * Jwt 관련 기능을 제공
 * @author Yuchan
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class JwtTokenUtil {

	final private String SIGNING_KEY = Constants.JwtConstants.NH_AUCTION_CERT_CREATE_KEY;
	final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS512;

	/**
	 * jwt 생성 및 리턴
	 * @param jwtTokenVo
	 * @param tokenType
	 * @return
	 */
	public String generateToken(JwtTokenVo jwtTokenVo, String tokenType) {
		Date now = new Date();
		JwtBuilder builder = Jwts.builder()
								.setHeader(createHeader())
								.setIssuedAt(now)
								.setClaims(createClaims(jwtTokenVo))
								.setExpiration(createExpiredDate(tokenType))
								.signWith(signatureAlgorithm, Constants.JwtConstants.NH_AUCTION_CERT_CREATE_KEY);
		return builder.compact();
	}
	
	/**
	 * jwt 생성에 필요한 헤더 정보
	 * @return Header 정보
	 */
	private Map<String, Object> createHeader() {
		Map<String, Object> header = new HashMap<>();
		header.put(Header.TYPE, Header.JWT_TYPE);
		header.put("alg", SignatureAlgorithm.HS512);
		return header;
	}
	// 
	/**
	 * jwtTokenVo에 있는 정보들을 Map에 담아 리턴한다.
	 * @param jwtTokenVo 
	 * @return Claim 정보
	 */
	private Map<String, Object> createClaims(JwtTokenVo jwtTokenVo) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(Constants.JwtConstants.JWT_CLAIM_USER_MEM_NUM, jwtTokenVo.getUserMemNum());
		claims.put(Constants.JwtConstants.JWT_CLAIM_AUCTION_HOUSE_CODE, jwtTokenVo.getAuctionHouseCode());
		claims.put(Constants.JwtConstants.JWT_CLAIM_USER_ROLE, jwtTokenVo.getUserRole());
		claims.put(Constants.JwtConstants.JWT_CLAIM_ISSUED_AT, new Date());
		claims.put(Constants.JwtConstants.JWT_CLAIM_MB_INTG_NO, jwtTokenVo.getMbIntgNo());
//		claims.put(Constants.JwtConstants.JWT_CLAIM_DEVICE_UUID, jwtTokenVo.getDeviceUUID());
//		claims.put(Constants.JwtConstants.JWT_CLAIM_USER_NAME, jwtTokenVo.getUserName());
//		claims.put(Constants.JwtConstants.JWT_CLAIM_USER_BIRTHDATE, jwtTokenVo.getUserBirthdate());
//		claims.put(Constants.JwtConstants.JWT_CLAIM_ENTRY_NUM, jwtTokenVo.getEntryNum());
//		claims.put(Constants.JwtConstants.JWT_CLAIM_AUCTION_CLASS, jwtTokenVo.getAuctionClass());
//		claims.put(Constants.JwtConstants.JWT_CLAIM_AUCTION_TYPE, jwtTokenVo.getAuctionType());
		return claims;
	}
	private Map<String, Object> createClaimsForAdmin(AdminJwtTokenVo jwtTokenVo) {
		Map<String, Object> claims = new HashMap<>();
		claims.put(Constants.JwtConstants.JWT_CLAIM_USER_MEM_NUM, jwtTokenVo.getUserMemNum());
		claims.put(Constants.JwtConstants.JWT_CLAIM_AUCTION_HOUSE_CODE, jwtTokenVo.getAuctionHouseCode());
		claims.put(Constants.JwtConstants.JWT_CLAIM_USER_ROLE, jwtTokenVo.getUserRole());
	
    	claims.put("userId", jwtTokenVo.getUserId());
    	claims.put("password", jwtTokenVo.getPassword());
    	claims.put("eno", jwtTokenVo.getEno());
    	claims.put("userCusName", jwtTokenVo.getUserCusName());
    	claims.put("na_bzplc", jwtTokenVo.getNa_bzplc());
    	claims.put("security", jwtTokenVo.getSecurity());
    	claims.put("apl_ed_dtm", jwtTokenVo.getApl_ed_dtm());
    	claims.put("na_bzplnm", jwtTokenVo.getNa_bzplnm());
    	claims.put("grp_c", jwtTokenVo.getGrp_c());
		claims.put(Constants.JwtConstants.JWT_CLAIM_ISSUED_AT, new Date());
//		claims.put(Constants.JwtConstants.JWT_CLAIM_DEVICE_UUID, jwtTokenVo.getDeviceUUID());
//		claims.put(Constants.JwtConstants.JWT_CLAIM_USER_NAME, jwtTokenVo.getUserName());
//		claims.put(Constants.JwtConstants.JWT_CLAIM_USER_BIRTHDATE, jwtTokenVo.getUserBirthdate());
//		claims.put(Constants.JwtConstants.JWT_CLAIM_ENTRY_NUM, jwtTokenVo.getEntryNum());
//		claims.put(Constants.JwtConstants.JWT_CLAIM_AUCTION_CLASS, jwtTokenVo.getAuctionClass());
//		claims.put(Constants.JwtConstants.JWT_CLAIM_AUCTION_TYPE, jwtTokenVo.getAuctionType());
		return claims;
	}
	
	/**
	 * 토큰 만료 일시를 답아 리턴한다.
	 * token 타입(access, refresh)에 따라 만료 일시가 다르지만 농협 경매 시스템에서는 access token만 사용할 예정
	 * @return 토큰 만료 일시
	 */
	private Date createExpiredDate(String tokenType) {
		Date date = new Date();
		if (Constants.JwtConstants.ACCESS_TOKEN.equals(tokenType)) {
			// access token의 만료 일시는 당일 23시 59분 59초까지만 유효
			Calendar calendar = Calendar.getInstance();
			calendar.set(calendar.get(Calendar.YEAR)
						, calendar.get(Calendar.MONTH)
						, calendar.get(Calendar.DATE)
						, 23
						, 59
						, 59);
			date = calendar.getTime();
		}
		else if (Constants.JwtConstants.REFRESH_TOKEN.equals(tokenType)) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(calendar.get(Calendar.YEAR) + 1
					, calendar.get(Calendar.MONTH)
					, calendar.get(Calendar.DATE)
					, 23
					, 59
					, 59);
			date = calendar.getTime();
		}
		else {
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.MINUTE, 3);
			date = calendar.getTime();
		}
		log.debug("{} expire {} ", tokenType, date);
		return date;
	}
	
	/**
	 * @param token
	 * @return claim 정보
	 */
	public Claims getClaims(String token) {
		try {
			return Jwts.parser()
						.setSigningKey(SIGNING_KEY)
						.parseClaimsJws(token)
						.getBody();
		}
		catch (PrematureJwtException exception) {
			log.error("[Token PrematureJwtException] 접근이 허용되기 전인 JWT가 수신된 경우");
			return null;
		}
		catch (MalformedJwtException exception) {
			log.error("[Token MalformedJwtException] 구조적인 문제가 있는 JWT인 경우");
			return null;
		}
		catch (ExpiredJwtException exception) {
			log.error("[Token ExpiredJwtException] 토큰의 기한 만료");
			return null;
		}
		catch (ClaimJwtException exception) {
			log.error("[Token ClaimJwtException] JWT Claim 검사가 실패했을 때");
			return null;
		}
		catch (UnsupportedJwtException exception) {
			log.error("[Token UnsupportedJwtException] 암호화된 JWT를 사용하는 애프리케이션에 암호화되지 않은 JWT가 전달되는 경우");
			return null;
		}
		catch (JwtException exception) {
			log.error("[Token JwtException] Token Tampered");
			return null;
		}
		catch (NullPointerException exception) {
			log.error("[Token NullPointerException] Token is null");
			return null;
		}
	}
	
	/**
	 * Claim에 있는 개별 정보 가져온다.
	 * @param token
	 * @param key
	 * @return
	 */
	public String getValue(String token, String key) {
		if (this.getClaims(token) == null || this.getClaims(token).get(key) == null) {
			return "";
		}
		return this.getClaims(token).get(key).toString();
	}
	
	/**
	 * 토큰의 유효성, 만료일자 체크
	 * @param token
	 * @return 토큰의 유효성 여부
	 */
	public boolean isValidToken(String token) {
		try {
			Claims claims = getClaims(token);
			return !claims.getExpiration().before(new Date());
		}
		catch (PrematureJwtException exception) {
			log.error("[Token PrematureJwtException] 접근이 허용되기 전인 JWT가 수신된 경우");
			return false;
		} catch (MalformedJwtException exception) {
			log.error("[Token MalformedJwtException] 구조적인 문제가 있는 JWT인 경우");
			return false;
		} catch (ExpiredJwtException exception) {
			log.error("[Token ExpiredJwtException] 토큰의 기한 만료");
			return false;
		} catch (ClaimJwtException exception) {
			log.error("[Token ClaimJwtException] JWT Claim 검사가 실패했을 때");
			return false;
		} catch (UnsupportedJwtException exception) {
			log.error("[Token UnsupportedJwtException] 암호화된 JWT를 사용하는 애프리케이션에 암호화되지 않은 JWT가 전달되는 경우");
			return false;
		} catch (JwtException exception) {
			log.error("[Token JwtException] Token Tampered");
			return false;
		} catch (NullPointerException exception) {
			log.error("[Token NullPointerException] Token is null");
			return false;
		}
	}
	
	/**
	 * 토큰을 TokenVo 형태로 반환
	 * @param token
	 * @return JwtTokenVo
	 */
	public JwtTokenVo getTokenVo(String token) {
		if (!this.isValidToken(token)) {
			return null;
		}
		else {
			return JwtTokenVo.builder()
							.auctionHouseCode(this.getValue(token, Constants.JwtConstants.JWT_CLAIM_AUCTION_HOUSE_CODE))
							.userMemNum(this.getValue(token, Constants.JwtConstants.JWT_CLAIM_USER_MEM_NUM))
							.userRole(this.getValue(token, Constants.JwtConstants.JWT_CLAIM_USER_ROLE))
							.mbIntgNo(this.getValue(token, Constants.JwtConstants.JWT_CLAIM_MB_INTG_NO))
							.build();
		}
	}
	
	public String generateToken(AdminJwtTokenVo jwtTokenVo, String tokenType) {
		// TODO Auto-generated method stub
		Date now = new Date();
		JwtBuilder builder = Jwts.builder()
								.setHeader(createHeader())
								.setIssuedAt(now)
								.setClaims(createClaimsForAdmin(jwtTokenVo))
								.setExpiration(createExpiredDate(tokenType))
								.signWith(signatureAlgorithm, SIGNING_KEY);
		return builder.compact();
	}
	
}