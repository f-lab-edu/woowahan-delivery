package com.dorkem.food.order.dto;

import java.util.List;

import jakarta.validation.constraints.*;

public class OrderDtos {

	public record CreateOrderRequest(
		@NotNull Long accountId,
		@NotNull Long storeId,
		@NotNull String addrRoad,
		@NotNull String addrDetail,
		String requestToStore,
		String requestToRider,
		@NotNull List<Item> items,
		@NotNull @Min(0) Integer deliveryFee
	) {
		public record Item(
			@NotNull Long menuId,
			@NotNull String menuName,
			@NotNull @Min(0) Integer unitPrice,
			@NotNull @Min(1) Integer quantity
		) {
		}
	}
}

