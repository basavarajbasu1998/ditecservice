package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class SubAuaUserResponse {

	private Long id;

	private String subAuaId;

	private String organisationName;

	private String businessApplicationName;

	private String status;

	private String applicationstatus;

	private String address;

	private String district;

	private String city;

	private String pincode;

	private String managementName;

	private String managementDesignationName;

	private String managementMobilenumber;

	private String managementEmail;

	private String technicalName;

	private String technicalDesignationName;

	private String technicalEmail;

	private String technicalMobilenumber;

	private String createdBy;

	private String createdDate;

	private String lastModifiedBy;

	private String lastModifiedDate;

	private String authenticationPurpose;

	private String kycpurpose;

	private Boolean otherservices;

	private String userName;

	private String password;

	private Boolean applicationEnvironmentjava;

	private Boolean applicationEnvironmentnet;

	private Boolean applicationEnvironmentphp;

	private Boolean rddevicesMantra;

	private Boolean rddevicesStartek;

	private Boolean integrationApprochAPI;

	private Boolean integrationApprochThinClient;

	private Boolean integrationApprochweb;

	private Boolean integrationApprochmobile;

	private Boolean auaDemographicCategory;

	private Boolean auaDemographic;

	private Boolean auaOtp;

	private Boolean auaFingerprint;

	private Boolean auaIris;

	private Boolean kuaOtp;

	private Boolean kuaFingerprint;

	private Boolean kuaIris;

	private Boolean auaDemoghrapicBasic;

	private Boolean auaDemoghrapicpoi;

	private Boolean auaDemoghrapicFull;

	private Boolean otherservicesdbt;

	private Boolean otherservicesdigitalsignature;

	private Boolean navigateStatus;

	private String modelTransaction;
	
	private Double integrationcharges;

}
