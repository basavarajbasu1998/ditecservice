package com.ta.ditec.services.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Null;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.MasterParameterDetails;
import com.ta.ditec.services.request.MasterParameterDetailsDeleteRequest;
import com.ta.ditec.services.request.MasterParameterDetailsRequest;
import com.ta.ditec.services.request.MasterParameterDetailsUpadeteRequest;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.MasterParameterDetailsService;
import com.ta.ditec.services.transfrom.MasterParameterDetailsTransfrom;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MasterParameterDetailsController {

	private static final Logger logger = LoggerFactory.getLogger(MasterParameterDetailsController.class);
	
	@Autowired
	private MasterParameterDetailsService masterParameterDetailsService;

	@PostMapping("/masterParameterDetails")
	public ResponseEntity<TaResponse<Null>> getMasterParameterDetails(@RequestBody  @Valid MasterParameterDetailsRequest req) {
		logger.debug(req.toString());

		MasterParameterDetailsTransfrom trans = new MasterParameterDetailsTransfrom();
		MasterParameterDetails mpd = trans.getMasterParameterDetailsTransfrom(req);
		masterParameterDetailsService.getMasterParameterDetails(mpd);
		TaResponse<Null> res = new TaResponse<>();
		res.setCode(1000);
		res.setMessage("data saved successfully");
		return ResponseEntity.ok(res);

	}

	@GetMapping("/masterParameterDetailsalldata")
	public ResponseEntity<TaResponse<List<MasterParameterDetails>>> getMasterParameterDetails() {
		List<MasterParameterDetails> listmpd = masterParameterDetailsService.getallMasterParameterDetailsData();
		
		logger.debug(listmpd.toString());
		
		TaResponse<List<MasterParameterDetails>> res = new TaResponse<>();
		res.setCode(1000);
		res.setMessage("get all data database");
		res.setType(Type.INFORMATION);
		res.setResponseData(listmpd);
		return ResponseEntity.ok(res);

	}

	@PostMapping("/masterParameterDetailsdeletead")
	public ResponseEntity<TaResponse<Null>> deleteMasterParameterDetailsDelete(
			@Valid @RequestBody MasterParameterDetailsDeleteRequest request) {
		System.out.println(request);
		masterParameterDetailsService.deleteMasterParameterDetailsById(request.getParameterId());
		TaResponse<Null> res = new TaResponse<Null>();
		res.setCode(000);
		res.setType(Type.INFORMATION);
		res.setMessage("successfully Deleted!");
		return ResponseEntity.ok(res);

	}

	@PostMapping(value = { "/masterParameterDetailsupdate" }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TaResponse<MasterParameterDetails>> updateAgencyDetails(
			@Valid @RequestBody MasterParameterDetailsUpadeteRequest request) {
		if (request != null) {
			MasterParameterDetails details = masterParameterDetailsService.getMasterParameterUpadteDetails(request);
			TaResponse<MasterParameterDetails> res = new TaResponse<MasterParameterDetails>();
			res.setCode(0);
			res.setMessage("Data saved successfully");
			res.setType(Type.INFORMATION);
			res.setResponseData(details);
			return ResponseEntity.ok(res);

		} else {
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}
	}

}
