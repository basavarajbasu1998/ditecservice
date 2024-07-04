package com.ta.ditec.services.request;

import lombok.Data;

@Data
public class DitecUserRolesRequest {

	private String userName;

	private String password;

	private String role;
}
