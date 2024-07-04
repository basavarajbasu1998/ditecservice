package com.ta.ditec.services.request;

import lombok.Data;

@Data
public class PaymentModeRequest {

	private String subAuaId;
	private String amount;
	private String chequeNumber;
	private String accountPersonName;
	private String ifscNo;
	private String bankName;
	private String paymentMode;
	private String acNo;

	private String chequeDate;
}
