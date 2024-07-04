package com.ta.ditec.services.response;

import java.util.List;

import lombok.Data;

@Data
public class TodayEkycResponse {

//	private Long ekycotp;
//	private Long ekycfingerprint;
//	private Long ekyciris;

	private List<Long> ekyccount;

	private List<String> ekycnames;
}
