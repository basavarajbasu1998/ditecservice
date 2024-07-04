package com.ta.ditec.services.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class SubAuaRequest {

		
	//@Size(min = 4, max = 25)
	private String address;

		
		//@Size(min = 4, max = 25)
	private String district;

		
		//@Size(min = 4, max = 25)
	private String city;

		//@NotBlank(message = "pincode  is must!")
	//	@Size(min = 6, max = 6)
	private String pincode;

		
		@NotBlank(message = "managementName  is must!")
		@Size(min = 4, max = 100)
	private String managementName;

		
		@NotBlank(message = "managementDesignationName  is must!")
		@Size(min = 4, max = 100)
	private String managementDesignationName;

	
	@NotBlank(message = "managementMobilenumber  is must!")
//	
//		@Length(min = 10, max = 13, message = "Mobile number must be 10 numbers or 13")
//		@Pattern(regexp = "^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}$", message = "Enter valid management Mobilenumber ")
	private String managementMobilenumber;

		@Email
	@NotBlank(message="Please enter management email")
	private String managementEmail;

		
		@NotBlank(message = "technicalName  is must!")
		@Size(min = 4, max = 100)
	private String technicalName;

		
	@NotBlank(message = "technicalDesignationName  is must!")
		@Size(min = 4, max = 100)
	private String technicalDesignationName;

		
		@NotBlank(message = "technicalEmail  is must!")
		@Email
	private String technicalEmail;


	@NotBlank(message = "technicalMobilenumber  is must!")
//		
//		@Length(min = 10, max = 13, message = "Mobile number must be 10 numbers or 13")
//		@Pattern(regexp = "^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}$", message = "Enter valid management Mobilenumber ")
	private String technicalMobilenumber;

		
	//	@NotBlank(message = "auaDemographicCategory  is must!")
		//@Size(min = 4, max = 100)
	private Boolean auaDemographicCategory;

		
	//	@NotBlank(message = "auaDemographic  is must!")
	//	@Size(min = 4, max = 100)
	private Boolean auaDemographic;

	
	//	@NotBlank(message = "auaOtp  is must!")
	//	@Size(min = 4, max = 100)
	private Boolean auaOtp;

		
	//	@NotBlank(message = "auaFingerprint  is must!")
		
	private Boolean auaFingerprint;

		
	//	@NotBlank(message = "auaIris  is must!")
		
	private Boolean auaIris;

		
	//	@NotBlank(message = "kuaOtp  is must!")
		
	private Boolean kuaOtp;

//		@NotNull(message = "kuaFingerprint  is must!")
//		@NotBlank
//		@Size(min = 4, max = 100)
	private Boolean kuaFingerprint;

//		@NotNull(message = "kuaIris  is must!")
//		@NotBlank
//		@Size(min = 4, max = 100)
	private Boolean kuaIris;

//		@NotNull(message = "auaDemoghrapicBasic  is must!")
//		@NotBlank
//		@Size(min = 4, max = 100)
	private Boolean auaDemoghrapicBasic;

//		@NotNull(message = "auaDemoghrapicpoi  is must!")
//		@NotBlank
//		@Size(min = 4, max = 100)
	private Boolean auaDemoghrapicpoi;

//		@NotNull(message = "auaDemoghrapicFull  is must!")
//		@NotBlank
//		@Size(min = 4, max = 100)
	private Boolean auaDemoghrapicFull;
	
	//	@NotNull
		@NotBlank(message = "organizationName  is must!")
		@Size(min = 4, max = 100)
	private String organisationName;

	//	@NotNull
		@NotBlank(message = "businessApplicationName  is must!")
		@Size(min = 4, max = 100)
	private String businessApplicationName;

	//	@NotBlank(message = "businessApplicationName  is must!")
	private String status;

	private String applicationstatus;

	@NotBlank(message = "authenticationPurpose  is must!")
	private String authenticationPurpose;

	@NotBlank(message = "kycpurpose  is must!")
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

}
