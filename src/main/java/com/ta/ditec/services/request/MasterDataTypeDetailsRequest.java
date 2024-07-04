package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MasterDataTypeDetailsRequest {


	@NotBlank
	@NotNull(message = "dataTypeName is must be enter")
	@Size(min = 4,max = 50)
	private String dataTypeName;
	
	@NotBlank
	@NotNull(message = "dataTypeStatus is must be enter")
	@Size(min = 4,max = 50)
	private String dataTypeStatus;
}
