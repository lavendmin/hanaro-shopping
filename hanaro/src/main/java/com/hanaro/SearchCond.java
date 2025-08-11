package com.hanaro;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.StringUtils;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchCond {
	private String searchTerm;

	@Builder.Default
	@Schema(defaultValue = "1", minimum = "1")
	private Integer page = 1;

	@Builder.Default
	@Schema(defaultValue = "5", minimum = "1")
	private Integer size = 5;

	@Builder.Default
	@Schema(defaultValue = "id")
	private String sortField = "id";

	@Builder.Default
	@Schema(defaultValue = "desc")
	private String sortDirection = "desc";

	public boolean needSearch() { // 분기용
		return StringUtils.hasText(searchTerm);
	}

	public Pageable getPageable() {
		setDefault();

		Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);

		return PageRequest.of(page - 1, size, sort);
	}

	private void setDefault() {
		if (page == null)
			page = 1;
		if (size == null)
			size = 5;
		if (sortField == null)
			sortField = "id";
		if (sortDirection == null)
			sortDirection = "desc";
	}
}
