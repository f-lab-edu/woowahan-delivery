package com.dorkem.food.menu.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dorkem.food.menu.entity.Menu;
import com.dorkem.food.menu.entity.QMenu;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MenuQueryRepository {

	private final JPAQueryFactory queryFactory;

	public Optional<Menu> findByIdAndStore(Long menuId, Long storeId) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(QMenu.menu)
				.where(
					QMenu.menu.menuId.eq(menuId),
					QMenu.menu.store.storeId.eq(storeId)
				)
				.fetchOne()
		);
	}
}
