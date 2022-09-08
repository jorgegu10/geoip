package com.appgate.geoip.domain.exception;

public class BusinessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public BusinessException(String errorMessage) {
        super(errorMessage);
    }
	
	public BusinessException() {
		super();
	}
	
	public BusinessException(String errorMessage, Throwable e) {
		super(errorMessage, e);
	}
}
