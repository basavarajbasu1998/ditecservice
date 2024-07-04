package com.ta.ditec.services.request;

import lombok.Data;

@Data
public class PaymentMasterRequest {

	private String subAuaId;
	
	private Double auaOtp;
	
	private Double kuaOtp;
	
	private Double auaBio;
	
	private Double kuaBio;
	
	private String startdate;
	
	private String enddate;
}
