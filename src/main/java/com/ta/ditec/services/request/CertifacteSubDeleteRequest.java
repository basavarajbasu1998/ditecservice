package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class CertifacteSubDeleteRequest {

	@NotBlank
	@NotNull(message = "id is must be enter")
	@Size(min = 1, max = 50)
	private Long id;
}
