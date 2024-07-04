package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.SubAuaCertifacte;
import com.ta.ditec.services.request.SubAuaCertifacteRequest;

public class SubAuaCertifacteTransform {

	public SubAuaCertifacte getUserMasterDetailsTransfrom(SubAuaCertifacteRequest req) {
		SubAuaCertifacte umd = new SubAuaCertifacte();
		BeanUtils.copyProperties(req, umd);
		umd.setCreatedDate(new Date());
		umd.setCreatedBy("superadmin");
		umd.setLastModifiedBy("superadmin");
		umd.setLastModifiedDate(new Date());
		return umd;
	}
}
