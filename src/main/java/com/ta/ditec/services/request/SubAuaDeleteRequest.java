package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SubAuaDeleteRequest {

	@NotBlank(message="Please enter the subauaid")
	private String subAuaId;
}
