package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.OtpMasterDetiles;
import com.ta.ditec.services.request.OtpMasterDetilesRequest;

public class OtpMasterDetilesTransform {

	public OtpMasterDetiles getOtpMasterDetilesRequest(OtpMasterDetilesRequest req) {
		OtpMasterDetiles om = new OtpMasterDetiles();
		BeanUtils.copyProperties(req, om);
		om.setCreatedDate(new Date());
		om.setCreatedBy("superadmin");
		om.setLastModifiedBy("superadmin");
		om.setLastModifiedDate(new Date());
		return om;
	}
}
