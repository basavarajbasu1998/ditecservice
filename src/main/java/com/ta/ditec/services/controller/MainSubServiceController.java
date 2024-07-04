package com.ta.ditec.services.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.MainSubService;
import com.ta.ditec.services.request.MainSubServiceRequest;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.MainSubServiceService;
import com.ta.ditec.services.transfrom.MainSubServiceTransform;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser/")
@Api(value = "Sub Service Detilles ", tags = "Sub Service Detilles ", description = "Sub Service Detilles  ")

public class MainSubServiceController {

	private static final Logger logger = LoggerFactory.getLogger(MainSubServiceController.class);

	@Autowired
	private MainSubServiceService mainSubServiceService;

	@PostMapping("/mainsubservice")
	public ResponseEntity<TaResponse<Object>> getMainSubService(@RequestBody @Valid MainSubServiceRequest req) {

		logger.debug(req.toString());

		MainSubServiceTransform trans = new MainSubServiceTransform();
		MainSubService subservice = trans.getIntegrationMasterDetillesTransform(req);
		mainSubServiceService.getMainSubServiceRequest(subservice);
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
