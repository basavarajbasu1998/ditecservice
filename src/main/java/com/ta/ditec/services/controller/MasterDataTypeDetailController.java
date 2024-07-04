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
import com.ta.ditec.services.model.MasterDataTypeDetails;
import com.ta.ditec.services.request.MasterDataTypeDetailsDeleteRequest;
import com.ta.ditec.services.request.MasterDataTypeDetailsRequest;
import com.ta.ditec.services.request.MasterDataTypeDetailsUpdateRequest;
import com.ta.ditec.services.request.PaginationRequest;
import com.ta.ditec.services.response.PaginationResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.MasterDataTypeDetailService;
import com.ta.ditec.services.transfrom.MasterDataTypeDetailTransfrom;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MasterDataTypeDetailController {

	private static final Logger logger = LoggerFactory.getLogger(MasterDataTypeDetailController.class);

	@Autowired
	private MasterDataTypeDetailService masterDataTypeDetailService;

	@PostMapping(value = { "/mdtype" }, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TaResponse<Null>> getMasterDataTypeDetail(
			@Valid @RequestBody MasterDataTypeDetailsRequest req) {

		logger.debug(req.toString());

		MasterDataTypeDetailTransfrom trans = new MasterDataTypeDetailTransfrom();
		MasterDataTypeDetails mdtype = trans.getMasterDataTypeDetailTransfrom(req);
		masterDataTypeDetailService.mdtypedetl(mdtype);
		TaResponse<Null> res = new TaResponse<Null>();
		res.setCode(0);
		res.setMessage("Data Saved Successfull!");
		res.setType(Type.INFORMATION);

		System.out.println(res);
		return ResponseEntity.ok(res);

	}

	@PostMapping("/getmdtype")
	public ResponseEntity<PaginationResponse<List<MasterDataTypeDetails>>> getOrganisationMasterDetails(
			@Valid @RequestBody PaginationRequest request) {

		logger.debug(request.toString());
		if (StringUtils.isEmpty(request.getViewType())) {
			if (request != null && request.getViewType() != null) {

				List<MasterDataTypeDetails> omd = masterDataTypeDetailService.fetchMasterDataTypeDetailList(request);
				PaginationResponse<List<MasterDataTypeDetails>> page = new PaginationResponse<List<MasterDataTypeDetails>>();
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
			List<MasterDataTypeDetails> omd = masterDataTypeDetailService.fetchMasterDataTypeDetailList(request);

			PaginationResponse<List<MasterDataTypeDetails>> page = new PaginationResponse<List<MasterDataTypeDetails>>();
			page.setStart(0);
			page.setEnd(0);
			page.setOrder(null);
			page.setOrderBy(null);
			page.setResponseData(omd);

			return ResponseEntity.ok(page);
		}

	}

	@PostMapping(value = { "/updatemdtype" }, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TaResponse<MasterDataTypeDetails>> updateAgencyDetails(
			@Valid @RequestBody MasterDataTypeDetailsUpdateRequest request) {
		logger.debug(request.toString());
		if (request != null) {
			MasterDataTypeDetails details = masterDataTypeDetailService.updateMasterDataTypeDetail(request);
			TaResponse<MasterDataTypeDetails> res = new TaResponse<MasterDataTypeDetails>();
			res.setCode(0);
			res.setMessage("Data saved successfully");
			res.setType(Type.INFORMATION);
			res.setResponseData(details);
			return ResponseEntity.ok(res);

		} else {
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}

	}

	@PostMapping("/deletemdtype")
	public ResponseEntity<TaResponse<Null>> deleteMasterDataTypeDetailById(
			@Valid @RequestBody MasterDataTypeDetailsDeleteRequest request) {

		logger.debug(request.toString());
		masterDataTypeDetailService.deleteMasterDataTypeDetailById(request.getDataTypeId());
		TaResponse<Null> res = new TaResponse<Null>();
		res.setCode(000);
		res.setType(Type.INFORMATION);
		res.setMessage("successfully Deleted!");
		return ResponseEntity.ok(res);

	}
}
