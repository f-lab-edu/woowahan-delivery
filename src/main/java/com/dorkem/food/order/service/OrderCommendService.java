package com.dorkem.food.order.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.dorkem.food.order.domain.OrderEventEntity;
import com.dorkem.food.order.domain.event.OrderCreatedEvent;
import com.dorkem.food.order.dto.OrderDtos;
import com.dorkem.food.order.repository.OrderEventRepository;

import jakarta.transaction.Transactional;
import tools.jackson.databind.ObjectMapper;

@Service
public class OrderCommendService {

	private final OrderEventRepository eventRepo;
	private final ObjectMapper objectMapper;

	public OrderCommendService(OrderEventRepository eventRepo, ObjectMapper objectMapper) {
		this.eventRepo = eventRepo;
		this.objectMapper = objectMapper;
	}

	@Transactional
	public String createOrder(OrderDtos.CreateOrderRequest req) {
		String orderId = UUID.randomUUID().toString();

		int totalMenuAmount = req.items().stream()
			.mapToInt(i -> i.unitPrice() * i.quantity()).sum();

		OrderCreatedEvent event = new OrderCreatedEvent(
			orderId,
			req.accountId(),
			req.storeId(),
			new OrderCreatedEvent.Address(req.addrRoad(), req.addrDetail()),
			req.requestToStore(),
			req.requestToRider(),
			req.items().stream()
				.map(i -> new OrderCreatedEvent.Item(i.menuId(), i.menuName(), i.unitPrice(), i.quantity()))
				.toList(),
			req.deliveryFee(),
			totalMenuAmount
		);

		String payload = writeJson(event);
		eventRepo.save(OrderEventEntity.of(orderId, 1L, "OrderCreated", payload));

		return orderId;
	}

	private String writeJson(Object obj) {
		try {
			return objectMapper.writeValueAsString(obj);
		} catch (Exception e) {
			throw new IllegalStateException("직렬화 실패", e);
		}
	}
}
