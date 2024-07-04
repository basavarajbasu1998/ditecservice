package com.ta.ditec.services.request;

import lombok.Data;

@Data
public class SubAuaCertifacteApproveRequest {

//	private String subauaId;

	private String status;

	private String certificateId;

	private String remarks;
}
