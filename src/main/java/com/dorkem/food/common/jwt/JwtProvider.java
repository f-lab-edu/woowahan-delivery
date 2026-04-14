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
			.claim("role", role)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
			.compact();
	}

	private String createOwnerToken(String userId, String role, Long storeId, long tokenValidTime) {
		return Jwts.builder()
			.setSubject(String.valueOf(userId))
			.claim("role", role)
			.claim("storeId", storeId)
			.setIssuedAt(new Date())
			.setExpiration(new Date(System.currentTimeMillis() + tokenValidTime))
			.signWith(SignatureAlgorithm.HS256, SECRET_KEY.getBytes())
			.compact();
	}

	public Long getUserIdFromToken(String token) {
		return Long.parseLong(getClaims(token).getSubject());
	}

	public String getRoleFromToken(String token) {
		return getClaims(token).get("role", String.class);
	}

	public Long getStoreIdFromToken(String token) { return getClaims(token).get("storeId", Long.class); }

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
