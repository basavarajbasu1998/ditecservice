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
@Table(name = "agency_details")
public class AgencyDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "agency_id")
	@Size(max = 20)
	private String agencyId;

	@Size(max = 80)
	@Column(name = "agency_first_name")
	private String agencyFirstName;

	@Size(max = 80)
	@Column(name = "agency_last_name")
	private String agencyLastName;

	@Size(max = 80)
	@Column(name = "agency_id_type")
	private String agencyIdType;

	@Size(max = 20)
	@Column(name = "agency_SSNO")
	private String agencySSNO;

	@Size(max = 40)
	@Column(name = "agency_state_name")
	private String agencyStateName;

	@Size(max = 50)
	@Column(name = "agency_level")
	private String agencyLevel;

	@Size(max = 80)
	@Column(name = "agency_department_list")
	private String agencyDepartmentList;

	@Size(max = 50)
	@Column(name = "agency_scheme_level")
	private String agencySchemeLevel;

	@Size(max = 50)
	@Column(name = "agency_distric_name")
	private String agencyDistrictName;

	@Size(max = 50)
	@Column(name = "agency_black_level")
	private String agencyBlackLevel;

	@Column(name = "agency_services")
	@Size(max = 50)
	private String agencyServices;

	@Column(name = "agency_select_menu")
	@Size(max = 50)
	private String agencySelectMenu;

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
