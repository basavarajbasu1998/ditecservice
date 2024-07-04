package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class MasterDataDetailsUpdateRequest {
	
	@NotBlank
	@NotNull(message = "dataType is must be enter")
	@Size(min = 4,max = 20)
	private String dataType;
	
	@NotBlank
	@NotNull(message = "dataValue is must be enter")
	@Size(min = 4,max = 50)
	private String dataValue;
	
	@NotBlank
	@NotNull(message = "dataParent is must be enter")
	@Size(min = 4,max = 50)
	private String dataParent;
	
	@NotBlank
	@NotNull(message = "dataId is must be enter")
	@Size(min = 4,max = 50)
	private String dataId;

}
