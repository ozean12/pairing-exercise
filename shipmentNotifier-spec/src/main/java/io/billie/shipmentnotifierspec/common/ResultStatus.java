package io.billie.shipmentnotifierspec.common;

import java.io.IOException;
import java.util.Properties;

public enum ResultStatus {

	SUCCESS(0, "success"),
	UNKNOWN(1, "unknown.error"),
	FAILURE(2, "internal.server.error"),
	OrderNotFoundException(3, "order.not.found"),
	ShipmentAmountIsGreaterThatOrderAmountException(4, "shipment.amount.greater.than.order.amount"),
	INVALID_PARAMETER(5, "invalid.parameter"),
	FORBIDDEN_REQUEST(6, "forbidden");

	private final String description;

	private final Integer status;

	ResultStatus(int status, String description) {
		this.status = status;
		String errorMsg = MessageHolder.ERROR_MESSAGE_PROPERTIES.getProperty(description);
		this.description = errorMsg != null ? errorMsg : description;
	}

	public String getDescription() {
		return description;
	}

	public Integer getStatus() {
		return status;
	}

	private static final class MessageHolder {
		private static final Properties ERROR_MESSAGE_PROPERTIES = new Properties();

		static {
			try {
				ERROR_MESSAGE_PROPERTIES.load((ResultStatus.class.getResourceAsStream("/error-messages.properties")));
			} catch (IOException e) {
				throw new ExceptionInInitializerError(e);
			}
		}
	}
}
