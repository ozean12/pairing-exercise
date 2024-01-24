package io.billie.shipmentnotifier.api;

import java.math.BigDecimal;
import java.util.Optional;

import io.billie.shipmentnotifier.AbstractBaseIT;
import io.billie.shipmentnotifier.model.order.Order;
import io.billie.shipmentnotifier.model.order.OrderStatus;
import io.billie.shipmentnotifier.model.order.dao.OrderRepository;
import io.billie.shipmentnotifier.model.shipment.Shipment;
import io.billie.shipmentnotifier.model.shipment.dao.ShipmentRepository;
import io.billie.shipmentnotifier.util.TestUtils;
import io.billie.shipmentnotifierspec.common.ResultStatus;
import io.billie.shipmentnotifierspec.request.ShipmentRequest;
import io.billie.shipmentnotifierspec.response.ShipmentResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;


public class ShipmentServiceIT extends AbstractBaseIT {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private ShipmentRepository shipmentRepository;

	@BeforeEach
	void destroy(){
		orderRepository.deleteAll();
		shipmentRepository.deleteAll();
	}

	@Test
	public void createShipmentNotification_successfully() {

		Order savedOrder = orderRepository.save(getValidOrder());
		ShipmentRequest shipmentRequest = new ShipmentRequest();
		shipmentRequest.setOrderTrackingCode(savedOrder.getTrackingCode());
		shipmentRequest.setShipmentAmount(new BigDecimal("100.55"));
		shipmentRequest.setMerchantNumber("123");

		ResponseEntity<ShipmentResponse> shipmentResponse = restTemplate.postForEntity(TestUtils.baseUrl(port) + "/billie/api/v1/shipments", shipmentRequest, ShipmentResponse.class);

		TestUtils.checkSuccessResponseDefaults(shipmentResponse);
		assertThat(shipmentResponse.getBody().getTrackingNumber()).isNotBlank();

		Optional<Shipment> shipment = shipmentRepository.findByTrackingCode(shipmentResponse.getBody().getTrackingNumber());
		assertThat(shipment.isPresent()).isEqualTo(Boolean.TRUE);
		assertThat(shipment.get().getOrder().getId()).isEqualTo(savedOrder.getId());
		assertThat(shipment.get().getAmount()).isEqualTo(shipmentRequest.getShipmentAmount());
		assertThat(shipment.get().getTrackingCode()).isNotBlank();
		assertThat(shipment.get().getTrackingCode()).isEqualTo(shipmentResponse.getBody().getTrackingNumber());

	}

	@Test
	public void createShipmentNotification_with_invalid_shipmentAmount_successfully() {

		Order savedOrder = orderRepository.save(getValidOrder());
		ShipmentRequest shipmentRequest = new ShipmentRequest();
		shipmentRequest.setOrderTrackingCode(savedOrder.getTrackingCode());

		ResponseEntity<ShipmentResponse> shipmentResponse = restTemplate.postForEntity(TestUtils.baseUrl(port) + "/billie/api/v1/shipments", shipmentRequest, ShipmentResponse.class);

		TestUtils.checkUnSuccessResponseDefaults(shipmentResponse);

		assertThat(shipmentResponse.getBody().getResult()).isNotNull();
		assertThat(shipmentResponse.getBody().getResult().getTitle()).isEqualTo(ResultStatus.INVALID_PARAMETER);
		assertThat(shipmentResponse.getBody().getResult().getMessage()).isEqualTo("shipment amount must be greater than zero");
	}

	@Test
	public void createShipmentNotification_with_zero_shipmentAmount_successfully() {

		Order savedOrder = orderRepository.save(getValidOrder());
		ShipmentRequest shipmentRequest = new ShipmentRequest();
		shipmentRequest.setOrderTrackingCode(savedOrder.getTrackingCode());
		shipmentRequest.setShipmentAmount(new BigDecimal(0));
		shipmentRequest.setMerchantNumber("123123");

		ResponseEntity<ShipmentResponse> shipmentResponse = restTemplate.postForEntity(TestUtils.baseUrl(port) + "/billie/api/v1/shipments", shipmentRequest, ShipmentResponse.class);

		TestUtils.checkUnSuccessResponseDefaults(shipmentResponse);

		assertThat(shipmentResponse.getBody().getResult()).isNotNull();
		assertThat(shipmentResponse.getBody().getResult().getTitle()).isEqualTo(ResultStatus.INVALID_PARAMETER);
		assertThat(shipmentResponse.getBody().getResult().getMessage()).isEqualTo("shipment amount must be greater than zero");
	}


