package com.ta.ditec.services.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.UserMasterDetails;
import com.ta.ditec.services.request.PaginationRequest;
import com.ta.ditec.services.request.UserMasterDetailesResetPasswordRequest;
import com.ta.ditec.services.request.UserMasterDetailsChangePasswordRequest;
import com.ta.ditec.services.request.UserMasterDetailsDeleteRequest;
import com.ta.ditec.services.request.UserMasterDetailsForgotPasswodRequest;
import com.ta.ditec.services.request.UserMasterDetailsLoginRequest;
import com.ta.ditec.services.request.UserMasterDetailsUpdateRequest;
import com.ta.ditec.services.request.UserMasterDetailsforgotPasswordCheckLinkRequest;

@Service
public interface UserMasterDetailsService {

	UserMasterDetails saveDetails(UserMasterDetails request, HttpServletRequest servletRequest);
	
	UserMasterDetails superAdminData(UserMasterDetails request,HttpServletRequest servletRequest);

	Page<UserMasterDetails> fetchUserMasterDetailsList(PaginationRequest request);
	
	List<UserMasterDetails> convertFiles();
	List<UserMasterDetails> checkData();

	UserMasterDetails updateUserMasterDetails(UserMasterDetailsUpdateRequest umdetil);
	
	UserMasterDetails findById(UserMasterDetailsDeleteRequest request);

	UserMasterDetails Login(UserMasterDetailsLoginRequest request);
	
	UserMasterDetails changePassword(UserMasterDetailesResetPasswordRequest req);

	UserMasterDetails activeLink(String email,HttpServletRequest servletRequest);
	
	UserMasterDetails forgotPassword(UserMasterDetailsForgotPasswodRequest request,HttpServletRequest servletRequest);

	void deleteUser(String id);

	UserMasterDetails forgotchangepassword(UserMasterDetailsChangePasswordRequest request);

	UserMasterDetails forgotPasswordCheckLink(UserMasterDetailsforgotPasswordCheckLinkRequest request);

	

	
	
	
}
