package com.dorkem.food.user.entity;

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
@Table(name = "addresses")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "address_id")
	private Long addressId;

	@Column(name = "address_tag")
	private String addressTag;

	@Column(name = "address", nullable = false)
	private String address;

	@Column(name = "address_detail", nullable = false)
	private String addressDetail;

	@Column(name = "request_to_rider")
	private String requestToRider;

	@Column(name = "entrance_access_password")
	private String entranceAccessPassword;

	@Column(name = "delivery_directions")
	private String deliveryDirections;

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "modified_at", nullable = false)
	private LocalDateTime modifiedAt;

	private Address(String addressTag, String address, String addressDetail,
		String requestToRider, String entranceAccessPassword, String deliveryDirections
	) {
		this.addressTag = addressTag;
		this.address = address;
		this.addressDetail = addressDetail;
		this.requestToRider = requestToRider;
		this.entranceAccessPassword = entranceAccessPassword;
		this.deliveryDirections = deliveryDirections;
	}

	public static Address createAddress(String addressTag, String address, String addressDetail,
		String requestToRider, String entranceAccessPassword, String deliveryDirections
	) {
		return new Address(addressTag, address, addressDetail, requestToRider, entranceAccessPassword,
			deliveryDirections);
	}
}
