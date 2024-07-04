package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserMasterDetailsLoginRequest {
	
	@NotBlank(message="Please enter the username")
	@Size(min=4,max=25)
	private String userName;
	
	@NotBlank(message="Enter the password")
	@Size(min=4,max=15)
	private String userPassword;
}
