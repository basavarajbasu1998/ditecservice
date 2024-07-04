package com.ta.ditec.services.controller;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ta.ditec.services.exception.Type;
import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.request.SubAuaApproveRequest;
import com.ta.ditec.services.request.SubAuaChangePasswordRequest;
import com.ta.ditec.services.request.SubAuaDeleteRequest;
import com.ta.ditec.services.request.SubAuaLoginRequest;
import com.ta.ditec.services.request.SubAuaRequest;
import com.ta.ditec.services.request.SubAuaUpdateRequest;
import com.ta.ditec.services.request.SubAuaUserForgotPasswodRequest;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.request.SubAuaUserResetPasswordRequest;
import com.ta.ditec.services.request.SubAuaforgotPasswordCheckLinkRequest;
import com.ta.ditec.services.request.TransactionModelRequest;
import com.ta.ditec.services.response.LoginResponse;
import com.ta.ditec.services.response.PaginationMeta;
import com.ta.ditec.services.response.SubAuaApproveResponse;
import com.ta.ditec.services.response.SubAuaChangepasswordResponse;
import com.ta.ditec.services.response.SubAuaUserForgotPasswordResponse;
import com.ta.ditec.services.response.SubAuaUserResponse;
import com.ta.ditec.services.response.SubauaAllDataResponse;
import com.ta.ditec.services.response.TaResponse;
import com.ta.ditec.services.response.TransactionModelResponse;
import com.ta.ditec.services.service.DitecUserRolesService;
import com.ta.ditec.services.service.SubAuaUserService;
import com.ta.ditec.services.transfrom.SubAuaTransform;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/v1/adminuser/")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Api(value = "SubAuaUser ", tags = "SubAuaUser", description = "SubAuaUser Register ")
public class SubAuaUserController {

	private static final Logger logger = LoggerFactory.getLogger(SubAuaUserController.class);

	@Autowired
	private SubAuaUserService subAuaUserService;

	@Autowired
	private DitecUserRolesService ditecUserRolesService;

//	@PostMapping("/subauadata")
//	@ApiResponses({ @ApiResponse(code = 200, message = "subauadata saved success"),
//			@ApiResponse(code = 400, message = "subauadata saved success"),
//			@ApiResponse(code = 404, message = "Resource not found"),
//			@ApiResponse(code = 500, message = "Internal server error") })
//	public ResponseEntity<TaResponse<Null>> getuser(@RequestBody @Valid SubAuaRequest req, HttpServletRequest servl)
//			throws MessagingException, IOException {
//		logger.debug(req.toString());
//		System.out.println(req);
//		SubAuaTransform trans = new SubAuaTransform();
//		SubAuaUser ages = trans.getUserMasterDetailsTransfrom(req);
//		System.out.println(ages);
//		subAuaUserService.savesubaua(ages, servl);
//		return createResponse(null, "Data Saved SuccessFully!", 1000, Type.INFORMATION);
//	}
//
//	@PostMapping("/auth/subaualogin")
//	@ApiOperation(value = "Sub Aua Login ", notes = "Sub Aua Login")
//	@ApiResponses({ @ApiResponse(code = 200, message = "Sub Aua Login"),
//			@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Resource not found"),
//			@ApiResponse(code = 500, message = "Internal server error") })
//	public ResponseEntity<TaResponse<LoginResponse>> subaualogin(@RequestBody @Valid SubAuaLoginRequest req,
//			HttpServletRequest servl) throws MessagingException {
//		logger.debug(req.toString());
//		System.out.println("dataaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa" + req);
//		LoginResponse login = subAuaUserService.loginstatus(req, servl);
////		boolean applicationstatus = subAuaUserService.getApplicationstatus(req, servl);
//
////		System.out.println("applicationstatus" + applicationstatus);
//
//		return createResponse(login, "success!", 1000, Type.INFORMATION);
////		DitecUserRoles users = ditecUserRolesService.getLoginRoles(req, servl);
//	}

//		if ("ADMIN".equals(users.getRole())) {
//			LoginResponse response = new LoginResponse();
//			response.setUser("ADMIN");
//			response.setUserName("admin");
//			logger.debug(response.toString());
//			return createResponse(response, "Login Successfull!", 1000, Type.INFORMATION);
//		} else if ("superadmin".equals(users.getRole())) {
//			LoginResponse response = new LoginResponse();
//			response.setUser("SUPERADMIN");
//			response.setUserName("superadmin");
//			logger.debug(response.toString());
//			return createResponse(response, "Login Successfull!", 1000, Type.INFORMATION);
//		} else {
//			String jwt = jwtUtils.generateToken(login);

//			if (login != null) {
//				LoginResponse response = new LoginResponse();
//				SubAuaUser user = new SubAuaUser();
//				response.setUser("SUBAUA");
////				response.setUserName("" + login.getSubAuaId());
//				response.setDocumentstatus(applicationstatus);
//				response.setFirsttimeuser(login.getFirsttimeuser());
////				response.setToken(jwt);
//				logger.debug(response.toString());
//				return createResponse(login, "success!", 1000, Type.INFORMATION);
//			} else {
//				LoginResponse response = new LoginResponse();
//				response.setUser("SUBAUA");
//				response.setUserName("" + req.getUserName());
//				logger.debug(response.toString());
//				return createResponse(null, "success!", 1001, Type.INFORMATION);
//			}
//	}

//	}

