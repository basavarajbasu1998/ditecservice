package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class AllSubAuaAuthResponse {

	private Long authdemographic;
	private Long authfingerprint;
	private Long authiris;
	private String subAuaId;

//	private List<Map<String, Object>> data; // List of data for Demographic, Fingerprint, and Iris
//	private List<String> agencyname;
}
