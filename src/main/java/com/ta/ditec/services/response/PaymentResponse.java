package com.ta.ditec.services.response;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class PaymentResponse {

	private String invoiceNumber;

	private String email;

	private String businessname;

	private String auabillingCycle;

	private String kuabillingCycle;

	private String address;

	private String mobileNumber;

	private String auaServicetype;
	private String kuaServicetype;

	private Double auapayment;

	private Double kuapayment;

	private Double subtotal;

	private BigDecimal sgst;

	private BigDecimal cgst;

	private BigDecimal total;

	private String startDate;

	private String endDate;

	private String duedate;

	private String amountwords;

}
