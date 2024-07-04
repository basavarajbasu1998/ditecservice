package com.ta.ditec.services.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.encrypt.SHAEnc;
import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.DitecUserRoles;
import com.ta.ditec.services.response.DitecUserRolesResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.DitecUserRolesService;

@RestController
@RequestMapping("api/v1")
public class DitecUserRolesController {

	@Autowired
	private DitecUserRolesService ditecUserRolesService;

	@PostMapping("/ditecroles")
	public ResponseEntity<TaResponse<DitecUserRolesResponse>> getDitecUserRoles() {
		DitecUserRoles row = ditecUserRolesService.getDitecUserRoles();
		DitecUserRolesResponse res = new DitecUserRolesResponse();
//		res.setUserName(row.getUserName());
		res.setPassword(SHAEnc.decryptData(row.getPassword()));
		res.setRole(row.getRole());
		return createResponse(res, "success", 1000, Type.INFORMATION);

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
