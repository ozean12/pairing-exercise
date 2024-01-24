package io.billie.shipmentnotifier.service.shipment;

import io.billie.shipmentnotifier.exception.BusinessException;
import io.billie.shipmentnotifier.model.shipment.Shipment;
import io.billie.shipmentnotifierspec.dto.ShipmentDto;

public interface ShipmentService {
	Shipment processShipment(ShipmentDto shipmentDto) throws BusinessException;

}
