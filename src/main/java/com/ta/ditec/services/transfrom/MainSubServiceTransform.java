package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.MainSubService;
import com.ta.ditec.services.request.MainSubServiceRequest;

public class MainSubServiceTransform {

	public MainSubService getIntegrationMasterDetillesTransform(MainSubServiceRequest req) {
		MainSubService master = new MainSubService();
		BeanUtils.copyProperties(req, master);
		master.setCreatedBy("superadmin");
		master.setCreatedDate(new Date());
		master.setLastModifiedBy("superadmin");
		master.setLastModifiedDate(new Date());
		return master;
	}
}
