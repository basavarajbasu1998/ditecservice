package com.ta.ditec.services.request;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class MainServicesRequest {

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String mainService;
}
