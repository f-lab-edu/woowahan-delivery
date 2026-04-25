package com.dorkem.food.common.filter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.dorkem.food.common.exception.ErrorCode;
import com.dorkem.food.common.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

	private final JwtProvider jwtProvider;
	private final ObjectMapper objectMapper;

	private static final List<String> WHITE_LIST = List.of(
		"/api/v1/users/auth/signup",
		"/api/v1/users/auth/login",
		"/api/v1/users/auth/refresh",
		"/api/v1/users/oauth",
		"/api/v1/owner/auth/login",
		"/v3/api-docs",
		"/swagger-ui",
		"/h2-console",
		"/api/v1/stores",
		"/actuator"
	);

	@Override
	protected void doFilterInternal(
		HttpServletRequest request,
		HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {

		String path = request.getRequestURI();

		if (isWhiteList(path)) {
			filterChain.doFilter(request, response);
			return;
		}

		String token = resolveToken(request);

		// 해시 검증
		if (token == null || !jwtProvider.validateToken(token)) {
			sendErrorResponse(response, ErrorCode.INVALID_TOKEN_ERROR, path);
			return;
		}

		// 프로퍼티 까기
		Long userId = jwtProvider.getUserIdFromToken(token);
		String role = jwtProvider.getRoleFromToken(token);

		request.setAttribute("userId", userId);
		request.setAttribute("role", role);

		// 일반 유저일 경우 null 반환
		Long storeId = jwtProvider.getStoreIdFromToken(token);
		if (storeId != null) {
			request.setAttribute("storeId", storeId);
		}

		filterChain.doFilter(request, response);
	}

	private boolean isWhiteList(String path) {
		return WHITE_LIST.stream().anyMatch(path::startsWith);
	}

	private String resolveToken(HttpServletRequest request) {
		String bearer = request.getHeader("Authorization");
		if (bearer != null && bearer.startsWith("Bearer ")) {
			return bearer.substring(7);
		}
		return null;
	}

	private void sendErrorResponse(HttpServletResponse response, ErrorCode errorCode, String path) throws IOException {
		response.setStatus(errorCode.getHttpStatus().value());
		response.setContentType("application/json;charset=UTF-8");

		Map<String, Object> errorBody = new HashMap<>();
		errorBody.put("status", errorCode.getHttpStatus().value());
		errorBody.put("code", errorCode.getCode());
		errorBody.put("message", errorCode.getMessage());
		errorBody.put("path", path);

		response.getWriter().write(objectMapper.writeValueAsString(errorBody));
	}
}
