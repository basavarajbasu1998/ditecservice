package com.ta.ditec.services.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.ConfigMasterDetiles;
import com.ta.ditec.services.request.ConfigMasterDetilesRequest;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.ConfigMasterDetilesService;
import com.ta.ditec.services.transfrom.ConfigMasterDetilesTransfrom;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ConfigMasterDetilesController {

	@Autowired
	private ConfigMasterDetilesService configMasterDetilesService;

	@PostMapping("/ditecconfig")
	public ResponseEntity<TaResponse<Object>> getDitecUserRoles(@RequestBody @Valid ConfigMasterDetilesRequest req) {
		ConfigMasterDetilesTransfrom trans = new ConfigMasterDetilesTransfrom();
		ConfigMasterDetiles row = trans.getConfigMasterDetilesTransform(req);
		configMasterDetilesService.getConfigMasterDetiles(row);
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
