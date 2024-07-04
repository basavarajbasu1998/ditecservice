package com.ta.ditec.services.response;

import java.util.List;

import lombok.Data;

@Data
public class AuaAuthMonthlyResponse {

	private List<String> authdates;
	private List<Integer> authfingerprint;
	private List<Integer> authiris;
	private List<Integer> authdemographic;

}
