// package com.dorkem.food.order.service;
//
// import static org.assertj.core.api.Assertions.*;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.TestPropertySource;
// import org.springframework.transaction.annotation.Transactional;
//
// import com.dorkem.food.store.entity.Menu;
// import com.dorkem.food.order.dto.request.DeliveryAddressRequest;
// import com.dorkem.food.order.dto.request.OrderCreateRequest;
// import com.dorkem.food.order.dto.request.OrderCreateItemRequest;
// import com.dorkem.food.order.dto.response.OrderHistoryPageResponse;
// import com.dorkem.food.order.dto.response.OrderResponse;
// import com.dorkem.food.order.entity.Order;
// import com.dorkem.food.order.entity.OrderStatus;
// import com.dorkem.food.order.repository.OrderRepository;
// import com.dorkem.food.store.entity.Store;
// import com.dorkem.food.store.entity.StoreStatus;
// import com.dorkem.food.user.entity.Customer;
// import com.dorkem.food.user.entity.Owner;
// import com.dorkem.food.user.entity.User;
//
// import jakarta.persistence.EntityManager;
// import jakarta.persistence.PersistenceContext;
//
// @SpringBootTest
// @Transactional
// @TestPropertySource(properties = "spring.sql.init.mode=never")
// class OrderServiceTest {
//
// 	@PersistenceContext
// 	EntityManager em;
// 	@Autowired
// 	OrderService orderService;
// 	@Autowired
// 	OrderRepository orderRepository;
//
// 	Customer customer;
// 	Owner owner;
// 	Store store;
// 	Menu bbulingCle;
// 	Menu cheeseBall;
//
// 	@BeforeEach
// 	void setUp() {
// 		User customerUser = createUser("최재혁", "password", "jaehyeok@ar.co.kr", "123-4567-8910", LoginType.KAKAO);
// 		customer = createCustomer(customerUser);
// 		User ownerUser = createUser("NEO", "password", "neo@ar.co.kr", "109-8765-4321", LoginType.KAKAO);
// 		owner = createOwner(ownerUser, "109-87-65432", "NEO");
// 		store = createStore(owner);
// 		bbulingCle = createMenu(store, "뿌링클", "맛있음", 20000);
// 		cheeseBall = createMenu(store, "치즈볼", "진짜맛있음", 5000);
// 	}
//
// 	@Test
// 	void 상품_주문() throws Exception {
// 		DeliveryAddressRequest deliveryInfo = new DeliveryAddressRequest(
// 			"서울시 금천구", "123호", "-", "-", "-"
// 		);
//
// 		List<OrderCreateItemRequest> itemRequests = new ArrayList<>();
// 		itemRequests.add(new OrderCreateItemRequest(bbulingCle.getMenuId(), 1));
// 		itemRequests.add(new OrderCreateItemRequest(cheeseBall.getMenuId(), 2));
//
// 		OrderCreateRequest request = new OrderCreateRequest(
// 			store.getStoreId(),
// 			itemRequests,
// 			deliveryInfo,
// 			"-",
// 			false,
// 			false
// 		);
//
// 		String orderId = orderService.createOrder(customer.getCustomerId(), request);
//
// 		Order order = orderRepository.findById(orderId)
// 			.orElseThrow(() -> new AssertionError("주문이 생성되지 않았습니다."));
//
// 		assertThat(order.getCurrentStatus()).isEqualTo(OrderStatus.CREATED);
// 		assertThat(order.getOrderItems()).hasSize(2);
// 		assertThat(order.getTotalPrice()).isEqualTo(20000 + 5000 * 2);
// 	}
//
// 	@Test
// 	void 없는_유저로_주문시_예외발생() {
// 		OrderCreateRequest request = new OrderCreateRequest(
// 			store.getStoreId(),
// 			List.of(new OrderCreateItemRequest(bbulingCle.getMenuId(), 1)),
// 			new DeliveryAddressRequest("-", "-", "-", "-", "-"),
// 			"-", false, false
// 		);
//
// 		assertThatThrownBy(() -> orderService.createOrder(-1L, request))
// 			.isInstanceOf(IllegalArgumentException.class);
// 	}
//
// 	@Test
// 	void 없는_메뉴로_주문시_예외발생() {
// 		OrderCreateRequest request = new OrderCreateRequest(
// 			store.getStoreId(),
// 			List.of(new OrderCreateItemRequest(-1L, 1)),
// 			new DeliveryAddressRequest("-", "-", "-", "-", "-"),
// 			"-", false, false
// 		);
//
// 		assertThatThrownBy(() -> orderService.createOrder(customer.getCustomerId(), request))
// 			.isInstanceOf(IllegalArgumentException.class);
// 	}
//
// 	@Test
// 	@DisplayName("정상적인 상태 흐름")
// 	void 상태흐름이_정상일때() {
// 		String orderId = createTestOrder();
// 		// orderService.requestPayment(orderId);
// 		// assertOrderStatus(orderId, OrderStatus.PAYMENT_REQUESTED);
// 		// orderService.completePayment(orderId);
// 		// assertOrderStatus(orderId, OrderStatus.PAYMENT_COMPLETED);
// 		orderService.acceptOrder(orderId);
// 		assertOrderStatus(orderId, OrderStatus.ACCEPTED);
//
// 		orderService.startCooking(orderId);
// 		assertOrderStatus(orderId, OrderStatus.COOKING);
//
// 		orderService.completeCooking(orderId);
// 		assertOrderStatus(orderId, OrderStatus.COOK_COMPLETED);
//
// 		orderService.requestDispatch(orderId);
// 		assertOrderStatus(orderId, OrderStatus.DISPATCH_REQUESTED);
//
// 		orderService.completeDispatch(orderId);
// 		assertOrderStatus(orderId, OrderStatus.DISPATCH_COMPLETED);
//
// 		orderService.startDelivery(orderId);
// 		assertOrderStatus(orderId, OrderStatus.DELIVERING);
//
// 		orderService.completeDelivery(orderId);
// 		assertOrderStatus(orderId, OrderStatus.DELIVERED);
// 	}
//
// 	@Test
// 	void 가게가_주문_거절() {
// 		String orderId = createTestOrder();
// 		orderService.requestPayment(orderId);
// 		orderService.completePayment(orderId);
// 		orderService.rejectOrder(orderId);
//
// 		assertOrderStatus(orderId, OrderStatus.REJECTED);
// 	}
//
// 	@Test
// 	void 없는_주문_취소시_예외발생() {
// 		assertThatThrownBy(() -> orderService.cancelOrder("없는ID"))
// 			.isInstanceOf(IllegalArgumentException.class);
// 	}
//
// 	@Test
// 	void 잘못된_상태에서_취소불가() {
// 		String orderId = createTestOrder();
// 		orderService.requestPayment(orderId);
// 		orderService.completePayment(orderId);
// 		assertThatNoException().isThrownBy(() -> orderService.cancelOrder(orderId));
// 	}
//
// 	@Test
// 	void 현재_진행중인_주문_조회_성공() {
// 		String orderId = createTestOrder();
// 		OrderResponse response = orderService.getCurrentUserOrders(customer.getCustomerId());
// 		assertThat(response).isNotNull();
// 		assertThat(response.orderId()).isEqualTo(orderId);
// 	}
//
// 	@Test
// 	void 진행중인_주문이_없으면_예외발생() {
// 		assertThatThrownBy(() -> orderService.getCurrentUserOrders(customer.getCustomerId()))
// 			.isInstanceOf(IllegalArgumentException.class);
// 	}
//
// 	@Test
// 	void 주문_히스토리_조회_성공() {
// 		String orderId1 = createTestOrder();
// 		entireProcess(orderId1);
//
// 		String orderId2 = createTestOrder();
// 		entireProcess(orderId2);
//
// 		OrderHistoryPageResponse response = orderService.getOrderHistory(customer.getCustomerId(), null, 10);
// 		assertThat(response.orders()).hasSize(2);
// 		assertThat(response.hasNext()).isFalse();
// 	}
//
// 	@Test
// 	void 주문_히스토리_무한스크롤_테스트() {
// 		for (int i = 0; i < 3; i++) {
// 			String oid = createTestOrder();
// 			entireProcess(oid);
// 		}
//
// 		OrderHistoryPageResponse firstPage = orderService.getOrderHistory(
// 			customer.getCustomerId(), null, 2
// 		);
// 		assertThat(firstPage.orders()).hasSize(2);
// 		assertThat(firstPage.hasNext()).isTrue();
//
// 		OrderHistoryPageResponse secondPage = orderService.getOrderHistory(
// 			customer.getCustomerId(), firstPage.nextCursor(), 2
// 		);
// 		assertThat(secondPage.orders()).hasSize(1);
// 		assertThat(secondPage.hasNext()).isFalse();
// 	}
//
// 	@Test
// 	void 주문_내역_삭제_성공() {
// 		String orderId = createTestOrder();
// 		entireProcess(orderId);
//
// 		orderService.deleteOrderHistory(customer.getCustomerId(), orderId);
// 		OrderHistoryPageResponse response = orderService.getOrderHistory(
// 			customer.getCustomerId(), null, 10
// 		);
// 		assertThat(response.orders()).isEmpty();
// 	}
//
// 	@Test
// 	void 없는_주문_삭제시_예외발생() {
// 		assertThatThrownBy(() -> orderService.deleteOrderHistory(customer.getCustomerId(), "없는ID"))
// 			.isInstanceOf(IllegalArgumentException.class);
// 	}
//
// 	private String createTestOrder() {
// 		DeliveryAddressRequest deliveryInfo = new DeliveryAddressRequest(
// 			"서울시 금천구", "123호", "-", "-", "-"
// 		);
// 		OrderCreateRequest request = new OrderCreateRequest(
// 			store.getStoreId(),
// 			List.of(new OrderCreateItemRequest(bbulingCle.getMenuId(), 1)),
// 			deliveryInfo,
// 			"-",
// 			false,
// 			false
// 		);
// 		return orderService.createOrder(customer.getCustomerId(), request);
// 	}
//
// 	private User createUser(String userName, String password, String email,
// 		String phoneNumber, LoginType loginType) {
// 		User user = User.createUser(
// 			loginType,
// 			email,
// 			userName,
// 			password,
// 			phoneNumber
// 		);
// 		em.persist(user);
// 		return user;
// 	}
//
// 	private Customer createCustomer(User user) {
// 		Customer customer = Customer.createCustomer(user);
// 		em.persist(customer);
// 		return customer;
// 	}
//
// 	private Owner createOwner(User user, String businessNumber, String ownerName) {
// 		Owner owner = Owner.createOwner(user, businessNumber, ownerName);
// 		em.persist(owner);
// 		return owner;
// 	}
//
// 	private Store createStore(Owner owner) {
// 		Store store = Store.createStore(
// 			owner,
// 			"BBQ",
// 			"123-45-67890",
// 			"서울특별시 강남구",
// 			"15층",
// 			null, null,
// 			StoreStatus.OPEN,
// 			null, null,
// 			15000,
// 			3000
// 		);
// 		em.persist(store);
// 		return store;
// 	}
//
// 	private Menu createMenu(Store store, String menuName, String menuDescription, int price) {
// 		Menu menu = Menu.createMenu(
// 			store,
// 			menuName,
// 			menuDescription,
// 			price
// 		);
// 		em.persist(menu);
// 		return menu;
// 	}
//
// 	private void assertOrderStatus(String orderId, OrderStatus expected) {
// 		Order order = orderRepository.findById(orderId)
// 			.orElseThrow(() -> new AssertionError("주문을 찾을 수 없습니다: " + orderId));
// 		assertThat(order.getCurrentStatus()).isEqualTo(expected);
// 	}
//
// 	private void entireProcess(String orderId) {
// 		orderService.acceptOrder(orderId);
// 		orderService.startCooking(orderId);
// 		orderService.completeCooking(orderId);
// 		orderService.requestDispatch(orderId);
// 		orderService.completeDispatch(orderId);
// 		orderService.startDelivery(orderId);
// 		orderService.completeDelivery(orderId);
// 	}
// }
