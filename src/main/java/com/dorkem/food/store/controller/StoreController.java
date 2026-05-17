package com.dorkem.food.store.controller;

import static com.dorkem.food.store.dto.response.MenuResponse.*;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dorkem.food.common.response.ResponseDto;
import com.dorkem.food.store.service.MenuService;
import com.dorkem.food.store.dto.response.StorePageResponse;
import com.dorkem.food.store.service.StoreService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/stores")
@RequiredArgsConstructor
public class StoreController {
	private final StoreService storeService;
	private final MenuService menuService;

	@GetMapping
	public ResponseEntity<ResponseDto<StorePageResponse>> getStores(
		@RequestParam(required = false) Integer categoryId,
		@RequestParam(required = false) Long cursor,
		@RequestParam(defaultValue = "15") int size
	) throws InterruptedException {
		Thread.sleep(200);
		return ResponseEntity.ok(ResponseDto.ok(storeService.getStores(categoryId, cursor, size)));
	}

	@GetMapping("/{storeId}/menus")
	public ResponseEntity<ResponseDto<MenuListResponse>> getMenus(
		@PathVariable Long storeId
	) {
		return ResponseEntity.ok(ResponseDto.ok(menuService.getMenusByStore(storeId)));
	}

	@PatchMapping("/{storeId}/orders/{orderId}/accept")
	public ResponseEntity<ResponseDto<Void>> acceptOrder(
		@PathVariable Long storeId,
		@PathVariable String orderId
	) {
		storeService.acceptOrder(storeId, orderId);
		return ResponseEntity.ok(ResponseDto.ok(null));
	}

	@PatchMapping("/{storeId}/orders/{orderId}/reject")
	public ResponseEntity<ResponseDto<Void>> rejectOrder(
		@PathVariable Long storeId,
		@PathVariable String orderId
	) {
		storeService.rejectOrder(storeId, orderId);
		return ResponseEntity.ok(ResponseDto.ok(null));
	}

	@PatchMapping("/{storeId}/orders/{orderId}/cooking")
	public ResponseEntity<ResponseDto<Void>> startCooking(
		@PathVariable Long storeId,
		@PathVariable String orderId
	) {
		storeService.startCooking(storeId, orderId);
		return ResponseEntity.ok(ResponseDto.ok(null));
	}

	@PatchMapping("/{storeId}/orders/{orderId}/cook-complete")
	public ResponseEntity<ResponseDto<Void>> completeCooking(
		@PathVariable Long storeId,
		@PathVariable String orderId
	) {
		storeService.completeCookingAndRequestDispatch(storeId, orderId);
		return ResponseEntity.ok(ResponseDto.ok(null));
	}
}
