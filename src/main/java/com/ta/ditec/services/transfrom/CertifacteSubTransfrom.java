package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.CertifacteSub;
import com.ta.ditec.services.request.CertifacteSubRequest;

public class CertifacteSubTransfrom {

	public CertifacteSub getCertifacteSub(CertifacteSubRequest req) {
		CertifacteSub cersub = new CertifacteSub();
		cersub.setCreatedBy("superadmin");
		cersub.setCreatedDate(new Date());
		cersub.setLastModifiedBy("superadmin");
		cersub.setLastModifiedDate(new Date());
		BeanUtils.copyProperties(req, cersub);
		return cersub;

	}
}
