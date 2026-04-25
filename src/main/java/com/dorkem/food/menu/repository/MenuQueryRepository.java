package com.dorkem.food.menu.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dorkem.food.menu.entity.Menu;
import static com.dorkem.food.menu.entity.QMenu.menu;
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
					menu.store.storeId.eq(storeId)
				)
				.fetchOne()
		);
	}
}
