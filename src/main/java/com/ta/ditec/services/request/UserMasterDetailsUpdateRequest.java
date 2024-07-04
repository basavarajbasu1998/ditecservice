package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

@Data
public class UserMasterDetailsUpdateRequest {
	
	@NotNull(message = "userId is must!")
	@NotBlank
	@Size(min = 4,max = 30)
	private String userId;
	
	@NotNull(message = "userName is must!")
	@NotBlank
	@Size(min = 4,max = 30)
	private String userName;

	@NotNull(message = "stateName is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String stateName;
	
	@NotNull(message = "level is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String level;
	
	@NotNull(message = "authorizedPersonName is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String authorizedPersonName;
	
	@NotNull(message = "authorizedPersonDesignation is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String authorizedPersonDesignation;
	
	@NotNull(message = "departmentName is must!")
	@NotBlank
	@Size(min = 3,max = 100)
	private String departmentName;
	
	@NotNull(message = "deptId is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String deptId;
	
	@NotNull(message = "schemeName is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String schemeName;
	
	@NotNull(message = "districName is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String districName;
	
	@NotNull(message = "blockLecels is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String blockLevels;
	
	@NotNull(message = "serviceOpted is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String serviceOpted;
	
	@NotNull(message = "serviceStartDateAndTime is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String serviceStartDateAndTime;
	
	@NotNull(message = "serviceEndDataAndTime id is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String serviceEndDataAndTime;
	
	@NotNull(message = "idType is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String idType;
	
	@NotNull(message = "idNo is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String idNo;
	
	@NotNull(message = "authenticationType id is must!")
	@NotBlank
	@Size(min = 4,max = 100)
	private String authenticationType;
	
	@NotNull
	@NotBlank
	@Length(min = 10, max = 13, message = "Mobile number must be 10 numbers or 13")
	@Pattern(regexp = "^(\\+91[\\-\\s]?)?[0]?(91)?[6789]\\d{9}$", message = "Enter valid mobile number")
	private String userMobileNumber;
	
	@NotNull(message = "Email id is must!")
	@NotBlank
	@Size(min = 4,max = 50)
	@Pattern(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$",message = "Enter valid EmailID")
	private String userEmail;
	
	@NotNull(message = "remarks must!")
	@NotBlank
	@Size(min = 4,max = 250)
	private String remarks;
	

}
