package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class StatusMasterDetailsUpdateRequest {
	
	@NotBlank
	@NotNull(message = "statusName is must be enter")
	@Size(min = 4,max = 40)
	private String statusName;
	
	@NotBlank
	@NotNull(message = "statusId is must be enter")
	@Size(min = 4,max = 40)
	private String statusId;

}
