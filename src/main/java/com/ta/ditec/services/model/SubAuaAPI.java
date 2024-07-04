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

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "subaua_apikeys")
public class SubAuaAPI {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Column
	private String subauaApikey;

	@Column
	private String prodkey;

	@Column
	private String stagingkey;

	@Column(name = "subaua_ditec_code_staging")
	private String subauastagekey;

	@Column(name = "subaua_ditec_code_production")
	private String subauaproductionkey;

//	@Column
//	private String environment;

	@Column
	private String subAuaId;

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
