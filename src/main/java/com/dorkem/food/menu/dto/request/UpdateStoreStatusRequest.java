package com.dorkem.food.menu.dto.request;

import com.dorkem.food.store.entity.StoreStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "가게 상태 변경 요청")
public record UpdateStoreStatusRequest(

	StoreStatus status
) {}
