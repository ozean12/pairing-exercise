package io.billie.shipmentnotifier.model.order;

import java.util.stream.Stream;

public enum OrderStatus {
	NEW(0), PARTIALLY_SHIPPED(1), FULLY_SHIPPED(2), CLOSED(3);


	private final int value;

	OrderStatus(int value) {
		this.value = value;
	}

	public static OrderStatus fromValue(int i) {
		return Stream.of(OrderStatus.values()).filter(status -> status.value == i)
				.findFirst()
				.orElseThrow(() -> new IllegalStateException("undefined value found for order type " + i));
	}

	public int toValue() {
		return value;
	}

}
