package com.ta.ditec.services.exception;

public class TaExceptionFilter extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final int code;
	private final String errorMsg;

	public TaExceptionFilter(int code, String errorMsg, String string) {
		
		this.code = code;
		this.errorMsg = errorMsg;

	}

	public int getCode() {
		return code;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	
}

