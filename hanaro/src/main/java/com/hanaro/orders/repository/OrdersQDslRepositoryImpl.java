package com.hanaro.orders.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import com.hanaro.SearchOrdersCond;
import com.hanaro.item.entity.QItem;
import com.hanaro.member.entity.QMember;
import com.hanaro.orders.entity.Orders;
import com.hanaro.orders.entity.QOrderItem;
import com.hanaro.orders.entity.QOrders;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrdersQDslRepositoryImpl implements OrdersQDslRepository {
	private final JPAQueryFactory query;

	@Override
	public Page<Orders> search(SearchOrdersCond searchOrdersCond) {
		QOrders o = QOrders.orders;
		QMember m = QMember.member;
		QOrderItem oi = QOrderItem.orderItem;
		QItem i = QItem.item;

		BooleanBuilder bb = new BooleanBuilder();

		// 회원 - 닉네임, 이메일에서 키워드 검색
		if (searchOrdersCond.needMemberSearch()) {
			String term = searchOrdersCond.getSearchMember();
			bb.and(m.nickname.containsIgnoreCase(term)
				.or(m.email.containsIgnoreCase(term)));
		}

		// 상품 - 이름, 설명에서 키워드 검색 - EXISTS 서브쿼리로 주문내역 필터
		if (searchOrdersCond.needItemSearch()) {
			String term = searchOrdersCond.getSearchItem();
			BooleanExpression itemExits = JPAExpressions.selectOne()
				.from(oi)
				.join(oi.item, i)
				.where(
					oi.orders.eq(o),
					i.name.containsIgnoreCase(term)
						.or(i.description.coalesce("").containsIgnoreCase(term))
				)
				.exists();
			bb.and(itemExits);
		}

		// 날짜 필터링
		LocalDate from = searchOrdersCond.getFromDate();
		LocalDate to = searchOrdersCond.getToDate();
		if (from != null || to != null) {
			LocalDateTime start = from != null ? from.atStartOfDay() : null;
			LocalDateTime end = to != null ? to.plusDays(1).atStartOfDay() : null;

			if (start != null)
				bb.and(o.createdAt.goe(start));
			if (end != null)
				bb.and(o.createdAt.lt(end));
		}

		Pageable pageable = searchOrdersCond.getPageable();

		List<Orders> content = query.selectFrom(o)
			.leftJoin(o.customer, m).fetchJoin() // 다대일
			.where(bb)
			.orderBy(o.id.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		JPAQuery<Long> countQuery = query.select(o.id.count())
			.from(o)
			.where(bb);

		return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
	}
}
