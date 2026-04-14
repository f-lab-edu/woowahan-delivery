package com.dorkem.food.user.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long userId;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private UserRole role;

	@Getter
	@Column(name = "email", nullable = false, unique = true)
	private String email;

	@Getter
	@Column(name = "username", nullable = false)
	private String username;

	@Getter
	@Column(name = "user_account")
	private String userAccount;

	@Column(name = "password")
	private String password;

	@Getter
	@Column(name = "phone_number", unique = true)
	private String phoneNumber;

	@Getter
	@Column(name = "user_profile", nullable = false)
	private String userProfile;

	@Enumerated(EnumType.STRING)
	@Column(name = "provider")
	private OAuthProvider provider;

	@Column(name = "provider_id")
	private String providerId;

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "modified_at", nullable = false)
	private LocalDateTime modifiedAt;

	private User(String email, String username, OAuthProvider provider, String providerId, String userProfile) {
		this.email = email;
		this.username = username;
		this.provider = provider;
		this.providerId = providerId;
		this.userProfile = userProfile;
		this.role = UserRole.CUSTOMER;
	}

	private User(String email, String username, String userAccount, String password,
		String phoneNumber, String userProfile
	) {
		this.email = email;
		this.username = username;
		this.userAccount = userAccount;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.userProfile = userProfile;
		this.role = UserRole.CUSTOMER;
	}

	public static User createUser(String email, String username, String userAccount, String password,
		String phoneNumber, String userProfile
	) {
		return new User(email, username, userAccount, password, phoneNumber, userProfile);
	}

	public static User createOAuthUser(String email, String username, OAuthProvider provider
		, String providerId, String userProfile
	) {
		return new User(email, username, provider, providerId, userProfile);
	}

	public String getUserRole() {
		return this.role.name();
	}

	public boolean matchPassword(String inputPassword) {
		return this.password.equals(inputPassword);
	}

	public void updateProfile(String username, String phoneNumber) {
		if (username != null) this.username = username;
		if (phoneNumber != null) this.phoneNumber = phoneNumber;
	}

	public void updatePassword(String newPassword) {
		this.password = newPassword;
	}

	public void updateProfileImage(String userProfile) {
		this.userProfile = userProfile;
	}

	public boolean isOAuthUser() {
		return this.provider != null;
	}
}
