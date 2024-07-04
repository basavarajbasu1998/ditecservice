package com.ta.ditec.services.controller;

import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.IntegrationMasterDetilles;
import com.ta.ditec.services.request.IntegrationMasterDetillesDeleteRequest;
import com.ta.ditec.services.request.IntegrationMasterDetillesRequest;
import com.ta.ditec.services.request.IntegrationMasterUpadateDetillesRequest;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.IntegrationMasterDetillesService;
import com.ta.ditec.services.transfrom.IntegrationMasterDetillesTransform;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser/")
@Api(value = "Integration MasterDetilles ", tags = "Integration MasterDetilles", description = "Integration MasterDetilles all service charges ")
public class IntegrationMasterDetillesController {

	private static final Logger logger = LoggerFactory.getLogger(IntegrationMasterDetillesController.class);

	@Autowired
	private IntegrationMasterDetillesService integrationMasterDetillesService;

	@PostMapping("/integrationMasterDetilles")
	public ResponseEntity<TaResponse<Object>> getIntegrationMasterDetilles(
			@RequestBody @Valid IntegrationMasterDetillesRequest req) {
		logger.debug(req.toString());

		IntegrationMasterDetillesTransform trans = new IntegrationMasterDetillesTransform();
		IntegrationMasterDetilles imd = trans.getIntegrationMasterDetillesTransform(req);
		integrationMasterDetillesService.getIntegrationMasterDetilles(imd);
		return createResponse(null, "success", 1000, Type.INFORMATION);

	}

	@GetMapping("/integrationDetailsalldata")
	public ResponseEntity<TaResponse<List<IntegrationMasterDetilles>>> getMasterParameterDetails() {

		List<IntegrationMasterDetilles> listmpd = integrationMasterDetillesService
				.getIntegrationMasterDetillesAllData();

		logger.debug(listmpd.toString());
		return createResponse(listmpd, "success", 1000, Type.INFORMATION);

	}

	@PostMapping("/integrationMasterDetillesdeletead")
	public ResponseEntity<TaResponse<Object>> deleteMasterParameterDetailsDelete(
			@Valid @RequestBody IntegrationMasterDetillesDeleteRequest req) {

		logger.debug(req.toString());

		integrationMasterDetillesService.deleteIntegrationMasterDetillesById(req.getIntegrationId());
		return createResponse(null, "success", 1000, Type.INFORMATION);

	}

	@PostMapping(value = { "/integrationMasterDetillesupdate" }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TaResponse<IntegrationMasterDetilles>> updateAgencyDetails(
			@Valid @RequestBody IntegrationMasterUpadateDetillesRequest request) {

		logger.debug(request.toString());

		IntegrationMasterDetilles details = integrationMasterDetillesService
				.getIntegrationMasterUpadateDetillesRequest(request);
		return createResponse(details, "success", 1000, Type.INFORMATION);

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