	@Test
	public void createShipmentNotification_with_invalid_orderTrackingCode_successfully() {

		ShipmentRequest shipmentRequest = new ShipmentRequest();
		shipmentRequest.setShipmentAmount(new BigDecimal("100.55"));

		ResponseEntity<ShipmentResponse> shipmentResponse = restTemplate.postForEntity(TestUtils.baseUrl(port) + "/billie/api/v1/shipments", shipmentRequest, ShipmentResponse.class);

		TestUtils.checkUnSuccessResponseDefaults(shipmentResponse);

		assertThat(shipmentResponse.getBody().getResult()).isNotNull();
		assertThat(shipmentResponse.getBody().getResult().getTitle()).isEqualTo(ResultStatus.INVALID_PARAMETER);
		assertThat(shipmentResponse.getBody().getResult().getMessage()).isEqualTo("order tracking code must be valid");
	}


	@Test
	public void createShipmentNotification_with_invalid_orderTrackingCode_orderNotFoundException() {

		ShipmentRequest shipmentRequest = new ShipmentRequest();
		shipmentRequest.setOrderTrackingCode("invalid_tracking_code");
		shipmentRequest.setShipmentAmount(new BigDecimal("100.55"));
		shipmentRequest.setMerchantNumber("123");

		ResponseEntity<ShipmentResponse> shipmentResponse = restTemplate.postForEntity(TestUtils.baseUrl(port) + "/billie/api/v1/shipments", shipmentRequest, ShipmentResponse.class);

		TestUtils.checkUnSuccessResponseDefaults(shipmentResponse);

		assertThat(shipmentResponse.getBody().getResult()).isNotNull();
		assertThat(shipmentResponse.getBody().getResult().getTitle()).isEqualTo(ResultStatus.OrderNotFoundException);
		assertThat(shipmentResponse.getBody().getResult().getMessage()).isEqualTo("invalid order with tracking number");
	}



	@Test
	public void createShipmentNotification_GreatThanOrderTotalAmount() {

		Order validOrder = getValidOrder();
		validOrder.setTotalAmount(new BigDecimal("50"));
		Order savedOrder = orderRepository.save(validOrder);
		ShipmentRequest shipmentRequest = new ShipmentRequest();
		shipmentRequest.setOrderTrackingCode(savedOrder.getTrackingCode());
		shipmentRequest.setShipmentAmount(new BigDecimal("100.55"));
		shipmentRequest.setMerchantNumber("123");

		ResponseEntity<ShipmentResponse> shipmentResponse = restTemplate.postForEntity(TestUtils.baseUrl(port) + "/billie/api/v1/shipments", shipmentRequest, ShipmentResponse.class);

		Optional<Shipment> shipment = shipmentRepository.findByTrackingCode(shipmentResponse.getBody().getTrackingNumber());
		assertThat(shipment.isPresent()).isEqualTo(Boolean.TRUE);
		assertThat(shipment.get().getOrder().getId()).isEqualTo(savedOrder.getId());
		assertThat(shipment.get().getAmount()).isEqualTo(shipmentRequest.getShipmentAmount());
		assertThat(shipment.get().getTrackingCode()).isEqualTo(shipmentResponse.getBody().getTrackingNumber());
		assertThat(shipment.get().getDescription()).isEqualTo(ResultStatus.ShipmentAmountIsGreaterThatOrderAmountException.name());

	}

	private Order getValidOrder() {
		Order order = new Order();
		order.setId(1L);
		order.setStatus(OrderStatus.NEW);
		order.setTotalAmount(new BigDecimal("100000.55"));
		order.setTrackingCode("123456789");
		order.setMerchantNumber("123");
		return order;
	}

}
