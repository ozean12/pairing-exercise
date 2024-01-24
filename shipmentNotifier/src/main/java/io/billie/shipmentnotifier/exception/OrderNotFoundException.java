package io.billie.shipmentnotifier.exception;

import io.billie.shipmentnotifierspec.common.ResultStatus;

public class OrderNotFoundException extends BusinessException {

	public OrderNotFoundException(String message) {
		super(message);
	}

	@Override
	public ResultStatus getResultStatus() {
		return ResultStatus.OrderNotFoundException;
	}
}
