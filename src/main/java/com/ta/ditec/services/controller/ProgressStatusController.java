package com.ta.ditec.services.controller;

import java.util.List;

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
import com.ta.ditec.services.model.ProgressStatus;
import com.ta.ditec.services.request.ProgressStatusRequest;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.ProgressStatusService;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "Progress Status ", tags = "Progress Status", description = "Progress Status Detiles ")
public class ProgressStatusController {

	private static final Logger logger = LoggerFactory.getLogger(ProgressStatusController.class);

	@Autowired
	private ProgressStatusService progressStatusService;

	@PostMapping("/progresstatus")
	public ResponseEntity<TaResponse<List<ProgressStatus>>> getProgressStatus(
			@RequestBody @Valid ProgressStatusRequest req) {

		logger.debug(req.toString());
		List<ProgressStatus> list = progressStatusService.getProgressStatus(req);

		return createResponse(list, "success", 1000, Type.INFORMATION);

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
