package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class InvoiceServiceResponse {

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
	private String billingcycle;
	private Integer status;
	private Double kua;
	private Double aua;
	private Double cgst;
	private Double sgst;
	private Double subtotal;
	private Double total;

	private String authpertrans;

	private String ekycpertrans;

	private long authTrns;
	private long ekycTrns;

	private String amountword;

}
