package com.dorkem.food.store.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dorkem.food.store.entity.embedded.StoreReviewStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stores")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Store {
	//TODO: 공통관심사끼리 embedded로 묶기

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "store_id")
	private Long storeId;

	@Column(name = "owner_id", nullable = false)
	private Long ownerId;

	@Column(name = "category_id", nullable = false)
	private Long categoryId;

	@Getter
	@Column(name = "thumbnail")
	private String thumbnail;

	@Getter
	@Embedded
	StoreReviewStatus reviewStatus = new StoreReviewStatus();

	@Getter
	@Column(name = "store_name", nullable = false)
	private String storeName;

	@Column(name = "business_number", nullable = false)
	private String businessNumber;

	@Column(name = "store_address", nullable = false)
	private String storeAddress;

	@Column(name = "store_address_details", nullable = false)
	private String storeAddressDetails;

	@Column(name = "latitude", precision = 13, scale = 10)
	private BigDecimal latitude;

	@Column(name = "longitude", precision = 13, scale = 10)
	private BigDecimal longitude;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private StoreStatus status;

	@Column(name = "open_time")
	private LocalTime openTime;

	@Column(name = "close_time")
	private LocalTime closeTime;

	@Getter
	@Column(name = "min_order_amount", nullable = false)
	private int minOrderAmount;

	@Getter
	@Column(name = "base_delivery_fee", nullable = false)
	private int baseDeliveryFee;

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "modified_at", nullable = false)
	private LocalDateTime modifiedAt;

	private Store(Long ownerId, Long categoryId, String thumbnail, String storeName, String businessNumber,
		String storeAddress, String storeAddressDetails, BigDecimal latitude, BigDecimal longitude,
		StoreStatus status, LocalTime openTime, LocalTime closeTime, int minOrderAmount, int baseDeliveryFee
	) {
		this.ownerId = ownerId;
		this.categoryId = categoryId;
		this.thumbnail = thumbnail;
		this.storeName = storeName;
		this.businessNumber = businessNumber;
		this.storeAddress = storeAddress;
		this.storeAddressDetails = storeAddressDetails;
		this.latitude = latitude;
		this.longitude = longitude;
		this.status = status;
		this.openTime = openTime;
		this.closeTime = closeTime;
		this.minOrderAmount = minOrderAmount;
		this.baseDeliveryFee = baseDeliveryFee;
	}

	public static Store createStore(Long ownerId, Long categoryId, String storeName,
		String businessNumber, String storeAddress, String storeAddressDetails,
		BigDecimal latitude, BigDecimal longitude, StoreStatus status, LocalTime openTime,
		LocalTime closeTime, int minOrderAmount, int baseDeliveryFee, String defaultThumbnail
	) {
		return new Store(
			ownerId, categoryId, defaultThumbnail, storeName, businessNumber, storeAddress, storeAddressDetails,
			latitude, longitude, status, openTime, closeTime, minOrderAmount, baseDeliveryFee
		);
	}

	public void updateStatus(StoreStatus status) {
		this.status = status;
	}

	public boolean isOpen() {
		return this.status == StoreStatus.OPEN;
	}

	public void updateThumbnail(String thumbnailUrl) {
		this.thumbnail = thumbnailUrl;
	}

	public void addReview(int rating) {
		this.reviewStatus = this.reviewStatus.addReview(rating);
	}
}
