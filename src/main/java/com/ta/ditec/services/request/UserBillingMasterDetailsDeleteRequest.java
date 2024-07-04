package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class UserBillingMasterDetailsDeleteRequest {

	@NotBlank(message = "agencyId is Must!")
	@Size(min = 4, max = 20)
	private String billingId;

}
