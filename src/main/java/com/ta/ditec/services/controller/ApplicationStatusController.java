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
import com.ta.ditec.services.model.ApplicationStatus;
import com.ta.ditec.services.request.ApplicationStatusRequest;
import com.ta.ditec.services.request.SubAUAApplicationStatusRequest;
import com.ta.ditec.services.response.SubAUAApplicationStatusResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.ApplicationStatusService;
import com.ta.ditec.services.transfrom.ApplicationStatusTransfrom;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "Application Status ", tags = "Application Status", description = "Application Status ")
public class ApplicationStatusController {
	private static final Logger logger = LoggerFactory.getLogger(ApplicationStatusController.class);

	@Autowired
	private ApplicationStatusService applicationStatusService;

	@PostMapping("/applicationstatus")
	public ResponseEntity<TaResponse<Object>> getApplicationStatus(@RequestBody @Valid ApplicationStatusRequest req) {
		
		
		logger.debug(req.toString());
		System.out.println("connnnreq" + req);
		ApplicationStatusTransfrom trans = new ApplicationStatusTransfrom();
		ApplicationStatus status = trans.getApplicationStatus(req);
		applicationStatusService.getApplicationStatus(status);
		return createResponse(null, "success", 1000, Type.INFORMATION);

	}

	@PostMapping("/subauastatustracker")
	public ResponseEntity<TaResponse<SubAUAApplicationStatusResponse>> getApplicationStatus1(
			@RequestBody @Valid SubAUAApplicationStatusRequest request) {
		logger.debug(request.toString());

		System.out.println("connnnreq" + request);
		SubAUAApplicationStatusResponse result = applicationStatusService.getApplicationStatustracker(request);
		return createResponse(result, "success", 1000, Type.INFORMATION);

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
