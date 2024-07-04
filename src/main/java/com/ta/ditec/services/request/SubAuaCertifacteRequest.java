package com.ta.ditec.services.request;

import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data

public class SubAuaCertifacteRequest {

//	private String certificate1;
//
//	private String certificate2;
//
//	private String certificate3;
//
//	private String certificate4;
//
//	private String certificate5;
//
//	private String certificate6;
//
//	private String approvalStatus;

	@NotBlank(message="Please enter the subauaid")
	private String subauaId;
	
	@NotBlank(message="Please enter the certificateId")
	private String certificateId;
	
	@NotBlank(message="Please enter the certificatePath")
	private String certificatePath;
	
	@NotBlank(message="Please enter the certificateStatus")
	private String certificateStatus;
	
	@NotBlank(message="Please enter the certificateReamrk")
	private String certificateReamrk;
	
	@NotBlank(message="Please enter the certificateApprovedReamrk")
	private String certificateApprovedReamrk;
	
	@NotBlank(message="Please enter the certificateRejectReamrk")
	private String certificateRejectReamrk;

}
