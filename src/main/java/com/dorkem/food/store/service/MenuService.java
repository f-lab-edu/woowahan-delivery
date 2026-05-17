package com.dorkem.food.store.service;

import static com.dorkem.food.store.dto.response.MenuResponse.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dorkem.food.store.repository.MenuRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MenuService {

	private final MenuRepository menuRepository;

	@Transactional
	public MenuListResponse getMenusByStore(Long storeId) {
		return MenuListResponse.createMenuListResponse(menuRepository.findByStoreId(storeId));
	}
}
