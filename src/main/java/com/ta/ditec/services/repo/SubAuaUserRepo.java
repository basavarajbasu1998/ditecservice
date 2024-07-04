package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.SubAuaUser;

@Repository
public interface SubAuaUserRepo extends JpaRepository<SubAuaUser, Long> {
	
	
	@Query("SELECT u FROM SubAuaUser u WHERE u.subAuaId = :userName")
	SubAuaUser getSubAuaId(@Param("userName") String userName);

	List<SubAuaUser> findBySubAuaId(String id);

	List<SubAuaUser> findBySubAuaIdAndPassword(String userName, String password);

	List<SubAuaUser> findByManagementEmail(String managementEmail);

	Page<SubAuaUser> findAllByOrderBySubAuaIdDesc(Pageable pageable);

	List<SubAuaUser> findAllByOrderBySubAuaIdDesc();

	List<SubAuaUser> findByManagementEmailOrManagementMobilenumber(String userName, String userName2);

//	@Query("SELECT u FROM SubAuaUser u WHERE u.managementEmail = :userName OR u.managementMobilenumber = :userName")
//	List<SubAuaUser> findByManagementEmailOrManagementMobileNumber(@Param("userName") String userName);
	@Query("SELECT u FROM SubAuaUser u WHERE u.managementEmail = :userName OR u.managementMobilenumber = :userName")
	List<SubAuaUser> findByManagementEmailOrManagementMobileNumber(@Param("userName") String userName);

	List<SubAuaUser> findByManagementMobilenumber(String managementMobilenumber);

//	SubAuaUser findByUserName(String username);
	
	

}
