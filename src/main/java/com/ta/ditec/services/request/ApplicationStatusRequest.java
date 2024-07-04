package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class ApplicationStatusRequest {

	@NotBlank
	@NotNull(message = "applicationStatus is must be enter")
	@Size(min = 4, max = 50)
	private String applicationStatus;

	@NotBlank
	@NotNull(message = "remark is must be enter")
	@Size(min = 4, max = 50)
	private String remark;

	@NotBlank
	@NotNull(message = "statusDescription is must be enter")
	@Size(min = 4, max = 50)
	private String statusDescription;

	@NotBlank
	@NotNull(message = "userId is must be enter")
	@Size(min = 4, max = 50)
	private String userId;
}
