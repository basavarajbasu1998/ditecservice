package com.ta.ditec.services.response;

import java.util.List;

import com.ta.ditec.services.model.IntgrationInvoiceServiceCharges;

import lombok.Data;

@Data
public class InvoiceResponse {

	private String organisationName;

	private String address;
	
	private String invoiceDate;
	
	private String pincode;
	private String managementName;
	private String managementMobilenumber;
	private String managementEmail;
	private String subAuaId;
	private String invoiceNumber;
	private String gstNumber;
	private Integer status;
	private Double subtotal;
	private Double cgst;
	private Double sgst;
	private Double total;

	private List<IntgrationInvoiceServiceCharges> intgrationInvoiceServiceCharges;

}
