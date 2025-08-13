package com.hanaro.cart.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
	private final CartRepository cartRepository;
	private final CartItemRepository cartItemRepository;
	private final ItemRepository itemRepository;

	private final ItemService itemService;

	@Override
	@Transactional
	public CartResponseDTO addItemToCart(Member member, CartRequestDTO cartRequestDTO) {
		Cart cart = cartRepository.findByCustomer(member);

		Item item = itemRepository.findById(cartRequestDTO.itemId())
			.orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다."));

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
		CartItem cartItem = cartItemRepository.findByMemberAndItem(member, item).orElse(null);

		if (cartItem == null) {
			cartItem = new CartItem();
			cartItem.setQuantity(Math.min(cartRequestDTO.quantity(), item.getStock())); // 최대 재고량까지
			cartItem.setCart(cart);
			cartItem.setItem(item);
		} else { // 같은 아이템이 있으면 수량 더하기
			int quantity = Math.min(cartItem.getQuantity() + cartRequestDTO.quantity(), item.getStock());
			cartItem.setQuantity(quantity);
		}

		cartItemRepository.save(cartItem);

		CartResponseDTO cartResponseDTO = new CartResponseDTO();
		cartResponseDTO.setCartId(cart.getId());
		cartResponseDTO.setQuantity(cartItem.getQuantity());
		cartResponseDTO.setItem(itemService.toItemDTO(item));

		return cartResponseDTO;
	}

	@Override
	@Transactional
	public CartResponseDTO updateCart(Member member, CartRequestDTO cartRequestDTO) {
		Cart cart = cartRepository.findByCustomer(member);
		if (cart == null)
			throw new IllegalArgumentException("장바구니가 없습니다.");

		Item item = itemRepository.findById(cartRequestDTO.itemId())
			.orElseThrow(() -> new IllegalArgumentException("해당 상품을 찾을 수 없습니다."));

		if (item.getStock() == 0) {
			throw new IllegalArgumentException("해당 상품은 품절 되었습니다.");
		}

		CartItem cartItem = cartItemRepository.findByMemberAndItem(member, item).orElseThrow(
			() -> new EntityNotFoundException("해당 상품을 장바구니에서 찾을 수 없습니다."));

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
	@Transactional
	public String deleteItemFromCart(Member member, Long itemId) {
		Item item = itemRepository.findById(itemId)
			.orElseThrow(() -> new EntityNotFoundException("해당 상품을 찾을 수 없습니다."));

		CartItem cartItem = cartItemRepository.findByMemberAndItem(member, item).orElseThrow(
			() -> new EntityNotFoundException("해당 상품을 장바구니에서 찾을 수 없습니다."));

		cartItemRepository.delete(cartItem);

		return "해당 상품을 장바구니에서 삭제하였습니다.";
	}

	@Override
	@Transactional
	public void clearCart(Member member) {
		Cart cart = cartRepository.findByCustomer(member);
		List<CartItem> cartItems = cartItemRepository.findAllByCart_Customer(member);
		cartItemRepository.deleteAll(cartItems);
	}

	@Override
	public List<CartResponseDTO> getCartItems(Member member) {
		Cart cart = cartRepository.findByCustomer(member);
		List<CartItem> cartItems = cartItemRepository.findAllByCart_Customer(member);
		return cartItems.stream().map(this::toDTO).toList();
	}

	private CartResponseDTO toDTO(CartItem cartItem) {
		return CartResponseDTO.builder()
			.cartId(cartItem.getCart().getId())
			.quantity(cartItem.getQuantity())
			.item(itemService.toItemDTO(cartItem.getItem()))
			.build();
	}

}
