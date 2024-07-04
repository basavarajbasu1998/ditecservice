package com.ta.ditec.services.response;

import java.math.BigInteger;
import java.util.Date;

import lombok.Data;

@Data
public class DitecReportsServiceResponse {

	private String agencyCode;
	private Date date;
	private BigInteger ekycTotalCountKua;
	private BigInteger otpYCountKua;
	private BigInteger bioYCountKua;
	private BigInteger ekycTotalCountAua;
	private BigInteger otpYCountAua;
	private BigInteger bioYCountAua;

	private Long slNo;
}
