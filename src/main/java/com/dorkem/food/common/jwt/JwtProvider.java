package com.dorkem.food.common.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtProvider {

	private static final String CLAIM_ROLE = "role";
	private static final String CLAIM_STORE_ID = "storeId";

	@Value("${jwt.secret}")
	private String SECRET_KEY;

	@Value("${jwt.access-token-expiration}")
	private long ACCESS_TOKEN_TIME;

	@Value("${jwt.refresh-token-expiration}")
	private long REFRESH_TOKEN_TIME;

	public String createAccessToken(Long userId, String role) {
		return createToken(String.valueOf(userId), role, ACCESS_TOKEN_TIME);
	}

	public String createOwnerAccessToken(Long userId, String role, Long storeId) {
		return createOwnerToken(String.valueOf(userId), role, storeId, ACCESS_TOKEN_TIME);
	}

	public String createRefreshToken(Long userId, String role) {
		return createToken(String.valueOf(userId), role, REFRESH_TOKEN_TIME);
	}

	private String createToken(String userId, String role, long tokenValidTime) {
		return Jwts.builder()
			.setSubject(userId)
			.claim(CLAIM_ROLE, role)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
			.compact();
	}

	private String createOwnerToken(String userId, String role, Long storeId, long tokenValidTime) {
		return Jwts.builder()
			.setSubject(String.valueOf(userId))
			.claim(CLAIM_ROLE, role)
			.claim(CLAIM_STORE_ID, storeId)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
			.compact();
	}

	public Long getUserIdFromToken(String token) {
		return Long.parseLong(getClaims(token).getSubject());
	}

	public String getRoleFromToken(String token) {
		return getClaims(token).get(CLAIM_ROLE, String.class);
	}

	public Long getStoreIdFromToken(String token) {
		return getClaims(token).get(CLAIM_STORE_ID, Long.class);
	}

	private Claims getClaims(String token) {
		return Jwts.parserBuilder()
			.setSigningKey(SECRET_KEY.getBytes())
			.build()
			.parseClaimsJws(token)
			.getBody();
	}

	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
				.setSigningKey(SECRET_KEY.getBytes())
				.build()
				.parseClaimsJws(token);
			return true;
		} catch (JwtException | IllegalArgumentException e) {
			return false;
		}
	}
}
