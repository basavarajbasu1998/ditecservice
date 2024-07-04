package com.ta.ditec.services.response;

import java.util.List;

import lombok.Data;

@Data
public class SubauaAllDataResponse {

	private List<SubAuaUserResponse> listData;

	private List<LatestTransResponse> latestdata;

	private PaginationMeta pagination;

}
