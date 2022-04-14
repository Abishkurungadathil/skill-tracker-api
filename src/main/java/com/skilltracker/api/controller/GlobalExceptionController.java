package com.skilltracker.api.controller;

import com.skilltracker.api.exeption.InvalidSkillEntryException;
import com.skilltracker.api.exeption.InvalidSkillProfileUpdateAttemptExeption;
import com.skilltracker.api.exeption.SkillTrackerException;
import com.skilltracker.api.model.SkillTrackerErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionController  {
	private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionController.class);

	@ExceptionHandler(value = { Exception.class })
	public ResponseEntity<Object> handleException(Exception ex) {
		logger.error("Exception: ", ex);		
		return new ResponseEntity<Object>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(
	  MethodArgumentNotValidException ex) {
	    Map<String, String> errors = new HashMap<>();
	    ex.getBindingResult().getAllErrors().forEach((error) -> {
	        String fieldName = ((FieldError) error).getField();
	        String errorMessage = error.getDefaultMessage();
	        errors.put(fieldName, errorMessage);
	    });
	    return errors;
	}


	@ExceptionHandler(InvalidSkillEntryException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<SkillTrackerErrorResponse> handleValidationExceptions(InvalidSkillEntryException exeption ){
		return getErrotResponse(exeption ,HttpStatus.BAD_REQUEST);
	}


	@ExceptionHandler(InvalidSkillProfileUpdateAttemptExeption.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public ResponseEntity<SkillTrackerErrorResponse> handleValidationExceptions(InvalidSkillProfileUpdateAttemptExeption exeption ){
		return getErrotResponse(exeption ,HttpStatus.BAD_REQUEST);
	}



	private  ResponseEntity<SkillTrackerErrorResponse> getErrotResponse(SkillTrackerException exeption, HttpStatus status){
		SkillTrackerErrorResponse response =new SkillTrackerErrorResponse();
		response.setErrorCode(exeption.getErrorCode());
        response.setErrorMessage(exeption.getMessage());
      return  new  ResponseEntity<>(response ,status);
	}
}
