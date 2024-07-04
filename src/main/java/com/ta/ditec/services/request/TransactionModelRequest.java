package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class TransactionModelRequest {

	private boolean applicationEnvironmentjava;
	private boolean applicationEnvironmentnet;
	private boolean applicationEnvironmentphp;
	private boolean auaDemographic;
	private boolean auaFingerprint;
	private boolean auaIris;
	private boolean auaOtp;
	private boolean integrationApprochAPI;
	private boolean integrationApprochThinClient;
	private boolean integrationApprochmobile;
	private boolean integrationApprochweb;
	private boolean kuaFingerprint;
	private boolean kuaIris;
	private boolean kuaotp;
	private boolean otherservicesdbt;
	private boolean otherservicesdigitalsignature;
	private boolean rddevicesMantra;
	private boolean rddevicesStartek;
	private String transactionModel;
	
	@NotBlank(message="Please enter the subauaid")
	private String subAuaId;
}
