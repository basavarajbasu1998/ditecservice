package com.ta.ditec.services.request;

import java.util.List;

import lombok.Data;

@Data
public class TodayAuthResponse {

	private List<Long> authcount;

	private List<String> authnames;
}
