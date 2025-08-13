package com.hanaro.cart.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Commit;

import com.hanaro.RepositoryTest;
import com.hanaro.cart.entity.Cart;
import com.hanaro.cart.entity.CartItem;
import com.hanaro.item.entity.Item;
import com.hanaro.item.repository.ItemRepository;
import com.hanaro.member.entity.Member;
import com.hanaro.member.repository.MemberRepository;

class CartRepositoryTest extends RepositoryTest {
	@Autowired
	CartRepository cartRepository;

	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	MemberRepository memberRepository;

	@Autowired
	ItemRepository itemRepository;

	@Test
	@Order(1)
	@Commit
	void makeCartPerMember() {
		List<Member> members = memberRepository.findAll();

		members.forEach(member -> {
			if (!cartRepository.existsByCustomer(member)) {
				Cart cart = new Cart();
				cart.setCustomer(member);
				cartRepository.save(cart);
			}
		});
		assertEquals(members.size(), cartRepository.count());
	}

	@Test
	@Order(2)
	@Commit
	void addTest() {
		long preCount = cartItemRepository.count();

		for (long memberId = 2; memberId <= 10; memberId++) {
			Member member = memberRepository.findById(memberId).orElseThrow();
			Cart cart = cartRepository.findByCustomer(member);

			CartItem cartItem;
			for (long itemId = 11; itemId <= 15; itemId++) {
				Item item = itemRepository.findById(itemId).orElseThrow();
				cartItem = new CartItem();
				cartItem.setItem(item);
				cartItem.setCart(cart);
				cartItem.setQuantity(2);

				cartItemRepository.save(cartItem);
			}
		}

		assertEquals(preCount + 45, cartItemRepository.count());
	}
}
