package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.SubAuaAPI;
import com.ta.ditec.services.request.SubAuaApiKeysRequest;

public class SubAuaAPITransform {

	public SubAuaAPI getSubAuaAPI(SubAuaApiKeysRequest req) {
		SubAuaAPI apis = new SubAuaAPI();
		apis.setCreatedBy("super admin");
		apis.setCreatedDate(new Date());
		apis.setLastModifiedBy("super admin");
		apis.setLastModifiedDate(new Date());
		BeanUtils.copyProperties(req, apis);
		return apis;

	}
}
