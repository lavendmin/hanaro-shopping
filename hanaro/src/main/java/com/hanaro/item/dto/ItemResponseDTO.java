package com.hanaro.item.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ItemResponseDTO {
	private Long id;
	private String name;
	private String description;
	private int stock;
	private int price;
	private double discount;
	private List<ItemImageDTO> images;
}
