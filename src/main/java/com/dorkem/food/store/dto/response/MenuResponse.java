package com.dorkem.food.store.dto.response;

import java.util.List;

import com.dorkem.food.store.entity.Menu;

public record MenuResponse(
	Long id,
	String menuName,
	Integer price
) {
	public static MenuResponse createMenuResponse(Menu menu) {
		return new MenuResponse(
			menu.getMenuId(),
			menu.getMenuName(),
			menu.getPrice()
		);
	}

	public record MenuListResponse(List<MenuResponse> menus) {
		public static MenuListResponse createMenuListResponse(List<Menu> menus) {
			return new MenuListResponse(
				menus.stream()
					.map(MenuResponse::createMenuResponse)
					.toList()
			);
		}
	}
}
