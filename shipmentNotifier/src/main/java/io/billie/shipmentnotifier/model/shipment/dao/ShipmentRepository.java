package io.billie.shipmentnotifier.model.shipment.dao;

import java.util.Optional;

import io.billie.shipmentnotifier.model.shipment.Shipment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

	Optional<Shipment> findByTrackingCode(String trackingCode);
}
