package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
public class SubAuaCertifacteUpdateRequest {

	@NotBlank(message="Please enter the subauaid")
	private String subauaId;

	@NotBlank(message="Please enter the certificate1")
	private String certificate1;

	@NotBlank(message="Please enter the certificate2")
	private String certificate2;

	@NotBlank(message="Please enter the certificate3")
	private String certificate3;

	@NotBlank(message="Please enter the certificate4")
	private String certificate4;

	@NotBlank(message="Please enter the certificate5")
	private String certificate5;

	@NotBlank(message="Please enter the certificate6")
	private String certificate6;

	@NotBlank(message="Please enter the approvalStatus")
	private String approvalStatus;

}
