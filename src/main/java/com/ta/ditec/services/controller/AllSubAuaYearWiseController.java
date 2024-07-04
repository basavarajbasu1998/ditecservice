package com.ta.ditec.services.controller;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.report.domain.AUTHTransaction;
import com.ta.ditec.services.report.domain.EKYCTransaction;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.AgencyLogResponse;
import com.ta.ditec.services.response.AuaAuthMonthlyResponse;
import com.ta.ditec.services.response.AuaEKYCMonthlyResponse;
import com.ta.ditec.services.response.LatestTransResponse;
import com.ta.ditec.services.response.PaginationMeta;
import com.ta.ditec.services.response.SubAuaMonthlyResponse;
import com.ta.ditec.services.response.SubAuaUserResponse;
import com.ta.ditec.services.response.SubauaAllDataResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.response.YearWiseAllAuthResponse;
import com.ta.ditec.services.service.AgencyLogService;
import com.ta.ditec.services.service.AllSubAuaYearWiseService;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("api/v1/adminuser/")
@Api(value = "All Year Wise Transaction Reportes", tags = "All Year Wise Transaction Reportes", description = "Based Year and Monthly  Transaction Reportes")
public class AllSubAuaYearWiseController {

	private static final Logger logger = LoggerFactory.getLogger(AllSubAuaYearWiseController.class);

	@Autowired
	private AllSubAuaYearWiseService allSubAuaYearWiseService;

	@Autowired
	private AgencyLogService agencyLogService;

	@PostMapping("/yearwisesubauacarddata")
	public ResponseEntity<TaResponse<YearWiseAllAuthResponse>> getYearWiseAllAuthResponse(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		YearWiseAllAuthResponse resp = allSubAuaYearWiseService.getYearWiseAllAuthResponse(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}
	@PostMapping("/auamonthlychartdata")
	public ResponseEntity<TaResponse<AuaAuthMonthlyResponse>> getAuaAuthMonthlyResponse(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		AuaAuthMonthlyResponse resp = allSubAuaYearWiseService.getAuthMonthDataResponse(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/kuamonthlychartdata")
	public ResponseEntity<TaResponse<AuaEKYCMonthlyResponse>> getAuaEKYCMonthlyResponse(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		AuaEKYCMonthlyResponse resp = allSubAuaYearWiseService.getAuaEKYCMonthlyResponse(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@GetMapping("/allsubaua")
	public ResponseEntity<TaResponse<AgencyLogResponse[]>> getAgencyLog() {
		AgencyLogResponse[] resp = agencyLogService.getAgencyLog();
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@GetMapping("/monthlywisechartdata")
	public ResponseEntity<TaResponse<SubAuaMonthlyResponse>> getMonthlyResponse() {
		SubAuaMonthlyResponse resp = allSubAuaYearWiseService.getMonthlyResponse();
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/ekycauthtodaytrnas")
	public ResponseEntity<TaResponse<Map<String, Object>>> getTodayAllCount(@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		Map<String, Object> resp = allSubAuaYearWiseService.getTodayAllCount(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/ekyctodaytrnas")
	public ResponseEntity<TaResponse<Map<String, Object>>> getEkycTodayAllCount(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		Map<String, Object> resp = allSubAuaYearWiseService.getTodayEkycAllCount(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/ekyctrans")
	public ResponseEntity<TaResponse<List<LatestTransResponse>>> getekycTrans(@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		List<LatestTransResponse> resp = allSubAuaYearWiseService.getekycTrans(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/authtrans")
	public ResponseEntity<TaResponse<List<LatestTransResponse> >> getAuthTrans(@RequestBody SubAuaUserRequest req) {
		List<LatestTransResponse> resp = allSubAuaYearWiseService.getAuthTrans(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}
		
	@PostMapping("/ekycreporttrans")
	public ResponseEntity<TaResponse<List<LatestTransResponse>>> getekycTopTrans(@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		List<LatestTransResponse> resp = allSubAuaYearWiseService.getekycTrans(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/authreporttrans")
	public ResponseEntity<TaResponse<List<LatestTransResponse> >> getAuthTopTrans(@RequestBody SubAuaUserRequest req) {
		List<LatestTransResponse> resp = allSubAuaYearWiseService.getAuthTrans(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
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
