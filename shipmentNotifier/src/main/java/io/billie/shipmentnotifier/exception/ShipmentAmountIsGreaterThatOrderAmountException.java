package io.billie.shipmentnotifier.exception;

import io.billie.shipmentnotifierspec.common.ResultStatus;

public class ShipmentAmountIsGreaterThatOrderAmountException extends BusinessException {
	public ShipmentAmountIsGreaterThatOrderAmountException(String s) {
		super(s);
	}

	@Override
	public ResultStatus getResultStatus() {
		return ResultStatus.ShipmentAmountIsGreaterThatOrderAmountException;
	}
}
