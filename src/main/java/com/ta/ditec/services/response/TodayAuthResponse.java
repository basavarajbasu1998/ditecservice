package com.ta.ditec.services.response;

import java.util.List;

import lombok.Data;

@Data
public class TodayAuthResponse {

	private List<Long> authcount;

	private List<String> authnames;
}
