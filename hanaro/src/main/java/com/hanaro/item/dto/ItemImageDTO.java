package com.hanaro.item.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hanaro.item.entity.ItemImage;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ItemImageDTO {
	private String orgname;
	private String savename;
	private String savedir;

	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private Long itemId;

	public ItemImageDTO(ItemImage itemImage) {
		orgname = itemImage.getOrgname();
		savename = itemImage.getSavename();
		savedir = itemImage.getSavedir();
		itemId = itemImage.getItem().getId();
	}

	public ItemImage toEntity() {
		return ItemImage.builder()
			.orgname(orgname)
			.savename(savename)
			.savedir(savedir)
			.build();
	}
}
