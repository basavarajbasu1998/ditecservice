package com.ta.ditec.services.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.MasterDataTypeDetails;
import com.ta.ditec.services.request.MasterDataTypeDetailsUpdateRequest;
import com.ta.ditec.services.request.PaginationRequest;

@Service
public interface MasterDataTypeDetailService {

	MasterDataTypeDetails mdtypedetl(MasterDataTypeDetails mdtypedetl);

	List<MasterDataTypeDetails> fetchMasterDataTypeDetailList(PaginationRequest request);

	MasterDataTypeDetails updateMasterDataTypeDetail(MasterDataTypeDetailsUpdateRequest request);

	void deleteMasterDataTypeDetailById(String id);

}
