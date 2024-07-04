package com.ta.ditec.services.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubAuaUserForgotPasswodRequest {
	
	@NotBlank(message="Please enter email")
	@Email
	private String managementEmail;

}
