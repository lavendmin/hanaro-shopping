package com.hanaro.cart.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanaro.cart.entity.CartItem;
import com.hanaro.item.entity.Item;
import com.hanaro.member.entity.Member;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
	@Query("SELECT c FROM CartItem c WHERE c.cart.customer = :member AND c.item = :item")
	Optional<CartItem> findByMemberAndItem(@Param("member") Member member, @Param("item") Item item);

	// List<CartItem> findAllByMember(Member member);

	List<CartItem> findAllByCart_Customer(Member member);
}
