package com.ta.ditec.services.request;

import lombok.Data;

@Data
public class SubAuaInvoiceUserRequest {

	private String subAuaId;

	private String startDate;

	private String endDate;
}
