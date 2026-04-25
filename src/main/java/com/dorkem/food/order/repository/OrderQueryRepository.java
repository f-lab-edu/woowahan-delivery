package com.dorkem.food.order.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dorkem.food.order.entity.Order;

import static com.dorkem.food.order.entity.QOrder.order;
import static com.dorkem.food.order.entity.QOrderItem.orderItem;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderQueryRepository {

	private final JPAQueryFactory queryFactory;

	public Optional<Order> findByCurrentOrder(Long customerId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(order)
				.where(
					order.customer.customerId.eq(customerId),
					order.isActive.isTrue()
				)
				.fetchOne()
		);
	}

	public Optional<Order> findOrderDetail(Long customerId, String orderId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(order)
				.where(
					order.orderId.eq(orderId),
					order.customer.customerId.eq(customerId),
					order.isDeleted.isFalse()
				)
				.fetchOne()
		);
	}

	public List<Order> findOrderHistory(Long customerId, LocalDateTime cursor, int limit) {
		return queryFactory
			.selectFrom(order)
			.where(
				order.customer.customerId.eq(customerId),
				order.isDeleted.isFalse(),
				order.createdAt.lt(cursor)
			)
			.orderBy(order.createdAt.desc())
			.limit(limit)
			.fetch();
	}

	public List<Order> findActiveOrdersByStore(Long storeId) {
		return queryFactory
			.selectFrom(order)
			.join(order.orderItems, orderItem).fetchJoin()
			.join(order.customer).fetchJoin()
			.where(
				order.store.storeId.eq(storeId),
				order.isActive.isTrue()
			)
			.orderBy(order.createdAt.asc())
			.fetch();
	}

	public List<Order> findCompletedOrdersByStore(Long storeId) {
		return queryFactory
			.selectFrom(order)
			.join(order.orderItems, orderItem).fetchJoin()
			.join(order.customer).fetchJoin()
			.where(
				order.store.storeId.eq(storeId),
				order.isActive.isFalse()
			)
			.orderBy(order.createdAt.desc())
			.fetch();
	}
}
