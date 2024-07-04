package com.ta.ditec.services.controller;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.convertfiles.UserMasterDetailsCSV;
import com.ta.ditec.services.convertfiles.UserMasterDetailsPDF;
import com.ta.ditec.services.encrypt.SHAEnc;
import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.UserMasterDetails;
import com.ta.ditec.services.request.EmailValidationRequest;
import com.ta.ditec.services.request.PaginationRequest;
import com.ta.ditec.services.request.UserMasterDetailesResetPasswordRequest;
import com.ta.ditec.services.request.UserMasterDetailsChangePasswordRequest;
import com.ta.ditec.services.request.UserMasterDetailsDeleteRequest;
import com.ta.ditec.services.request.UserMasterDetailsForgotPasswodRequest;
import com.ta.ditec.services.request.UserMasterDetailsLoginRequest;
import com.ta.ditec.services.request.UserMasterDetailsRequest;
import com.ta.ditec.services.request.UserMasterDetailsUpdateRequest;
import com.ta.ditec.services.request.UserMasterDetailsforgotPasswordCheckLinkRequest;
import com.ta.ditec.services.response.PaginationResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.response.UserMasterDetailsForgotPasswordResponse;
import com.ta.ditec.services.response.UserMasterDetailsLoginResponse;
import com.ta.ditec.services.response.UserMasterDetailsResponse;
import com.ta.ditec.services.service.UserMasterDetailsService;
import com.ta.ditec.services.transfrom.UserMasterDetailsTransfrom;

@RestController

