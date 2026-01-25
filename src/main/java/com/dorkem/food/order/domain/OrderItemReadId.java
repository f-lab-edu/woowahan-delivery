package com.dorkem.food.order.domain;

import java.io.Serializable;
import java.util.Objects;

public class OrderItemReadId implements Serializable {
	private String orderId;
	private Integer lineNo;

	public OrderItemReadId() {
	}

	public OrderItemReadId(String orderId, Integer lineNo) {
		this.orderId = orderId;
		this.lineNo = lineNo;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof OrderItemReadId that))
			return false;
		return Objects.equals(orderId, that.orderId) && Objects.equals(lineNo, that.lineNo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(orderId, lineNo);
	}
}
