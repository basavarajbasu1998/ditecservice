package com.ta.ditec.services.serviceimpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.MainServices;
import com.ta.ditec.services.repo.MainServicesRepo;
import com.ta.ditec.services.service.MainServicesService;

@Service
public class MainServicesServiceImpl implements MainServicesService {

	 private static final Logger logger = LoggerFactory.getLogger(MainServicesServiceImpl.class);
	@Autowired
	private MainServicesRepo mainServicesRepo;

	@Override
	public MainServices getMainServices(MainServices req) {
		
		MainServices lists = mainServicesRepo.save(req);
		logger.info("Data saved successfully ");
		return lists;
	}

}
