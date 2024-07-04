package com.ta.ditec.services.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.convertfiles.PDFGenerationService;
import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.PaymentPlanDetails;
import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.request.SubAuaLoginRequest;
import com.ta.ditec.services.request.SubAuaRequest;
import com.ta.ditec.services.response.AuthTotalResponse;
import com.ta.ditec.services.response.LoginResponse;
import com.ta.ditec.services.response.SubAuaMonthlyResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.service.AllSubAuaService;
import com.ta.ditec.services.service.AllSubAuaYearWiseService;
import com.ta.ditec.services.service.DitecUserRolesService;
import com.ta.ditec.services.service.OnboardDocumentsSevice;
import com.ta.ditec.services.service.PaymentPlanDetailsService;
import com.ta.ditec.services.service.SubAuaCertifacteService;
import com.ta.ditec.services.service.SubAuaUserService;
import com.ta.ditec.services.transfrom.SubAuaTransform;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "AuthSubAuaUser ", tags = "SubAuaUser", description = "SubAuaUser Register ")
public class AuthController {

	private static final Logger logger = LoggerFactory.getLogger(SubAuaUserController.class);

	@Autowired
	private SubAuaUserService subAuaUserService;

	@Autowired
	private DitecUserRolesService ditecUserRolesService;

	@Autowired
	private AllSubAuaService allSubAuaService;

	@Autowired
	private AllSubAuaYearWiseService allSubAuaYearWiseService;

	@Autowired
	private SubAuaCertifacteService subAuaCertifacteService;

	@Autowired
	private OnboardDocumentsSevice onboardDocumentsSevice;

	@PostMapping("/auth/subauadata")
	@ApiResponses({ @ApiResponse(code = 200, message = "subauadata saved success"),
			@ApiResponse(code = 400, message = "subauadata saved success"),
			@ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<TaResponse<Null>> getuser(@RequestBody @Valid SubAuaRequest req, HttpServletRequest servl)
			throws MessagingException, IOException {
		logger.debug(req.toString());
		System.out.println(req);
		SubAuaTransform trans = new SubAuaTransform();
		SubAuaUser ages = trans.getUserMasterDetailsTransfrom(req);
		System.out.println(ages);
		subAuaUserService.savesubaua(ages, servl);
		return createResponse(null, "Data Saved SuccessFully!", 1000, Type.INFORMATION);
	}

	@PostMapping("/auth/subaualogin")
	@ApiOperation(value = "Sub Aua Login ", notes = "Sub Aua Login")
	@ApiResponses({ @ApiResponse(code = 200, message = "Sub Aua Login"),
			@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<TaResponse<LoginResponse>> subaualogin(@RequestBody @Valid SubAuaLoginRequest req,
			HttpServletRequest servl) throws MessagingException {
		logger.debug(req.toString());
		System.out.println("dataaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + req);
		LoginResponse login = subAuaUserService.loginstatus(req, servl);
//		boolean applicationstatus = subAuaUserService.getApplicationstatus(req, servl);

//		System.out.println("applicationstatus" + applicationstatus);

		return createResponse(login, "success!", 1000, Type.INFORMATION);
//		DitecUserRoles users = ditecUserRolesService.getLoginRoles(req, servl);
	}

	@GetMapping("/auth/allsubservice")
//	@ApiOperation(value = "Get all items", notes = "Retrieve all items")
	public ResponseEntity<TaResponse<AuthTotalResponse>> getAllSubAuaService() {
		AuthTotalResponse resp = allSubAuaService.getAuthTotalResponse();
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@GetMapping("/auth/todaytotalbarchartsubauadata")
	public ResponseEntity<TaResponse<Map<String, Object>>> getTodayTotalKuaResponses() {
		Map<String, Object> resp = allSubAuaService.getTodayTotalsubauaKuaResponseForEachAgency();
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@GetMapping("/auth/totalbarchartsubauadata")
	public ResponseEntity<TaResponse<Map<String, Object>>> getTotalKuaResponses() {
		Map<String, Object> resp = allSubAuaService.getTotalsubauaKuaResponseForEachAgency();
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@GetMapping("/auth/monthlywisechartdata")
	public ResponseEntity<TaResponse<SubAuaMonthlyResponse>> getMonthlyResponse() {
		SubAuaMonthlyResponse resp = allSubAuaYearWiseService.getMonthlyResponse();
		return createResponse(resp, "success", 1000, Type.INFORMATION);
	}

	@RequestMapping(value = "/auth/getpdffile/{fileId}", method = RequestMethod.GET)
	public void getFile(HttpServletResponse response, @Valid @PathVariable String fileId) {
		String filePath = subAuaCertifacteService.getCertificateFilePath(fileId);
		System.out.println(filePath);

		logger.debug(filePath);

		if (filePath != null) {
			try {
				File file = new File(filePath);
				if (file.exists()) {
					response.setContentType("application/pdf");
					response.setHeader("Content-disposition", "attachment; filename=\"" + fileId + ".pdf" + "\"");
					FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			} catch (IOException e) {
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	@RequestMapping(value = "/getdocumentfile/{fileId}", method = RequestMethod.GET)
	public void getFilePdf(HttpServletResponse response, @Valid @PathVariable String fileId) {
		String filePath = onboardDocumentsSevice.getCertificateFilePath(fileId);
		logger.debug(filePath);
		System.out.println(filePath);

		if (filePath != null) {
			try {
				File file = new File(filePath);
				if (file.exists()) {
					response.setContentType("application/pdf");
					response.setHeader("Content-disposition", "attachment; filename=\"" + fileId + ".pdf" + "\"");
					FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
				} else {
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				}
			} catch (IOException e) {
				e.printStackTrace();
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}
		} else {
			response.setStatus(HttpServletResponse.SC_NOT_FOUND);
		}
	}
	
	
	@Autowired
	private PDFGenerationService pdfGenerationService;

	@GetMapping(value = "/auth/get", produces = MediaType.APPLICATION_PDF_VALUE)
	public ResponseEntity<byte[]> generatePDF() {
		try {
			byte[] pdfBytes = pdfGenerationService.generatePDF();
			return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(pdfBytes);
		} catch (IOException e) {
			e.printStackTrace();
			return ResponseEntity.status(500).body(null);
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
