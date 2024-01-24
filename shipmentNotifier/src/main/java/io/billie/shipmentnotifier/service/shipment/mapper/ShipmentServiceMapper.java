package io.billie.shipmentnotifier.service.shipment.mapper;

import io.billie.shipmentnotifier.model.order.Order;
import io.billie.shipmentnotifier.model.shipment.Shipment;
import io.billie.shipmentnotifier.model.shipment.ShipmentStatus;
import io.billie.shipmentnotifier.utils.StringUtils;
import io.billie.shipmentnotifierspec.common.ResultStatus;
import io.billie.shipmentnotifierspec.dto.ShipmentDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ShipmentServiceMapper {

	@Mapping(target = "order", source = "order")
	@Mapping(target = "amount", source = "shipmentDto.shipmentAmount")
	@Mapping(target = "status", source = "isSuccessful", qualifiedByName = "toShipmentStatus")
	@Mapping(target = "description", source = "isSuccessful", qualifiedByName = "toShipmentDescription")
	@Mapping(target = "trackingCode", expression = "java(generateTrackingCode())")
	Shipment toShipment(ShipmentDto shipmentDto, Order order, boolean isSuccessful);


	@Named("toShipmentStatus")
	default ShipmentStatus toShipmentStatus(boolean isSuccessful) {
		return isSuccessful ? ShipmentStatus.SUCCESSFUL : ShipmentStatus.FAILED;
	}

	@Named("toShipmentDescription")
	default String toShipmentDescription(boolean isSuccessful) {
		return isSuccessful ? ResultStatus.SUCCESS.name() : ResultStatus.ShipmentAmountIsGreaterThatOrderAmountException.name();
		// it's not best practise solution, it's all about time I don't have time
	}

	default String generateTrackingCode() {
		return StringUtils.generateTrackingCode();
	}
}
