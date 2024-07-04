package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class PaginationResponse<T> {
	
	private Integer start;
	private Integer end;
	private String order;
	private String orderBy;
	private Long total;
	
	private T responseData;
	
	

}
