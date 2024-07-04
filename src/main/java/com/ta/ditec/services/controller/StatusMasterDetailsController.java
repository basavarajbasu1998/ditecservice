package com.ta.ditec.services.controller;

import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.StatusMasterDetails;
import com.ta.ditec.services.request.PaginationRequest;
import com.ta.ditec.services.request.StatusMasterDetailsDeleteRequest;
import com.ta.ditec.services.request.StatusMasterDetailsRequset;
import com.ta.ditec.services.request.StatusMasterDetailsUpdateRequest;
import com.ta.ditec.services.response.PaginationResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.StatusMasterDetailsService;
import com.ta.ditec.services.transfrom.StatusMasterDetailsTransfrom;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "StatusMasterDetail ", tags = "StatusMasterDetail", description = "Status Master Detail Detiles ")
public class StatusMasterDetailsController {

	private static final Logger logger = LoggerFactory.getLogger(StatusMasterDetailsController.class);
	@Autowired
	private StatusMasterDetailsService statusMasterDetailsService;

	@PostMapping(value = { "/smd" }, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TaResponse<Null>> getStatusMasterDetails(@Valid @RequestBody StatusMasterDetailsRequset req) {

		logger.debug(req.toString());

		StatusMasterDetailsTransfrom trans = new StatusMasterDetailsTransfrom();
		StatusMasterDetails smd = trans.getStatusMasterDetailsTransfrom(req);
		statusMasterDetailsService.saveDetails(smd);
		TaResponse<Null> res = new TaResponse<Null>();
		res.setCode(0);
		res.setMessage("Data saved Successfully!");
		res.setType(Type.INFORMATION);
		return ResponseEntity.ok(res);

	}

	@PostMapping("/smddetls")
	public ResponseEntity<PaginationResponse<List<StatusMasterDetails>>> getOrganisationMasterDetails(
			@RequestBody PaginationRequest request) {
		logger.debug(request.toString());
		if (StringUtils.isEmpty(request.getViewType())) {
			if (request != null && request.getViewType() != null) {

				List<StatusMasterDetails> omd = statusMasterDetailsService.fetchStatusMasterDetails(request);
				PaginationResponse<List<StatusMasterDetails>> page = new PaginationResponse<List<StatusMasterDetails>>();
				page.setStart(request.getStart());
				page.setEnd(request.getEnd());
				page.setOrder(request.getOrder());
				page.setOrderBy(request.getOrderBy());
				page.setResponseData(omd);
				return ResponseEntity.ok(page);

			} else {
				throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.BAD_REQUEST.getErrorMsg());
			}
		} else {
			List<StatusMasterDetails> omd = statusMasterDetailsService.fetchStatusMasterDetails(request);

			PaginationResponse<List<StatusMasterDetails>> page = new PaginationResponse<List<StatusMasterDetails>>();
			page.setStart(0);
			page.setEnd(0);
			page.setOrder(null);
			page.setOrderBy(null);
			page.setResponseData(omd);

			return ResponseEntity.ok(page);
		}

	}

	@PostMapping(value = { "/updatesmd" }, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TaResponse<StatusMasterDetails>> updateAgencyDetails(
			@Valid @RequestBody StatusMasterDetailsUpdateRequest request) {
		logger.debug(request.toString());

		StatusMasterDetails details = statusMasterDetailsService.UpdateStatusMasterDetails(request);
		TaResponse<StatusMasterDetails> res = new TaResponse<StatusMasterDetails>();
		res.setCode(0);
		res.setMessage("Data saved successfully");
		res.setType(Type.INFORMATION);
		res.setResponseData(details);
		return ResponseEntity.ok(res);

	}

	@PostMapping("/deletesmd")
	public ResponseEntity<TaResponse<Null>> deleteRegisterAgencyDetailsById(
			@Valid @RequestBody StatusMasterDetailsDeleteRequest request) {

		logger.debug(request.toString());
		statusMasterDetailsService.deleteUser(request.getStatusId());
		TaResponse<Null> res = new TaResponse<Null>();
		res.setCode(000);
		res.setType(Type.INFORMATION);
		res.setMessage("successfully Deleted!");
		return ResponseEntity.ok(res);

	}
}
