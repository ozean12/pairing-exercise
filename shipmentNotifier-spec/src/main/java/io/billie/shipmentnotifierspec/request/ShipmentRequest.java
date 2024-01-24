package io.billie.shipmentnotifierspec.request;


import java.io.Serializable;
import java.math.BigDecimal;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;


@Data
public class ShipmentRequest implements Serializable {


	@NotBlank(message = "invalid.order.trackingCode")
	private String orderTrackingCode;

	@NotNull(message = "invalid.shipment.amount")
	@Min(value = 1, message = "shipment.amount.must.greater.zero")
	private BigDecimal shipmentAmount;

	@NotBlank(message = "invalid.merchant.number")
	private String merchantNumber;

}