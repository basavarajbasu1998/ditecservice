package com.ta.ditec.services.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.MasterDataDetails;
import com.ta.ditec.services.request.MasterDataDetailsUpdateRequest;
import com.ta.ditec.services.request.PaginationRequest;

@Service
public interface MasterDataDetailService {

	MasterDataDetails masterdata(MasterDataDetails masterdata);

	List<MasterDataDetails> fetchMasterDataDetailList(PaginationRequest request);

	MasterDataDetails updateMasterDataDetail(MasterDataDetailsUpdateRequest agdetil);

	void deleteMasterDataDetailById(String id);

}
