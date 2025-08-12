package com.hanaro;

import org.springframework.util.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchCond extends PageCond {
	@Schema(example = "", defaultValue = "")
	private String searchTerm;

	public boolean needSearch() { // 분기용
		return StringUtils.hasText(searchTerm);
	}

}
