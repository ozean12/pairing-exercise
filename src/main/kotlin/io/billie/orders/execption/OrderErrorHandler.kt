package io.billie.orders.execption

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class OrderErrorHandler {

    @ResponseStatus (HttpStatus.BAD_REQUEST)
    @ExceptionHandler (InvalidOrderAmountException::class)
    fun handleInvalidMerchantRequestException(e: InvalidOrderAmountException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(e.message)
    }

    @ResponseStatus (HttpStatus.NOT_FOUND)
    @ExceptionHandler (OrderNotFoundException::class)
    fun handleOrderNotFoundException(e: OrderNotFoundException): ResponseEntity<Any> {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.message)
    }
}