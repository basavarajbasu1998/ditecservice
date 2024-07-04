package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SubAuaChangePasswordRequest {

	
	@NotBlank(message="Please enter the subauaid")
	private String subAuaId;
	
	@NotBlank(message="Please enter the old password")
	private String password;
	
	@NotBlank(message="Please enter the newpassword")
	private String newpassword;
	
	
	
}
