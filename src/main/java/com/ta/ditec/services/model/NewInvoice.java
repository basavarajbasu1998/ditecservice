package com.ta.ditec.services.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "newinvoice")
@Data
public class NewInvoice {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column
	private String subAuaId;

	@Column
	private String invoiceNumber;

	@Column
	private String gstNumber;

	@Column
	private String billingcycle;

	@Column
	private String status;

	@Column
	private String remarks;

	@Column
	private double kuaamount;

	@Column
	private double auaamount;

	@Column
	private double cgst;

	@Column
	private double sgst;

	@Column
	private double subtotal;

	@Column
	private double total;

	@Column
	private long authTrns;

	@Column
	private long authperTrns;

	@Column
	private long ekycTrns;

	@Column
	private long ekycperTrns;

	@Column
	private String amountword;

}
