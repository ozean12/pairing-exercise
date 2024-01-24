package io.billie.shipmentnotifier.util;

import io.billie.shipmentnotifierspec.common.ResponseService;
import io.billie.shipmentnotifierspec.common.ResultStatus;
import org.assertj.core.api.Assertions;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

public class TestUtils {

	public static String baseUrl(int port) {
		return "http://localhost:" + port;
	}


	public static void checkSuccessResponseDefaults(ResponseEntity<? extends ResponseService> response) {
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getResult()).isNotNull();
		Assertions.assertThat(response.getBody().getResult().getTitle()).isEqualTo(ResultStatus.SUCCESS);
	}

	public static void checkUnSuccessResponseDefaults(ResponseEntity<? extends ResponseService> response) {
		Assertions.assertThat(response).isNotNull();
		Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
		Assertions.assertThat(response.getHeaders().getContentType()).isEqualTo(MediaType.APPLICATION_JSON);
		Assertions.assertThat(response.getBody()).isNotNull();
		Assertions.assertThat(response.getBody().getResult()).isNotNull();
		Assertions.assertThat(response.getBody().getResult().getTitle()).isNotEqualTo(ResultStatus.SUCCESS);
	}

}
