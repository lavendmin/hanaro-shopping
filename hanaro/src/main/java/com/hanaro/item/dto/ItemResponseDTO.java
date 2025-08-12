package com.hanaro.item.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ItemResponseDTO extends ItemDTO {
	private Long id;
	private List<ItemImageDTO> images;
}
