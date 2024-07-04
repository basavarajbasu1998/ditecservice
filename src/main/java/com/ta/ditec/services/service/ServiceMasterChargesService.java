package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.ServiceMasterCharges;
import com.ta.ditec.services.request.ServiceMasterChargesUpdateRequest;

public interface ServiceMasterChargesService {

	public ServiceMasterCharges getservicecharge(ServiceMasterCharges charge);

	public ServiceMasterCharges getServiceMasterChargesUpdateRequest(ServiceMasterChargesUpdateRequest update);

	public List<ServiceMasterCharges> getServiceMasterChargesAllData();

	void deleteServiceMasterChargesById(String id);

}
