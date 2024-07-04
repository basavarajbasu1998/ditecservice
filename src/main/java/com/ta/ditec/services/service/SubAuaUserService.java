package com.ta.ditec.services.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.request.SubAuaApproveRequest;
import com.ta.ditec.services.request.SubAuaChangePasswordRequest;
import com.ta.ditec.services.request.SubAuaLoginRequest;
import com.ta.ditec.services.request.SubAuaUpdateRequest;
import com.ta.ditec.services.request.SubAuaUserForgotPasswodRequest;
import com.ta.ditec.services.request.SubAuaUserRequest;
import com.ta.ditec.services.request.SubAuaUserResetPasswordRequest;
import com.ta.ditec.services.request.SubAuaforgotPasswordCheckLinkRequest;
import com.ta.ditec.services.request.TransactionModelRequest;
import com.ta.ditec.services.response.LoginResponse;
import com.ta.ditec.services.response.SubAuaUserResponse;
import com.ta.ditec.services.response.TransactionModelResponse;

public interface SubAuaUserService {

	public SubAuaUser getsubauaupdateuser(SubAuaUpdateRequest req);

//	Page<SubAuaUser> fetchSubauaList(PaginationRequest request);
//
	void deleteAgencyDetailsById(String id);

//
	public List<SubAuaUser> getAllData();

//	public List<SubAuaUser> getapporve(SubAuaApproveRequest req,HttpServletRequest servletRequest)throws MessagingException;

	public List<SubAuaUser> getapprove(SubAuaApproveRequest req, HttpServletRequest servletRequest)
			throws MessagingException;

	public Page<SubAuaUser> getAllDataResponse(Pageable pageable);

	public Page<SubAuaUserResponse> getSubAuaRePage(Pageable pageable);

	public LoginResponse loginstatus(SubAuaLoginRequest req, HttpServletRequest servl);

	public boolean getApplicationstatus(SubAuaLoginRequest req, HttpServletRequest servl);


	SubAuaUser savesubaua(SubAuaUser omdetil, HttpServletRequest servletRequest) throws IOException, MessagingException;

	SubAuaUser changePassword(SubAuaUserResetPasswordRequest req);

	public SubAuaUser getforgotPassword(SubAuaUserForgotPasswodRequest req, HttpServletRequest servletRequest)
			throws MessagingException;

	public SubAuaUser forgotchangepassword(SubAuaChangePasswordRequest request);

	public SubAuaUser chceckactivelink(SubAuaforgotPasswordCheckLinkRequest req);

	public SubAuaUser getAllDataResponse(@Valid SubAuaApproveRequest req);

	public TransactionModelResponse getsubauaEachPayment(TransactionModelRequest req);

	public SubAuaUser getChangePassword(SubAuaChangePasswordRequest req);

	public SubAuaUser getAccountDetiles(SubAuaUserRequest req);
	
	



}
