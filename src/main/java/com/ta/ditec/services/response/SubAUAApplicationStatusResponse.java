package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class SubAUAApplicationStatusResponse {

	private String subauaId;
	
	private String statusCount;
	
	private String presentStep;
	
	private String nextStep;

	
}
