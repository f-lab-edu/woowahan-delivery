package com.dorkem.food.user.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dorkem.food.common.exception.CommonException;
import com.dorkem.food.common.exception.ErrorCode;
import com.dorkem.food.common.jwt.JwtProvider;
import com.dorkem.food.menu.entity.Menu;
import com.dorkem.food.menu.repository.MenuQueryRepository;
import com.dorkem.food.order.dto.response.OwnerOrderResponse;
import com.dorkem.food.order.entity.Order;
import com.dorkem.food.order.repository.OrderQueryRepository;
import com.dorkem.food.order.service.OrderService;
import com.dorkem.food.store.entity.Store;
import com.dorkem.food.store.entity.StoreStatus;
import com.dorkem.food.store.repository.StoreQueryRepository;
import com.dorkem.food.store.repository.StoreRepository;
import com.dorkem.food.user.dto.request.OwnerLoginRequest;
import com.dorkem.food.user.dto.response.LoginResponse;
import com.dorkem.food.user.entity.Owner;
import com.dorkem.food.user.entity.User;
import com.dorkem.food.user.entity.auth.RefreshToken;
import com.dorkem.food.user.repository.OwnerRepository;
import com.dorkem.food.user.repository.RefreshTokenRepository;
import com.dorkem.food.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OwnerService {

	private final UserRepository userRepository;
	private final OwnerRepository ownerRepository;
	private final StoreRepository storeRepository;
	private final StoreQueryRepository storeQueryRepository;
	private final MenuQueryRepository menuQueryRepository;
	private final OrderQueryRepository orderQueryRepository;
	private final OrderService orderService;
	private final RefreshTokenRepository refreshTokenRepository;
	private final JwtProvider jwtProvider;

	@Transactional
	public LoginResponse ownerLogin(OwnerLoginRequest request) {
		User user = getUserByEmail(request.email());
		matchPassword(request.password(), user);

		Owner owner = getOwnerByUser(user);
		validateStoreAccess(request.storeId(), owner);

		return issueOwnerTokens(user, request.storeId());
	}

	@Transactional(readOnly = true)
	public List<OwnerOrderResponse> getActiveOrders(Long storeId) {
		List<Order> orders = orderQueryRepository.findActiveOrdersByStore(storeId);
		return toOwnerOrderResponses(orders);
	}

	@Transactional(readOnly = true)
	public List<OwnerOrderResponse> getCompletedOrders(Long storeId) {
		List<Order> orders = orderQueryRepository.findCompletedOrdersByStore(storeId);
		return toOwnerOrderResponses(orders);
	}

	@Transactional
	public void completeOrder(Long storeId, String orderId) {
		orderService.completeCooking(storeId, orderId);
	}

	@Transactional
	public void callRider(Long storeId, String orderId) {
		orderService.requestDispatch(storeId, orderId);
	}

	@Transactional
	public void updateStoreStatus(Long storeId, StoreStatus status) {
		Store store = getStore(storeId);
		store.updateStatus(status);
	}

	@Transactional
	public void toggleMenuSoldOut(Long storeId, Long menuId) {
		Menu menu = getMenu(menuId, storeId);
		menu.toggleSoldOut();
	}

	private User getUserByEmail(String email) {
		return userRepository.findByEmail(email)
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_USER));
	}

	private void matchPassword(String rawPassword, User user) {
		if (!user.matchPassword(rawPassword)) {
			throw new CommonException(ErrorCode.FAILURE_LOGIN);
		}
	}

	private Owner getOwnerByUser(User user) {
		return ownerRepository.findByUser(user)
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_OWNER));
	}

	private void validateStoreAccess(Long storeId, Owner owner) {
		storeQueryRepository.findByIdAndOwner(storeId, owner)
			.orElseThrow(() -> new CommonException(ErrorCode.FORBIDDEN_STORE_ACCESS));
	}

	private Store getStore(Long storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STORE));
	}

	private Menu getMenu(Long menuId, Long storeId) {
		return menuQueryRepository.findByIdAndStore(menuId, storeId)
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MENU));
	}

	private LoginResponse issueOwnerTokens(User user, Long storeId) {
		String accessToken = jwtProvider.createOwnerAccessToken(user.getUserId(), user.getUserRole(), storeId);
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

	private List<OwnerOrderResponse> toOwnerOrderResponses(List<Order> orders) {
		List<OwnerOrderResponse> responses = new ArrayList<>();
		for (Order order : orders) {
			responses.add(OwnerOrderResponse.from(order));
		}
		return responses;
	}
}
