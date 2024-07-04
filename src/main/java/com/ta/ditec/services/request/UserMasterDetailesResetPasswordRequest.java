package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMasterDetailesResetPasswordRequest {

	@NotBlank(message="Please enter the username")
	private String userName;

	@NotBlank(message="Enter the password")
	private String password;
}
