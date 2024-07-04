package com.ta.ditec.services.transfrom;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.UserBillingMasterDetails;
import com.ta.ditec.services.request.UserBillingMasterDetailsUpdateRequest;

public class UserBillingDetailsUpdateTransform {

	public UserBillingMasterDetails getUserBillingMasterDetailsUpdateTransfrom(UserBillingMasterDetailsUpdateRequest req) {
		UserBillingMasterDetails ubdetl = new UserBillingMasterDetails();
		BeanUtils.copyProperties(req, ubdetl);
		return ubdetl;
	}

}
