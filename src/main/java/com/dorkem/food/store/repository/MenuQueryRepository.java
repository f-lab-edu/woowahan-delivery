package com.dorkem.food.store.repository;

import static com.dorkem.food.store.entity.QMenu.*;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dorkem.food.store.entity.Menu;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MenuQueryRepository {

	private final JPAQueryFactory queryFactory;

	public Optional<Menu> findByIdAndStore(Long menuId, Long storeId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(menu)
				.where(
					menu.menuId.eq(menuId),
					menu.storeId.eq(storeId)
				)
				.fetchOne()
		);
	}
}
