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

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "integration_invoice")
public class IntegrationInvoiceDetiles {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Long id;

	@Column
	private String organisationName;

	@Column
	private String address;

	@Column
	private String pincode;

	@Column
	private String managementName;

	@Column
	private String managementMobilenumber;

	@Column
	private String managementEmail;

	@Column
	private String subAuaId;

	@Column
	private String invoiceNumber;

	@Column
	private String gstNumber;

	@Column
	private Integer status;

	@Column
	private Double subtotal;

	@Column
	private Double cgst;

	@Column
	private Double sgst;

	@Column
	private Double total;

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
