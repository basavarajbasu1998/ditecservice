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
public class ModuleMasterDetailsRequest {


	@NotBlank
	@NotNull(message = "moduleName is must be enter")
	@Size(min = 4,max = 50)
	private String moduleName;
	
	@NotBlank
	@NotNull(message = "moduleIcon is must be enter")
	@Size(min = 4,max = 200)
	private String moduleIcon;
	
	@NotBlank
	@NotNull(message = "moduleUrl is must be enter")
	@Size(min = 4,max = 200)
	private String moduleUrl;

}
