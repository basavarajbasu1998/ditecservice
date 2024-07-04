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
import com.ta.ditec.services.model.IntegrationMasterDetilles;
import com.ta.ditec.services.repo.IntegrationMasterDetillesRepo;
import com.ta.ditec.services.request.IntegrationMasterUpadateDetillesRequest;
import com.ta.ditec.services.service.IntegrationMasterDetillesService;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class IntegrationMasterDetillesServiceImpl implements IntegrationMasterDetillesService {
	 private static final Logger logger = LoggerFactory.getLogger(IntegrationMasterDetillesServiceImpl.class);

	@Autowired
	private IntegrationMasterDetillesRepo integrationMasterDetillesRepo;

	@Override
	public IntegrationMasterDetilles getIntegrationMasterDetilles(IntegrationMasterDetilles req) {

		System.out.println("serrrrrrrrrrrrrrrrr" + req);

		IntegrationMasterDetilles intmaster = integrationMasterDetillesRepo.save(req);
		logger.info("Data saved succesfully to database");
		intmaster.setIntegrationId("IMD" + TAUtils.getId(intmaster.getId()));
		integrationMasterDetillesRepo.save(intmaster);
		logger.info("Data saved successfully ");
		return intmaster;
	}

	@Override
	public List<IntegrationMasterDetilles> getIntegrationMasterDetillesAllData() {
		try {
			List<IntegrationMasterDetilles> listImd = integrationMasterDetillesRepo.findAll();
			logger.info("Data fetched successfully from database");
			if (CollectionUtils.isNotEmpty(listImd)) {
				logger.debug(listImd.toString());
				return listImd;
			} else {
				logger.error("Exception occurred in the request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (TaException e) {
			
			logger.error("TA Exception occurred");
			throw e;
		} catch (Exception e) {
			logger.error("Exception occurred from the server side");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

	@Override
	public void deleteIntegrationMasterDetillesById(String id) {

		try {
			if (id != null) {
				List<IntegrationMasterDetilles> d = integrationMasterDetillesRepo.findByIntegrationId(id);
				logger.info("Data fetched successfully");
				if (!CollectionUtils.isEmpty(d)) {

					integrationMasterDetillesRepo.delete(d.get(0));
				} else {
					logger.error("Database doesnt have the data");
					throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			}
		} catch (TaException e) {
			logger.error("Some exception occurred");
			throw e;
		} catch (Exception e) {
			logger.error("Exception occurred from the server side");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

	@Override
	public IntegrationMasterDetilles getIntegrationMasterUpadateDetillesRequest(
			IntegrationMasterUpadateDetillesRequest req) {

		try {
			if (req != null && req.getIntegrationId() != null) {

				List<IntegrationMasterDetilles> depdetailsDB = integrationMasterDetillesRepo
						.findByIntegrationId(req.getIntegrationId());

				IntegrationMasterDetilles depDB = null;

				if (Objects.nonNull(depdetailsDB) && CollectionUtils.isNotEmpty(depdetailsDB)) {
					depDB = depdetailsDB.get(0);
				}

				if (Objects.nonNull(req.getIntegrationCharges())) {
					depDB.setIntegrationCharges(req.getIntegrationCharges());
				}

				if (Objects.nonNull(req.getIntegrationType()) && StringUtils.isNotEmpty(req.getIntegrationType())) {
					depDB.setIntegrationType(req.getIntegrationType());
				}

				logger.info("Data saved successfully");
				return integrationMasterDetillesRepo.save(depDB);
				
				
			} else {
				logger.error("Exception from the request");
				throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} catch (TaException e) {
			logger.error("Exception occurred as a TA exception");
			throw e;

		} catch (Exception e) {
			logger.error("Exception occurred from the server side ");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

}
