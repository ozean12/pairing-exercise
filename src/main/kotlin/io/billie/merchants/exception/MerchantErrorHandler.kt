package io.billie.merchants.exception

import io.billie.customers.exception.CustomerNotFoundException
import io.billie.orders.execption.OrderNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class MerchantErrorHandler {

    @ResponseStatus (HttpStatus.NOT_FOUND)
    @ExceptionHandler (MerchantNotFoundException::class)
    fun handleMerchantNotFoundException(e: MerchantNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.message)
    }

    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ExceptionHandler (InvalidMerchantRequestException::class)
    fun handleInvalidMerchantRequestException(e: InvalidMerchantRequestException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.message)
    }

    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ExceptionHandler (CustomerNotFoundException::class)
    fun handleCustomerNotFoundException(e: CustomerNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.message)
    }
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ExceptionHandler (OrderNotFoundException::class)
    fun handleOrderNotFoundException(e: OrderNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.message)
    }
}