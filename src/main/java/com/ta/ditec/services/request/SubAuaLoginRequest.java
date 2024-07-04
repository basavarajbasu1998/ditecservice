package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SubAuaLoginRequest {

	@NotBlank(message = "userName  is must!")
	@Size(min = 4, max = 25)
	private String userName;

	
	@NotBlank(message = "password  is must!")
	@Size(min = 4, max = 10)
	private String password;

	

}
