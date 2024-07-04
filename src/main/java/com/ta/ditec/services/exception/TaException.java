package com.ta.ditec.services.exception;


public class TaException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final ErrorCode errorCode;
	private final String errorDesc;

	public TaException(ErrorCode errorCode, String errorDesc) {
		super(errorDesc);
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;

	}

	/**
	 * Gets error code.
	 *
	 * @return the error code
	 */
	public ErrorCode getErrorCode() {
		return errorCode;
	}

	/**
	 * Gets error desc.
	 *
	 * @return the error desc
	 */
	public String getErrorDesc() {
		return errorDesc;
	}

	@Override
	public String toString() {
		return new StringBuilder().append("TAException{").append("errorCode=").append(errorCode).append(", errorDesc='")
				.append(errorDesc).append('\'' + '}').toString();
	}
}

