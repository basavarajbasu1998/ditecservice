package com.ta.ditec.services.response;



import lombok.Data;

@Data
public class UserMasterDetailsResponse {

	private String userId;
	private String stateName;
	private String level;
	private String authorizedPersonName;
	private String authorizedPersonDesignation;
	private String departmentName;
	private String deptId;
	private String schemeName;
	private String districName;
	private String blockLevels;
	private String serviceOpted;
	private String serviceStartDateAndTime;
	private String serviceEndDataAndTime;
	private String idType;
	private String idNo;
	private String authenticationType;
	private String userName;
	private String userPassword;
	private String userMobileNumber;
	private String userEmail;
	private Integer emailVerifyLink;
	private Integer moblieNumberVerifyLink;
	private Integer forgotPasswordVerifyLink;
	private String status;
	private Integer firstTimeUser;
	private String remarks;
}
