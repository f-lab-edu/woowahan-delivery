package com.dorkem.food.order.dto.request;

import com.dorkem.food.order.entity.OrderItem;

public record OrderItemDto(
	String menuName,
	int quantity,
	int totalPrice
) {
	public static OrderItemDto from(OrderItem item) {
		return new OrderItemDto(
			item.getMenuName(),
			item.getQuantity(),
			item.getTotalPrice()
		);
	}
}
