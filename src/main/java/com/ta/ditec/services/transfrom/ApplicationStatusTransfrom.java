package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.ApplicationStatus;
import com.ta.ditec.services.request.ApplicationStatusRequest;

public class ApplicationStatusTransfrom {

	public ApplicationStatus getApplicationStatus(ApplicationStatusRequest req) {
		ApplicationStatus status = new ApplicationStatus();
		BeanUtils.copyProperties(req, status);
		status.setCreatedBy("superadmin");
		status.setCreatedDate(new Date());
		status.setLastModifiedBy("superadmin");
		status.setLastModifiedDate(new Date());
		return status;
	}
	

}
