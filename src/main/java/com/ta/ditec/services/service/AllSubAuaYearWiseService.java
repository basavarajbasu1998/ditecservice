package com.ta.ditec.services.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ta.ditec.services.report.domain.AUTHTransaction;
import com.ta.ditec.services.report.domain.EKYCTransaction;
import com.ta.ditec.services.request.DitecReportsRequest;
import com.ta.ditec.services.request.SubAuaRequest;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.AuaAuthMonthlyResponse;
import com.ta.ditec.services.response.AuaEKYCMonthlyResponse;
import com.ta.ditec.services.response.AuthEkycTotalTransResponse;
import com.ta.ditec.services.response.LatestTransResponse;
import com.ta.ditec.services.response.SubAuaMonthlyResponse;
import com.ta.ditec.services.response.YearWiseAllAuthResponse;

public interface AllSubAuaYearWiseService {

	public YearWiseAllAuthResponse getYearWiseAllAuthResponse(SubAuaUserRequest req);

	public AuaAuthMonthlyResponse getAuthMonthDataResponse(SubAuaUserRequest req);

	public AuaEKYCMonthlyResponse getAuaEKYCMonthlyResponse(SubAuaUserRequest req);

	public SubAuaMonthlyResponse getMonthlyResponse();

	public List<LatestTransResponse> getAuthTrans(SubAuaUserRequest req);

	public List<LatestTransResponse> getekycTrans(SubAuaUserRequest req);
	

	public List<LatestTransResponse> getekycTop100Trans(DitecReportsRequest req);

	public List<LatestTransResponse> getAuthTop100Trans(DitecReportsRequest req);

//	public List<AuthEkycTotalTransResponse> getTodayAllCount(SubAuaUserRequest req);
	public Map<String, Object> getTodayAllCount(SubAuaUserRequest req);

	public Map<String, Object> getTodayEkycAllCount(SubAuaUserRequest req);

//	public AuthEkycTotalTransResponse getEkycTodayAllCount(SubAuaUserRequest req);
}
