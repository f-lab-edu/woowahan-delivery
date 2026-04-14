package com.dorkem.food.menu.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.dorkem.food.store.entity.Store;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
	@ManyToOne
	@JoinColumn(name = "store_id")
	private Store store;

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

	public void toggleSoldOut() {
		this.isSoldOut = !this.isSoldOut;
	}

	@CreatedDate
	@Column(name = "created_at", nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(name = "modified_at", nullable = false)
	private LocalDateTime modifiedAt;

	private Menu(Store store, String menuName, String menuDescription, int price
	) {
		this.store = store;
		this.menuName = menuName;
		this.menuDescription = menuDescription;
		this.price = price;
	}

	public static Menu createMenu(Store store, String menuName, String menuDescription, int price
	) {
		return new Menu(store, menuName, menuDescription, price);
	}
}
