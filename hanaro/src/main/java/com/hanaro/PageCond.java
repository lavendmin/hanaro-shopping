package com.hanaro;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PageCond {
	@Schema(defaultValue = "1", minimum = "1")
	private Integer page = 1;

	@Schema(defaultValue = "5", minimum = "1")
	private Integer size = 5;

	@Schema(defaultValue = "id")
	private String sortField = "id";

	@Schema(defaultValue = "desc")
	private String sortDirection = "desc";

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
