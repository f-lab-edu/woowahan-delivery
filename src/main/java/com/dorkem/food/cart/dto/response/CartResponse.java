package com.dorkem.food.cart.dto.response;

import java.util.List;

import com.dorkem.food.cart.entity.Cart;
import com.dorkem.food.cart.entity.CartItem;

public record CartResponse(
	Long cartId,
	Long storeId,
	List<CartItemResponse> items,
	int totalPrice
) {
	public static CartResponse createCartResponse(Cart cart) {
		return new CartResponse(
			cart.getCartId(),
			cart.getStoreId(),
			cart.getItems().stream()
				.map(CartItemResponse::createCartItemResponse)
				.toList(),
			cart.getTotalPrice()
		);
	}

	public record CartItemResponse(
		Long cartItemId,
		Long menuId,
		String menuName,
		int price,
		int quantity,
		int subtotal
	) {
		public static CartItemResponse createCartItemResponse(CartItem item) {
			return new CartItemResponse(
				item.getCartItemId(),
				item.getMenuId(),
				item.getMenuName(),
				item.getPrice(),
				item.getQuantity(),
				item.getSubtotal()
			);
		}
	}
}
