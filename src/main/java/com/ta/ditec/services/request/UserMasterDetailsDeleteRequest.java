package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class UserMasterDetailsDeleteRequest {
	
	
	@NotBlank(message = "userId is Must!")
	
	private String userId;

}
