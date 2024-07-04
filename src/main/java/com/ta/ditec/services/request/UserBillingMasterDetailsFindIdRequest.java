package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserBillingMasterDetailsFindIdRequest {
	
	@NotBlank(message = "billingId is must be enter")
	
	@Size(min = 4, max = 20)
	private String billingId;
	
//
//	@NotNull(message = "entitylevel is must be enter")
//	@Size(min = 4, max = 50)
//	private String agencyName;

}
