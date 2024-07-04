package com.ta.ditec.services.serviceimpl;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.MasterParameterDetails;
import com.ta.ditec.services.repo.MasterParameterDetailsRepo;
import com.ta.ditec.services.request.MasterParameterDetailsUpadeteRequest;
import com.ta.ditec.services.service.MasterParameterDetailsService;

@Service
public class MasterParameterDetailsServiceImpl implements MasterParameterDetailsService {
	 private static final Logger logger = LoggerFactory.getLogger(MasterParameterDetailsServiceImpl.class);

	@Autowired
	private MasterParameterDetailsRepo masterParameterDetailsRepo;

	@Override
	public MasterParameterDetails getMasterParameterDetails(MasterParameterDetails master) {

		try {

			if (master != null) {
				MasterParameterDetails masterpara = masterParameterDetailsRepo.save(master);
//		masterpara.setParameterId("MPD" + TAUtils.getId(masterpara.getId()));
				masterParameterDetailsRepo.save(master);
				logger.info("Data saved successfully");
				return masterpara;
			} else {
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

	@Override
	public List<MasterParameterDetails> getallMasterParameterDetailsData() {
		List<MasterParameterDetails> listmaster = masterParameterDetailsRepo.findAll();

		try {
			if (CollectionUtils.isNotEmpty(listmaster)) {
				return listmaster;
			} else {
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

	@Override
	public void deleteMasterParameterDetailsById(String id) {

		try {
			if (id != null) {
				List<MasterParameterDetails> d = masterParameterDetailsRepo.findByParameterId(id);
				logger.info("Data fetched successfully");
				if (!CollectionUtils.isEmpty(d)) {
					masterParameterDetailsRepo.delete(d.get(0));
				} else {
					throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			} else {
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NOT_FOUND.getErrorMsg());
			}
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

	@Override
	public MasterParameterDetails getMasterParameterUpadteDetails(MasterParameterDetailsUpadeteRequest master) {

		try {
			if (master != null && master.getParameterId() != null) {

				List<MasterParameterDetails> depdetailsDB = masterParameterDetailsRepo
						.findByParameterId(master.getParameterId());
				logger.info("Data fetched successfully");

				MasterParameterDetails depDB = null;

				if (Objects.nonNull(depdetailsDB) && CollectionUtils.isNotEmpty(depdetailsDB)) {
					depDB = depdetailsDB.get(0);
				}

				if (Objects.nonNull(master.getParameterName()) && StringUtils.isNotEmpty(master.getParameterName())) {
					depDB.setParameterName(master.getParameterName());
				}

				if (Objects.nonNull(master.getParametervalue()) && StringUtils.isNotEmpty(master.getParametervalue())) {
					depDB.setParametervalue(master.getParametervalue());
				}

				return masterParameterDetailsRepo.save(depDB);
			} else {
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}

		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

}
