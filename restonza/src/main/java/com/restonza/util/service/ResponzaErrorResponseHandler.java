/**
 * 
 */
package com.restonza.util.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.restonza.vo.RestonzaRestResponseVO;

/**
 * @author Flex-Grow Developers
 * {@code: returns custom response as a part of exception handling} 
 */
@ControllerAdvice(basePackages = {"com.restonza.controller"} )
public class ResponzaErrorResponseHandler {
	/**
	 * Used For: Capturing all exceptions thrown by controller
	 * @param Exception e
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody RestonzaRestResponseVO exception(Exception e) {
		ValidationErrorSupport validationErrorSupport = new ValidationErrorSupport("Unexpected error occured! Please contact support group for assistance",e.getMessage());
		return new RestonzaRestResponseVO("danger", validationErrorSupport);
	}
	
	/**
	 * Used For: Capturing all CKDCustomException custom exception
	 * @param RestonzaCustomException e
	 */
	@ExceptionHandler(RestonzaCustomException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody RestonzaRestResponseVO exception(RestonzaCustomException e) {
		return new RestonzaRestResponseVO("danger", e.getMessage());
	}
	/**
	 * Used For: Capturing all validation related errors
	 * @param MethodArgumentNotValidException validationError
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	public @ResponseBody RestonzaRestResponseVO exception(MethodArgumentNotValidException validationError) {
		ValidationError validationErrorObject = ValidationErrorBuilder.fromBindingErrors(validationError.getBindingResult());
		return new RestonzaRestResponseVO("danger", validationErrorObject);
	}
}
