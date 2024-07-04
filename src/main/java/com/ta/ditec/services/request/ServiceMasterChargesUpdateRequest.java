package com.ta.ditec.services.request;

import lombok.Data;

@Data
public class ServiceMasterChargesUpdateRequest {

	private String serviceMasterChargesId;

	private String servicetype;

	private Double transactionStart1;

	private Double transactionStart2;

	private Double transactionStart3;

	private Double transactionStart4;

	private Double transactionEnd1;

	private Double transactionEnd2;

	private Double transactionEnd3;

	private Double transactionEnd4;

	private Double transactioncharges1;

	private Double transactioncharges2;

	private Double transactioncharges3;

	private Double transactioncharges4;

}
