package com.dorkem.food.order.domain;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "order_events",
	uniqueConstraints = @UniqueConstraint(
		name = "uk_order_events_agg_ver",
		columnNames = {"aggregateId", "version"}
	)
)
public class OrderEventEntity {

	@Id
	private String eventId;

	@Column(nullable = false, length = 30)
	private String aggregateType;

	@Column(nullable = false, length = 60)
	private String aggregateId;

	@Column(nullable = false)
	private Long version;

	@Column(nullable = false, length = 60)
	private String eventType;

	@Lob
	@Column(nullable = false)
	private String payload;

	@Column(nullable = false)
	private LocalDateTime occurredAt;

	protected OrderEventEntity() {
	}

	public static OrderEventEntity of(String aggregateId, long version, String eventType, String payload) {
		OrderEventEntity e = new OrderEventEntity();
		e.eventId = UUID.randomUUID().toString();
		e.aggregateType = "ORDER";
		e.aggregateId = aggregateId;
		e.version = version;
		e.eventType = eventType;
		e.payload = payload;
		e.occurredAt = LocalDateTime.now();
		return e;
	}

	public String getAggregateId() {
		return aggregateId;
	}

	public Long getVersion() {
		return version;
	}

	public String getEventType() {
		return eventType;
	}

	public String getPayload() {
		return payload;
	}
}
