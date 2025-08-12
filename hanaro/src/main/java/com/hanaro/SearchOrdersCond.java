package com.hanaro;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class SearchOrdersCond extends PageCond {
	@Schema(example = "", defaultValue = "")
	private String searchMember;

	@Schema(example = "", defaultValue = "")
	private String searchItem;

	@Schema(description = "조회 시작일(포함) yyyy-MM-dd", example = "2025-08-11")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate fromDate;

	@Schema(description = "조회 종료일(포함) yyyy-MM-dd", example = "2025-08-12")
	@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
	private LocalDate toDate;

	public boolean needMemberSearch() { // 분기용
		return StringUtils.hasText(searchMember);
	}

	public boolean needItemSearch() {
		return StringUtils.hasText(searchItem);
	}

}
