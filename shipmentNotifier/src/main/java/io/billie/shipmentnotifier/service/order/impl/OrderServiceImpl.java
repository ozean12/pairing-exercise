package io.billie.shipmentnotifier.service.order.impl;

import io.billie.shipmentnotifier.config.ShipmentNotifierConfig;
import io.billie.shipmentnotifier.exception.OrderNotFoundException;
import io.billie.shipmentnotifier.model.order.Order;
import io.billie.shipmentnotifier.model.order.dao.OrderRepository;
import io.billie.shipmentnotifier.service.order.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final OrderRepository orderRepository;

	@Transactional(readOnly = true)
	public Order findByTrackingCodeAndMerchantNumber(String orderTrackingCode, String merchantNumber) throws OrderNotFoundException {
		return orderRepository.findByTrackingCodeAndMerchantNumber(orderTrackingCode, merchantNumber)
				.orElseThrow(() -> new OrderNotFoundException("invalid tracking number"));
	}
}
