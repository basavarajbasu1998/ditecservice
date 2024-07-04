package com.ta.ditec.services.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.response.SubAuaOrgResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.DitecReportsService;

@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
public class DitecReportController {

	@Autowired
	private DitecReportsService ditecReportsService;

	@GetMapping("/orglist")
	public ResponseEntity<TaResponse<List<SubAuaOrgResponse>>> getSubUser() {
		List<SubAuaOrgResponse> orglist = ditecReportsService.getList();
		return createResponse(orglist, "Sucess", 1000, null);

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
