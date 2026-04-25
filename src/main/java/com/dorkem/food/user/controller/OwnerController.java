package com.dorkem.food.user.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dorkem.food.common.annotation.AuthStoreId;
import com.dorkem.food.common.response.ResponseDto;
import com.dorkem.food.menu.dto.request.UpdateStoreStatusRequest;
import com.dorkem.food.order.dto.response.OwnerOrderResponse;
import com.dorkem.food.user.dto.request.OwnerLoginRequest;
import com.dorkem.food.user.dto.response.LoginResponse;
import com.dorkem.food.user.service.OwnerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/owner")
@RequiredArgsConstructor
public class OwnerController {

	private final OwnerService ownerService;

	@PostMapping("/auth/login")
	public ResponseEntity<ResponseDto<LoginResponse>> ownerLogin(
		@RequestBody OwnerLoginRequest request
	) {
		return ResponseEntity.ok(ResponseDto.ok(ownerService.ownerLogin(request)));
	}

	@GetMapping("/orders/active")
	public ResponseEntity<ResponseDto<List<OwnerOrderResponse>>> getActiveOrders(
		@AuthStoreId Long storeId
	) {
		return ResponseEntity.ok(ResponseDto.ok(ownerService.getActiveOrders(storeId)));
	}

	@GetMapping("/orders/completed")
	public ResponseEntity<ResponseDto<List<OwnerOrderResponse>>> getCompletedOrders(
		@AuthStoreId Long storeId
	) {
		return ResponseEntity.ok(ResponseDto.ok(ownerService.getCompletedOrders(storeId)));
	}

	@PatchMapping("/orders/{orderId}/complete")
	public ResponseEntity<ResponseDto<Void>> completeOrder(
		@AuthStoreId Long storeId,
		@PathVariable String orderId
	) {
		ownerService.completeOrder(storeId, orderId);
		return ResponseEntity.ok(ResponseDto.ok(null));
	}

	@PostMapping("/orders/{orderId}/rider")
	public ResponseEntity<ResponseDto<Void>> callRider(
		@AuthStoreId Long storeId,
		@PathVariable String orderId
	) {
		ownerService.callRider(storeId, orderId);
		return ResponseEntity.ok(ResponseDto.ok(null));
	}

	@PatchMapping("/store/status")
	public ResponseEntity<ResponseDto<Void>> updateStoreStatus(
		@AuthStoreId Long storeId,
		@RequestBody UpdateStoreStatusRequest request
	) {
		ownerService.updateStoreStatus(storeId, request.status());
		return ResponseEntity.ok(ResponseDto.ok(null));
	}

	@PatchMapping("/menus/{menuId}/sold-out")
	public ResponseEntity<ResponseDto<Void>> toggleSoldOut(
		@AuthStoreId Long storeId,
		@PathVariable Long menuId
	) {
		ownerService.updateMenuSoldOutStatus(storeId, menuId);
		return ResponseEntity.ok(ResponseDto.ok(null));
	}
}
