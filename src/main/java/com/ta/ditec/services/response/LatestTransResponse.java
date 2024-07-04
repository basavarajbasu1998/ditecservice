package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class LatestTransResponse {

	private String requestdate;
	private String requesttxnid;
	private String errorMsg;
	private long slNo;
}
