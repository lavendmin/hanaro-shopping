package com.hanaro.cart.entity;

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
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(
	uniqueConstraints = {
		@UniqueConstraint(name = "uk_Cart_Item", columnNames = {"Cart", "Item"})
	}
)
public class CartItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	@ColumnDefault("1")
	private int quantity;

	@ManyToOne
	@JoinColumn(name = "cart", nullable = false, foreignKey = @ForeignKey(
		name = "fk_CartItem_cart",
		foreignKeyDefinition = "FOREIGN KEY (cart) REFERENCES Cart(id) ON DELETE CASCADE"))
	private Cart cart;

	@ManyToOne
	@JoinColumn(name = "item", nullable = false, foreignKey = @ForeignKey(
		name = "fk_CartItem_item",
		foreignKeyDefinition = "FOREIGN KEY (item) REFERENCES Item(id) ON DELETE CASCADE"))
	private Item item;
}
