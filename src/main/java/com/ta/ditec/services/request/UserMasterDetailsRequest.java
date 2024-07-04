package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMasterDetailsRequest {

	
	@NotBlank(message = "userName is must!")
	@Size(min = 4, max = 30)	
	private String userName;

	
	@Size(min = 4, max = 100)
	private String stateName;

	
	@Size(min = 4, max = 100)
	private String level;

	
	@Size(min = 4, max = 100)
	private String authorizedPersonName;

	@NotNull(message = "authorizedPersonDesignation is must!")
	@NotBlank
	@Size(min = 4, max = 100)
	private String authorizedPersonDesignation;

	@NotNull(message = "departmentName is must!")
	@NotBlank
	@Size(min = 3, max = 100)
	private String departmentName;

	@NotNull(message = "deptId is must!")
	@NotBlank
	@Size(min = 4, max = 100)
	private String deptId;

	@NotNull(message = "schemeName is must!")
	@NotBlank
	@Size(min = 4, max = 100)
	private String schemeName;

	@NotNull(message = "districName is must!")
	@NotBlank
	@Size(min = 4, max = 100)
	private String districName;

	@NotNull(message = "blockLecels is must!")
	@NotBlank
	@Size(min = 4, max = 100)
	private String blockLevels;

	@NotNull(message = "serviceOpted is must!")
	@NotBlank
	@Size(min = 4, max = 100)
	private String serviceOpted;

	@NotNull(message = "serviceStartDateAndTime is must!")
	@NotBlank
	@Size(min = 4, max = 100)
	private String serviceStartDateAndTime;

	@NotNull(message = "serviceEndDataAndTime id is must!")
	@NotBlank
	@Size(min = 4, max = 100)
	private String serviceEndDataAndTime;

	@NotNull(message = "idType is must!")
	@NotBlank
	@Size(min = 4, max = 100)
	private String idType;

	@NotNull(message = "idNo is must!")
	@NotBlank
	@Size(min = 4, max = 100)
	private String idNo;

	@NotNull(message = "authenticationType id is must!")
	@NotBlank
	@Size(min = 4, max = 100)
	private String authenticationType;

	@NotNull
	@NotBlank
	@Length(min = 10, max = 13)
	@Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\d{10,12}$", message = "Enter valid mobile number")
	private String userMobileNumber;

	@NotNull(message = "Email id is must!")
	@NotBlank
	@Size(min = 4, max = 50)
	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "Enter valid EmailID")
	private String userEmail;

}
