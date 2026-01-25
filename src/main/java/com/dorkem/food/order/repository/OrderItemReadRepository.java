package com.dorkem.food.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dorkem.food.order.domain.OrderItemRead;
import com.dorkem.food.order.domain.OrderItemReadId;

public interface OrderItemReadRepository extends JpaRepository<OrderItemRead, OrderItemReadId> {
}
