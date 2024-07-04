package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class EmailValidationRequest {
	
	@NotBlank
	@NotNull(message = "email is must be enter")
	@Size(min = 4, max = 50)
	private String email;


}
