package com.ta.ditec.services.securityconfig;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ta.ditec.services.model.DitecUserRoles;
import com.ta.ditec.services.model.SubAuaUser;
import com.ta.ditec.services.repo.DitecUserRolesRepo;
import com.ta.ditec.services.repo.SubAuaUserRepo;

@Service
public class OurUserDetailsService implements UserDetailsService {

	@Autowired
	private SubAuaUserRepo subAuaUserRepo;

	@Autowired
	private DitecUserRolesRepo ditecUserRolesRepo;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		List<SubAuaUser> userDetailsList = subAuaUserRepo.findByManagementEmailOrManagementMobileNumber(username);
		if (!userDetailsList.isEmpty()) {
			System.out.println("our servicess insideeeeeeeeeeeee" + userDetailsList);
			return userDetailsList.get(0);
		}

		List<DitecUserRoles> rols = ditecUserRolesRepo.findByUserName(username);
		if (!rols.isEmpty()) {
			System.out.println("our servicess insideeeeeeeeeeeee" + rols);
			return rols.get(0);
		}
		throw new UsernameNotFoundException("User not found with username: " + username);
	}

}
