package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class MasterDataDetailsDeleteRequest {
	
	@NotBlank
	@NotNull(message = "dataId is Must!")
	private String dataId;

}
