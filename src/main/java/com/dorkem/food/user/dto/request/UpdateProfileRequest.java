package com.dorkem.food.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "프로필 수정 요청")
public record UpdateProfileRequest(

	@Schema(description = "닉네임")
	String username,

	@Schema(description = "전화번호")
	String phoneNumber
) {
}
