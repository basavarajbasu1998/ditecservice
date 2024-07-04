package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SubAuaUserRequest {

	@NotBlank(message = "Please enter the subauaid")
	private String subAuaId;

//	@NotBlank(message = "Please enter the accountStaus")
	private String accountStaus;
}
