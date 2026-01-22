package com.dorkem.food.order.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class DeliveryAddressSnapshot {

	@Column(name = "delivery_address", nullable = false, length = 255)
	private String address;

	@Column(name = "delivery_address_detail", nullable = false, length = 255)
	private String addressDetail;

	protected DeliveryAddressSnapshot() {
	}

	private DeliveryAddressSnapshot(String address, String addressDetail) {
		this.address = address;
		this.addressDetail = addressDetail;
	}

	public static DeliveryAddressSnapshot of(String address, String addressDetail) {
		if (address == null || address.isBlank()) {
			throw new IllegalArgumentException("주소는 필수입니다.");
		}
		if (addressDetail == null || addressDetail.isBlank()) {
			throw new IllegalArgumentException("상세주소는 필수입니다.");
		}
		return new DeliveryAddressSnapshot(address, addressDetail);
	}

	public String getAddress() {
		return address;
	}

	public String getAddressDetail() {
		return addressDetail;
	}
}
