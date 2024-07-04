package com.ta.ditec.services.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.PaymentPlanDetails;
import com.ta.ditec.services.request.PaymentPlanDetailsRequest;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.PaymentPlanDetailsService;

@RestController
@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PaymentPlanDetailsController {

	@Autowired
	private PaymentPlanDetailsService paymentPlanDetailsService;

	@PostMapping("/paymentdetiles")
	public ResponseEntity<TaResponse<PaymentPlanDetails>> getpaymentPlanDetails(
			@RequestBody @Valid PaymentPlanDetails req) {
		PaymentPlanDetails pays = paymentPlanDetailsService.getPaymentPlanDetails(req);
		return createResponse(pays, "success", 1000, Type.INFORMATION);

	}

	@PostMapping("/paymnetplasview")
	public ResponseEntity<TaResponse<List<PaymentPlanDetails>>> getpaymentPlanDetailsList(
			@RequestBody @Valid PaymentPlanDetailsRequest req) {
		List<PaymentPlanDetails> pays = paymentPlanDetailsService.getPaymentPlanDetailsReq(req);
		return createResponse(pays, "success", 1000, Type.INFORMATION);
	}
	
	

	<T> ResponseEntity<TaResponse<T>> createResponse(T data, String message, int code, Type type) {
		TaResponse<T> response = new TaResponse<>();
		response.setResponseData(data);
		response.setMessage(message);
		response.setCode(code);
		response.setType(type);
		return ResponseEntity.ok(response);
	}
}
