package com.ta.ditec.services.serviceimpl;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.encrypt.SHAEnc;
import com.ta.ditec.services.exception.ErrorCode;
import com.ta.ditec.services.exception.TaException;
import com.ta.ditec.services.model.ConfigMasterDetiles;
import com.ta.ditec.services.model.DitecUserRoles;
import com.ta.ditec.services.password.Password;
import com.ta.ditec.services.repo.ConfigMasterDetilesRepo;
import com.ta.ditec.services.repo.DitecUserRolesRepo;
import com.ta.ditec.services.request.SubAuaLoginRequest;
import com.ta.ditec.services.service.DitecUserRolesService;
import com.ta.ditec.services.utils.AsyncEmailSender;
import com.ta.ditec.services.utils.DitecMailConstant;
import com.ta.ditec.services.utils.TAConstantes;

@Service
public class DitecUserRolesServiceImpl implements DitecUserRolesService {

	@Autowired
	private DitecUserRolesRepo ditecUserRolesRepo;

	@Autowired
	private ConfigMasterDetilesRepo configMasterDetilesRepo;

	@Autowired
	private AsyncEmailSender asyncEmailSender;

	@Override
	public DitecUserRoles getDitecUserRoles() {
		List<DitecUserRoles> allData = ditecUserRolesRepo.findAll();

		
		String key = "superadmin_email";
		List<ConfigMasterDetiles> detiles = configMasterDetilesRepo.findByKey(key);

		if (!allData.isEmpty()) {
			return allData.get(0);
		}
		DitecUserRoles user = new DitecUserRoles();
		String sts = Password.generatePassword();
		user.setCreatedBy("super admin");
		user.setCreatedDate(new Date());
		user.setLastModifiedBy("super admin");
		user.setLastModifiedDate(new Date());
		user.setPassword(SHAEnc.encryptData(sts));
		user.setUserName("admin");
		user.setRole("ADMIN");
		asyncEmailSender.sendEmailAsync(detiles.get(0).getValue(),
				DitecMailConstant.getHtmlMessageAdminLoginDetiles(user), TAConstantes.LOGIN);
		return ditecUserRolesRepo.save(user);
	}

	@Override
	public DitecUserRoles getLoginRoles(SubAuaLoginRequest req, HttpServletRequest servl) {
		List<DitecUserRoles> roles = ditecUserRolesRepo.findByUserName(req.getUserName());
		if (CollectionUtils.isNotEmpty(roles)) {
			String sts = SHAEnc.encryptData(req.getPassword());
			if (roles.get(0).getPassword().equals(sts)) {
				return roles.get(0);
			} else {
				throw new TaException(ErrorCode.USER_CRED_VALID, ErrorCode.USER_CRED_VALID.getErrorMsg());
			}

		} else {
			throw new TaException(ErrorCode.NO_DATA_FOUND, ErrorCode.NO_DATA_FOUND.getErrorMsg());
		}
	}

}
