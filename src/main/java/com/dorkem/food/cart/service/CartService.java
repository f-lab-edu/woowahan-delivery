package com.dorkem.food.cart.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dorkem.food.cart.dto.request.AddCartItemRequest;
import com.dorkem.food.cart.dto.request.UpdateCartItemRequest;
import com.dorkem.food.cart.dto.response.CartResponse;
import com.dorkem.food.cart.entity.Cart;
import com.dorkem.food.cart.entity.CartItem;
import com.dorkem.food.cart.repository.CartItemRepository;
import com.dorkem.food.cart.repository.CartQueryRepository;
import com.dorkem.food.cart.repository.CartRepository;
import com.dorkem.food.common.exception.CommonException;
import com.dorkem.food.common.exception.ErrorCode;
import com.dorkem.food.store.entity.Menu;
import com.dorkem.food.store.repository.MenuRepository;
import com.dorkem.food.store.entity.Store;
import com.dorkem.food.store.repository.StoreRepository;
import com.dorkem.food.user.entity.Customer;
import com.dorkem.food.user.repository.CustomerQueryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final CartQueryRepository cartQueryRepository;
	private final CustomerQueryRepository customerQueryRepository;
	private final MenuRepository menuRepository;
	private final StoreRepository storeRepository;

	@Transactional
	public CartResponse getCart(Long userId) {
		Customer customer = getCustomer(userId);
		Cart cart = getOrCreateCart(customer.getCustomerId());
		return CartResponse.createCartResponse(cart);
	}

	@Transactional
	public CartResponse addItem(Long userId, AddCartItemRequest request) {
		Customer customer = getCustomer(userId);
		Cart cart = getOrCreateCart(customer.getCustomerId());
		Menu menu = getMenu(request);

		cart.getItems().stream()
			.filter(item -> item.getMenuId().equals(menu.getMenuId()))
			.findFirst()
			.ifPresentOrElse(
				item -> item.updateQuantity(request.quantity()),
				() -> cart.addItem(
					menu.getStoreId(),
					CartItem.createCartItem(menu.getMenuId(), menu.getMenuName(), menu.getPrice(), request.quantity())
				)
			);

		return CartResponse.createCartResponse(cart);
	}

	@Transactional
	public CartResponse updateItem(Long userId, Long cartItemId, UpdateCartItemRequest request) {
		Customer customer = getCustomer(userId);
		Cart cart = findExistingCart(customer.getCustomerId());
		CartItem cartItem = findItemInCart(cartItemId, cart);

		cartItem.updateQuantity(request.quantity());

		if (cartItem.getQuantity() <= 0) {
			removeCartItem(cart, cartItem);
		}

		return CartResponse.createCartResponse(cart);
	}

	@Transactional
	public CartResponse removeItem(Long userId, Long cartItemId) {
		Customer customer = getCustomer(userId);
		Cart cart = findExistingCart(customer.getCustomerId());
		CartItem cartItem = findItemInCart(cartItemId, cart);

		cart.getItems().remove(cartItem);
		cartItemRepository.delete(cartItem);

		return CartResponse.createCartResponse(cart);
	}

	private Customer getCustomer(Long userId) {
		return customerQueryRepository.findByUserId(userId)
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CUSTOMER));
	}

	private Cart getOrCreateCart(Long customerId) {
		return cartQueryRepository.getCustomerCart(customerId)
			.orElseGet(() -> cartRepository.save(Cart.createCart(customerId)));
	}

	private Menu getMenu(AddCartItemRequest request) {
		return menuRepository.findById(request.menuId())
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_MENU));
	}

	private CartItem findItemInCart(Long cartItemId, Cart cart) {
		return cart.getItems().stream()
			.filter(item -> item.getCartItemId().equals(cartItemId))
			.findFirst()
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CART_ITEM));
	}

	private Cart findExistingCart(Long customerId) {
		return cartQueryRepository.getCustomerCart(customerId)
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_CART));
	}

	private Store getStore(Long storeId) {
		return storeRepository.findById(storeId)
			.orElseThrow(() -> new CommonException(ErrorCode.NOT_FOUND_STORE));
	}

	private void removeCartItem(Cart cart, CartItem cartItem) {
		cart.getItems().remove(cartItem);
		cartItemRepository.delete(cartItem);
	}
}
