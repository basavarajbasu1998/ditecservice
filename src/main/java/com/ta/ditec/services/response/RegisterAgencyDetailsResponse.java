package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class RegisterAgencyDetailsResponse {

	private String agencyId;

	private String agencyName;

	private String agencyDesignation;

	private String agencyMobileNumber;

	private String agencyEmailId;

	private String agencyStatus;
}
