package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.ServiceMasterCharges;
import com.ta.ditec.services.request.ServiceMasterChargesRequest;

public class ServiceMasterChargesTransfrom {

	public ServiceMasterCharges getServiceMasterCharges(ServiceMasterChargesRequest req) {

		ServiceMasterCharges charge = new ServiceMasterCharges();
		BeanUtils.copyProperties(req, charge);
		charge.setCreatedBy("superadmin");
		charge.setCreatedDate(new Date());
		charge.setLastModifiedBy("superadmin");
		charge.setLastModifiedDate(new Date());
		return charge;
	}
}
