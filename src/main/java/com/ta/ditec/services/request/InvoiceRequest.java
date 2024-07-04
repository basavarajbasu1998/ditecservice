package com.ta.ditec.services.request;

import javax.validation.constraints.Size;

import lombok.Data;

@Data
public class InvoiceRequest {

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String invoiceNo;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String date;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String client;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String address;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String email;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String billingCycle;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String billingCycle1;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String totalperiod;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String subtotal;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String sgst;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String cgst;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String total;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String description;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String description1;

//	@NotBlank
//	@NotNull(message = "integrationType is must be enter")
	@Size(min = 4, max = 50)
	private String subAuaId;
}
