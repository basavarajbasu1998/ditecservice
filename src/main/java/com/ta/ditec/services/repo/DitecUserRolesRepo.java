package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.DitecUserRoles;

@Repository
public interface DitecUserRolesRepo extends JpaRepository<DitecUserRoles, Long> {

	List<DitecUserRoles> findByUserName(String userName);

}
