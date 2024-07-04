package com.ta.ditec.services.serviceimpl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.TAErrorCodes;
import com.ta.ditec.services.repo.TAErrorRepo;
import com.ta.ditec.services.service.TAErrorcodesService;

@Service
public class TAErrorcodesServiceImpl implements TAErrorcodesService {

	 private static final Logger logger = LoggerFactory.getLogger(TAErrorcodesServiceImpl.class);
	@Autowired
	private TAErrorRepo repo;

	public TAErrorCodes adderor(TAErrorCodes t) {
		if (t != null) {
			System.out.println(t.getErrordesc());
			TAErrorCodes save = repo.save(t);
			return save;
		} else {
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}
	}

	@Override
	public List<TAErrorCodes> getall() {
		List<TAErrorCodes> findAll = repo.findAll();
		logger.info("Data fetched successfully");
		return findAll;
	}

	@Override
	public TAErrorCodes getbycode(String errorcode) {
		if (errorcode != null) {
			TAErrorCodes findByErrorcode = repo.findByErrorcode(errorcode);
			logger.info("Data fetched successfully");
			return findByErrorcode;
		} else {
			logger.error("Exception occurred as there is a bad request");
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}
	}

}
