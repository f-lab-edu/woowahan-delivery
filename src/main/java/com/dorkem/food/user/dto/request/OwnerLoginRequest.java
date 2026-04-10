package com.dorkem.food.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "오너 로그인 요청")
public record OwnerLoginRequest (

	@Schema(description = "이메일")
	String email,

	@Schema(description = "비밀번호")
	String password,

	@Schema(description = "가게 ID")
	Long storeId
) {}
