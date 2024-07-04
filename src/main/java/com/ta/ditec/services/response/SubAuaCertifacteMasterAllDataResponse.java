package com.ta.ditec.services.response;

import java.util.List;

import com.ta.ditec.services.model.CertifacteSub;

import lombok.Data;

@Data
public class SubAuaCertifacteMasterAllDataResponse {
	private Integer slNo;
	private Long id;
	private String certfacteMasterTitle;
	private String createdBy;
	private String lastModifiedBy;
	private String createdDate;
	private String lastModifiedDate;

	private List<CertifacteSub> list;
}
