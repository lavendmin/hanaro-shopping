package com.hanaro.config;

import java.util.List;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import com.hanaro.orders.entity.OrderItem;
import com.hanaro.orders.entity.SaleItemStat;
import com.hanaro.orders.entity.SaleStat;
import com.hanaro.orders.repository.OrdersRepository;
import com.hanaro.orders.repository.SaleStatRepository;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class OrderBatchConfig {
	private final OrdersRepository ordersRepository;
	private final SaleStatRepository saleStatRepository;

	// Job -> Step -> Reader(Orders) -> Processor -> Writer(SaleStat)

	@Bean
	public Job statJob(JobRepository jobRepository, Step statStep) {
		return new JobBuilder("statJob", jobRepository)
			.incrementer(new RunIdIncrementer())
			.start(statStep)
			.build();
	}

	@Bean
	public Step statStep(JobRepository jobRepository,
		PlatformTransactionManager transactionManager) {
		return new StepBuilder("statStep", jobRepository)
			.<SaleStat, SaleStat>chunk(5, transactionManager)
			.reader(statReader(null)) // StepScope로 CSV 경로 받음
			.processor(statProcessor(null))
			.writer(statWriter())
			.build();
	}

	@Bean
	public ItemWriter<SaleStat> statWriter() {
		return new RepositoryItemWriterBuilder<SaleStat>()
			.repository(saleStatRepository)
			.methodName("save")
			.build();
	}

	@Bean
	@StepScope
	public ItemProcessor<SaleStat, SaleStat> statProcessor(@Value("#{jobParameters['saledt']}") String saledt) {
		System.out.println("xxx - saledt = " + saledt);
		return stat -> {
			SaleStat todayStat = SaleStat.builder()
				.saledt(saledt)
				.ordercnt(stat.getOrdercnt())
				.build();
			System.out.println("bbb - todayStat = " + todayStat);

			List<OrderItem> oitems = ordersRepository.getTodayItemStat(saledt);
			List<SaleItemStat> todayItems = oitems.stream()
				.map(oi -> SaleItemStat.builder()
					.saledt(todayStat)
					.item(oi.getItem())
					.amt(oi.getPrice())
					.cnt(oi.getQuantity())
					.build())
				.toList();
			System.out.println("bbb - todayItems = " + todayItems);

			todayStat.setSaleItemStats(todayItems);
			int sum = todayItems.stream().mapToInt(SaleItemStat::getAmt).sum();
			todayStat.setTotamt(sum);

			return todayStat;
		};
	}

	@Bean
	@StepScope
	public ItemReader<SaleStat> statReader(@Value("#{jobParameters['saledt']}") String saledt) {
		SaleStat todayStat = ordersRepository.getTodayStat(saledt); // 통계 낼 기반이 되는 데이터 읽어 옴
		System.out.println("bbr - todayStat = " + todayStat);
		return new ListItemReader<>(List.of(todayStat));
	}
}