	@PostMapping(value = { "/subauaupdate" }, produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	@ApiOperation(value = "Sub Aua Update ", notes = "Sub Aua update ")
	@ApiResponses({ @ApiResponse(code = 200, message = "Sub Aua update"),
			@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<TaResponse<SubAuaUser>> updateAgencyDetails(@Valid @RequestBody SubAuaUpdateRequest request) {
		if (request != null) {
			SubAuaUser details = subAuaUserService.getsubauaupdateuser(request);
			return createResponse(details, "success!", 1000, Type.INFORMATION);
		} else {
			return createResponse(null, "failure!", 1000, Type.INFORMATION);
		}
	}

	@PostMapping("/subdeletead")
	@ApiOperation(value = "Sub Aua Deletead ", notes = "Sub Aua Deletead")
	@ApiResponses({ @ApiResponse(code = 200, message = "Sub Aua Deletead"),
			@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<TaResponse<Object>> deleteAgencyDetailsById(@Valid @RequestBody SubAuaDeleteRequest request) {
		logger.debug(request.toString());
		subAuaUserService.deleteAgencyDetailsById(request.getSubAuaId());
		return createResponse(null, "success!", 1000, Type.INFORMATION);

	}

	@GetMapping("/subuseralldata")
	@ApiOperation(value = "Getting all subdatas", notes = "Retrieve a resource")
	@ApiResponses({ @ApiResponse(code = 200, message = "Getting all subdata "),
			@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<TaResponse<List<SubAuaUser>>> getall() {
		List<SubAuaUser> list = subAuaUserService.getAllData();
		logger.debug(list.toString());
		return createResponse(list, "success!", 1000, Type.INFORMATION);
	}

	@GetMapping("/subuseralldatatable")
	@ApiOperation(value = "Getting all subdatas", notes = "Retrieve a resource")
	@ApiResponses({ @ApiResponse(code = 200, message = "Getting all subdata "),
			@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<TaResponse<SubauaAllDataResponse>> getalltabledata(Pageable pageable) {
		Page<SubAuaUserResponse> list = subAuaUserService.getSubAuaRePage(pageable);
		List<SubAuaUserResponse> allData = list.getContent();
		PaginationMeta pagination = PaginationMeta.createPagination(list);
		SubauaAllDataResponse setDataforResponse = new SubauaAllDataResponse();
		setDataforResponse.setListData(allData);
		setDataforResponse.setPagination(pagination);
		logger.debug(setDataforResponse.toString());
		return createResponse(setDataforResponse, "success!", 1000, Type.INFORMATION);

	}

	@PostMapping("/subuserdatabyid")
	public ResponseEntity<SubAuaUser> getalltablebyId(@Valid @RequestBody SubAuaApproveRequest req) {
		logger.debug(req.toString());
		SubAuaUser list = subAuaUserService.getAllDataResponse(req);
		return ResponseEntity.ok(list);
	}

	@PostMapping("/approvereq")
	@ApiOperation(value = "Approved/Rejeacted request", notes = "Approved/Rejeacted")
	@ApiResponses({ @ApiResponse(code = 200, message = "Approved or Rejeacted subdata "),
			@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<TaResponse<SubAuaApproveResponse>> getuser(@Valid @RequestBody SubAuaApproveRequest req,
			HttpServletRequest servl) throws MessagingException, IOException {
		logger.debug(req.toString());
		try {
			List<SubAuaUser> list = subAuaUserService.getapprove(req, servl);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		SubAuaApproveResponse res1 = new SubAuaApproveResponse();
		res1.setStatus(req.getStatus());
		res1.setSubAuaId(req.getSubAuaId());
		logger.debug(res1.toString());
		return createResponse(res1, "success!", 1000, Type.INFORMATION);

	}

	@PostMapping("/subchangepassword")
	@ApiOperation(value = "Changed password request", notes = "Changed password request subaua")
	@ApiResponses({ @ApiResponse(code = 200, message = "Changed password request subdata "),
			@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<TaResponse<SubAuaChangepasswordResponse>> getSubuserChangePassword(
			@RequestBody @Valid SubAuaUserResetPasswordRequest req) {
		logger.debug(req.toString());

		SubAuaUser changeuser = subAuaUserService.changePassword(req);
		SubAuaChangepasswordResponse ress = new SubAuaChangepasswordResponse();
		ress.setFirsttimeuser(changeuser.getFirsttimeuser());
		return createResponse(ress, "success!", 1000, Type.INFORMATION);

	}

	@PostMapping("/subauaforgotpsw")
	@ApiOperation(value = "Forgot password request", notes = "Forgot password request subaua")
	@ApiResponses({ @ApiResponse(code = 200, message = "Forgot password request subdata "),
			@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<TaResponse<SubAuaUserForgotPasswordResponse>> getforgotpsw(
			@RequestBody @Valid SubAuaUserForgotPasswodRequest req, HttpServletRequest servlreq)
			throws MessagingException {
		logger.debug(req.toString());
		if (req.getManagementEmail() != null) {
			SubAuaUser user = subAuaUserService.getforgotPassword(req, servlreq);
			SubAuaUserForgotPasswordResponse ress = new SubAuaUserForgotPasswordResponse();
			ress.setSubAuaId(user.getSubAuaId());
			return createResponse(ress, "success!", 1000, Type.INFORMATION);
		} else {
			return createResponse(null, "success!", 1001, Type.INFORMATION);
		}

	}

	@PostMapping("/forgotpasswordchange")
	@ApiResponses({ @ApiResponse(code = 200, message = "forgotpassword change amount subdata "),
			@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<TaResponse<Object>> getChangepassword(@RequestBody @Valid SubAuaChangePasswordRequest req) {
		System.out.println("controller");
		logger.debug(req.toString());
		if (req.getSubAuaId() != null && req.getPassword() != null) {
			SubAuaUser user = subAuaUserService.forgotchangepassword(req);
			return createResponse(null, "success!", 1000, Type.INFORMATION);
		} else {
			return createResponse(null, "success!", 1001, Type.INFORMATION);
		}

	}

	@PostMapping("/subauaemailcheck")
	public ResponseEntity<TaResponse<SubAuaUser>> getCheckActivelink(
			@RequestBody @Valid SubAuaforgotPasswordCheckLinkRequest req) {
		logger.debug(req.toString());
		if (StringUtils.isNotEmpty(req.getSubAuaId())) {
			SubAuaUser subaua = subAuaUserService.chceckactivelink(req);
			return createResponse(null, "success!", 1000, Type.INFORMATION);
		} else {
			return createResponse(null, "success!", 1001, Type.INFORMATION);
		}
	}

	@PostMapping("/transactionamount")
	@ApiOperation(value = "Transaction amount request", notes = "Transaction amount request subaua")
	@ApiResponses({ @ApiResponse(code = 200, message = "Transaction amount subdata "),
			@ApiResponse(code = 400, message = "Bad request"), @ApiResponse(code = 404, message = "Resource not found"),
			@ApiResponse(code = 500, message = "Internal server error") })
	public ResponseEntity<TaResponse<TransactionModelResponse>> getTransactionModelResponse(
			@RequestBody @Valid TransactionModelRequest req) {
		logger.debug(req.toString());
		TransactionModelResponse trans = subAuaUserService.getsubauaEachPayment(req);
		return createResponse(trans, "success!", 1000, Type.INFORMATION);

	}

	@PostMapping("/subauachangepsw")
	public ResponseEntity<TaResponse<Object>> getChangePassword(@RequestBody @Valid SubAuaChangePasswordRequest req) {
		logger.debug(req.toString());

		SubAuaUser user = subAuaUserService.getChangePassword(req);
		logger.debug(user.toString());
		return createResponse(null, "success!", 1000, Type.INFORMATION);

	}

	@PostMapping("/accountdetiles")
	public ResponseEntity<TaResponse<Object>> getAccountdetiles(@RequestBody @Valid SubAuaUserRequest req) {
		logger.debug(req.toString());
		System.out.println("reqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqAcccccc"+req);
		SubAuaUser user = subAuaUserService.getAccountDetiles(req);
		logger.debug(user.toString());
		return createResponse(null, "success!", 1000, Type.INFORMATION);

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
