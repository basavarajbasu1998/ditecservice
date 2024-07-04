package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.ModuleMasterDetails;
import com.ta.ditec.services.request.ModuleMasterDetailsRequest;

public class ModuleMasterDetailsTransfrom {

	public ModuleMasterDetails getModuleMasterDetailsTransfrom(ModuleMasterDetailsRequest req) {
		ModuleMasterDetails md = new ModuleMasterDetails();
		BeanUtils.copyProperties(req, md);
		md.setCreatedDate(new Date());
		md.setCreatedBy("superadmin");
		md.setLastModifiedBy("superadmin");
		md.setLastModifiedDate(new Date());
		return md;
	}
}
