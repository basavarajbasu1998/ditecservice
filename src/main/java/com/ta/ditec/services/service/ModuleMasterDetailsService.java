package com.ta.ditec.services.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.ModuleMasterDetails;
import com.ta.ditec.services.request.ModuleMasterDetailsUpdateRequest;
import com.ta.ditec.services.request.PaginationRequest;

@Service
public interface ModuleMasterDetailsService {

	ModuleMasterDetails saveDetails(ModuleMasterDetails request);

	List<ModuleMasterDetails> fetchMasterDataTypeDetailList(PaginationRequest request);

	ModuleMasterDetails updateModuleMasterDetails(ModuleMasterDetailsUpdateRequest mmdetl);

	void deleteUser(String id);

}
