/**
 * 
 */
package com.restonza.util.service;

/**
 * @author Flex-Grow developers
 * Used for setting values for custom exception
 *
 */
public class ValidationErrorSupport {
	String message;
	String causedescription;
	
	public ValidationErrorSupport() {
		super();
	}
	
	public ValidationErrorSupport(String message, String causedescription) {
		super();
		this.message = message;
		this.causedescription = causedescription;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCausedescription() {
		return causedescription;
	}

	public void setCausedescription(String causedescription) {
		this.causedescription = causedescription;
	}
	
	
}
