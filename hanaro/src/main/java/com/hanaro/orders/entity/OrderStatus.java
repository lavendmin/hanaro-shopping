package com.hanaro.orders.entity;

public enum OrderStatus {
	PAID, // 결제 완료
	PREPARING, // 배송 준비
	SHIPPING, // 배송 중
	DELIVERED // 배송 완료
}
