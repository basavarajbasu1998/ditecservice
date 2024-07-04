package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class UserMasterDetailsLoginResponse {
	
	private String userId;
	private String userName;
	private String userMobileNumber;
	private String userEmail;
	private Integer firstTimeUser;

}
