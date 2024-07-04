package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.MasterParameterDetails;
import com.ta.ditec.services.request.MasterParameterDetailsUpadeteRequest;

public interface MasterParameterDetailsService {

	public MasterParameterDetails getMasterParameterDetails(MasterParameterDetails master);

	public MasterParameterDetails getMasterParameterUpadteDetails(MasterParameterDetailsUpadeteRequest master);

	List<MasterParameterDetails> getallMasterParameterDetailsData();

	void deleteMasterParameterDetailsById(String id);

}
