package com.ta.ditec.services.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserMasterDetailsForgotPasswodRequest {
	
	
	@NotBlank(message="Please enter the user email")
	@Email
	private String userEmail;

}
