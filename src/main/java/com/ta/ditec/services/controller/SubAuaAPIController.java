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
import com.ta.ditec.services.model.SubAuaAPI;
import com.ta.ditec.services.request.SubAuaApiKeysRequest;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.SubAuaAPIService;
import com.ta.ditec.services.transfrom.SubAuaAPITransform;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "API KEYS", tags = "API KEYS", description = "API KEYS")
public class SubAuaAPIController {

	private static final Logger logger = LoggerFactory.getLogger(SubAuaAPIController.class);

	@Autowired
	private SubAuaAPIService subAuaAPIService;

	@PostMapping("/apikeys")
	public ResponseEntity<TaResponse<SubAuaAPI>> getSubAuaAPI(@RequestBody @Valid SubAuaApiKeysRequest req) {

		System.out.println("apikey request getting" + req);

		logger.debug(req.toString());
		SubAuaAPITransform trans = new SubAuaAPITransform();
		SubAuaAPI apis = trans.getSubAuaAPI(req);
		subAuaAPIService.getApikeys(apis);
		return createResponse(apis, "success!", 1000, Type.INFORMATION);

	}

	@PostMapping("/subauakey")
	public ResponseEntity<TaResponse<List<SubAuaAPI>>> getlists(@RequestBody @Valid SubAuaApiKeysRequest req) {
		logger.debug(req.toString());
		List<SubAuaAPI> lists = subAuaAPIService.getSubAuaAPIAll(req);
		return createResponse(lists, "success!", 1000, Type.INFORMATION);

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
