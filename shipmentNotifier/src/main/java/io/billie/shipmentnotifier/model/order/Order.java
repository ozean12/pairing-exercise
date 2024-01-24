package io.billie.shipmentnotifier.model.order;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Index;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import io.billie.shipmentnotifier.model.base.JpaBaseEntity;
import io.billie.shipmentnotifier.model.shipment.Shipment;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "orders", indexes = {
		@Index(name = "idx_order_tracking_code", columnList = "tracking_code"),
		@Index(name = "idx_order_status", columnList = "status")
})
@Getter
@Setter
public class Order extends JpaBaseEntity {

	@NotNull
	@Column(name = "merchant_number")
	private String merchantNumber;

	@NotNull
	@Column(name = "total_amount")
	private BigDecimal totalAmount;

	@Column(name = "tracking_code", unique = true)
	@NotNull
	private String trackingCode;

	@OneToMany(mappedBy = "order")
	private List<Shipment> shipments = new ArrayList<>();

	@NotNull
	@Enumerated
	@Column(name = "status", columnDefinition = "smallint")
	private OrderStatus status;

}
