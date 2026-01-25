package com.dorkem.food.order.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items_read")
@IdClass(OrderItemReadId.class)
public class OrderItemRead {

	@Id
	private String orderId;

	@Id
	private Integer lineNo;

	@Column(nullable = false)
	private Long menuId;

	@Column(nullable = false, length = 100)
	private String menuName;

	@Column(nullable = false)
	private Integer unitPrice;

	@Column(nullable = false)
	private Integer quantity;

	protected OrderItemRead() {}

	public static OrderItemRead of(String orderId, int lineNo, Long menuId, String menuName, int unitPrice, int quantity) {
		OrderItemRead e = new OrderItemRead();
		e.orderId = orderId;
		e.lineNo = lineNo;
		e.menuId = menuId;
		e.menuName = menuName;
		e.unitPrice = unitPrice;
		e.quantity = quantity;
		return e;
	}
}

