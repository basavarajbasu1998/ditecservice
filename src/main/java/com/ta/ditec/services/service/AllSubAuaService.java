package com.ta.ditec.services.service;

import java.util.Map;

import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.AuthTotalResponse;
import com.ta.ditec.services.response.SubAuaTransactionResponse;
import com.ta.ditec.services.response.TodayAuthResponse;
import com.ta.ditec.services.response.TodayEkycResponse;

public interface AllSubAuaService {

	AuthTotalResponse getAuthTotalResponse();

	TodayAuthResponse getYearAuthResponse(SubAuaUserRequest req);

	TodayEkycResponse getYearKycResponse(SubAuaUserRequest req);

	TodayAuthResponse getTodayAuthResponse(SubAuaUserRequest req);

	TodayEkycResponse getTodayKycResponse(SubAuaUserRequest req);

//	public AllSubAuaAuthResponse[] getTodaysubauaAuthResponseForEachAgency(SubAuaUserRequest req);

	Map<String, Object> getTodaysubauaAuthResponseForEachAgency(SubAuaUserRequest req);

	public Map<String, Object> getTodaysubauaKuaResponseForEachAgency(SubAuaUserRequest req);

	Map<String, Object> getTodayTotalsubauaKuaResponseForEachAgency();
	
	Map<String, Object> getTotalsubauaKuaResponseForEachAgency();

	SubAuaTransactionResponse getAuthTotalRequest(SubAuaUserRequest req);

	public TodayAuthResponse getSubAuaWiseTodayAuthResponse(SubAuaUserRequest req);

}
