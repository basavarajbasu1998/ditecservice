package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.UserMasterDetails;
import com.ta.ditec.services.request.UserMasterDetailsRequest;

public class UserMasterDetailsTransfrom {

	public UserMasterDetails getUserMasterDetailsTransfrom(UserMasterDetailsRequest req) {
		UserMasterDetails umd = new UserMasterDetails();
		BeanUtils.copyProperties(req, umd);
		umd.setCreatedDate(new Date());
		umd.setCreatedBy("superadmin");
		umd.setLastModifiedBy("superadmin");
		umd.setLastModifiedDate(new Date());
		return umd;
	}
}
