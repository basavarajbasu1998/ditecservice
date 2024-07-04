package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.MainServices;
import com.ta.ditec.services.request.MainServicesRequest;

public class MainServicesTransform {

	public MainServices getIntegrationMasterDetillesTransform(MainServicesRequest req) {
		MainServices master = new MainServices();
		BeanUtils.copyProperties(req, master);
		master.setCreatedBy("superadmin");
		master.setCreatedDate(new Date());
		master.setLastModifiedBy("superadmin");
		master.setLastModifiedDate(new Date());
		return master;
	}
}
