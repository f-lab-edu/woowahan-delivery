package com.dorkem.food.order.domain;

import java.time.LocalDateTime;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@Column(nullable = false)
	private Long accountId;

	@Column(nullable = false)
	private Long storeId;

	@Column(nullable = false)
	private Long addressId; // 주소가 삭제되었을 때를 고려해야함 (차라리 스냅샷?)

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 30)
	private OrderStatus orderStatus;

	@Column(nullable = false)
	private Integer totalMenuAmount;

	@Column(nullable = false)
	private Integer deliveryFee;

	@Column(nullable = false)
	private Integer totalPayAmount;

	@Column(length = 255)
	private String requestToStore;

	@Column(length = 255)
	private String requestToRider;

	@Column(nullable = false)
	private LocalDateTime createdAt;

	protected Order() {}

}
