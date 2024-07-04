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
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "organisation_master_details")
public class OrganisationMasterDetails {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Size(max = 120)
	@Column(name = "organisation_name")
	private String organisationName;

	@Size(max = 20)
	@Column(name = "organisation_id",unique = true)
	private String organisationId;
	
	@Size(max = 100)
	@Column(name = "organisation_contact_name")
	private String organisationContactName;
	
	@Size(max = 13)
	@Column(name = "organsation_contact_mobile_number",unique = true)
	private String organisationContactMobileNumber;
	
	@Size(max = 150)
	@Column(name = "organisation_contact_email_id",unique = true)
	private String organisationContactEmailId;
	
	
	@Column(name = "active_link")
	@Range(max = 4)
	private Integer activeLink;
	
	@CreatedBy
	@Column(name = "created_by")
	private String createdBy;
	
	@CreatedDate
	@Column(name = "created_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdDate;
		
	@LastModifiedBy
	@Column(name = "last_modified_by")
	private String lastModifiedBy;
	
	@LastModifiedDate
	@Column(name = "last_modified_date")
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

}
