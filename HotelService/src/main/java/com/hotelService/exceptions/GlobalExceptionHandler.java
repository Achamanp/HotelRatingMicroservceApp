package com.hotelService.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
	    ErrorResponse error = new ErrorResponse(
	        ex.getMessage(),
	        HttpStatus.NOT_FOUND,
	        LocalDateTime.now()
	    );
	    return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}


}
