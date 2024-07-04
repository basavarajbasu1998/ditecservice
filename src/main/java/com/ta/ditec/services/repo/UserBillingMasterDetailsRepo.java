package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.UserBillingMasterDetails;

@Repository
public interface UserBillingMasterDetailsRepo extends JpaRepository<UserBillingMasterDetails, Long> {

	List<UserBillingMasterDetails> findByBillingId(String billingId);

	Page<UserBillingMasterDetails> findAllByBillingtypeContainingOrAgencybillingCycleContaining(String search,
			String search2, Pageable paging);

	List<UserBillingMasterDetails> findByUserId(String userId);


}
