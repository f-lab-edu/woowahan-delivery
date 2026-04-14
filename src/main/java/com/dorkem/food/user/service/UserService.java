package com.dorkem.food.user.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dorkem.food.common.config.S3Properties;
import com.dorkem.food.common.exception.CommonException;
import com.dorkem.food.common.exception.ErrorCode;
import com.dorkem.food.common.jwt.JwtProvider;
import com.dorkem.food.oauth.entity.client.OAuthClient;
import com.dorkem.food.oauth.entity.info.OAuthUserInfo;
import com.dorkem.food.user.dto.request.LoginRequest;
import com.dorkem.food.user.dto.request.RefreshTokenRequest;
import com.dorkem.food.user.dto.request.SignupRequest;
import com.dorkem.food.user.dto.request.UpdatePasswordRequest;
import com.dorkem.food.user.dto.request.UpdateProfileRequest;
import com.dorkem.food.user.dto.response.AccessTokenResponse;
import com.dorkem.food.user.dto.response.LoginResponse;
import com.dorkem.food.user.dto.response.UserProfileResponse;
import com.dorkem.food.user.entity.Customer;
import com.dorkem.food.user.entity.User;
import com.dorkem.food.user.entity.auth.RefreshToken;
import com.dorkem.food.user.repository.CustomerRepository;
import com.dorkem.food.user.repository.RefreshTokenRepository;
import com.dorkem.food.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

	private final UserRepository userRepository;
	private final CustomerRepository customerRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtProvider jwtProvider;
	private final S3Properties s3Properties;
	private final List<OAuthClient> oAuthClients;

	@Transactional
	public Long signup(SignupRequest request) {
		if (userRepository.existsByEmail(request.email())) {
			throw new CommonException(ErrorCode.DUPLICATED_EMAIL);
		}
		if (userRepository.existsByPhoneNumber(request.phoneNumber())) {
			throw new CommonException(ErrorCode.DUPLICATED_PHONE_NUMBER);
		}

		User user = User.createUser(
			request.email(),
			request.username(),
			request.userAccount(),
			request.password(),
			request.phoneNumber(),
			s3Properties.getDefaultProfileImage()
		);
		userRepository.save(user);

		Customer customer = Customer.createCustomer(user);
		customerRepository.save(customer);

		return user.getUserId();
	}

	@Transactional
	public LoginResponse login(LoginRequest request) {
		User user = getUserByEmail(request);
		matchPassword(request, user);
		return issueTokens(user);
	}

	@Transactional
	public LoginResponse oAuthLogin(String provider, String code) {
		OAuthClient client = getClient(provider);
		String accessToken = client.getAccessToken(code);
		OAuthUserInfo userInfo = client.getUserInfo(accessToken);

		User user = userRepository.findByProviderAndProviderId(
				userInfo.getProvider(),
				userInfo.getProviderId()
			)
			.orElseGet(() -> {
				User newUser = userRepository.save(
					User.createOAuthUser(
						userInfo.getEmail(),
						userInfo.getUsername(),
						userInfo.getProvider(),
						userInfo.getProviderId(),
						s3Properties.getDefaultProfileImage()
					)
				);
				customerRepository.save(Customer.createCustomer(newUser));
				return newUser;
			});
		return issueTokens(user);
	}

	@Transactional(readOnly = true)
	public AccessTokenResponse refreshAccessToken(RefreshTokenRequest request) {
		String oldRefreshToken = request.refreshToken();
		isTokenValid(oldRefreshToken);

		Long userId = jwtProvider.getUserIdFromToken(oldRefreshToken);
		RefreshToken savedToken = getStoredRefreshToken(userId);
		matchWithStoredToken(savedToken, oldRefreshToken);

		User user = userRepository.findById(userId)
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));

		String newAccessToken = jwtProvider.createAccessToken(userId, user.getUserRole());

		return new AccessTokenResponse(newAccessToken);
	}

	@Transactional
	public void logout(Long userId) {
		refreshTokenRepository.deleteByUserId(userId);
	}

	@Transactional(readOnly = true)
	public UserProfileResponse getProfile(Long userId) {
		User user = getUser(userId);
		return UserProfileResponse.getUserInfo(user);
	}

	@Transactional
	public void updateProfile(Long userId, UpdateProfileRequest request) {
		User user = getUser(userId);

		if (request.phoneNumber() != null
			&& userRepository.existsByPhoneNumber(request.phoneNumber())) {
			throw new CommonException(ErrorCode.DUPLICATED_PHONE_NUMBER);
		}

		user.updateProfile(request.username(), request.phoneNumber());
	}

	@Transactional
	public void updatePassword(Long userId, UpdatePasswordRequest request) {
		User user = getUser(userId);

		if (user.isOAuthUser()) {
			throw new CommonException(ErrorCode.FORBIDDEN_OAUTH_PASSWORD_CHANGE);
		}
		if (!user.matchPassword(request.currentPassword())) {
			throw new CommonException(ErrorCode.INVALID_CURRENT_PASSWORD);
		}
		if (user.matchPassword(request.newPassword())) {
			throw new CommonException(ErrorCode.DUPLICATED_NEW_PASSWORD);
		}

		user.updatePassword(request.newPassword());
	}

	@Transactional
	public void updateProfileImage(Long userId, String imageUrl) {
		User user = getUser(userId);
		user.updateProfileImage(imageUrl);
	}

	private void matchPassword(LoginRequest request, User user) {
		if (!user.matchPassword(request.password())) {
			throw new CommonException(ErrorCode.FAILURE_LOGIN);
		}
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
	}

	private User getUserByEmail(LoginRequest request) {
		return userRepository.findByEmail(request.email())
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
	}

	private OAuthClient getClient(String provider) {
		return oAuthClients.stream()
			.filter(client -> client.getProvider().name().equalsIgnoreCase(provider))
			.findFirst()
			.orElseThrow(() -> new CommonException(ErrorCode.UNSUPPORTED_OAUTH_PROVIDER));
	}

	private LoginResponse issueTokens(User user) {
		String accessToken = jwtProvider.createAccessToken(user.getUserId(), user.getUserRole());
		String refreshToken = jwtProvider.createRefreshToken(user.getUserId(), user.getUserRole());

		RefreshToken refreshTokenEntity = refreshTokenRepository.findByUserId(user.getUserId())
			.map(token -> {
				token.updateToken(refreshToken);
				return token;
			})
			.orElse(new RefreshToken(user.getUserId(), refreshToken));
		refreshTokenRepository.save(refreshTokenEntity);

		return new LoginResponse(accessToken, refreshToken);
	}

	private void isTokenValid(String oldRefreshToken) {
		if (!jwtProvider.validateToken(oldRefreshToken)) {
			throw new CommonException(ErrorCode.EXPIRED_TOKEN_ERROR);
		}
	}

	private RefreshToken getStoredRefreshToken(Long userId) {
		return refreshTokenRepository.findByUserId(userId)
			.orElseThrow(() -> new CommonException(ErrorCode.INVALID_TOKEN_ERROR));
	}

	private static void matchWithStoredToken(RefreshToken savedToken, String oldRefreshToken) {
		if (!savedToken.getToken().equals(oldRefreshToken)) {
			throw new CommonException(ErrorCode.INVALID_TOKEN_ERROR);
		}
	}

	public String getLoginUrl(String provider) {
		return getClient(provider).getLoginUrl();
	}
}
