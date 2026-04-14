package com.dorkem.food.store.repository;

import static com.dorkem.food.store.entity.QStore.*;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.dorkem.food.store.entity.QStore;
import com.dorkem.food.store.entity.Store;
import com.dorkem.food.user.entity.Owner;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class StoreQueryRepository {

	private final JPAQueryFactory queryFactory;

	public List<Store> findStoresByCategory(Integer categoryId, Long cursor, int limit) {
		return queryFactory
			.selectFrom(store)
			.where(
				eqCategory(categoryId),
				ltCursor(cursor)
			)
			.orderBy(store.storeId.desc())
			.limit(limit)
			.fetch();
	}

	public Optional<Store> findByIdAndOwner(Long storeId, Owner owner) {
		return Optional.ofNullable(
			queryFactory
				.selectFrom(QStore.store)
				.where(
					QStore.store.storeId.eq(storeId),
					QStore.store.owner.eq(owner)
				)
				.fetchOne()
		);
	}

	private BooleanExpression eqCategory(Integer categoryId) {
		return categoryId != null ? store.category.categoryId.eq(categoryId) : null;
	}

	private BooleanExpression ltCursor(Long cursor) {
		return cursor != null ? store.storeId.lt(cursor) : null;
	}
}
