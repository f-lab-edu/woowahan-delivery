package com.dorkem.food.order.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dorkem.food.order.domain.OrderRead;

public interface OrderReadRepository extends JpaRepository<OrderRead, String> {
}
