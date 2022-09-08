package com.appgate.geoip.domain.exception;

public class TechnicalException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public TechnicalException(String errorMessage) {
        super(errorMessage);
    }
	
	public TechnicalException() {
		super();
	}
	
	public TechnicalException(String errorMessage, Throwable e) {
		super(errorMessage, e);
	}
}
