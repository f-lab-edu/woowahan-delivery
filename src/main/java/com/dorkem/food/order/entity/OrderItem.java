package com.dorkem.food.order.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "order_item")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderItem {

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_item_id")
	private Long orderItemId;

	@Column(name = "menu_id", nullable = false)
	private Long menuId;

	@Getter
	@Column(name = "menu_name", nullable = false)
	private String menuName;

	@Getter
	@Column(name = "order_price", nullable = false)
	private int orderPrice;

	@Getter
	@Column(name = "quantity", nullable = false)
	private int quantity;

	private OrderItem(Long menuId, String menuName, int orderPrice, int quantity) {
		this.menuId = menuId;
		this.menuName = menuName;
		this.orderPrice = orderPrice;
		this.quantity = quantity;
	}

	public static OrderItem createOrderItem(Long menuId, String menuName, int price, int quantity) {
		return new OrderItem(menuId, menuName, price, quantity);
	}

	public int getTotalPrice() {
		return this.orderPrice * this.quantity;
	}
}
