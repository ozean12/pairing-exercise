package io.billie.shipmentnotifierspec.dto;


import java.math.BigDecimal;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class ShipmentDto {

	private String orderTrackingCode;

	private BigDecimal shipmentAmount;

	private String merchantNumber;

}
