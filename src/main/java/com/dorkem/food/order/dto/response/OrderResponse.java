package com.dorkem.food.order.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.dorkem.food.order.entity.DisplayStatus;
import com.dorkem.food.order.entity.Order;

import com.dorkem.food.order.entity.OrderItem;

public record OrderResponse(
	String orderId,
	String storeName,
	DisplayStatus displayStatus,
	String address,
	String addressDetail,
	String requestToRider,
	String requestToStore,
	boolean noCutlery,
	boolean noSideDish,
	List<OrderItemResponse> orderItems,
	int totalPrice,
	LocalDateTime orderedAt
) {

	public static OrderResponse createOrderResponse(Order order) {
		return new OrderResponse(
			order.getOrderId(),
			order.getStoreName(),
			order.getCurrentStatus().displayOrderStatus(),
			order.getUserDeliveryInfo().getAddress(),
			order.getUserDeliveryInfo().getAddressDetail(),
			order.getUserDeliveryInfo().getRequestToRider(),
			order.getOrderRequirement().getRequestToStore(),
			order.getOrderRequirement().isNoCutlery(),
			order.getOrderRequirement().isNoSideDish(),
			order.getOrderItems().stream()
				.map(OrderItemResponse::createOrderItemResponse)
				.toList(),
			order.getTotalPrice(),
			order.getCreatedAt()
		);
	}

	public record HistoryDetailResponse(
		String orderId,
		String storeName,
		String address,
		String addressDetail,
		String requestToRider,
		String requestToStore,
		boolean noCutlery,
		boolean noSideDish,
		String phoneNumber,
		List<OrderItemResponse> orderItems,
		int totalPrice,
		LocalDateTime orderedAt
	) {
		public static HistoryDetailResponse createHistoryDetailResponse(Order order) {
			return new HistoryDetailResponse(
				order.getOrderId(),
				order.getStoreName(),
				order.getUserDeliveryInfo().getAddress(),
				order.getUserDeliveryInfo().getAddressDetail(),
				order.getUserDeliveryInfo().getRequestToRider(),
				order.getOrderRequirement().getRequestToStore(),
				order.getOrderRequirement().isNoCutlery(),
				order.getOrderRequirement().isNoSideDish(),
				order.getCustomerPhone(),
				order.getOrderItems().stream()
					.map(OrderItemResponse::createOrderItemResponse)
					.toList(),
				order.getTotalPrice(),
				order.getCreatedAt()
			);
		}
	}

	public record HistoryResponse(
		String orderId,
		String storeName,
		LocalDateTime orderedAt,
		List<String> menuNames,
		int totalPrice
	) {
		public static HistoryResponse createHistoryResponse(Order order) {
			return new HistoryResponse(
				order.getOrderId(),
				order.getStoreName(),
				order.getCreatedAt(),
				order.getOrderItems().stream()
					.map(OrderItem::getMenuName)
					.toList(),
				order.getTotalPrice()
			);
		}
	}

	public record OrderItemResponse(
		String menuName,
		int orderPrice,
		int quantity,
		int totalPrice
	) {
		public static OrderItemResponse createOrderItemResponse(OrderItem item) {
			return new OrderItemResponse(
				item.getMenuName(),
				item.getOrderPrice(),
				item.getQuantity(),
				item.getTotalPrice()
			);
		}
	}
}
