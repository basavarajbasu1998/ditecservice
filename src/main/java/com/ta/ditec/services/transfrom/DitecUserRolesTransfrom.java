package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.DitecUserRoles;
import com.ta.ditec.services.request.DitecUserRolesRequest;

public class DitecUserRolesTransfrom {

	public DitecUserRoles getApplicationStatus(DitecUserRolesRequest req) {
		DitecUserRoles status = new DitecUserRoles();
		BeanUtils.copyProperties(req, status);
		status.setCreatedBy(req.getUserName());
		status.setCreatedDate(new Date());
		status.setLastModifiedBy(req.getUserName());
		status.setLastModifiedDate(new Date());
		return status;
	}
}
