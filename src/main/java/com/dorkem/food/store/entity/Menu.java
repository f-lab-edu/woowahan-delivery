package com.dorkem.food.store.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menus")
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Menu {

	@Id
	@Getter
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id")
	private Long menuId;

	@Getter
	@Column(name = "store_id", nullable = false)
	private Long storeId;

	@Getter
	@Column(name = "menu_name", nullable = false)
	private String menuName;

	@Getter
	@Column(name = "menu_description")
	private String menuDescription;

	@Getter
	@Column(name = "price", nullable = false)
	private int price;

	@Getter
	@Column(name = "is_sold_out", nullable = false)
	private boolean isSoldOut = false;

	public void markSoldOut() {
		this.isSoldOut = true;
	}

	public void markOnSale() {
		this.isSoldOut = false;
	}

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "modified_at", nullable = false)
	private LocalDateTime modifiedAt;

	private Menu(Long storeId, String menuName, String menuDescription, int price
	) {
		this.storeId = storeId;
		this.menuName = menuName;
		this.menuDescription = menuDescription;
		this.price = price;
	}

	public static Menu createMenu(Long storeId, String menuName, String menuDescription, int price
	) {
		return new Menu(storeId, menuName, menuDescription, price);
	}
}
