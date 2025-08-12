package com.hanaro.orders.entity;

public enum OrderStatus {
	PAID, // 결제 완료
	PREPARING, // 배송 준비
	SHIPPING, // 배송 중
	DELIVERED; // 배송 완료

	public OrderStatus getNextStatus() {
		return switch (this) {
			case PAID -> PREPARING;
			case PREPARING -> SHIPPING;
			case SHIPPING -> DELIVERED;
			default -> throw new IllegalStateException("Cannot determine next status for: " + this);
		};
	}

	public int statusInterval() {
		return switch (this) {
			case PAID -> 5;
			case PREPARING -> 15;
			case SHIPPING -> 60;
			default -> -1;
		};
	}
}
