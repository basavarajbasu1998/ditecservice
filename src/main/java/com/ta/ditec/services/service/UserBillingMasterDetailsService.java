package com.ta.ditec.services.service;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.UserBillingMasterDetails;
import com.ta.ditec.services.request.PaginationRequest;
import com.ta.ditec.services.request.UserBillingMasterDetailsFindIdRequest;
import com.ta.ditec.services.request.UserMasterBillingCheckDatarequest;

@Service
public interface UserBillingMasterDetailsService {

	UserBillingMasterDetails userbillmastdetil(UserBillingMasterDetails userbillmastdetil);

	Page<UserBillingMasterDetails> fetchRegisterAgencyDetailsList(PaginationRequest request);

	UserBillingMasterDetails fetchFindAng(UserBillingMasterDetailsFindIdRequest req);

	UserBillingMasterDetails updateUserBillingMasterDetails(UserBillingMasterDetails agdetil);

	void deleteUserBillingMasterDetailsById(String id);

	UserBillingMasterDetails checkUserBillingData(UserMasterBillingCheckDatarequest request);

	

}
