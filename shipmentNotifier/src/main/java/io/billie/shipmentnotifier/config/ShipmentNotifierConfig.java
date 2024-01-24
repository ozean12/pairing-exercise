package io.billie.shipmentnotifier.config;

import lombok.AllArgsConstructor;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@AllArgsConstructor
public class ShipmentNotifierConfig {


	private final Environment environment;

}
