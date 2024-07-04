package com.ta.ditec.services.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.ServiceCharges;
import com.ta.ditec.services.model.UIDAIErrorcodes;
import com.ta.ditec.services.repo.ServiceChargesRepo;
import com.ta.ditec.services.repo.UIDAIerrorRepo;
import com.ta.ditec.services.service.UIDAIErrorservice;

@Service
public class UIDAIErrorImpl implements UIDAIErrorservice {

	private static final Logger logger = LoggerFactory.getLogger(UIDAIErrorImpl.class);

	@Autowired
	private UIDAIerrorRepo repo;

	@Autowired
	private ServiceChargesRepo serviceChargesRepo;

	public UIDAIErrorcodes add(UIDAIErrorcodes u) {
		UIDAIErrorcodes save = repo.save(u);
		return save;
	}

	@Override
	public List<UIDAIErrorcodes> getall() {
		List<UIDAIErrorcodes> findAll = repo.findAll();
		logger.info("Data fetched successfully");
		return findAll;
	}

	public UIDAIErrorcodes getbycode(String s) {
		UIDAIErrorcodes findByErrorcode = repo.findByErrorcode(s);
		logger.info("Data fetched successfully");
		System.out.println(findByErrorcode);
		return findByErrorcode;

	}

}
