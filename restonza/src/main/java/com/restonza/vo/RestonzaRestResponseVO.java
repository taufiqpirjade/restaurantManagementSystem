/**
 * 
 */
package com.restonza.vo;

/**
 * @author flex-grow developers
 *
 */
public class RestonzaRestResponseVO {
	private String status;
	private Object response;
	
	public RestonzaRestResponseVO() {
	}

	public RestonzaRestResponseVO(String status, Object response) {
		this.status = status;
		this.response = response;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Object getResponse() {
		return response;
	}
	public void setResponse(Object response) {
		this.response = response;
	}

	@Override
	public String toString() {
		return "RestonzaRestResponseVO [status=" + status + ", response="
				+ response.toString() + "]";
	}
}
