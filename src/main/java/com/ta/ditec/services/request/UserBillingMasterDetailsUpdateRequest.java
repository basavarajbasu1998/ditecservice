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
public class UserBillingMasterDetailsUpdateRequest {

	@NotBlank
	@NotNull(message = "agencybilling is must be enter")
	@Size(min = 4, max = 150)
	private String billingtype;

	@NotBlank
	@NotNull(message = "agencybillingCycle is must be enter")
	@Size(min = 4, max = 150)
	private String agencybillingCycle;

	@NotBlank
	@NotNull(message = "billingId is must be enter")
	@Size(min = 4, max = 150)
	private String billingId;

	@NotBlank
	@NotNull(message = "departmentName is must be enter")
	@Size(min = 4, max = 150)
	private String departmentName;

	@NotBlank
	@NotNull(message = "servicesStartDateandtime is must be enter")
	@Size(min = 4, max = 150)
	private String servicesStartDateandtime;

	@NotBlank
	@NotNull(message = "servicesStartDateandtime is must be enter")
	@Size(min = 4, max = 50)
	private String servicesEndDateandtime;

	@NotBlank
	@NotNull(message = "deptID is must be enter")
	@Size(min = 4, max = 150)
	private String deptID;

}
