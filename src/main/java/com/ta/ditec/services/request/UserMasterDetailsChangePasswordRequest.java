package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserMasterDetailsChangePasswordRequest {

	@NotBlank(message="Enter the username")
	private String userId;
	
	@NotBlank(message="enter the password ")
	private String userPassword;

}
