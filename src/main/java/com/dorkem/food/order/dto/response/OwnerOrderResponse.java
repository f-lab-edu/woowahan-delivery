package com.dorkem.food.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.dorkem.food.order.dto.request.OrderItemDto;
import com.dorkem.food.order.entity.Order;

public record OwnerOrderResponse(
	String orderId,
	String customerPhone,
	List<OrderItemDto> items,
	String status,
	int totalPrice,
	LocalDateTime createdAt
) {
	public static OwnerOrderResponse from(Order order) {
		return new OwnerOrderResponse(
			order.getOrderId(),
			order.getCustomer().getPhoneNumber(),
			order.getOrderItems().stream()
				.map(OrderItemDto::from)
				.toList(),
			order.getCurrentStatus().name(),
			order.getTotalPrice(),
			order.getCreatedAt()
		);
	}
}
