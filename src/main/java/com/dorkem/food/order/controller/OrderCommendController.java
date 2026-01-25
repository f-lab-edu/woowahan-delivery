package com.dorkem.food.order.controller;

import org.springframework.web.bind.annotation.*;

import com.dorkem.food.order.dto.OrderDtos.*;
import com.dorkem.food.order.service.OrderCommendService;

@RestController
@RequestMapping("/orders")
public class OrderCommendController {

	private final OrderCommendService commendService;

	public OrderCommendController(OrderCommendService commendService) {
		this.commendService = commendService;
	}

	@PostMapping
	public void create(
		@RequestBody CreateOrderRequest req
	) {
		String orderId = commendService.createOrder(req);
	}
}
