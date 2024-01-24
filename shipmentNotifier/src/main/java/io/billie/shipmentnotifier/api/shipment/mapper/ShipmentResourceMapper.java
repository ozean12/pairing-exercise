package io.billie.shipmentnotifier.api.shipment.mapper;

import io.billie.shipmentnotifier.model.shipment.Shipment;
import io.billie.shipmentnotifier.model.shipment.ShipmentStatus;
import io.billie.shipmentnotifierspec.common.Result;
import io.billie.shipmentnotifierspec.common.ResultStatus;
import io.billie.shipmentnotifierspec.dto.ShipmentDto;
import io.billie.shipmentnotifierspec.request.ShipmentRequest;
import io.billie.shipmentnotifierspec.response.ShipmentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", imports = ResultStatus.class)
public interface ShipmentResourceMapper {

	ShipmentDto toShipmentModel(ShipmentRequest request);

	@Mapping(target = "result", source = ".", qualifiedByName = "toShipmentResponseStatus")
	@Mapping(target = "trackingNumber", source = "trackingCode")
	ShipmentResponse toShipmentResponse(Shipment shipment);

	@Named("toShipmentResponseStatus")
	default Result toShipmentResponseStatus(Shipment shipment) {
		return shipment.getStatus().equals(ShipmentStatus.SUCCESSFUL) ? new Result(ResultStatus.SUCCESS) : new Result(ResultStatus.ShipmentAmountIsGreaterThatOrderAmountException);
	}
}
