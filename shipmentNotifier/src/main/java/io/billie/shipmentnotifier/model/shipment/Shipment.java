package io.billie.shipmentnotifier.model.shipment;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.billie.shipmentnotifier.model.base.JpaBaseEntity;
import io.billie.shipmentnotifier.model.order.Order;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "shipments", indexes = {
		@Index(name = "idx_tracking_code", columnList = "tracking_code"),
		@Index(name = "idx_order_id", columnList = "order_id"),
		@Index(name = "idx_status", columnList = "status")
})
@Getter
@Setter
public class Shipment extends JpaBaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@Column(name = "tracking_code", unique = true)
	private String trackingCode;

	@Column(name = "amount")
	@NotNull
	private BigDecimal amount;

	@NotNull
	@Enumerated
	@Column(name = "status", columnDefinition = "smallint")
	private ShipmentStatus status;

	@Column(name = "description")
	private String description;

}

