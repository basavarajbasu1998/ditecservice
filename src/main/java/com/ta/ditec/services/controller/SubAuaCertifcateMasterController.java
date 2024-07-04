package com.ta.ditec.services.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Null;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.SubAuaCertifacteMaster;
import com.ta.ditec.services.request.SubAuaCertifacteMasterDeleteRequest;
import com.ta.ditec.services.request.SubAuaCertifacteMasterRequest;
import com.ta.ditec.services.request.SubAuaCertifacteMasterUpadateRequest;
import com.ta.ditec.services.response.SubAuaCertifacteMasterAllDataResponse;
import com.ta.ditec.services.response.SubAuaCertifacteMasterResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.SubAuaCertifacteMasterService;
import com.ta.ditec.services.transfrom.SubAuaCertifacteMasterTransfrom;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "SubAua Certifacte Master ", tags = "SubAua CertifacteMaster", description = "SubAuaCertifacteMaster ")
public class SubAuaCertifcateMasterController {
	
	private static final Logger logger = LoggerFactory.getLogger(SubAuaCertifcateMasterController.class);

	@Autowired
	private SubAuaCertifacteMasterService subAuaCertifacteMasterService;

	@PostMapping("/subAuacertifactemaster")
	public ResponseEntity<TaResponse<SubAuaCertifacteMasterResponse>> getCertifacteSub(
			@RequestBody  @Valid SubAuaCertifacteMasterRequest req) {
		
		logger.debug(req.toString());
		SubAuaCertifacteMasterTransfrom trans = new SubAuaCertifacteMasterTransfrom();
		SubAuaCertifacteMaster sub = trans.getSubAuaCertifacteMaster(req);
		subAuaCertifacteMasterService.getSubAuaCertifacteMaster(sub);
		TaResponse<SubAuaCertifacteMasterResponse> res = new TaResponse<>();
		SubAuaCertifacteMasterResponse resp = new SubAuaCertifacteMasterResponse();
		resp.setParentId(sub.getId());
		resp.setCertfacteMasterTitle(req.getCertfacteMasterTitle());
		res.setCode(1000);
		res.setMessage("Data saved successfully");
		res.setType(Type.INFORMATION);
		res.setResponseData(resp);
		return ResponseEntity.ok(res);

	}

	@GetMapping("/subAuacertifactemasteralldata")
	public ResponseEntity<TaResponse<List<SubAuaCertifacteMasterAllDataResponse>>> getaldata() {
		List<SubAuaCertifacteMasterAllDataResponse> sublist = subAuaCertifacteMasterService.getallData();
logger.debug(sublist.toString());
		TaResponse<List<SubAuaCertifacteMasterAllDataResponse>> list = new TaResponse<List<SubAuaCertifacteMasterAllDataResponse>>();

		list.setCode(1000);
		list.setMessage("get all data");
		list.setType(Type.INFORMATION);
		list.setResponseData(sublist);
		return ResponseEntity.ok(list);

	}

	@PostMapping("/subAuacertifactemasterupdate")
	public ResponseEntity<TaResponse<SubAuaCertifacteMaster>> getCertifacteSubupdate(
			@RequestBody SubAuaCertifacteMasterUpadateRequest req) {
		logger.debug(req.toString());
		SubAuaCertifacteMaster list = subAuaCertifacteMasterService.updateSubAuaCertifacteMaster(req);

		TaResponse<SubAuaCertifacteMaster> res = new TaResponse<>();
		res.setCode(1000);
		res.setMessage("updated");
		res.setType(Type.INFORMATION);
		res.setResponseData(list);
		return ResponseEntity.ok(res);
	}

	@PostMapping("/subauacertifactemasterdelete")
	public ResponseEntity<TaResponse<Null>> getalDelete(@RequestBody SubAuaCertifacteMasterDeleteRequest req) {
		logger.debug(req.toString());
		System.out.println(req);
		subAuaCertifacteMasterService.getData(req.getId());
		TaResponse<Null> res = new TaResponse<>();
		res.setCode(1000);
		res.setMessage("data deleted");
		res.setType(Type.INFORMATION);
		return ResponseEntity.ok(res);

	}

}
