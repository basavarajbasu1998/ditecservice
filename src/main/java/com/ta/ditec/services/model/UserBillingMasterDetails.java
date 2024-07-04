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
@Table(name = "User_Billing_Master_Details")
public class UserBillingMasterDetails {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Column(name = "billing_id",unique = true)
	@Size(max = 50)
	private String billingId;
	
	@Column(name = "user_id",unique = true)
	@Size(max = 50)
	private String userId;

	@Column(name = "agency_entity_level")
	@Size(max = 50)
	private String entitylevel;

	@Column(name = "billing_type")
	@Size(max = 150)
	private String billingtype;

	@Column(name = "billing_cycle")
	@Size(max = 150)
	private String agencybillingCycle;

	@Column(name = "department_Name")
	@Size(max = 150)
	private String departmentName;

	@Column(name = "Services_start_Date_and_time")
	@Size(max = 150)
	private String servicesStartDateandtime;

	@Column(name = "Services_end_Date_and_time")
	@Size(max = 150)
	private String servicesEndDateandtime;

	@Column(name = "dept_id")
	@Size(max = 50)
	private String deptID;
	
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
