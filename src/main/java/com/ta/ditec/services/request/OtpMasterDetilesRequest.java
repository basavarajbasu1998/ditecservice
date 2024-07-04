package com.ta.ditec.services.request;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class OtpMasterDetilesRequest {

//	@NotBlank
//	@NotNull(message = "parameterName is must be enter")
	@Size(min = 4, max = 50)
	private String otptype;

//	@NotBlank
//	@NotNull(message = "parameterName is must be enter")
	@Size(min = 4, max = 50)
	private String otp;

//	@NotBlank
//	@NotNull(message = "parameterName is must be enter")
	@Size(min = 4, max = 50)
	private String resendOtp;

//	@NotBlank
//	@NotNull(message = "parameterName is must be enter")
	@Size(min = 4, max = 50)
	private String userName;

//	@NotBlank
//	@NotNull(message = "parameterName is must be enter")
	@Size(min = 4, max = 50)
	private String status;
}
