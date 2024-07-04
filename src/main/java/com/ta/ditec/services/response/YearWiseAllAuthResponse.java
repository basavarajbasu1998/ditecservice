package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class YearWiseAllAuthResponse {

	private Long yearwisetotal;
	private Long yearwiseauthtotal;
	private Long yearwiseekyctotal;

	private Long authdemo;
	private Long authbio;
	private Long authiris;

	private Long ekycotp;
	private Long ekycbio;
	private Long ekyciris;

}
