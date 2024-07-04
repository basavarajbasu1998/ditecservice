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
public class StatusMasterDetailsRequset {

	@NotBlank
	@NotNull(message = "statusName is must be enter")
	@Size(min = 4,max = 40)
	private String statusName;

}
