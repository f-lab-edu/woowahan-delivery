package com.dorkem.food.order.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders_read")
public class OrderRead {

	@Id
	private String orderId;

	@Column(nullable = false)
	private Long accountId;

	@Column(nullable = false)
	private Long storeId;

	@Column(nullable = false, length = 30)
	private String orderStatus;

	@Column(nullable = false)
	private Integer totalMenuAmount;

	@Column(nullable = false)
	private Integer deliveryFee;

	@Column(nullable = false)
	private Integer totalPayAmount;

	@Column(nullable = false, length = 255)
	private String addrRoad;

	@Column(nullable = false, length = 255)
	private String addrDetail;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	protected OrderRead() {
	}

	public static OrderRead created(
		String orderId, Long accountId, Long storeId,
		int totalMenuAmount, int deliveryFee,
		String addrRoad, String addrDetail
	) {
		OrderRead o = new OrderRead();
		o.orderId = orderId;
		o.accountId = accountId;
		o.storeId = storeId;
		o.orderStatus = "CREATED";
		o.totalMenuAmount = totalMenuAmount;
		o.deliveryFee = deliveryFee;
		o.totalPayAmount = totalMenuAmount + deliveryFee;
		o.addrRoad = addrRoad;
		o.addrDetail = addrDetail;
		o.createdAt = LocalDateTime.now();
		return o;
	}
}