@RequestMapping("api/v1")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserMasterDetailsController {

	private static final Logger logger = LoggerFactory.getLogger(UserMasterDetailsController.class);

	@Autowired
	private UserMasterDetailsService userMasterDetailsService;

	@Autowired
	private UserMasterDetailsCSV usermasterdetailscsv;

	@PostMapping(value = { "/umd" }, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TaResponse<Null>> getUserMasterDetails(@Valid @RequestBody UserMasterDetailsRequest req,

			HttpServletRequest servletRequest) {

		logger.debug(req.toString());

		System.out.println("ddddddddd00" + req);
		UserMasterDetailsTransfrom trans = new UserMasterDetailsTransfrom();
		UserMasterDetails umdel = trans.getUserMasterDetailsTransfrom(req);
		logger.debug(umdel.toString());
		UserMasterDetails saveDetails = userMasterDetailsService.saveDetails(umdel, servletRequest);
		logger.debug(saveDetails.toString());
		TaResponse<Null> res = new TaResponse<Null>();
		res.setCode(1000);
		res.setMessage("Data saved successfully");
		res.setType(Type.INFORMATION);
		return ResponseEntity.ok(res);

	}

	@PostMapping("/umdetl")
	public ResponseEntity<PaginationResponse<List<UserMasterDetails>>> getOrganisationMasterDetails(
			@RequestBody PaginationRequest request) {
		logger.debug(request.toString());
		if (StringUtils.isEmpty(request.getViewType())) {
			Page<UserMasterDetails> omd = userMasterDetailsService.fetchUserMasterDetailsList(request);
			PaginationResponse<List<UserMasterDetails>> page = new PaginationResponse<List<UserMasterDetails>>();
			page.setStart(omd.getNumber());
			page.setEnd(omd.getNumberOfElements());
			page.setOrder(request.getOrder());
			page.setOrderBy(request.getOrderBy());
			page.setResponseData(omd.getContent());
			page.setTotal(omd.getTotalElements());
			return ResponseEntity.ok(page);

		} else {
			Page<UserMasterDetails> omd = userMasterDetailsService.fetchUserMasterDetailsList(request);
			logger.debug(omd.toString());
			PaginationResponse<List<UserMasterDetails>> page = new PaginationResponse<List<UserMasterDetails>>();
			page.setStart(omd.getNumber());
			page.setEnd(omd.getNumberOfElements());
			page.setOrder(null);
			page.setOrderBy(null);
			page.setTotal(omd.getTotalElements());
			page.setResponseData(omd.getContent());
			logger.debug(page.toString());
			return ResponseEntity.ok(page);
		}
	}

	@PostMapping(value = { "/updateumd" }, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TaResponse<Null>> updateAgencyDetails(
			@Valid @RequestBody UserMasterDetailsUpdateRequest request) {
		logger.debug(request.toString());

		userMasterDetailsService.updateUserMasterDetails(request);
		TaResponse<Null> res = new TaResponse<Null>();
		res.setCode(1000);
		res.setMessage("Data Updatated successfully");
		res.setType(Type.INFORMATION);
		return ResponseEntity.ok(res);

	}

	@PostMapping(value = { "/umdfindbyid" }, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TaResponse<UserMasterDetails>> findByData(
			@RequestBody UserMasterDetailsDeleteRequest request) {
		logger.debug(request.toString());
		if (StringUtils.isNotEmpty(request.getUserId())) {

			UserMasterDetails details = userMasterDetailsService.findById(request);

			TaResponse<UserMasterDetails> response = new TaResponse<UserMasterDetails>();
			response.setCode(1000);
			response.setMessage("success");
			response.setType(Type.INFORMATION);
			response.setResponseData(details);

			return ResponseEntity.ok(response);

		}

		else {
			TaResponse<UserMasterDetails> response = new TaResponse<UserMasterDetails>();
			response.setCode(ErrorCode.BAD_REQUEST.getCode());
			response.setMessage("Data must be not null");
			response.setType(Type.INFORMATION);
			return ResponseEntity.ok(response);
		}
	}

	@PostMapping(value = { "/deleteumd" }, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TaResponse<Null>> deleteUserBillingMasterDetailsById(
			@Valid @RequestBody UserMasterDetailsDeleteRequest request) {
		logger.debug(request.toString());

		userMasterDetailsService.deleteUser(request.getUserId());
		TaResponse<Null> res = new TaResponse<Null>();
		res.setCode(1000);
		res.setType(Type.INFORMATION);
		res.setMessage("successfully Deleted!");
		return ResponseEntity.ok(res);

	}

	@PostMapping(value = { "/loginumd" }, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<TaResponse<UserMasterDetailsLoginResponse>> getLogin(
			@RequestBody UserMasterDetailsLoginRequest request) {
		logger.debug(request.toString());

		UserMasterDetails details = userMasterDetailsService.Login(request);
		UserMasterDetailsLoginResponse response = new UserMasterDetailsLoginResponse();
		response.setUserId(details.getUserId());
		response.setUserMobileNumber(details.getUserMobileNumber());
		response.setUserEmail(details.getUserEmail());
		response.setFirstTimeUser(details.getFirstTimeUser());
		response.setUserName(details.getUserName());

		logger.debug(response.toString());
		TaResponse<UserMasterDetailsLoginResponse> res = new TaResponse<UserMasterDetailsLoginResponse>();
		res.setCode(1000);
		res.setType(Type.INFORMATION);
		res.setMessage("Success");
		res.setResponseData(response);
		System.out.println("return controller");
		return ResponseEntity.ok(res);

	}

	@PostMapping("/changepsw")
	public ResponseEntity<TaResponse> getLoginDetails(@RequestBody @Valid UserMasterDetailesResetPasswordRequest req) {
		logger.debug(req.toString());
		if (StringUtils.isNotEmpty(req.getPassword())) {
			System.out.println(req + "reqwqqq");
			UserMasterDetails ma = userMasterDetailsService.changePassword(req);
			logger.debug(ma.toString());
			TaResponse res = new TaResponse();
			res.setCode(1000);
			res.setMessage("Password changed");
			res.setType(Type.INFORMATION);
			return ResponseEntity.ok(res);
		} else {
			TaResponse res = new TaResponse();
			res.setCode(ErrorCode.BAD_REQUEST.getCode());
			res.setMessage("data must not be null");
			res.setType(Type.INFORMATION);
			return ResponseEntity.ok(res);
		}
	}

	@PostMapping("/umd/activationmail")
	public ResponseEntity<TaResponse<Null>> getemailbyvalidation(@RequestBody @Valid EmailValidationRequest request,
			HttpServletRequest servletRequest) {
		logger.debug(request.toString());
		System.out.println("email: " + request.getEmail());
		if (StringUtils.isNotEmpty(request.getEmail())) {
			String email = SHAEnc.decryptData(request.getEmail());
			System.out.println(email);
			UserMasterDetails activeLink = userMasterDetailsService.activeLink(email, servletRequest);

			logger.debug(activeLink.toString());
			;
			TaResponse<Null> response = new TaResponse<Null>();
			response.setCode(1000);
			response.setMessage("email verfied sucessfully");
			response.setType(Type.INFORMATION);
			return ResponseEntity.ok(response);
		} else {
			TaResponse<Null> response = new TaResponse<Null>();
			response.setCode(ErrorCode.BAD_REQUEST.getCode());
			response.setMessage("data must not be null");
			response.setType(Type.INFORMATION);
			return ResponseEntity.ok(response);
		}

	}

	@GetMapping("/checkuser")
	public ResponseEntity<TaResponse<Null>> checkUser() throws IOException {

		List<UserMasterDetails> checkData = userMasterDetailsService.checkData();
		logger.debug(checkData.toString());
		TaResponse<Null> response1 = new TaResponse<Null>();
		response1.setCode(1000);
		response1.setMessage("user is there!");
		response1.setType(Type.INFORMATION);
		return ResponseEntity.ok(response1);
	}

	@PostMapping("/forgotpassword")
	public ResponseEntity<TaResponse<UserMasterDetailsForgotPasswordResponse>> fogotPassword(
			@RequestBody UserMasterDetailsForgotPasswodRequest request, HttpServletRequest servletRequest) {
		logger.debug(request.toString());
		if (StringUtils.isNotEmpty(request.getUserEmail())) {
			UserMasterDetails details = userMasterDetailsService.forgotPassword(request, servletRequest);
			UserMasterDetailsForgotPasswordResponse umd = new UserMasterDetailsForgotPasswordResponse();
			umd.setUserId(details.getUserId());
			logger.debug(umd.toString());
			TaResponse<UserMasterDetailsForgotPasswordResponse> response = new TaResponse<UserMasterDetailsForgotPasswordResponse>();
			response.setCode(1000);
			response.setMessage("valid User!");
			response.setType(Type.INFORMATION);
			response.setResponseData(umd);
			return ResponseEntity.ok(response);
		} else {
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}

	}

	@PostMapping("/changepassword")
	public ResponseEntity<TaResponse<Null>> changepassword(@RequestBody UserMasterDetailsChangePasswordRequest request,
			HttpServletRequest servletRequest) {

		logger.debug(request.toString());
		if (StringUtils.isNotEmpty(request.getUserId()) && StringUtils.isNotEmpty(request.getUserPassword())) {
			UserMasterDetails details = userMasterDetailsService.forgotchangepassword(request);
			logger.debug(details.toString());
			TaResponse response = new TaResponse();
			response.setCode(1000);
			response.setMessage("Successfully changed!");
			response.setType(Type.INFORMATION);
			return ResponseEntity.ok(response);
		} else {
			throw new TaException(ErrorCode.BAD_REQUEST, ErrorCode.BAD_REQUEST.getErrorMsg());
		}

	}

	@PostMapping("/forgotpasswordlinkcheck")
	public ResponseEntity<TaResponse<UserMasterDetails>> forgotPasswordCheckLink(
			@RequestBody UserMasterDetailsforgotPasswordCheckLinkRequest request, HttpServletRequest servletRequest) {
		logger.debug(request.toString());
		if (StringUtils.isNotEmpty(request.getUserId())) {
			UserMasterDetails details = userMasterDetailsService.forgotPasswordCheckLink(request);

			logger.debug(details.toString());
			UserMasterDetailsResponse umd = new UserMasterDetailsResponse();
			TaResponse<UserMasterDetails> response = new TaResponse<UserMasterDetails>();
			response.setCode(1000);
			response.setMessage("Successfully changed!");
			response.setType(Type.INFORMATION);
			response.setResponseData(details);
			return ResponseEntity.ok(response);
		} else {
			throw new TaException(ErrorCode.INTERNAL_ERROR, ErrorCode.INTERNAL_ERROR.getErrorMsg());
		}

	}

//	@GetMapping("/exportumdcsv")
//	public void userMasterDetailsCSVconvert(HttpServletResponse response) throws IOException {
//		response.setContentType("text/csv");
//		response.addHeader("Content-Disposition", "attachment; filename=\"usermasterdetails.csv\"");
//		usermasterdetailscsv.writeUserMasterDetailstoCsv(userMasterDetailsService.convertFiles(), response.getWriter());
//	}

	@GetMapping("/exportumdpdf")
	public void generatePdfFile(HttpServletResponse response) throws IOException {
		response.setContentType("application/pdf");
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
		String currentDateTime = dateFormat.format(new Date());
		String headerkey = "Content-Disposition";
		String headervalue = "attachment; filename=usermasterdetails_" + currentDateTime + ".pdf";
		response.setHeader(headerkey, headervalue);
		List<UserMasterDetails> umddetails = userMasterDetailsService.convertFiles();
		logger.debug(umddetails.toString());
		UserMasterDetailsPDF PDF = new UserMasterDetailsPDF();
		PDF.generateumdPDF(umddetails, response);

	}
}
