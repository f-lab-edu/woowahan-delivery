package com.dorkem.food.cart.repository;

import static com.dorkem.food.cart.entity.QCartItem.*;
import static com.dorkem.food.cart.entity.QCart.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dorkem.food.cart.entity.Cart;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CartQueryRepository {
	private final JPAQueryFactory queryFactory;

	public Optional<Cart> getCustomerCart(Long customerId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(cart)
				.leftJoin(cart.items, cartItem).fetchJoin()
				.where(cart.customerId.eq(customerId))
				.fetchOne()
		);
	}
}
