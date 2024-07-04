package com.ta.ditec.services.serviceimpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.PaymentPlanDetails;
import com.ta.ditec.services.repo.PaymentPlanDetailsRepo;
import com.ta.ditec.services.request.PaymentPlanDetailsRequest;
import com.ta.ditec.services.service.PaymentPlanDetailsService;

@Service
public class PaymentPlanDetailsServiceImpl implements PaymentPlanDetailsService {

	@Autowired
	private PaymentPlanDetailsRepo paymentPlanDetailsRepo;

	@Override
	public PaymentPlanDetails getPaymentPlanDetails(PaymentPlanDetails req) {
		PaymentPlanDetails pay = paymentPlanDetailsRepo.save(req);
		return pay;
	}

	@Override
	public List<PaymentPlanDetails> getPaymentPlanDetailsReq(PaymentPlanDetailsRequest req) {
		List<PaymentPlanDetails> listrepo = paymentPlanDetailsRepo.findByPlanType(req.getPlanType());
		return listrepo;
	}

}
