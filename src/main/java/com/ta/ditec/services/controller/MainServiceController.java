package com.ta.ditec.services.controller;

import javax.validation.Valid;

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
import com.ta.ditec.services.model.MainServices;
import com.ta.ditec.services.request.MainServicesRequest;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.MainServicesService;
import com.ta.ditec.services.transfrom.MainServicesTransform;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "Main Service Detilles ", tags = "Main Service Detilles ", description = "Main Service Detilles  ")
public class MainServiceController {
	private static final Logger logger = LoggerFactory.getLogger(MainServiceController.class);

	@Autowired
	private MainServicesService mainServicesService;

	@PostMapping("/mainservice")
	public ResponseEntity<TaResponse<Object>> getMainService(@RequestBody @Valid MainServicesRequest req) {
		logger.debug(req.toString());

		MainServicesTransform trans = new MainServicesTransform();
		MainServices mainservice = trans.getIntegrationMasterDetillesTransform(req);
		mainServicesService.getMainServices(mainservice);
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
