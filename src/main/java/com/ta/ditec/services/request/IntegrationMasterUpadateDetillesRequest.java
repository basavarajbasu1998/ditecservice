package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class IntegrationMasterUpadateDetillesRequest {

	@NotBlank
	@NotNull(message = "integrationId is must be enter")
	@Size(min = 1, max = 50)
	private String integrationId;

	@NotBlank
	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String integrationType;

	@NotBlank
	@NotNull(message = "integrationCharges is must be enter")
	@Size(min = 1, max = 50)
	private Double integrationCharges;
}
