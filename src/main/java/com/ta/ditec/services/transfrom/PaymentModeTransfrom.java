package com.ta.ditec.services.transfrom;

import java.util.Date;

import org.springframework.beans.BeanUtils;

import com.ta.ditec.services.model.PaymentMode;
import com.ta.ditec.services.request.PaymentModeRequest;

public class PaymentModeTransfrom {

	public PaymentMode getPaymentModetransform(PaymentModeRequest req) {
		PaymentMode mode = new PaymentMode();
		mode.setLastModifiedBy("superadmin");
		mode.setCreatedBy("superadmin");
		mode.setCreatedDate(new Date());
		mode.setLastModifiedDate(new Date());
		BeanUtils.copyProperties(req, mode);
		return mode;
	}
}
