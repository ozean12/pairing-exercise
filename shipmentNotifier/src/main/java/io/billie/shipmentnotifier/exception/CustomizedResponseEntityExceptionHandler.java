package io.billie.shipmentnotifier.exception;

import java.security.InvalidParameterException;

import io.billie.shipmentnotifierspec.common.ResponseService;
import io.billie.shipmentnotifierspec.common.Result;
import io.billie.shipmentnotifierspec.common.ResultStatus;
import io.billie.shipmentnotifierspec.response.GeneralResponse;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@ControllerAdvice
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private Environment env;

	@Autowired
	public void setEnv(Environment env) {
		this.env = env;
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		logger.error("validation exception {}", ex);

		return new ResponseEntity<>(new GeneralResponse(createResult(getCustomMessage(ex))),
				HttpStatus.UNPROCESSABLE_ENTITY);

	}

	@ExceptionHandler(BusinessException.class)
	public final ResponseEntity<ResponseService> handleBusinessException(BusinessException ex, WebRequest request) {
		logger.error("", ex);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(new GeneralResponse(ex.getResultStatus()), headers,
				HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@ExceptionHandler(InvalidParameterException.class)
	public final ResponseEntity<ResponseService> handleInvalidParameterException(InvalidParameterException ex,
			WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(new GeneralResponse(ResultStatus.INVALID_PARAMETER), headers,
				HttpStatus.UNPROCESSABLE_ENTITY);
	}


	@ExceptionHandler(UnsupportedOperationException.class)
	public final ResponseEntity<ResponseService> handleUnsupportedOperationException(UnsupportedOperationException ex,
			WebRequest request) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		return new ResponseEntity<>(new GeneralResponse(ResultStatus.FORBIDDEN_REQUEST), headers,
				HttpStatus.UNPROCESSABLE_ENTITY);
	}

//	@ExceptionHandler(Exception.class)
//	public final ResponseEntity<ResponseService> handleGeneralException(Exception ex, WebRequest request) {
//		logger.error("", ex);
//
//		HttpHeaders headers = new HttpHeaders();
//
//		headers.setContentType(MediaType.APPLICATION_JSON);
//
//		return new ResponseEntity<>(new GeneralResponse(ResultStatus.FAILURE), headers,
//				HttpStatus.UNPROCESSABLE_ENTITY);
//	}


	@ExceptionHandler(OrderNotFoundException.class)
	public final ResponseEntity<ResponseService> handleOrderNotFoundException(Exception ex, WebRequest request) {
		logger.error("", ex);

		HttpHeaders headers = new HttpHeaders();

		headers.setContentType(MediaType.APPLICATION_JSON);

		return new ResponseEntity<>(new GeneralResponse(ResultStatus.OrderNotFoundException), headers,
				HttpStatus.UNPROCESSABLE_ENTITY);
	}

	private String getCustomMessage(MethodArgumentNotValidException ex) {
		for (ObjectError error : ex.getBindingResult().getAllErrors()) {
			if (!StringUtils.isEmpty(error.getDefaultMessage())) {
				String customMessage = env.getProperty(error.getDefaultMessage());
				if (!StringUtils.isEmpty(customMessage)) {
					return customMessage;
				}
			}
		}
		return null;
	}

	private Result createResult(String customMessage) {
		Result result = new Result();
		if (customMessage != null) {
			result.setMessage(customMessage);
		} else {
			result.setMessage(ResultStatus.INVALID_PARAMETER.getDescription());
		}
		result.setStatus(ResultStatus.INVALID_PARAMETER.getStatus());
		result.setTitle(ResultStatus.INVALID_PARAMETER);
		return result;
	}

}