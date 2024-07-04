package com.ta.ditec.services.response;

import java.util.List;

import lombok.Data;

@Data
public class AuaEKYCMonthlyResponse {

	private List<String> ekycdates;
	private List<Integer> ekycdemographic;
	private List<Integer> ekycfingerprint;
	private List<Integer> ekyciris;

}
