package com.ta.ditec.services.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.ProgressStatus;
import com.ta.ditec.services.repo.ProgressStatusRepo;
import com.ta.ditec.services.request.ProgressStatusRequest;
import com.ta.ditec.services.service.ProgressStatusService;

@Service
public class ProgressStatusServiceImpl implements ProgressStatusService {
	
	 private static final Logger logger = LoggerFactory.getLogger(ProgressStatusServiceImpl.class);

	@Autowired
	private ProgressStatusRepo progressStatusRepo;

	@Override
	public List<ProgressStatus> getProgressStatus(ProgressStatusRequest req) {
		List<ProgressStatus> status = progressStatusRepo.findBySubAuaId(req.getSubAuaId());
		logger.info("Data fetched successfully");
		return status;
	}

}
