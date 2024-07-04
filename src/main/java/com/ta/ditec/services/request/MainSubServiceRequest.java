package com.ta.ditec.services.request;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class MainSubServiceRequest {

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String parentId;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String servicename;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String servicecharge;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String transactioncharge;
}
