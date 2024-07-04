package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserMasterDetailsforgotPasswordCheckLinkRequest {
	
	
	@NotBlank(message="Please enter the userId")
	private String userId;

}
