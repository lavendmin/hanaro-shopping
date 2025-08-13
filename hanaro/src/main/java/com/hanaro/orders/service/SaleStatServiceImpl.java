package com.hanaro.orders.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.hanaro.PageCond;
import com.hanaro.orders.dto.SaleItemStatDTO;
import com.hanaro.orders.dto.SaleStatDTO;
import com.hanaro.orders.entity.OrderStatus;
import com.hanaro.orders.entity.SaleItemStat;
import com.hanaro.orders.entity.SaleStat;
import com.hanaro.orders.repository.OrdersRepository;
import com.hanaro.orders.repository.SaleItemStatRepository;
import com.hanaro.orders.repository.SaleStatRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SaleStatServiceImpl implements SaleStatService {
	private final JobLauncher jobLauncher;
	private final Job statJob;

	private final OrdersRepository ordersRepository;
	private final SaleStatRepository saleStatRepository;
	private final SaleItemStatRepository saleItemStatRepository;

	@Override
	public BatchStatus runStatBatch() throws Exception {
		JobParameters jobParameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis())
			.addString("saledt", LocalDate.now().toString())
			.toJobParameters();

		return jobLauncher.run(statJob, jobParameters).getStatus();
	}

	@Override
	public Page<SaleStatDTO> getSaleStat(PageCond pageCond) {
		Pageable pageable = pageCond.getPageable();
		Page<SaleStat> saleStats = saleStatRepository.findAll(pageable);
		return saleStats.map(SaleStatServiceImpl::toSaleStatDTO);
	}

	@Override
	public Page<SaleItemStatDTO> getSaleItemStat(PageCond pageCond) {
		Pageable pageable = pageCond.getPageable();
		Page<SaleItemStat> saleItemStats = saleItemStatRepository.findAll(pageable);
		return saleItemStats.map(SaleStatServiceImpl::toSaleItemStatDTO);
	}

	private static SaleStatDTO toSaleStatDTO(SaleStat saleStat) {
		return SaleStatDTO.builder()
			.saledt(saleStat.getSaledt())
			.ordercnt(saleStat.getOrdercnt())
			.totamt(saleStat.getTotamt())
			.build();
	}

	private static SaleItemStatDTO toSaleItemStatDTO(SaleItemStat saleStat) {
		return SaleItemStatDTO.builder()
			.id(saleStat.getId())
			.saledt(saleStat.getSaledt().getSaledt())
			.itemId(saleStat.getItem().getId())
			.cnt(saleStat.getCnt())
			.amt(saleStat.getAmt())
			.build();
	}

	@Scheduled(cron = "0/10 * * * * *")
	public void updateStatusBatch() throws Exception {
		OrderStatus status = OrderStatus.PAID;
		while (status != OrderStatus.DELIVERED) {
			LocalDateTime now = LocalDateTime.now();

			int affectedRowCount = ordersRepository.updateStatusBatch(
				status.getNextStatus(), status, now.minusMinutes(status.statusInterval()));
			System.out.println("affectedRowCount = " + affectedRowCount);

			status = status.getNextStatus();
		}
	}

	@Scheduled(cron = "0/10 * * * * *")
	public void batchStatistics() throws Exception {
		runStatBatch();
	}
}
