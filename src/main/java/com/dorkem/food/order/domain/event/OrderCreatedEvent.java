package com.dorkem.food.order.domain.event;

import java.util.List;

public record OrderCreatedEvent(
	String orderId,
	Long accountId,
	Long storeId,
	Address address,
	String requestToStore,
	String requestToRider,
	List<Item> items,
	Integer deliveryFee,
	Integer totalMenuAmount
) {
	public record Address(String road, String detail) {
	}

	public record Item(Long menuId, String menuName, Integer unitPrice, Integer quantity) {
	}
}
