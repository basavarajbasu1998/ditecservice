package com.ta.ditec.services.response;

import com.ta.ditec.services.exception.Type;

import lombok.Data;

@Data
public class TaExceptionResponse {

	private int code;
	private String message;
	private Type type;

}
