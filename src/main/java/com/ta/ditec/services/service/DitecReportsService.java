package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.request.DitecReportsRequest;
import com.ta.ditec.services.response.DitecReportsServiceResponse;
import com.ta.ditec.services.response.LatestTransResponse;
import com.ta.ditec.services.response.SubAuaOrgResponse;

public interface DitecReportsService {

	public List<DitecReportsServiceResponse> getDitecReportsServiceResponse(DitecReportsRequest req);

	public List<SubAuaOrgResponse> getList();
	List<LatestTransResponse> geteTop100Trans(DitecReportsRequest req);

	// DitecReportsServiceResponse req
}
