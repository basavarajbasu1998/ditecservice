package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.UserBillingMasterDetails;
import com.ta.ditec.services.request.UserBillingMasterDetailsRequest;

public class UserBillingMasterDetailsTransfrom {

	public UserBillingMasterDetails getUserBillingMasterDetailsTransfrom(UserBillingMasterDetailsRequest req) {
		UserBillingMasterDetails ubdetl = new UserBillingMasterDetails();
		BeanUtils.copyProperties(req, ubdetl);
		ubdetl.setCreatedDate(new Date());
		ubdetl.setCreatedBy("superadmin");
		ubdetl.setLastModifiedBy("superadmin");
		ubdetl.setLastModifiedDate(new Date());
		return ubdetl;
	}
}
