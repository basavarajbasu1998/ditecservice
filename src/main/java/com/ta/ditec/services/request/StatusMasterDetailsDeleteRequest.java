package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class StatusMasterDetailsDeleteRequest {
	
	
	@NotBlank
	@NotNull(message = "statusId is Must!")
	private String statusId;

}
