package com.dorkem.food.common.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	UNSUPPORTED_OAUTH_PROVIDER(40000, HttpStatus.BAD_REQUEST, "지원하지 않는 소셜 로그인입니다."),
	INVALID_CURRENT_PASSWORD(40001, HttpStatus.BAD_REQUEST, "현재 비밀번호가 일치하지 않습니다."),
	DUPLICATED_NEW_PASSWORD(40002, HttpStatus.BAD_REQUEST, "현재 비밀번호와 동일한 비밀번호로 변경할 수 없습니다."),
	FAILURE_LOGIN(40100, HttpStatus.UNAUTHORIZED, "잘못된 아이디 또는 비밀번호입니다."),
	EXPIRED_TOKEN_ERROR(40101, HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
	INVALID_TOKEN_ERROR(40102, HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
	FORBIDDEN_OAUTH_PASSWORD_CHANGE(40301, HttpStatus.FORBIDDEN, "소셜 로그인 사용자는 비밀번호를 변경할 수 없습니다."),
	NOT_FOUND_STORE_IN_TOKEN(40103, HttpStatus.UNAUTHORIZED, "토큰에 가게 정보가 없습니다."),
	FORBIDDEN_ORDER_ACCESS(40302, HttpStatus.FORBIDDEN, "주문에 접근 권한이 없습니다."),
	FORBIDDEN_STORE_ACCESS(40303, HttpStatus.FORBIDDEN, "해당 가게에 접근 권한이 없습니다."),
	NOT_FOUND_CUSTOMER(40401, HttpStatus.NOT_FOUND, "해당 고객을 찾을 수 없습니다."),
	NOT_FOUND_USER(40402, HttpStatus.NOT_FOUND, "해당 유저를 찾을 수 없습니다."),
	NOT_FOUND_OWNER(40403, HttpStatus.NOT_FOUND, "해당 점주를 찾을 수 없습니다."),
	NOT_FOUND_STORE(40404, HttpStatus.NOT_FOUND, "해당 가게를 찾을 수 없습니다."),
	NOT_FOUND_MENU(40405, HttpStatus.NOT_FOUND, "해당 메뉴를 찾을 수 없습니다."),
	NOT_FOUND_ORDER(40406, HttpStatus.NOT_FOUND, "해당 주문을 찾을 수 없습니다."),
	NOT_FOUND_CART(40407, HttpStatus.NOT_FOUND, "해당 장바구니를 찾을 수 없습니다."),
	NOT_FOUND_CART_ITEM(40408, HttpStatus.NOT_FOUND, "해당 장바구니 메뉴를 찾을 수 없습니다."),
	NOT_FOUND_CURRENT_ORDER(40407, HttpStatus.NOT_FOUND, "현재 진행 중인 주문이 없습니다."),
	NOT_FOUND_ORDER_HISTORY(40408, HttpStatus.NOT_FOUND, "주문 내역을 찾을 수 없습니다."),
	DUPLICATED_EMAIL(40901, HttpStatus.CONFLICT, "이미 가입된 이메일입니다."),
	DUPLICATED_PHONE_NUMBER(40902, HttpStatus.CONFLICT, "이미 가입된 휴대폰 번호입니다."),
	INTERNAL_SERVER_ERROR(50000, HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러입니다.");

	private final Integer code;
	private final HttpStatus httpStatus;
	private final String message;
}
