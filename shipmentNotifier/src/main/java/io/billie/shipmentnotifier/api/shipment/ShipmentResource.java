package io.billie.shipmentnotifier.api.shipment;


import javax.validation.Valid;

import io.billie.shipmentnotifier.api.shipment.mapper.ShipmentResourceMapper;
import io.billie.shipmentnotifier.exception.BusinessException;
import io.billie.shipmentnotifier.model.shipment.Shipment;
import io.billie.shipmentnotifier.service.shipment.ShipmentService;
import io.billie.shipmentnotifierspec.request.ShipmentRequest;
import io.billie.shipmentnotifierspec.response.ShipmentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("billie/api/v1/shipments")
@RequiredArgsConstructor
public class ShipmentResource {


	private final ShipmentService shipmentService;

	private final ShipmentResourceMapper shipmentResourceMapper;


	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ShipmentResponse> notifyShipment(@RequestBody @Valid ShipmentRequest request) throws BusinessException {
		log.info("Received shipment request: {}", request);
		Shipment shipment = shipmentService.processShipment(shipmentResourceMapper.toShipmentModel(request));
		return ResponseEntity.ok(shipmentResourceMapper.toShipmentResponse(shipment));
	}

}
