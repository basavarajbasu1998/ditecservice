package com.ta.ditec.services.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.MainSubService;
import com.ta.ditec.services.repo.MainSubServiceRepo;
import com.ta.ditec.services.service.MainSubServiceService;

@Service
public class MainSubServiceServiceImpl implements MainSubServiceService {
	 private static final Logger logger = LoggerFactory.getLogger(MainSubServiceServiceImpl.class);

	@Autowired
	private MainSubServiceRepo mainSubServiceRepo;

	@Override
	public MainSubService getMainSubServiceRequest(MainSubService req) {
		MainSubService service = mainSubServiceRepo.save(req);
		logger.info("Data saved successfully");
		return service;
	}

}
