package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.request.SubAuaRequest;



public class SubAuaTransform {

	public SubAuaUser getUserMasterDetailsTransfrom(SubAuaRequest req) {
		SubAuaUser umd = new SubAuaUser();
		BeanUtils.copyProperties(req, umd);
		umd.setCreatedDate(new Date());
		umd.setCreatedBy("superadmin");
		umd.setLastModifiedBy("superadmin");
		umd.setLastModifiedDate(new Date());
		return umd;
	}
}
