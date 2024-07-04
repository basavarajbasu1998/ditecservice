package com.ta.ditec.services.request;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class NotificationRequest {

//	@NotBlank
//	@NotNull(message = "parameterName is must be enter")
	@Size(min = 4, max = 50)
	private String subAuaId;

//	@NotBlank
//	@NotNull(message = "parameterName is must be enter")
//	@Size(min = 1, max = 50)
	private Long id;
}
