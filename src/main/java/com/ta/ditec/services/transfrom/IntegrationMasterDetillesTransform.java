package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.IntegrationMasterDetilles;
import com.ta.ditec.services.request.IntegrationMasterDetillesRequest;

public class IntegrationMasterDetillesTransform {

	public IntegrationMasterDetilles getIntegrationMasterDetillesTransform(IntegrationMasterDetillesRequest req) {
		IntegrationMasterDetilles master = new IntegrationMasterDetilles();
		BeanUtils.copyProperties(req, master);
		master.setCreatedBy("superadmin");
		master.setCreatedDate(new Date());
		master.setLastModifiedBy("superadmin");
		master.setLastModifiedDate(new Date());
		return master;

	}

}
