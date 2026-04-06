package com.dorkem.food.user.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record UpdatePasswordRequest(

	@Schema(description = "현재 비밀번호")
	String currentPassword,

	@Schema(description = "새 비밀번호")
	String newPassword
) {
}
