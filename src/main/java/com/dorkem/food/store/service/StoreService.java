package com.dorkem.food.store.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dorkem.food.category.entity.Category;
import com.dorkem.food.common.config.S3Properties;
import com.dorkem.food.order.service.OrderService;
import com.dorkem.food.store.dto.request.CreateStoreRequest;
import com.dorkem.food.store.dto.response.StorePageResponse;
import com.dorkem.food.store.dto.response.StoreResponse.StoreSummaryResponse;
import com.dorkem.food.store.entity.Store;
import com.dorkem.food.store.repository.StoreQueryRepository;
import com.dorkem.food.store.repository.StoreRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StoreService {

	private final StoreRepository storeRepository;
	private final StoreQueryRepository storeQueryRepository;
	private final OrderService orderService;
	private final S3Properties s3Properties;

	public void createStore(CreateStoreRequest request, Long ownerId, Long categoryId) {

		Store store = Store.createStore(
			ownerId,
			categoryId,
			request.storeName(),
			request.businessNumber(),
			request.storeAddress(),
			request.storeAddressDetails(),
			request.latitude(),
			request.longitude(),
			request.status(),
			request.openTime(),
			request.closeTime(),
			request.minOrderAmount(),
			request.baseDeliveryFee(),
			s3Properties.getDefaultStoreImage()
		);

		storeRepository.save(store);
	}

	@Transactional
	public StorePageResponse getStores(Integer categoryId, Long cursor, int size) {
		List<Store> stores = storeQueryRepository.findStoresByCategory(categoryId, cursor, size + 1);

		boolean hasNext = stores.size() > size;
		List<Store> content = hasNext ? stores.subList(0, size) : stores;

		Long nextCursor = hasNext ? content.get(content.size() - 1).getStoreId() : null;

		return new StorePageResponse(
			content.stream().map(StoreSummaryResponse::createStoreSummaryResponse).toList(),
			nextCursor,
			hasNext
		);
	}

	public void acceptOrder(Long storeId, String orderId) {
		orderService.acceptOrder(storeId, orderId);
	}

	@Transactional
	public void rejectOrder(Long storeId, String orderId) {
		orderService.rejectOrder(storeId, orderId);
	}

	@Transactional
	public void startCooking(Long storeId, String orderId) {
		orderService.startCooking(storeId, orderId);
	}

	@Transactional
	public void completeCookingAndRequestDispatch(Long storeId, String orderId) {
		orderService.completeCooking(storeId, orderId);
		orderService.requestDispatch(storeId, orderId);
	}
}
