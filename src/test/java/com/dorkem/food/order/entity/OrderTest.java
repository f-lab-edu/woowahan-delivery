package com.dorkem.food.order.entity;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.dorkem.food.order.entity.embedded.OrderRequirement;
import com.dorkem.food.order.entity.embedded.UserDeliveryInfo;

@DisplayName("Order 엔티티 테스트")
class OrderTest {
	private Order order;

	@BeforeEach
	void setUp() {
		OrderRequirement orderRequirement = mock(OrderRequirement.class);
		UserDeliveryInfo userDeliveryInfo = mock(UserDeliveryInfo.class);
		List<OrderItem> orderItems = List.of(mock(OrderItem.class));

		order = Order.createOrder(1L, "테스트가게", 1L, "010-0000-0000", orderRequirement, userDeliveryInfo, orderItems);
	}

	@Test
	@DisplayName("주문 생성 시 현재 상태는 CREATED 이다")
	void 주문생성시_상태는_CREATED() {
		assertThat(order.getCurrentStatus()).isEqualTo(OrderStatus.CREATED);
	}

	@Test
	@DisplayName("주문 생성 시 히스토리에 CREATED가 쌓인다")
	void 주문시_히스토리에_CREATED_쌓임() {
		assertThat(order.getOrderStatusHistories()).hasSize(1);
		assertThat(order.getOrderStatusHistories().get(0).getStatus()).isEqualTo(OrderStatus.CREATED);
	}

	@Test
	@DisplayName("정상적인 상태 흐름")
	void 상태흐름이_정상일때() {
		order.requestPayment();
		order.completePayment();
		order.accept();
		order.startCooking();
		order.completeCooking();
		order.requestDispatch();
		order.startDelivery();
		order.completeDelivery();
	}

	@Test
	@DisplayName("잘못된 상태 전이 시 예외 발생")
	void 상태전이_잘못되면_에러남() {
		assertThatThrownBy(() -> order.startCooking())
			.isInstanceOf(IllegalStateException.class);
	}

	@Test
	@DisplayName("CANCELLED 상태에서 취소하면 예외가 발생한다")
	void Cancelled_일_때는_취소안됨() {
		order.cancel();

		assertThatThrownBy(() -> order.cancel())
			.isInstanceOf(IllegalStateException.class);
	}
}
