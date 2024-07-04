package com.ta.ditec.services.serviceimpl;

import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.ServiceMasterCharges;
import com.ta.ditec.services.repo.ServiceMasterChargesRepo;
import com.ta.ditec.services.request.ServiceMasterChargesUpdateRequest;
import com.ta.ditec.services.service.ServiceMasterChargesService;
import com.ta.ditec.services.utils.TAUtils;

@Service
public class ServiceMasterChargesServiceImpl implements ServiceMasterChargesService {
	
	 private static final Logger logger = LoggerFactory.getLogger(ServiceMasterChargesServiceImpl.class);

	@Autowired
	private ServiceMasterChargesRepo serviceMasterChargesRepo;

	@Override
	public ServiceMasterCharges getservicecharge(ServiceMasterCharges charge) {
		ServiceMasterCharges servicecharge = serviceMasterChargesRepo.save(charge);
		logger.info("Data saved successfully");
		servicecharge.setServiceMasterChargesId("SMC" + TAUtils.getId(servicecharge.getId()));
		serviceMasterChargesRepo.save(charge);
		return servicecharge;
	}

	@Override
	public List<ServiceMasterCharges> getServiceMasterChargesAllData() {
		List<ServiceMasterCharges> listdata = serviceMasterChargesRepo.findAll();

		return listdata;
	}

	@Override
	public ServiceMasterCharges getServiceMasterChargesUpdateRequest(ServiceMasterChargesUpdateRequest update) {

		try {
			if (update != null && update.getServiceMasterChargesId() != null) {

				List<ServiceMasterCharges> depdetailsDB = serviceMasterChargesRepo
						.findByServiceMasterChargesId(update.getServiceMasterChargesId());

				ServiceMasterCharges depDB = null;

				if (Objects.nonNull(depdetailsDB) && CollectionUtils.isNotEmpty(depdetailsDB)) {
					depDB = depdetailsDB.get(0);
				}

				if (Objects.nonNull(update.getTransactioncharges1())) {
					depDB.setTransactioncharges1(update.getTransactioncharges1());
				}

				if (Objects.nonNull(update.getTransactioncharges2())) {
					depDB.setTransactioncharges2(update.getTransactioncharges2());
				}
				if (Objects.nonNull(update.getTransactioncharges3())) {
					depDB.setTransactioncharges3(update.getTransactioncharges3());
				}
				if (Objects.nonNull(update.getTransactioncharges4())) {
					depDB.setTransactioncharges4(update.getTransactioncharges4());
				}

				if (Objects.nonNull(update.getTransactionEnd1())) {
					depDB.setTransactionEnd1(update.getTransactionEnd1());
				}
				if (Objects.nonNull(update.getTransactionEnd2())) {
					depDB.setTransactionEnd2(update.getTransactionEnd2());
				}
				if (Objects.nonNull(update.getTransactionEnd3())) {
					depDB.setTransactionEnd3(update.getTransactionEnd3());
				}
				if (Objects.nonNull(update.getTransactionEnd4())) {
					depDB.setTransactionEnd4(update.getTransactionEnd4());
				}

				if (Objects.nonNull(update.getTransactionStart1())) {
					depDB.setTransactionStart1(update.getTransactionStart1());
				}
				if (Objects.nonNull(update.getTransactionStart2())) {
					depDB.setTransactionStart2(update.getTransactionStart2());
				}
				if (Objects.nonNull(update.getTransactionStart3())) {
					depDB.setTransactionStart3(update.getTransactionStart3());
				}
				if (Objects.nonNull(update.getTransactionStart4())) {
					depDB.setTransactionStart4(update.getTransactionStart4());
				}

				return serviceMasterChargesRepo.save(depDB);
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
	public void deleteServiceMasterChargesById(String id) {

		try {
			if (id != null) {
				List<ServiceMasterCharges> d = serviceMasterChargesRepo.findByServiceMasterChargesId(id);
				if (!CollectionUtils.isEmpty(d)) {

					serviceMasterChargesRepo.delete(d.get(0));
				} else {
					logger.error("Data not found in the database");
					throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
				}
			} 
		} catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
	}

}
