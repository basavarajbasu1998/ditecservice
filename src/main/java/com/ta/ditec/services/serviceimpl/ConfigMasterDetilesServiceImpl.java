package com.ta.ditec.services.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.ConfigMasterDetiles;
import com.ta.ditec.services.repo.ConfigMasterDetilesRepo;
import com.ta.ditec.services.service.ConfigMasterDetilesService;

@Service
public class ConfigMasterDetilesServiceImpl implements ConfigMasterDetilesService {

	@Autowired
	private ConfigMasterDetilesRepo configMasterDetilesRepo;

	@Override
	public ConfigMasterDetiles getConfigMasterDetiles(ConfigMasterDetiles req) {
		ConfigMasterDetiles config = configMasterDetilesRepo.save(req);
		return config;
	}

}
