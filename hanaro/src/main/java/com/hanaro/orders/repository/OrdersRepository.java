package com.hanaro.orders.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.hanaro.orders.entity.OrderItem;
import com.hanaro.orders.entity.OrderStatus;
import com.hanaro.orders.entity.Orders;
import com.hanaro.orders.entity.SaleStat;

public interface OrdersRepository extends JpaRepository<Orders, Long>, OrdersQDslRepository {
	@Modifying
	@Transactional
	@Query("UPDATE Orders o SET o.orderStatus = :nextStatus, o.statedAt = now()"
		+ " WHERE o.orderStatus = :status AND o.statedAt <= :timeToUp")
	int updateStatusBatch(
		@Param("nextStatus") OrderStatus nextStatus,
		@Param("status") OrderStatus status,
		@Param("timeToUp") LocalDateTime timeToUp);

	@Query(value = "select 'today' as saledt, count(*) as ordercnt, 0 as totamt from Orders o"
		+ " where o.createdAt between concat(:saledt, ' 00:00:00.00') and concat(:saledt, ' 23:59:59.99')", nativeQuery = true)
	public SaleStat getTodayStat(@Param("saledt") String saledt); // 

	@Query(value =
		"select oi.item as id, max(oi.id) as orders, oi.item, sum(oi.quantity) as quantity, sum(oi.price) as price"
			+ "  from Orders o inner join OrderItem oi on o.id = oi.orders"
			+ " where o.createdAt between concat(:saledt, ' 00:00:00.00') and concat(:saledt, ' 23:59:59.99')"
			+ " group by oi.item", nativeQuery = true)
	public List<OrderItem> getTodayItemStat(@Param("saledt") String saledt);
}
