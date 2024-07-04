package com.ta.ditec.services.serviceimpl;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.ApplicationStatus;
import com.ta.ditec.services.repo.ApplicationStatusRepo;
import com.ta.ditec.services.request.SubAUAApplicationStatusRequest;
import com.ta.ditec.services.response.SubAUAApplicationStatusResponse;
import com.ta.ditec.services.service.ApplicationStatusService;

@Service
public class ApplicationStatusServiceImpl implements ApplicationStatusService {
	
	 private static final Logger logger = LoggerFactory.getLogger(ApplicationStatusServiceImpl.class);

	@Autowired
	private ApplicationStatusRepo applicationStatusRepo;

	@Override
	public ApplicationStatus getApplicationStatus(ApplicationStatus req) {
		System.out.println("reqqqqqqqqqqqqqqqqq"+req);
		ApplicationStatus status = applicationStatusRepo.save(req);
		return status;
	}

	@Override
	public SubAUAApplicationStatusResponse getApplicationStatustracker(SubAUAApplicationStatusRequest request) {
		SubAUAApplicationStatusResponse res=new SubAUAApplicationStatusResponse();
		List<ApplicationStatus> list=applicationStatusRepo.findAllByUserId(request.getSubauaid());
		
		logger.info("Data fetched from data base "+list.toString());
		if(CollectionUtils.isNotEmpty(list))
		{
			res.setStatusCount(""+list.size());
			res.setPresentStep(list.get(list.size()-1).getApplicationStatus());
			res.setNextStep(list.get(list.size()-1).getNextStatus());
			res.setSubauaId(request.getSubauaid());
		}
		
		logger.debug(res.toString());
		return res;
	}

}
