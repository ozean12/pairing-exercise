package io.billie.shipmentnotifier.utils;

import java.util.UUID;


public final class StringUtils {

	private StringUtils() {
	}

	public static String generateTrackingCode() {
		return String.valueOf(Math.abs(UUID.randomUUID().toString().hashCode())) + System.currentTimeMillis();
	}

}
