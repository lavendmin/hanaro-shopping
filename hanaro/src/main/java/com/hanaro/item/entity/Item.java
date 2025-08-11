package com.hanaro.item.entity;

import java.util.List;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 50, unique = true)
	private String name;

	@Column
	private String description;

	@Column(nullable = false)
	@ColumnDefault("0")
	private int stock;

	@Column(nullable = false)
	@ColumnDefault("0")
	private int price;

	@Column(nullable = false)
	@Builder.Default
	private double discount = 0.0;

	@OneToMany(mappedBy = "item", cascade = CascadeType.ALL)
	private List<ItemImage> images;
}
