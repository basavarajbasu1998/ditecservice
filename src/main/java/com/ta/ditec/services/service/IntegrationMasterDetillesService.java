package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.IntegrationMasterDetilles;
import com.ta.ditec.services.request.IntegrationMasterUpadateDetillesRequest;

public interface IntegrationMasterDetillesService {

	public IntegrationMasterDetilles getIntegrationMasterDetilles(IntegrationMasterDetilles req);

	public IntegrationMasterDetilles getIntegrationMasterUpadateDetillesRequest(
			IntegrationMasterUpadateDetillesRequest req);

	public List<IntegrationMasterDetilles> getIntegrationMasterDetillesAllData();

	void deleteIntegrationMasterDetillesById(String id);
}
