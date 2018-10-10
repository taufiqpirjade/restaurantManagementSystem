/**
 * 
 */
package com.restonza.util.service;

/**
 * @author Flex-Grow developers
 * CKD custom exception class 
 * Used for registering exception with user defined error messages
 *
 */
public class RestonzaCustomException extends Exception {
	private static final long serialVersionUID = 4812929099333268865L;

	private String message;
	
	public RestonzaCustomException() {
		super();
	}
	
	public RestonzaCustomException(String message) {
		super(message);
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
}
