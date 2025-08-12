package com.hanaro.orders.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.hanaro.PageCond;
import com.hanaro.SearchOrdersCond;
import com.hanaro.cart.entity.CartItem;
import com.hanaro.cart.repository.CartItemRepository;
import com.hanaro.cart.service.CartService;
import com.hanaro.item.entity.Item;
import com.hanaro.item.repository.ItemRepository;
import com.hanaro.member.entity.Member;
import com.hanaro.orders.dto.OrderDTO;
import com.hanaro.orders.dto.OrderItemDTO;
import com.hanaro.orders.entity.OrderItem;
import com.hanaro.orders.entity.OrderStatus;
import com.hanaro.orders.entity.Orders;
import com.hanaro.orders.repository.OrderItemRepository;
import com.hanaro.orders.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrdersServiceImpl implements OrdersService {
	private final OrdersRepository ordersRepository;
	private final OrderItemRepository orderItemRepository;
	private final CartItemRepository cartItemRepository;
	private final CartService cartService;
	private final ItemRepository itemRepository;

	@Override
	public OrderDTO createOrder(Member member) {
		List<CartItem> cartItems = cartItemRepository.findAllByCart_Customer(member);

		Orders orders = new Orders();
		orders.setCustomer(member);
		orders.setOrderStatus(OrderStatus.PAID);

		ordersRepository.save(orders);

		OrderDTO orderDTO = new OrderDTO();
		List<OrderItemDTO> orderItemDTOs = new ArrayList<>();

		int total = 0;

		for (CartItem cartItem : cartItems) {
			Item item = cartItem.getItem();
			OrderItem orderItem = new OrderItem();

			int discountedPrice = (int)((1 - 0.01 * item.getDiscount()) * item.getPrice());
			total += discountedPrice * cartItem.getQuantity();

			// 각 cartItem -> orderItem
			orderItem.setOrders(orders);
			orderItem.setItem(item);
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setPrice(discountedPrice);

			orderItemRepository.save(orderItem);

			// item 재고 수량 조정
			item.setStock(item.getStock() - orderItem.getQuantity());
			itemRepository.save(item);

			orderItemDTOs.add(toOrderItemDTO(orderItem));
		}

		orders.setTotalAmount(total);

		// TODO: 장바구니 안의 내용 다 지우기 함수 <- orderItem으로 다 옮긴 게 성공한 뒤에 하고 싶음. 트랜잭션...?
		cartService.clearCart(member); // 장바구니 비우기

		orderDTO.setOrderStatus(orders.getOrderStatus());
		orderDTO.setTotalAmount(orders.getTotalAmount());
		orderDTO.setOrderItems(orderItemDTOs);

		return orderDTO;
	}

	@Override
	public Page<OrderDTO> getMyOrders(Member member, PageCond pageCond) {
		Pageable pageable = pageCond.getPageable();

		Page<Orders> orders = ordersRepository.findAll(pageable);

		return orders.map(OrdersServiceImpl::toOrderDTO);
	}

	@Override
	public Page<OrderDTO> getOrders(SearchOrdersCond searchOrdersCond) {
		Page<Orders> orders = ordersRepository.search(searchOrdersCond);

		return orders.map(OrdersServiceImpl::toOrderDTO);
	}

	private static OrderItemDTO toOrderItemDTO(OrderItem orderItem) {
		return OrderItemDTO.builder()
			.orderId(orderItem.getOrders().getId())
			.itemId(orderItem.getItem().getId())
			.itemName(orderItem.getItem().getName())
			.quantity(orderItem.getQuantity())
			.discountedPrice(orderItem.getPrice())
			.discount(orderItem.getItem().getDiscount())
			.originalPrice(orderItem.getItem().getPrice())
			.build();
	}

	private static OrderDTO toOrderDTO(Orders orders) {
		List<OrderItemDTO> orderItemDTOs = orders.getOrderItems().stream()
			.map(OrdersServiceImpl::toOrderItemDTO).toList();

		return OrderDTO.builder()
			.orderStatus(orders.getOrderStatus())
			.totalAmount(orders.getTotalAmount())
			.orderItems(orderItemDTOs)
			.build();
	}
}
