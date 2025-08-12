package com.hanaro.cart.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.hanaro.cart.entity.Cart;
import com.hanaro.member.entity.Member;

public interface CartRepository extends JpaRepository<Cart, Long> {
	@Query("SELECT c FROM Cart c WHERE c.customer = :member")
	Cart findByMember(@Param("member") Member member);

	Cart findByCustomer(Member member);

	boolean existsByCustomer(Member member);

}
