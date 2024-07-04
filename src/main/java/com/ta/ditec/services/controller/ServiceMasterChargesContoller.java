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
import com.ta.ditec.services.model.ServiceMasterCharges;
import com.ta.ditec.services.request.ServiceMasterChargesDeleteRequest;
import com.ta.ditec.services.request.ServiceMasterChargesRequest;
import com.ta.ditec.services.request.ServiceMasterChargesUpdateRequest;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.ServiceMasterChargesService;
import com.ta.ditec.services.transfrom.ServiceMasterChargesTransfrom;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "Service Master Charges ", tags = "Service Master Charges", description = "Service Master Charges Detiles ")
public class ServiceMasterChargesContoller {

	private static final Logger logger = LoggerFactory.getLogger(ServiceMasterChargesContoller.class);

	@Autowired
	private ServiceMasterChargesService serviceMasterChargesService;

	@PostMapping("/servicemastercharge")
	public ResponseEntity<TaResponse<Null>> getServiceMasterCharges(
			@RequestBody @Valid ServiceMasterChargesRequest service) {

		logger.debug(service.toString());
		ServiceMasterChargesTransfrom trans = new ServiceMasterChargesTransfrom();
		ServiceMasterCharges charge = trans.getServiceMasterCharges(service);
		logger.debug(charge.toString());
		serviceMasterChargesService.getservicecharge(charge);
		TaResponse<Null> res = new TaResponse<>();
		res.setCode(1000);
		res.setMessage("data saved successfully");
		res.setType(Type.INFORMATION);
		return ResponseEntity.ok(res);

	}

	@GetMapping("/servicemasterchargealldata")
	public ResponseEntity<TaResponse<List<ServiceMasterCharges>>> getMasterParameterDetails() {
		List<ServiceMasterCharges> listmpd = serviceMasterChargesService.getServiceMasterChargesAllData();
		logger.debug(listmpd.toString());
		TaResponse<List<ServiceMasterCharges>> res = new TaResponse<>();
		res.setCode(1000);
		res.setMessage("get all data database");
		res.setType(Type.INFORMATION);
		res.setResponseData(listmpd);
		return ResponseEntity.ok(res);

	}

	@PostMapping("/serviceMasterChargesDetillesdeletead")
	public ResponseEntity<TaResponse<Null>> deleteMasterParameterDetailsDelete(
			@Valid @RequestBody ServiceMasterChargesDeleteRequest request) {
		System.out.println(request);
		logger.debug(request.toString());
		serviceMasterChargesService.deleteServiceMasterChargesById(request.getServiceMasterChargesId());
		TaResponse<Null> res = new TaResponse<Null>();
		res.setCode(000);
		res.setType(Type.INFORMATION);
		res.setMessage("successfully Deleted!");
		return ResponseEntity.ok(res);

	}

	@PostMapping(value = { "/serviceMasterChargesDetillesupdate" }, produces = {
			MediaType.APPLICATION_JSON_VALUE }, consumes = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TaResponse<ServiceMasterCharges>> updateAgencyDetails(
			@Valid @RequestBody ServiceMasterChargesUpdateRequest request) {
		logger.debug(request.toString());

		ServiceMasterCharges details = serviceMasterChargesService.getServiceMasterChargesUpdateRequest(request);
		TaResponse<ServiceMasterCharges> res = new TaResponse<ServiceMasterCharges>();
		res.setCode(0);
		res.setMessage("Data saved successfully");
		res.setType(Type.INFORMATION);
		res.setResponseData(details);
		return ResponseEntity.ok(res);

	}

}
