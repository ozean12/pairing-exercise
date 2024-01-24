package io.billie.shipmentnotifier.model.shipment;

public enum ShipmentStatus {

	SUCCESSFUL(0), FAILED(1); // it could be more base on business, pay status and etc...

	private int value;

	ShipmentStatus(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
}
