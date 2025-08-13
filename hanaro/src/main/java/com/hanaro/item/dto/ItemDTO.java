package com.hanaro.item.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class ItemDTO {
	@NotBlank(message = "상품명을 입력해주세요.")
	@Schema(name = "name", example = "newItem")
	private String name;

	@Size(max = 500, message = "상품 설명은 500자를 넘을 수 없습니다.")
	@Schema(name = "description", example = "new Description")
	private String description;

	@Min(value = 1, message = "재고는 최소 1개 이상이어야 합니다.")
	@Schema(name = "stock", example = "20")
	private int stock;

	@Min(value = 0, message = "가격은 0원 이상이어야 합니다.")
	@Schema(name = "price", example = "10000")
	private int price;

	@DecimalMax(value = "100.0", inclusive = true, message = "할인율은 100퍼센트 이하여야 합니다.")
	@DecimalMin(value = "0.0", inclusive = true, message = "할인율은 0퍼센트 이상이어야 합니다.")
	@Schema(name = "discount", example = "10.0")
	private double discount;
}
