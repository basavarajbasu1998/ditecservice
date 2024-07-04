package com.ta.ditec.services.controller;

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
import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.request.OtpRequset;
import com.ta.ditec.services.request.OtpResendRequset;
import com.ta.ditec.services.response.LoginResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.OtpMasterDetilesService;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/auth")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "OTP ", tags = "OTP", description = "OTP Service ")
public class OtpMasterDetilesController {

	private static final Logger logger = LoggerFactory.getLogger(OtpMasterDetilesController.class);

	@Autowired
	private OtpMasterDetilesService otpMasterDetilesService;

	@PostMapping("/enterotp")
	public ResponseEntity<TaResponse<LoginResponse>> getOtpRequestMasterDetiles(@RequestBody @Valid OtpRequset req) {
		logger.debug(req.toString());

		LoginResponse resp = otpMasterDetilesService.getOtpMasterDetiles(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);

	}

	@PostMapping("/otp")
	public ResponseEntity<TaResponse<Null>> getOtpMasterDetiles(@RequestBody @Valid OtpRequset req) {
		logger.debug(req.toString());

		SubAuaUser user = otpMasterDetilesService.user(req);
		return createResponse(null, "success", 1000, Type.INFORMATION);

	}

	@PostMapping("/resendotp")
	public ResponseEntity<TaResponse<Object>> getResendOtpMasterDetiles(@RequestBody @Valid OtpResendRequset req) {
		logger.debug(req.toString());
		SubAuaUser user = otpMasterDetilesService.resenduser(req);
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
