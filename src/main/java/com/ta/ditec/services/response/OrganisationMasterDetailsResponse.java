package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class OrganisationMasterDetailsResponse {

	private String organisationName;

	private Long organisationId;

	private String organisationContactName1;

	private String organisationContactMobileNumber;

	private String organisationContactEmailId;
}
