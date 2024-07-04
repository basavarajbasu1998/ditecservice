package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class MasterDataTypeDetailsUpdateRequest {
	
	@NotBlank
	@NotNull(message = "dataTypeName is must be enter")
	@Size(min = 4,max = 50)
	private String dataTypeName;
	
	@NotBlank
	@NotNull(message = "dataTypeStatus is must be enter")
	@Size(min = 4,max = 50)
	private String dataTypeStatus;
	
	@NotBlank
	@NotNull(message = "dataTypeId is must be enter")
	@Size(min = 4,max = 20)
	private String dataTypeId;

}
