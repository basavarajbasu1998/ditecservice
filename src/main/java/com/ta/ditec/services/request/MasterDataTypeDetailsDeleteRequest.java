package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MasterDataTypeDetailsDeleteRequest {
	
	@NotBlank
	@NotNull(message = "dataTypeId is Must!")
	private String dataTypeId;

}
