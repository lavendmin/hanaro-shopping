package com.hanaro.orders;

import org.hibernate.annotations.ColumnDefault;

import com.hanaro.BaseEntity;
import com.hanaro.member.entity.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Orders extends BaseEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private OrderStatus orderStatus; // status null이면 주문 취소 가능

	@Column(nullable = false)
	@ColumnDefault("0")
	private int totalAmount;

	@ManyToOne
	@JoinColumn(name = "customer", foreignKey = @ForeignKey(
		name = "fk_Orders_customer_Member",
		foreignKeyDefinition = "FOREIGN KEY (customer) REFERENCES Member(id) ON DELETE SET NULL"))
	private Member customer;

	/*
	@OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<OrderItem> orderItems = new ArrayList<>();
	 */
}
