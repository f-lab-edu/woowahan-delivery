package com.dorkem.food.cart.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "carts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cart_id")
	private Long cartId;

	@Column(name = "customer_id", nullable = false, unique = true)
	private Long customerId;

	@Column(name = "store_id")
	private Long storeId;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "cart_id", nullable = false)
	private List<CartItem> items = new ArrayList<>();

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "modified_at", nullable = false)
	private LocalDateTime modifiedAt;

	private Cart(Long customerId) {
		this.customerId = customerId;
	}

	public static Cart createCart(Long customerId) {
		return new Cart(customerId);
	}

	public void addItem(Long storeId, CartItem item) {
		if (this.storeId != null && !this.storeId.equals(storeId)) {
			this.items.clear();
		}
		this.storeId = storeId;
		this.items.add(item);
	}

	public void clearItems() {
		this.items.clear();
		this.storeId = null;
	}

	public int getTotalPrice() {
		return items.stream()
			.mapToInt(CartItem::getSubtotal)
			.sum();
	}
}
