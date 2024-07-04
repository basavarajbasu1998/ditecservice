package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class LoginResponse {

	private String user;

	private String userName;

	private boolean documentstatus;

	private Integer firsttimeuser;
	
	private String token;
	
	private String subauakey;
	
	private String subauaname;

}
