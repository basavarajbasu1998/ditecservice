package com.ta.ditec.services.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.StatusMasterDetails;
import com.ta.ditec.services.request.PaginationRequest;
import com.ta.ditec.services.request.StatusMasterDetailsUpdateRequest;
@Service
public interface StatusMasterDetailsService {

	StatusMasterDetails saveDetails(StatusMasterDetails request);

	List<StatusMasterDetails> fetchStatusMasterDetails(PaginationRequest request);

	StatusMasterDetails UpdateStatusMasterDetails(StatusMasterDetailsUpdateRequest smdetil);

	void deleteUser(String id);
}
