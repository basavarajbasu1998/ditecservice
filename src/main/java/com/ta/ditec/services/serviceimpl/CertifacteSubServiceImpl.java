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
import com.ta.ditec.services.repo.CertifacteSubRepo;
import com.ta.ditec.services.request.CertifacteSubUpdaterequest;
import com.ta.ditec.services.response.CertifacteSubAllDataResponse;
import com.ta.ditec.services.service.CertifacteSubService;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class CertifacteSubServiceImpl implements CertifacteSubService {

	 private static final Logger logger = LoggerFactory.getLogger(CertifacteSubServiceImpl.class);
	@Autowired
	private CertifacteSubRepo certifacteSubRepo;

	@Override
	public CertifacteSub getCertifacteSub(CertifacteSub req) {

		if (req != null) {

			if (certifacteSubRepo.existsByCertificateTitle(req.getCertificateTitle())) {
				throw new TaException(ErrorCode.ALREADY_TITLE_EXIST, ErrorCode.ALREADY_TITLE_EXIST.getErrorMsg());
			}

			CertifacteSub repo = certifacteSubRepo.save(req);
			logger.info("Data saved successfully");
			return repo;
		} else {
			logger.error("Found some exception in request");
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			
		}
	}

	@Override
	public List<CertifacteSubAllDataResponse> getAllData() {
		try {
			List<CertifacteSub> list = certifacteSubRepo.findAll();
			logger.info("Data fetched successfully");
			if (CollectionUtils.isNotEmpty(list)) {
				
				return transfromresponse(list);
			} else {
				logger.error("Exception from the request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (TaException e) {
			logger.error("Some exception occurred");
			throw e;
		} catch (Exception e) {
			logger.error("Exception occured from server side");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

	private List<CertifacteSubAllDataResponse> transfromresponse(List<CertifacteSub> list) {
		List<CertifacteSubAllDataResponse> res = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {
			CertifacteSubAllDataResponse subres = new CertifacteSubAllDataResponse();
			CertifacteSub entity = list.get(i);
			BeanUtils.copyProperties(entity, subres);

			subres.setId(entity.getId());
			subres.setCreatedBy(entity.getCreatedBy());
			subres.setCreatedDate(TAUtils.dateformat("dd/MM/yyyy hh:mm a", entity.getCreatedDate()));
			subres.setLastModifiedBy(entity.getLastModifiedBy());
			subres.setLastModifiedDate(TAUtils.dateformat("dd/MM/yyyy hh:mm a", entity.getLastModifiedDate()));
			subres.setSNo(i + 1);
		
			res.add(subres);
			logger.debug(res.toString());
		}
		System.out.println("certifactesuballdata certifactesuballdata " + res);
		return res;
	}

	@Override
	public void deleteUser(Long id) {
		try {
			Optional<CertifacteSub> list = certifacteSubRepo.findById(id);

			if (list.isPresent()) {
				System.out.println("certifactesuball" + list);
				certifacteSubRepo.delete(list.get());
			} else {
				logger.error("Exception from the request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (Exception e) {
			
			logger.error("Exception occured from server side");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

	@Override
	public CertifacteSub getCertifacteSub(CertifacteSubUpdaterequest req) {

		try {

			if (req != null && req.getId() != null) {

				Optional<CertifacteSub> list = certifacteSubRepo.findById(req.getId());
				logger.info("Data fetched successfully");
				CertifacteSub master = null;
				if (Objects.nonNull(list)) {
					master = list.get();
				}
				if (Objects.nonNull(req.getCertificateTitle()) && StringUtils.isNotEmpty(req.getCertificateTitle())) {
					master.setCertificateTitle(req.getCertificateTitle());
				}

				return certifacteSubRepo.save(master);
			} else {
				
				logger.error("Exception in the request ");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (Exception e) {
			logger.error("Exception occurred from server");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

}
