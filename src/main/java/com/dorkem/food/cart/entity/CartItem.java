package com.dorkem.food.cart.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_item_id")
	private Long cartItemId;

	@Column(name = "menu_id", nullable = false)
	private Long menuId;

	@Column(name = "menu_name", nullable = false)
	private String menuName;

	@Column(name = "price", nullable = false)
	private int price;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	private CartItem(Long menuId, String menuName, int price, int quantity) {
		this.menuId = menuId;
		this.menuName = menuName;
		this.price = price;
		this.quantity = quantity;
	}

	public static CartItem createCartItem(Long menuId, String menuName, int price, int quantity) {
		return new CartItem(menuId, menuName, price, quantity);
	}

	public void updateQuantity(int quantity) {
		this.quantity += quantity;
	}

	public int getSubtotal() {
		return this.price * this.quantity;
	}
}
