package com.customer.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import com.customer.demo.ErrorResponse;
import com.customer.demo.CustomerNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler  {
	
	
	// Custom exception handler for CustomerNotFoundException
    @ExceptionHandler(value= CustomerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCustomerNotFoundException(CustomerNotFoundException ex){
    	return ResponseEntity.status(HttpStatus.NOT_FOUND)
    						 .header("Content-Type", "application/json")
    						 .body(new ErrorResponse(HttpStatus.NOT_FOUND.value(),
    						  ex.getMessage()));
    }
    
    // Generic exception handler for other exceptions
    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                             .header("Content-Type", "application/json")
                             .body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "An unexpected error occurred"));
    }
	
}