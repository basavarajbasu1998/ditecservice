package com.ta.ditec.services.request;

import lombok.Data;

@Data
public class PaginationRequest {

	private Integer start;
	private Integer end;
	private String order;
	private String orderBy;
	private String search;
	private String[] searchBy;
	private String viewType;

}
