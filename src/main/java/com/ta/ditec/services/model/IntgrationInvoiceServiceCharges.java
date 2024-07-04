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

@Data
//@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "intigration_invoicecharge")
public class IntgrationInvoiceServiceCharges {

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private int id;

	@Column
	private String mainService;

	@Column
	private String subService;

	@Column
	private String amount;

	@Column
	private String subAuaId;

	public IntgrationInvoiceServiceCharges(int id, String mainService, String subService, String amount,
			String subAuaId) {
		this.id = id;
		this.mainService = mainService;
		this.subService = subService;
		this.amount = amount;
		this.subAuaId = subAuaId;
	}

}
