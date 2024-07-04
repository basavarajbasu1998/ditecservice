package com.ta.ditec.services.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_master_details")
public class UserMasterDetails {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_id", unique = true)
	@Size(max = 20)
	private String userId;

	@Column(name = "state_name")
	@Size(max = 100)
	private String stateName;

	@Column(name = "level")
	@Size(max = 100)
	private String level;

	@Column(name = "authorized_person_name")
	@Size(max = 150)
	private String authorizedPersonName;

	@Column(name = "authorized_person_designation")
	@Size(max = 150)
	private String authorizedPersonDesignation;

	@Column(name = "departmant_name")
	@Size(max = 150)
	private String departmentName;

	@Column(name = "dept_id")
	@Size(max = 100)
	private String deptId;

	@Column(name = "scheme_name")
	@Size(max = 100)
	private String schemeName;

	@Column(name = "district_name")
	@Size(max = 150)
	private String districName;

	@Column(name = "block_level")
	@Size(max = 100)
	private String blockLevels;

	@Column(name = "service_opted")
	@Size(max = 100)
	private String serviceOpted;

	@Column(name = "service_start_date_and_time")
	@Size(max = 100)
	private String serviceStartDateAndTime;

	@Column(name = "service_end_date_and_time")
	@Size(max = 100)
	private String serviceEndDataAndTime;

	@Column(name = "id_type")
	@Size(max = 100)
	private String idType;

	@Column(name = "id_no")
	@Size(max = 100)
	private String idNo;

	@Column(name = "authentication_type")
	@Size(max = 100)
	private String authenticationType;

	@Column(name = "user_name", unique = true)
	@Size(max = 50)
	private String userName;

	@Column(name = "user_password")
	@Size(max = 50)
	private String userPassword;

	@Column(name = "user_mobile_number", unique = true)
	@Size(max = 13)
	private String userMobileNumber;

	@Column(name = "user_email", unique = true)
	@Size(max = 150)
	private String userEmail;

	@Column(name = "email_verify_link")
	@Range(max = 4)
	private Integer emailVerifyLink;

	@Column(name = "mobile_number_verify_link")
	@Range(max = 4)
	private Integer moblieNumberVerifyLink;

	@Column(name = "forgot_password_verifycation_link")
	@Range(max = 4)
	private Integer forgotPasswordVerifyLink;

	@Column(name = "status")
	@Size(max = 20)
	private String status;

	@Column(name = "first_time_user")
	@Range(max = 4)
	private Integer firstTimeUser;

	@Column(name = "remarks")
	private String remarks;

	@Column(name = "created_by")
	@CreatedBy
	private String createdBy;

	@Column(name = "created_date")
	@CreatedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;

	@Column(name = "last_modified_by")
	@LastModifiedBy
	private String lastModifiedBy;

	@Column(name = "last_modified_date")
	@LastModifiedDate
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

}
