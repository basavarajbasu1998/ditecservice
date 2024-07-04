package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class ModuleMasterDetailsDeteleRequest {
	
	@NotBlank
	@NotNull(message = "moduleId is Must!")
	private String moduleId;

}
