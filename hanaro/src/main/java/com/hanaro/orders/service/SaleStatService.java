package com.hanaro.orders.service;

import org.springframework.batch.core.BatchStatus;
import org.springframework.data.domain.Page;

import com.hanaro.PageCond;
import com.hanaro.orders.dto.SaleItemStatDTO;
import com.hanaro.orders.dto.SaleStatDTO;

public interface SaleStatService {
	BatchStatus runStatBatch() throws Exception;

	Page<SaleStatDTO> getSaleStat(PageCond pageCond);

	Page<SaleItemStatDTO> getSaleItemStat(PageCond pageCond);
}
