package com.ta.ditec.services.utils;

import org.springframework.http.ResponseEntity;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.response.TaResponse;

public class TAConstantesResponse {
	public static <T> ResponseEntity<TaResponse<T>> createResponse(T data, String message, int code, Type type) {
		TaResponse<T> response = new TaResponse<>();
		response.setResponseData(data);
		response.setMessage(message);
		response.setCode(code);
		response.setType(type);
		return ResponseEntity.ok(response);
	}

}
