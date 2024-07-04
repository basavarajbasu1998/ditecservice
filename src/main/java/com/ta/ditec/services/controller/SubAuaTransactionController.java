package com.ta.ditec.services.controller;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.SubAuaTransactionResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.AllSubAuaService;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "SubAuaTransaction ", tags = "SubAuaTransaction", description = "SubAua Wise Transaction Reportes ")
public class SubAuaTransactionController {

	private static final Logger logger = LoggerFactory.getLogger(SubAuaTransactionController.class);
	@Autowired
	private AllSubAuaService allSubAuaService;

//	@PreAuthorize("hasAuthority('ADMIN,SUBAUA')")
	@PostMapping("/subauatotaltransc")
	public ResponseEntity<TaResponse<SubAuaTransactionResponse>> getAllSubAuaService(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());

		SubAuaTransactionResponse resp = allSubAuaService.getAuthTotalRequest(req);
		return createResponse(resp, "success!", 1000, Type.INFORMATION);

	}

//	@PreAuthorize("hasAuthority('ADMIN,SUBAUA')")
	@PostMapping("/subauamonthlywisetransc")
	public ResponseEntity<TaResponse<SubAuaTransactionResponse>> getAllSubAuaMonthlywiseService(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());

		SubAuaTransactionResponse resp = allSubAuaService.getAuthTotalRequest(req);
		return createResponse(resp, "success!", 1000, Type.INFORMATION);

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
