package com.ta.ditec.services.service;

import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.request.OtpRequset;
import com.ta.ditec.services.request.OtpResendRequset;
import com.ta.ditec.services.response.LoginResponse;

public interface OtpMasterDetilesService {

	public LoginResponse getOtpMasterDetiles(OtpRequset req);

	public SubAuaUser user(OtpRequset req);
	public SubAuaUser resenduser(OtpResendRequset req);
}
