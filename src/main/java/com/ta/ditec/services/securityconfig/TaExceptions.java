package com.ta.ditec.services.securityconfig;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ta.ditec.services.exception.ErrorCode;

public class TaExceptions extends RuntimeException {
	private final ErrorCode errorCode;
	private final String errorDesc;

	public TaExceptions(ErrorCode errorCode, String errorDesc) {
		super(errorDesc);
		this.errorCode = errorCode;
		this.errorDesc = errorDesc;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public String toJson() {
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.writeValueAsString(new TaErrorResponse(errorCode.getCode(), errorDesc));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return "{\"errorCode\": \"" + errorCode.getCode() + "\", \"errorMessage\": \"" + errorDesc + "\"}";
		}
	}

	private static class TaErrorResponse {
		private final int errorCode;
		private final String errorMessage;

		public TaErrorResponse(int errorCode, String errorMessage) {
			this.errorCode = errorCode;
			this.errorMessage = errorMessage;
		}

		public int getErrorCode() {
			return errorCode;
		}

		public String getErrorMessage() {
			return errorMessage;
		}
	}

	
}
