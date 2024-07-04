package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.SubAuaCertifacteMaster;
import com.ta.ditec.services.request.SubAuaCertifacteMasterUpadateRequest;
import com.ta.ditec.services.response.SubAuaCertifacteMasterAllDataResponse;

public interface SubAuaCertifacteMasterService {

	SubAuaCertifacteMaster getSubAuaCertifacteMaster(SubAuaCertifacteMaster req);

	List<SubAuaCertifacteMasterAllDataResponse> getallData();

	public void getData(Long id);

	SubAuaCertifacteMaster updateSubAuaCertifacteMaster(SubAuaCertifacteMasterUpadateRequest req);

}
