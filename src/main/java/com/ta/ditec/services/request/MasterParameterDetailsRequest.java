package com.ta.ditec.services.request;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class MasterParameterDetailsRequest {

//	@NotBlank
//	@NotNull(message = "parametervalue is must be enter")
	@Size(min = 4, max = 50)
	private String parametervalue;

//	@NotBlank
//	@NotNull(message = "parameterName is must be enter")
	@Size(min = 4, max = 50)
	private String parameterName;
}
