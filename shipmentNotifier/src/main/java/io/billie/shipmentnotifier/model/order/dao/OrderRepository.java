package io.billie.shipmentnotifier.model.order.dao;

import java.util.Optional;

import io.billie.shipmentnotifier.model.order.Order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

	Optional<Order> findByTrackingCodeAndMerchantNumber(String orderTrackingCode,String merchantNumber);
}
