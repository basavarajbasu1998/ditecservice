package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.UserMasterDetails;

@Repository
public interface UserMasterDetailsRepository extends JpaRepository<UserMasterDetails, Long> {

	List<UserMasterDetails> findByUserId(String userId);

	List<UserMasterDetails> findByUserName(String userName);
	
	List<UserMasterDetails> findByuserEmail(String userEmail);

	Page<UserMasterDetails> findAllByUserNameContainingOrUserMobileNumberContainingOrUserEmailContaining(String search,
			String search2, String search3, Pageable paging);
	
	boolean existsByuserName(String userName);

	boolean existsByuserEmail(String userEmail);
	
	boolean existsByuserMobileNumber(String userMobileNumber);

	

	 

	     UserMasterDetails findByUserPassword(String userPassword);

		Page<UserMasterDetails> findAllByUserNameContainingOrUserMobileNumberContainingOrUserEmailContainingAndUserNameNot(
				String search, String search2, String search3, Pageable paging, String string);

		Page<UserMasterDetails> findAllByUserNameNot(String string,Pageable paging);

	
	

	


}
