package com.ta.ditec.services.service;

import java.util.List;

import com.ta.ditec.services.model.PaymentMode;
import com.ta.ditec.services.request.PaymentModeNavigateRequest;
import com.ta.ditec.services.request.PaymentModeRequest;

public interface PaymentModeService {

	public PaymentMode getPaymentMode(PaymentMode req);

	public List<PaymentMode> getpayModes(PaymentModeRequest req);

	public List<PaymentMode> getpayModes(PaymentModeNavigateRequest req);
}
