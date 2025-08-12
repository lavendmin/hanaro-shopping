package com.hanaro.orders.entity;

import org.hibernate.annotations.ColumnDefault;

import com.hanaro.item.entity.Item;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@ColumnDefault("1")
	private int quantity;

	@Column(nullable = false)
	@ColumnDefault("0")
	private int price; // 할인율 반영 가격

	@ManyToOne
	@JoinColumn(name = "orders", nullable = false, foreignKey = @ForeignKey(
		name = "fk_OrderItem_orders",
		foreignKeyDefinition = "FOREIGN KEY (orders) REFERENCES Orders(id) ON DELETE CASCADE"))
	private Orders orders;

	@ManyToOne
	@JoinColumn(name = "item", nullable = false, foreignKey = @ForeignKey(
		name = "fk_OrderItem_item",
		foreignKeyDefinition = "FOREIGN KEY (item) REFERENCES Item(id) ON DELETE CASCADE"))
	private Item item;
}
