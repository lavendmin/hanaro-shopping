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
public class SaleStatDTO {
	private String saledt;
	private int ordercnt;
	private Integer totamt;
}
