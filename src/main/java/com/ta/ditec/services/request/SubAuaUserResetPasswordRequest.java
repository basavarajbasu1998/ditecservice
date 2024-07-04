package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SubAuaUserResetPasswordRequest {

	@NotBlank(message="Please enter the subauaid")
	private String subAuaId;

	@NotBlank(message="Please enter the password")
	private String password;

	
}
