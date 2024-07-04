package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class InvoiceTransResponse {

	private String subAuaId;

	private String invoiceId;

	private String startDate;

	private String endDate;

	private String status;

	private double netamount;

	private long id;

}
