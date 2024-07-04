package com.ta.ditec.services.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "status_master_details")
public class StatusMasterDetails {
	
	@Id
	@GeneratedValue
	private Long id;
	
	
	@Column(name = "status_id")
	@Size(max = 20)
	private String statusId;
	
	@Column(name = "status_name")
	@Size(max = 50)
	private String statusName;
	
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
	@LastModifiedBy
	@Temporal(TemporalType.TIMESTAMP)
	private Date lastModifiedDate;

}
