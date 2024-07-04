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
public class UserBillingMasterDetailsRequest {

	@NotBlank
	@NotNull(message = "entitylevel is must be enter")
	@Size(min = 4, max = 50)
	private String entitylevel;
	
	@NotBlank
	@NotNull(message = "userId is must be enter")
	@Size(min = 4, max = 50)
	private String userId;

	@NotBlank
	@NotNull(message = "billingtype is must be enter")
	@Size(min = 4, max = 80)
	private String billingtype;

	@NotBlank
	@NotNull(message = "agencybillingCycle is must be enter")
	@Size(min = 4, max = 80)
	private String agencybillingCycle;

	@NotBlank
	@NotNull(message = "departmentName is must be enter")
	@Size(min = 4, max = 80)
	private String departmentName;

	@NotBlank
	@NotNull(message = "servicesStartDateandtime is must be enter")
	@Size(min = 4, max = 80)
	private String servicesStartDateandtime;

	@NotBlank
	@NotNull(message = "servicesEndDateandtime is must be enter")
	@Size(min = 4, max = 80)
	private String servicesEndDateandtime;

	@NotBlank
	@NotNull(message = "deptID is must be enter")
	@Size(min = 4, max = 15)
	private String deptID;
}
