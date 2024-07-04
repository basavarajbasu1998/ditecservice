package com.ta.ditec.services.request;

import lombok.Data;

@Data
public class SubAuaUpdateRequest {

	private String subAuaId;

//	@NotNull(message = "address  is must!")
//	@NotBlank
//	@Size(min = 4, max = 250)
	private String address;

//	@NotNull(message = "district  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private String district;

//	@NotNull(message = "city  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private String city;

//	@NotNull(message = "pincode  is must!")
//	@NotBlank
//	@Size(min = 4, max = 13)
	private String pincode;

//	@NotNull(message = "managementName  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private String managementName;

//	@NotNull(message = "managementDesignationName  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private String managementDesignationName;

//	@NotNull(message = "managementMobilenumber  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
//	@Length(min = 10, max = 13, message = "Mobile number must be 10 numbers or 13")
//	@Pattern(regexp = "^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}$", message = "Enter valid management Mobilenumber ")
	private String managementMobilenumber;

//	@NotNull(message = "managementEmail  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
//	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Enter valid EmailID")
	private String managementEmail;

//	@NotNull(message = "technicalName  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private String technicalName;

//	@NotNull(message = "technicalDesignationName  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private String technicalDesignationName;

//	@NotNull(message = "technicalEmail  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
//	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Enter valid EmailID")
	private String technicalEmail;

//	@NotNull(message = "technicalMobilenumber  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
//	@Length(min = 10, max = 13, message = "Mobile number must be 10 numbers or 13")
//	@Pattern(regexp = "^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}$", message = "Enter valid management Mobilenumber ")
	private String technicalMobilenumber;

//	@NotNull(message = "auaDemographicCategory  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private Boolean auaDemographicCategory;

//	@NotNull(message = "auaDemographic  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private Boolean auaDemographic;

//	@NotNull(message = "auaOtp  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private Boolean auaOtp;

//	@NotNull(message = "auaFingerprint  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private Boolean auaFingerprint;

//	@NotNull(message = "auaIris  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private Boolean auaIris;

//	@NotNull(message = "kuaOtp  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private Boolean kuaOtp;

//	@NotNull(message = "kuaFingerprint  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private Boolean kuaFingerprint;

//	@NotNull(message = "kuaIris  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private Boolean kuaIris;

//	@NotNull(message = "auaDemoghrapicBasic  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private Boolean auaDemoghrapicBasic;

//	@NotNull(message = "auaDemoghrapicpoi  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private Boolean auaDemoghrapicpoi;

//	@NotNull(message = "auaDemoghrapicFull  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private Boolean auaDemoghrapicFull;
//
//	@NotNull(message = "organizationName  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private String organisationName;

//	@NotNull(message = "businessApplicationName  is must!")
//	@NotBlank
//	@Size(min = 4, max = 100)
	private String businessApplicationName;

	private String status;

	private String applicationstatus;

	private String authenticationPurpose;

	private String kycpurpose;

	private Boolean applicationEnvironmentjava;

	private Boolean applicationEnvironmentnet;

	private Boolean applicationEnvironmentphp;

	private Boolean rddevicesMantra;

	private Boolean rddevicesStartek;

	private Boolean integrationApprochAPI;

	private Boolean integrationApprochThinClient;

	private Boolean integrationApprochweb;

	private Boolean integrationApprochmobile;

	private Boolean otherservices;

	private String userName;

	private String password;

	private Boolean otherservicesdbt;

	private Boolean otherservicesdigitalsignature;

	private String modelTransaction;

	private Double integrationcharges;

}
