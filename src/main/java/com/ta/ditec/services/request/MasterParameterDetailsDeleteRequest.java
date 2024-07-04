package com.ta.ditec.services.request;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class MasterParameterDetailsDeleteRequest {

//	@NotBlank
//	@NotNull(message = "parameterId is must be enter")
	@Size(min = 4, max = 50)
	private String parameterId;

}
