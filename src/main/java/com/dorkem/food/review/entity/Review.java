package com.dorkem.food.review.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
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
@Table(name = "reviews")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "review_id")
	private Long reviewId;

	@Getter
	@Column(name = "rating", nullable = false)
	private int rating;

	@Column(name = "content", nullable = false)
	private String content;

	@Column(name = "store_id", nullable = false)
	private Long storeId;

	@Column(name = "customer_id", nullable = false)
	private Long customerId;

	@Column(name = "order_id", nullable = false)
	private String orderId;

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "modified_at", nullable = false)
	private LocalDateTime modifiedAt;

	private Review(Long storeId, Long customerId, String orderId, int rating, String content) {
		this.storeId = storeId;
		this.customerId = customerId;
		this.orderId = orderId;
		this.rating = rating;
		this.content = content;
	}

	public static Review createReview(Long storeId, Long customerId, String orderId, int rating, String content) {
		return new Review(storeId, customerId, orderId, rating, content);
	}
}
