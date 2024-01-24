package io.billie.shipmentnotifierspec.response;


import io.billie.shipmentnotifierspec.common.ResponseService;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ShipmentResponse extends ResponseService {

	private String trackingNumber;

}
