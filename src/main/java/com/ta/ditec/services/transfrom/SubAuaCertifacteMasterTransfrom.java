package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.SubAuaCertifacteMaster;
import com.ta.ditec.services.request.SubAuaCertifacteMasterRequest;

public class SubAuaCertifacteMasterTransfrom {

	public SubAuaCertifacteMaster getSubAuaCertifacteMaster(SubAuaCertifacteMasterRequest req) {
		SubAuaCertifacteMaster master = new SubAuaCertifacteMaster();
		BeanUtils.copyProperties(req, master);
		master.setCreatedBy("superadmin");
		master.setCreatedDate(new Date());
		master.setLastModifiedBy("superadmin");
		master.setLastModifiedDate(new Date());
		return master;

	}
}
