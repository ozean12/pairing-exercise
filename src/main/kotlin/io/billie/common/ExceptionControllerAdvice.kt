package io.billie.common


import com.fasterxml.jackson.annotation.JsonInclude
import io.billie.shipments.exceptions.OrderNotFoundException
import io.billie.shipments.exceptions.ShipmentAmountExceedsRemainingAmount
import io.billie.shipments.exceptions.ShipmentNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.time.LocalDateTime
import java.util.*
import kotlin.collections.HashMap

// TODO do this later for global exception handling

//@RestControllerAdvice
class ExceptionControllerAdvice {

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException::class)
//    fun handleValidationExceptions(ex: MethodArgumentNotValidException): Map<String, String?>? {
//        val errors: MutableMap<String, String?> = HashMap()
//        ex.bindingResult.allErrors.forEach { error: ObjectError ->
//            val fieldName = (error as FieldError).field
//            val errorMessage = error.getDefaultMessage()
//            errors[fieldName] = errorMessage
//        }
//        return errors
//    }


//    @ExceptionHandler(
//        OrderNotFoundException::class,
//        ShipmentAmountExceedsRemainingAmount::class,
//        ShipmentNotFoundException::class
//    )
//    fun handleCustomExceptions(ex: RuntimeException, request: WebRequest): ResponseEntity<ErrorsDetails> {
//        val response = when (ex) {
//            is OrderNotFoundException -> createErrorResponse(
//                HttpStatus.NOT_FOUND,
//                ErrorsDetails(message = "Order not found", reason = ex.message!!)
//            )
//
//            is ShipmentNotFoundException -> createErrorResponse(
//                HttpStatus.NOT_FOUND,
//                ErrorsDetails(message = "Shipment not found", reason = ex.message!!)
//            )
//
//            is ShipmentAmountExceedsRemainingAmount -> createErrorResponse(
//                HttpStatus.BAD_REQUEST,
//                ErrorsDetails(message = "Shipment amount exceeds remaining amount", reason = ex.message!!)
//            )
//
//            else -> createErrorResponse(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                ErrorsDetails(message = "Internal Error", reason = "Something went wrong please try again later")
//            )
//        }
//        return response
//    }
}

//@JsonInclude(JsonInclude.Include.NON_NULL)
//data class ErrorsDetails(val time: LocalDateTime = LocalDateTime.now(), val message: String, val reason: String)
//
//fun createErrorResponse(httpStatus: HttpStatus, errorsDetails: ErrorsDetails) =
//    ResponseEntity
//        .status(httpStatus)
//        .contentType(MediaType.APPLICATION_PROBLEM_JSON)
//        .body(errorsDetails)

