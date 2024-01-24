package io.billie.shipmentnotifier.service.shipment.impl;

import java.math.BigDecimal;

import io.billie.shipmentnotifier.exception.BusinessException;
import io.billie.shipmentnotifier.model.order.Order;
import io.billie.shipmentnotifier.model.shipment.Shipment;
import io.billie.shipmentnotifier.model.shipment.dao.ShipmentRepository;
import io.billie.shipmentnotifier.service.order.OrderService;
import io.billie.shipmentnotifier.service.shipment.ShipmentService;
import io.billie.shipmentnotifier.service.shipment.mapper.ShipmentServiceMapper;
import io.billie.shipmentnotifierspec.dto.ShipmentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ShipmentServiceImpl implements ShipmentService {

	private final ShipmentRepository shipmentRepository;

	private final OrderService orderService;

	private final ShipmentServiceMapper shipmentServiceMapper;

	@Transactional
	@Override
	public Shipment processShipment(ShipmentDto shipmentDto) throws BusinessException {
		log.info("Processing shipment: {}", shipmentDto);
		Order order = orderService.findByTrackingCodeAndMerchantNumber(shipmentDto.getOrderTrackingCode(), shipmentDto.getMerchantNumber());

		Shipment savedShipment = shipmentRepository.save(shipmentServiceMapper.toShipment(shipmentDto, order, isGreaterThanOrderAmount(shipmentDto, order)));
		log.info("Shipment saved with tracking code: {} and status: {}", savedShipment.getTrackingCode(), savedShipment.getStatus().name());

		return savedShipment;
	}

	private boolean isGreaterThanOrderAmount(ShipmentDto shipmentDto, Order order) {
		BigDecimal reduceOrderShipmentsAmount = order.getShipments()
				.stream()
				.map(Shipment::getAmount)
				.reduce(BigDecimal.ZERO, BigDecimal::add);
		return order.getTotalAmount().subtract(reduceOrderShipmentsAmount.add(shipmentDto.getShipmentAmount())).compareTo(BigDecimal.ZERO) >= 0;
	}

}
