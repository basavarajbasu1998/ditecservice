package com.ta.ditec.services.request;

import lombok.Data;

@Data
public class InvoiceTransDetilesRequest {

	private String invoiceId;

	private String remarks;

	private String status;

	private String subAuaId;

}
