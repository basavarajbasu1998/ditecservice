package com.ta.ditec.services.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.CertifacteSub;
import com.ta.ditec.services.model.SubAuaCertifacteMaster;
import com.ta.ditec.services.repo.CertifacteSubRepo;
import com.ta.ditec.services.repo.SubAuaCertifacteMasterRepo;
import com.ta.ditec.services.request.SubAuaCertifacteMasterUpadateRequest;
import com.ta.ditec.services.response.SubAuaCertifacteMasterAllDataResponse;
import com.ta.ditec.services.service.SubAuaCertifacteMasterService;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class SubAuaCertifacteMasterServiceImpl implements SubAuaCertifacteMasterService {
	
	 private static final Logger logger = LoggerFactory.getLogger(SubAuaCertifacteMasterServiceImpl.class);

	@Autowired
	private SubAuaCertifacteMasterRepo subAuaCertifacteMasterRepo;

	@Autowired
	private CertifacteSubRepo certifacteSubRepo;

	@Override
	public SubAuaCertifacteMaster getSubAuaCertifacteMaster(SubAuaCertifacteMaster req) {

		if (req != null) {
			if (subAuaCertifacteMasterRepo.existsByCertfacteMasterTitle(req.getCertfacteMasterTitle())) {
				throw new TaException(ErrorCode.ALREADY_TITLE_EXIST, ErrorCode.ALREADY_TITLE_EXIST.getErrorMsg());
			}
			SubAuaCertifacteMaster master = subAuaCertifacteMasterRepo.save(req);
			logger.info("Data saved successfully");
			System.out.println(master);
			return master;
		} else {
			logger.error("Exception occurred as there is a bad request");
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}

	}

	@Override
	public List<SubAuaCertifacteMasterAllDataResponse> getallData() {
		try {
			List<SubAuaCertifacteMaster> list = subAuaCertifacteMasterRepo.findAll();
			
			logger.info("Data fetched successfully");

			if (CollectionUtils.isNotEmpty(list)) {

				return transfromto(list);
			} else {
				logger.error("Exception occurred as there is a bad request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

	private List<SubAuaCertifacteMasterAllDataResponse> transfromto(List<SubAuaCertifacteMaster> list) {
		List<SubAuaCertifacteMasterAllDataResponse> res = new ArrayList<>();
		List<CertifacteSub> sublists = certifacteSubRepo.findAll();
		logger.info("Data fetched successfully");
		for (int i = 0; i < list.size(); i++) {
			SubAuaCertifacteMasterAllDataResponse resp = new SubAuaCertifacteMasterAllDataResponse();
			SubAuaCertifacteMaster master = list.get(i);
			BeanUtils.copyProperties(master, resp);
			resp.setSlNo(i + 1);
			resp.setId(master.getId());
			resp.setCertfacteMasterTitle(master.getCertfacteMasterTitle());
			resp.setCreatedBy(master.getCreatedBy());
			resp.setLastModifiedBy(master.getLastModifiedBy());
			resp.setCreatedDate(TAUtils.dateformat("dd/MM/yyyy hh:mm a", master.getCreatedDate()));
			resp.setLastModifiedDate(TAUtils.dateformat("dd/MM/yyyy hh:mm a", master.getLastModifiedDate()));
			resp.setList(sublists);
			res.add(resp);
		}

		return res;
	}

	@Override
	public void getData(Long id) {
		try {
			Optional<SubAuaCertifacteMaster> optionalSubAuaCertifacteMaster = subAuaCertifacteMasterRepo.findById(id);
			logger.info("Data fetched successfully");

			if (optionalSubAuaCertifacteMaster.isPresent()) {
				SubAuaCertifacteMaster subAuaCertifacteMaster = optionalSubAuaCertifacteMaster.get();
				subAuaCertifacteMasterRepo.delete(subAuaCertifacteMaster);
			} else {
				logger.error("Exception occurred as there is a bad request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

	@Override
	public SubAuaCertifacteMaster updateSubAuaCertifacteMaster(SubAuaCertifacteMasterUpadateRequest req) {
		System.out.println(req);

		try {

			if (req != null && req.getId() != null) {

				Optional<SubAuaCertifacteMaster> list = subAuaCertifacteMasterRepo.findById(req.getId());
				logger.info("Data fetched successfully");
				SubAuaCertifacteMaster master = null;

				if (Objects.nonNull(list)) {
					master = list.get();
				}

				if (Objects.nonNull(req.getCertfacteMasterTitle())
						&& StringUtils.isNotEmpty(req.getCertfacteMasterTitle())) {
					master.setCertfacteMasterTitle(req.getCertfacteMasterTitle());
				}
				logger.info("Data saved successfully");
				return subAuaCertifacteMasterRepo.save(master);
			} else {
				logger.error("Exception occurred as there is a bad request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (TaException e) {
			throw e;

		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

}
