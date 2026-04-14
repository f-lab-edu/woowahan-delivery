package com.dorkem.food.order.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dorkem.food.order.entity.Order;
import com.dorkem.food.order.entity.QOrder;
import com.dorkem.food.order.entity.QOrderItem;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

	private final JPAQueryFactory queryFactory;

	public Optional<Order> findByCurrentOrder(Long customerId) {
		Order order = queryFactory
			.selectFrom(QOrder.order)
			.where(
				QOrder.order.customer.customerId.eq(customerId),
				QOrder.order.isActive.isTrue()
			)
			.fetchOne();
		return Optional.ofNullable(order);
	}

	public Optional<Order> findOrderDetail(Long customerId, String orderId) {
		Order order = queryFactory
			.selectFrom(QOrder.order)
			.where(
				QOrder.order.orderId.eq(orderId),
				QOrder.order.customer.customerId.eq(customerId),
				QOrder.order.isDeleted.isFalse()
			)
			.fetchOne();
		return Optional.ofNullable(order);
	}

	public List<Order> findOrderHistory(Long customerId, LocalDateTime cursor, int limit) {
		return queryFactory
			.selectFrom(QOrder.order)
			.where(
				QOrder.order.customer.customerId.eq(customerId),
				QOrder.order.isDeleted.isFalse(),
				QOrder.order.createdAt.lt(cursor)
			)
			.orderBy(QOrder.order.createdAt.desc())
			.limit(limit)
			.fetch();
	}

	public List<Order> findActiveOrdersByStore(Long storeId) {
		return queryFactory
			.selectFrom(QOrder.order)
			.join(QOrder.order.orderItems, QOrderItem.orderItem).fetchJoin()
			.join(QOrder.order.customer).fetchJoin()
			.where(
				QOrder.order.store.storeId.eq(storeId),
				QOrder.order.isActive.isTrue()
			)
			.orderBy(QOrder.order.createdAt.asc())
			.fetch();
	}

	public List<Order> findCompletedOrdersByStore(Long storeId) {
		return queryFactory
			.selectFrom(QOrder.order)
			.join(QOrder.order.orderItems, QOrderItem.orderItem).fetchJoin()
			.join(QOrder.order.customer).fetchJoin()
			.where(
				QOrder.order.store.storeId.eq(storeId),
				QOrder.order.isActive.isFalse()
			)
			.orderBy(QOrder.order.createdAt.desc())
			.fetch();
	}
}
