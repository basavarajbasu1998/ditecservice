package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.ConfigMasterDetiles;
import com.ta.ditec.services.request.ConfigMasterDetilesRequest;

public class ConfigMasterDetilesTransfrom {

	public ConfigMasterDetiles getConfigMasterDetilesTransform(ConfigMasterDetilesRequest req) {
		ConfigMasterDetiles det = new ConfigMasterDetiles();
		det.setCreatedBy("super admin");
		det.setCreatedDate(new Date());
		BeanUtils.copyProperties(req, det);
		return det;
	}
}
