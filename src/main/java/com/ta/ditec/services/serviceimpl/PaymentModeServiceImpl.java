package com.ta.ditec.services.serviceimpl;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.PaymentMode;
import com.ta.ditec.services.repo.PaymentModeRepo;
import com.ta.ditec.services.request.PaymentModeNavigateRequest;
import com.ta.ditec.services.request.PaymentModeRequest;
import com.ta.ditec.services.service.PaymentModeService;

@Service
public class PaymentModeServiceImpl implements PaymentModeService {
	
	 private static final Logger logger = LoggerFactory.getLogger(PaymentModeServiceImpl.class);

	@Autowired
	private PaymentModeRepo paymentModeRepo;

	@Override
	public PaymentMode getPaymentMode(PaymentMode req) {
		PaymentMode payment = paymentModeRepo.save(req);
		logger.info("Data saved successfully");
		payment.setNavigateStatus(false);
		paymentModeRepo.save(req);
		return payment;
	}

	@Override
	public List<PaymentMode> getpayModes(PaymentModeNavigateRequest req) {
		List<PaymentMode> paylist = paymentModeRepo.findBySubAuaId(req.getSubAuaId());
		logger.info("Data fetched successfully");
		if (CollectionUtils.isNotEmpty(paylist)) {
//			paylist.get(0).setNavigateStatus(true);
//			paymentModeRepo.save(paylist.get(0));
			return paylist;
		} else {
			logger.error("Exception occurred as there is a bad request");
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}
	}

	@Override
	public List<PaymentMode> getpayModes(PaymentModeRequest req) {
		
		try {
		if (req != null) {
			List<PaymentMode> paylist = paymentModeRepo.findBySubAuaId(req.getSubAuaId());
			logger.info("Data fetched successfully");
			if (CollectionUtils.isNotEmpty(paylist)) {
				PaymentMode sts = paylist.get(0);
				sts.setAccountPersonName(req.getAccountPersonName());
				sts.setAcNo(req.getAcNo());
				sts.setAmount(req.getAmount());
				sts.setBankName(req.getBankName());
				sts.setChequeDate(req.getChequeDate());
				sts.setPaymentMode(req.getPaymentMode());
				sts.setIfscNo(req.getIfscNo());
				sts.setChequeNumber(req.getChequeNumber());
				sts.setNavigateStatus(true);
				sts.setCreatedBy("super admin");
				sts.setCreatedDate(new Date());
				sts.setLastModifiedBy("super admin");
				sts.setLastModifiedDate(new Date());
				paymentModeRepo.save(sts);
				return paylist;
			} else {
				logger.error("Data not found in the database");
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
			}
		} else {
			logger.error("Exception occurred as there is a bad request");
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}
		}catch (TaException e) {
			throw e;
		}catch (Exception e) {
			logger.error("Exception occurred as an internal server error");
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}
		
		
	}
		

}
