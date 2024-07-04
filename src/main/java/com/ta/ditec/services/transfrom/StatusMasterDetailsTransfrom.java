package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.StatusMasterDetails;
import com.ta.ditec.services.request.StatusMasterDetailsRequset;

public class StatusMasterDetailsTransfrom {

	public StatusMasterDetails getStatusMasterDetailsTransfrom(StatusMasterDetailsRequset req) {
		StatusMasterDetails smd = new StatusMasterDetails();
		BeanUtils.copyProperties(req, smd);
		smd.setCreatedDate(new Date());
		smd.setCreatedBy("superadmin");
		smd.setLastModifiedBy("superadmin");
		smd.setLastModifiedDate(new Date());
		return smd;
	}
}
