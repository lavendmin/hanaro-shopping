package com.hanaro.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hanaro.cart.dto.CartRequestDTO;
import com.hanaro.cart.dto.CartResponseDTO;
import com.hanaro.cart.entity.Cart;
import com.hanaro.cart.entity.CartItem;
import com.hanaro.cart.repository.CartItemRepository;
import com.hanaro.cart.repository.CartRepository;
import com.hanaro.item.entity.Item;
import com.hanaro.item.repository.ItemRepository;
import com.hanaro.item.service.ItemService;
import com.hanaro.member.entity.Member;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	// private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;

	private final ItemService itemService;

	@Override
	public CartResponseDTO addItemToCart(Member member, CartRequestDTO cartRequestDTO) {
		// Member member = memberRepository.findById(memberId)
		// 	.orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

		Cart cart = cartRepository.findByMember(member);

		Item item = itemRepository.findById(cartRequestDTO.itemId())
			.orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

		if (item.getStock() == 0) {
			throw new IllegalArgumentException("해당 상품은 품절 되었습니다.");
		}

		// 장바구니 없으면 새로 생성
		if (cart == null) {
			cart = new Cart();
			cart.setCustomer(member);
			cartRepository.save(cart);
		}

		// cartItem 저장
		CartItem cartItem = cartItemRepository.findByMemberAndItem(member, item);

		if (cartItem == null) {
			cartItem = new CartItem();
			cartItem.setQuantity(Math.min(cartRequestDTO.quantity(), item.getStock())); // 최대 재고량까지
			cartItem.setCart(cart);
			cartItem.setItem(item);
		} else { // 같은 아이템이 있으면 수량 더하기
			int quantity = Math.min(cartItem.getQuantity() + cartRequestDTO.quantity(), item.getStock());
			cartItem.setQuantity(quantity);
		}

		// if (item.getStock() < cartItem.getQuantity()) {
		// 	int possibleQuantity = item.getStock() - cartRequestDTO.quantity();
		// 	throw new IllegalArgumentException("주문 가능 수량을 초과하였습니다. 주문 가능 수량: " + possibleQuantity);
		// }

		cartItemRepository.save(cartItem);

		CartResponseDTO cartResponseDTO = new CartResponseDTO();
		cartResponseDTO.setCartId(cart.getId());
		cartResponseDTO.setQuantity(cartItem.getQuantity());
		cartResponseDTO.setItem(itemService.toItemDTO(item));

		return cartResponseDTO;
	}

	@Override
	public CartResponseDTO updateCart(Member member, CartRequestDTO cartRequestDTO) {
		Cart cart = cartRepository.findByMember(member);
		if (cart == null)
			throw new IllegalArgumentException("장바구니가 없습니다.");

		Item item = itemRepository.findById(cartRequestDTO.itemId())
			.orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

		if (item.getStock() == 0) {
			throw new IllegalArgumentException("해당 상품은 품절 되었습니다.");
		}

		CartItem cartItem = cartItemRepository.findByMemberAndItem(member, item);
		if (cartItem == null)
			throw new IllegalArgumentException("장바구니에 해당 상품이 없습니다.");

		// 업데이트
		cartItem.setQuantity(Math.min(cartRequestDTO.quantity(), item.getStock()));
		cartItemRepository.save(cartItem);

		CartResponseDTO cartResponseDTO = new CartResponseDTO();
		cartResponseDTO.setCartId(cart.getId());
		cartResponseDTO.setQuantity(cartItem.getQuantity());
		cartResponseDTO.setItem(itemService.toItemDTO(item));

		return cartResponseDTO;
	}

	@Override
	public String deleteItemFromCart(Member member, Long itemId) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

		CartItem cartItem = cartItemRepository.findByMemberAndItem(member, item);

		cartItemRepository.delete(cartItem);

		return "해당 상품을 장바구니에서 삭제하였습니다.";
	}

	@Override
	public void clearCart(Member member) {
		Cart cart = cartRepository.findByMember(member);
		List<CartItem> cartItems = cartItemRepository.findAllByCart_Customer(member);
		cartItemRepository.deleteAll(cartItems);
	}

}
