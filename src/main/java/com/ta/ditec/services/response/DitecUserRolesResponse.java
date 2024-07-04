package com.ta.ditec.services.response;

import lombok.Data;

@Data
public class DitecUserRolesResponse {

	private String userName;

	private String password;
	private String role;
}
