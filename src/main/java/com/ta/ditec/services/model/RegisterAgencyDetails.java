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
@Table(name = "Register_Agency_Details")
public class RegisterAgencyDetails {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Column(name = "agency_id")
	@Size(max = 50)
	private String agencyId;

	@Column(name = "agency_name")
	@Size(max = 50)
	private String agencyName;

	@Column(name = "agency_designation")
	@Size(max = 100)
	private String agencyDesignation;

	@Column(name = "agency_mobile_number")
	@Size(max = 13)
	private String agencyMobileNumber;

	@Column(name = "agency_email_id")
	@Size(max = 25)
	private String agencyEmailId;

	@Column(name = "agency_status")
	@Size(max = 50)
	private String agencyStatus;

	@CreatedBy
	@Size(max = 25)
	@Column(name = "created_by")
	private String createdBy;

	@Temporal(TemporalType.TIMESTAMP)
	@CreatedDate
	@Column(name = "created_date")
	private Date createdDate;

	@LastModifiedBy
	@Size(max = 25)
	@Column(name = "last_modified_by")
	private String lastModifiedBy;

	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name = "last_modified_date")
	private Date lastModifiedDate;
}
