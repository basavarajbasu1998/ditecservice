package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data

public class Errorcode {

	@NotBlank
	@NotNull(message = "errorcode is must be enter")
	@Size(min = 4, max = 50)
	private String errorcode;
}
