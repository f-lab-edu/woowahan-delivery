package com.dorkem.food.user.dto.response;

import com.dorkem.food.user.entity.User;

public record UserProfileResponse(
	Long userId,
	String email,
	String userAccount,
	String username,
	String phoneNumber,
	String userProfile,
	boolean isOAuthUser
) {
	public static UserProfileResponse getUserInfo(User user) {
		return new UserProfileResponse(
			user.getUserId(),
			user.getEmail(),
			user.getUserAccount(),
			user.getUsername(),
			user.getPhoneNumber(),
			user.getUserProfile(),
			user.isOAuthUser()
		);
	}
}
