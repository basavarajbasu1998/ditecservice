package com.ta.ditec.services.request;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class OtpRequset {

//	@NotBlank
//	@NotNull(message = "parameterName is must be enter")
	@Size(min = 4, max = 50)
	private String userName;

//	@NotBlank
//	@NotNull(message = "parameterName is must be enter")
	@Size(min = 4, max = 50)
	private String otpType;

//	@NotBlank
//	@NotNull(message = "parameterName is must be enter")
	@Size(min = 4, max = 50)
	private String otp;
}
