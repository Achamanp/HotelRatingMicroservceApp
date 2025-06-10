package com.FirstMicroServicesApplication.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandeler {
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> userNotFoundExceptionHandler(UserNotFoundException ex){
		ErrorResponse err = new ErrorResponse(ex.getMessage(), HttpStatus.NOT_FOUND, LocalDateTime.now());
		return new ResponseEntity<ErrorResponse>(err, HttpStatus.NOT_FOUND);
		
	}
}
