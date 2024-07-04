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
import org.springframework.data.annotation.LastModifiedDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "servicemastercharge")

public class ServiceMasterCharges {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Column
	private String serviceMasterChargesId;

	@Column
	private String servicetype;

	@Column
	private Double transactionStart1;

	@Column
	private Double transactionStart2;

	@Column
	private Double transactionStart3;

	@Column
	private Double transactionStart4;

	private Double transactionEnd1;

	@Column
	private Double transactionEnd2;

	@Column
	private Double transactionEnd3;

	@Column
	private Double transactionEnd4;

	@Column
	private Double transactioncharges1;

	@Column
	private Double transactioncharges2;

	@Column
	private Double transactioncharges3;

	@Column
	private Double transactioncharges4;

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
