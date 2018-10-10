/**
 * 
 */
package com.restonza.util.service;

import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

/**
 * @author Flex-Grow Developers
 * Used For: Binding all validation related error
 */
public class ValidationErrorBuilder {
	/**
	 * Used For: Bundled request validation error
	 * @param errors
	 * @return ValidationError object
	 */
	public static ValidationError fromBindingErrors(Errors errors) {
        ValidationError error = new ValidationError("Please check request parameters");
        for (ObjectError objectError : errors.getAllErrors()) {
            error.addValidationError(objectError.getDefaultMessage());
        }
        return error;
    }
}
