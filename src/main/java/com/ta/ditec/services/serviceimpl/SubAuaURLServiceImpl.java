package com.ta.ditec.services.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.SubAuaURL;
import com.ta.ditec.services.repo.SubAuaURLRepo;
import com.ta.ditec.services.service.SubAuaURLService;

@Service
public class SubAuaURLServiceImpl implements SubAuaURLService {
	
	 private static final Logger logger = LoggerFactory.getLogger(SubAuaURLServiceImpl.class);

	@Autowired
	private SubAuaURLRepo subAuaURLRepo;

	@Override
	public SubAuaURL getSubAuaURL(SubAuaURL req) {
		SubAuaURL urls = subAuaURLRepo.save(req);
		logger.info("Data saved successfully");
		return urls;
	}

}
