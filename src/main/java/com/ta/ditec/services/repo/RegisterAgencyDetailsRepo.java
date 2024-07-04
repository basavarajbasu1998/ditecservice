package com.ta.ditec.services.repo;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.RegisterAgencyDetails;

@Repository
public interface RegisterAgencyDetailsRepo extends JpaRepository<RegisterAgencyDetails, Serializable> {

	List<RegisterAgencyDetails> findByAgencyId(String agencyId);

	Page<RegisterAgencyDetails> findAllByAgencyNameContainingOrAgencyDesignationContainingOrAgencyMobileNumberContainingOrAgencyEmailIdContaining(
			String search, String search2,String search3,String search4, Pageable paging);

	
}
