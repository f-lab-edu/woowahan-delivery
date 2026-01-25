package com.dorkem.food.order.domain;

public enum OrderStatus {
	CREATED,
	PAID,
	ACCEPTED,
	RIDER_ASSIGNED,
	DELIVERED,
	CANCELED;

	public boolean canTrans(OrderStatus next) {
		if (next == null || this == next) {
			return false;
		}

		return switch (this) {
			case CREATED -> next == PAID || next == CANCELED;
			case PAID -> next == ACCEPTED || next == CANCELED;
			case ACCEPTED -> next == RIDER_ASSIGNED || next == CANCELED;
			case RIDER_ASSIGNED -> next == DELIVERED || next == CANCELED;
			case DELIVERED -> false;
			case CANCELED -> false;
		};
	}
}
