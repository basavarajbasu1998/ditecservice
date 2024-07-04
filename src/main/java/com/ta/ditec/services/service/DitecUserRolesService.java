package com.ta.ditec.services.service;

import javax.servlet.http.HttpServletRequest;

import com.ta.ditec.services.model.DitecUserRoles;
import com.ta.ditec.services.request.SubAuaLoginRequest;

public interface DitecUserRolesService {

	DitecUserRoles getDitecUserRoles();

	DitecUserRoles getLoginRoles(SubAuaLoginRequest req, HttpServletRequest servl);

}
