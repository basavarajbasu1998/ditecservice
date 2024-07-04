package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.CertifacteSub;
import com.ta.ditec.services.request.CertifacteSubUpdaterequest;
import com.ta.ditec.services.response.CertifacteSubAllDataResponse;

public interface CertifacteSubService {

	public CertifacteSub getCertifacteSub(CertifacteSub req);

	public List<CertifacteSubAllDataResponse> getAllData();

	void deleteUser(Long id);
	
	public CertifacteSub getCertifacteSub(CertifacteSubUpdaterequest req);
}
