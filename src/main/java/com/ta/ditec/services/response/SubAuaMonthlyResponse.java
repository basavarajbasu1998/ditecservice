package com.ta.ditec.services.response;

import java.util.List;

import lombok.Data;

@Data
public class SubAuaMonthlyResponse {

	private List<String> dailydate;

	private List<Long> dailycounts;

	
}
