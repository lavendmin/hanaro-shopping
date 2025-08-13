package com.hanaro.orders.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SaleItemStatDTO {
	private Integer id;
	private String saledt;
	private Long itemId;

	private int cnt;
	private int amt;
}
