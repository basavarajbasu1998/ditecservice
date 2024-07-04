package com.ta.ditec.services.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Null;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.PaymentMode;
import com.ta.ditec.services.request.PaymentModeNavigateRequest;
import com.ta.ditec.services.request.PaymentModeRequest;
import com.ta.ditec.services.response.PaymentModeResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.PaymentModeService;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "PaymentMode ", tags = "Payment Mode", description = "Payment Mode Service Detiles ")
public class PaymentModeController {

	private static final Logger logger = LoggerFactory.getLogger(PaymentModeController.class);

	@Autowired
	private PaymentModeService paymentModeService;

	@PostMapping("/paymentnavigate")
	public ResponseEntity<TaResponse<PaymentModeResponse>> get(@RequestBody @Valid PaymentModeNavigateRequest req) {

		logger.debug(req.toString());

		List<PaymentMode> paymentlist = paymentModeService.getpayModes(req);
		PaymentModeResponse resp = new PaymentModeResponse();
		resp.setNavigateStatus(paymentlist.get(0).getNavigateStatus());
		return createResponse(resp, "success", 1000, Type.INFORMATION);

	}

	@PostMapping("/paymentmode")
	public ResponseEntity<TaResponse<Null>> getPaymentMode(@RequestBody @Valid PaymentModeRequest req) {
		logger.debug(req.toString());

		List<PaymentMode> paymentlist = paymentModeService.getpayModes(req);
		return createResponse(null, "success", 1000, Type.INFORMATION);

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
