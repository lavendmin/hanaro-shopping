package com.hanaro.item.dto;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hanaro.item.entity.Item;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class ItemRequestDTO extends ItemDTO {
	private List<MultipartFile> files;

	public Item toEntity() {
		return Item.builder()
			.name(getName())
			.description(getDescription())
			.stock(getStock())
			.price(getPrice())
			.discount(getDiscount())
			.build();
	}
}
