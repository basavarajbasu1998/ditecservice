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

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.CertifacteSub;
import com.ta.ditec.services.request.CertifacteSubDeleteRequest;
import com.ta.ditec.services.request.CertifacteSubRequest;
import com.ta.ditec.services.request.CertifacteSubUpdaterequest;
import com.ta.ditec.services.response.CertifacteSubAllDataResponse;
import com.ta.ditec.services.response.CertifacteSubResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.CertifacteSubService;
import com.ta.ditec.services.transfrom.CertifacteSubTransfrom;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "Certifacte Detilles ", tags = "Certifacte Detilles", description = "Certifacte Documents Detilles  ")
public class CertifacteSubController {
	private static final Logger logger = LoggerFactory.getLogger(CertifacteSubController.class);

	@Autowired
	private CertifacteSubService certifacteSubService;

	@PostMapping("/certifacteSub")
	public ResponseEntity<TaResponse<CertifacteSubResponse>> getCertifacteSub(
			@RequestBody @Valid CertifacteSubRequest req) {
		logger.debug(req.toString());

		CertifacteSubTransfrom trans = new CertifacteSubTransfrom();
		CertifacteSub sub = trans.getCertifacteSub(req);
		certifacteSubService.getCertifacteSub(sub);
		CertifacteSubResponse resp = new CertifacteSubResponse();
		resp.setCertificateTitle(req.getCertificateTitle());
		resp.setParentId(req.getParentId());
		TaResponse<CertifacteSubResponse> res = new TaResponse<>();
		res.setCode(1000);
		res.setMessage("Data saved successfully");
		res.setType(Type.INFORMATION);
		res.setResponseData(resp);
		return ResponseEntity.ok(res);

	}

	@GetMapping("/certifactesuballdata")
	public ResponseEntity<TaResponse<List<CertifacteSubAllDataResponse>>> getaldata() {
		List<CertifacteSubAllDataResponse> sublist = certifacteSubService.getAllData();
		TaResponse<List<CertifacteSubAllDataResponse>> list = new TaResponse<List<CertifacteSubAllDataResponse>>();
		list.setCode(1000);
		list.setMessage("get all data");
		list.setType(Type.INFORMATION);
		list.setResponseData(sublist);
		return ResponseEntity.ok(list);

	}

	@PostMapping("/certifactesubupdate")
	public ResponseEntity<TaResponse<CertifacteSub>> getCertifacteSubupdate(
			@RequestBody CertifacteSubUpdaterequest req) {
		logger.debug(req.toString());

		CertifacteSub list = certifacteSubService.getCertifacteSub(req);
		TaResponse<CertifacteSub> res = new TaResponse<>();
		res.setCode(1000);
		res.setMessage("updated");
		res.setType(Type.INFORMATION);
		res.setResponseData(list);
		return ResponseEntity.ok(res);

	}

	@PostMapping("/certifacteSubdelete")
	public ResponseEntity<TaResponse<Null>> getalDelete(@RequestBody CertifacteSubDeleteRequest req) {
		System.out.println(req);
		logger.debug(req.toString());

		certifacteSubService.deleteUser(req.getId());
		TaResponse<Null> res = new TaResponse<>();
		res.setCode(1000);
		res.setMessage("data deleted");
		res.setType(Type.INFORMATION);
		return ResponseEntity.ok(res);

	}

}
