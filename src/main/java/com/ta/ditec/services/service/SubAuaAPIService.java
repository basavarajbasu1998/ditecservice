package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.SubAuaAPI;
import com.ta.ditec.services.request.SubAuaApiKeysRequest;

public interface SubAuaAPIService {

//	public SubAuaAPI getSubAuaAPIStage(SubAuaAPI req);
//	public SubAuaAPI getSubAuaAPIProd(SubAuaAPI req);

	public SubAuaAPI getApikeys(SubAuaAPI req);

	public List<SubAuaAPI> getSubAuaAPIAll(SubAuaApiKeysRequest req);

}
