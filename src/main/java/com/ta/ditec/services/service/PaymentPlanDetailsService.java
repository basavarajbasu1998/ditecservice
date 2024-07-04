package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.PaymentPlanDetails;
import com.ta.ditec.services.request.PaymentPlanDetailsRequest;

public interface PaymentPlanDetailsService {

	public PaymentPlanDetails getPaymentPlanDetails(PaymentPlanDetails req);

	public List<PaymentPlanDetails> getPaymentPlanDetailsReq(PaymentPlanDetailsRequest req);
}
