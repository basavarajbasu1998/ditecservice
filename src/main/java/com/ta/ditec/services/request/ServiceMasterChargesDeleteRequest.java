package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class ServiceMasterChargesDeleteRequest {

	@NotBlank
	private String serviceMasterChargesId;
}
