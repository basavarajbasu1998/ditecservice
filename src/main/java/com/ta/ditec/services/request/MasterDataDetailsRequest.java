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
public class MasterDataDetailsRequest {
	
	@NotBlank
	@NotNull(message = "dataType is must be enter")
	@Size(min = 4,max = 50)
	private String dataType;
	
	@NotBlank
	@NotNull(message = "dataValue is must be enter")
	@Size(min = 4,max = 50)
	private String dataValue;
	
	@NotBlank
	@NotNull(message = "dataParent is must be enter")
	@Size(min = 4,max = 50)
	private String dataParent;
}
