package com.ta.ditec.services.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.coyote.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.convertfiles.PDFGenerationService;
import com.ta.ditec.services.convertfiles.UserMasterDetailsCSV;
import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.request.DitecReportsRequest;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.response.AuthTotalResponse;
import com.ta.ditec.services.response.DitecReportsServiceResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.response.TodayAuthResponse;
import com.ta.ditec.services.response.TodayEkycResponse;
import com.ta.ditec.services.service.AllSubAuaService;
import com.ta.ditec.services.service.DitecReportsService;

import io.swagger.annotations.Api;

@RestController
//@RequestMapping("api/v1")
@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "All Transaction Reportes", tags = "All Transaction Reportes", description = "Based  Transaction Reportes")
public class AllSubAuaServiceController {

	private static final Logger logger = LoggerFactory.getLogger(AllSubAuaServiceController.class);

	@Autowired
	private AllSubAuaService allSubAuaService;

	private final DitecReportsService ditecReportsService;

	@Autowired
	private PDFGenerationService pdfGenerationService;

	@Autowired
	private UserMasterDetailsCSV userMasterDetailsCSV;

	@Autowired
	public AllSubAuaServiceController(DitecReportsService ditecReportsService) {
		this.ditecReportsService = ditecReportsService;
	}

	@Autowired
//	private DitecReportsService ditecReportsService;

	@GetMapping("/allsubservice")
//	@ApiOperation(value = "Get all items", notes = "Retrieve all items")
	public ResponseEntity<TaResponse<AuthTotalResponse>> getAllSubAuaService() {
		AuthTotalResponse resp = allSubAuaService.getAuthTotalResponse();
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/auayeardonutcharts")
	public ResponseEntity<TaResponse<TodayAuthResponse>> getAllDonutAUASubAuaService(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		TodayAuthResponse resp = allSubAuaService.getYearAuthResponse(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/kuayeardonutcharts")
	public ResponseEntity<TaResponse<TodayEkycResponse>> getAllDonutKUASubAuaService(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		TodayEkycResponse resp = allSubAuaService.getYearKycResponse(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/auatodaydonutcharts")
	public ResponseEntity<TaResponse<TodayAuthResponse>> getAllTodayDonutAUASubAuaService(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		TodayAuthResponse resp = allSubAuaService.getTodayAuthResponse(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/kuatodaydonutcharts")
	public ResponseEntity<TaResponse<TodayEkycResponse>> getAllDonutTodayKUASubAuaService(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		TodayEkycResponse resp = allSubAuaService.getTodayKycResponse(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/authtodaybarchartsubauadata")
	public ResponseEntity<TaResponse<Map<String, Object>>> getTodayAuthResponses(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		Map<String, Object> resp = allSubAuaService.getTodaysubauaAuthResponseForEachAgency(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/kuatodaybarchartsubauadata")
	public ResponseEntity<TaResponse<Map<String, Object>>> getTodayKuaResponses(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		Map<String, Object> resp = allSubAuaService.getTodaysubauaKuaResponseForEachAgency(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@PostMapping("/subauatotaldonutcharts")
	public ResponseEntity<TaResponse<TodayAuthResponse>> getSubauaAllTodayDonutAUASubAuaService(
			@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		TodayAuthResponse resp = allSubAuaService.getSubAuaWiseTodayAuthResponse(req);
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

//	@GetMapping("/dashboardreports")
//	public ResponseEntity<TaResponse<List<DitecReportsServiceResponse>>> getDitecReportsServiceResponse() {
//		List<DitecReportsServiceResponse> resp = ditecReportsService.getDitecReportsServiceResponse();
//		return createResponse(resp, "success", 1000, Type.INFORMATION);
//	}

	@PostMapping("/dashboardreports")
	public ResponseEntity<TaResponse<List<DitecReportsServiceResponse>>> getReports(
			@RequestBody DitecReportsRequest req) throws IOException {
		List<DitecReportsServiceResponse> resp = ditecReportsService.getDitecReportsServiceResponse(req);
		PDFGenerationService ser = new PDFGenerationService();
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}
	
	

	@PostMapping("/dashboardreportspdf")
	public ResponseEntity<byte[]> getReportsPDF(@RequestBody DitecReportsRequest req) throws IOException {
		List<DitecReportsServiceResponse> resp = ditecReportsService.getDitecReportsServiceResponse(req);
		PDFGenerationService ser = new PDFGenerationService();
		byte[] pdfBytes = ser.generatePdf(resp);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("filename", "report.pdf");
		return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
	}


	@PostMapping(path = "/dashboardreportscsv", consumes = "application/json", produces = "text/csv")
	public void getReportsCSV(@RequestBody DitecReportsRequest req, HttpServletResponse response) {
		List<DitecReportsServiceResponse> resp = ditecReportsService.getDitecReportsServiceResponse(req);

		response.setContentType("text/csv");
		response.setHeader("Content-Disposition", "attachment; filename=\"report.csv\"");

		try {
			OutputStream outputStream = response.getOutputStream();
			userMasterDetailsCSV.writeStudentsToCsv(resp, outputStream);
		} catch (IOException e) {
			e.printStackTrace();
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
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
