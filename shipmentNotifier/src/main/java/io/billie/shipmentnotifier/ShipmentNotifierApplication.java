package io.billie.shipmentnotifier;

import java.math.BigDecimal;

import io.billie.shipmentnotifier.model.order.Order;
import io.billie.shipmentnotifier.model.order.OrderStatus;
import io.billie.shipmentnotifier.model.order.dao.OrderRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableTransactionManagement
@ConfigurationPropertiesScan
@SpringBootApplication
@PropertySource({ "classpath:error-messages.properties" })
public class ShipmentNotifierApplication implements CommandLineRunner {

	private final OrderRepository orderRepository;

	public ShipmentNotifierApplication(OrderRepository orderRepository) {
		this.orderRepository = orderRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(ShipmentNotifierApplication.class, args);
	}


	@Override
	public void run(String... args) throws Exception {

		Order order = new Order();
		order.setMerchantNumber("TestMerchant@123123");
		order.setTrackingCode("test123");
		order.setTotalAmount(new BigDecimal(100000));
		order.setStatus(OrderStatus.NEW);
		orderRepository.save(order);
	}
}
