package io.billie.shipmentnotifier.service.order;

import io.billie.shipmentnotifier.exception.OrderNotFoundException;
import io.billie.shipmentnotifier.model.order.Order;

public interface OrderService {
	Order findByTrackingCodeAndMerchantNumber(String orderTrackingCode, String merchantNumber) throws OrderNotFoundException;
}
