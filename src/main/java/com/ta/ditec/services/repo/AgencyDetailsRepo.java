package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.AgencyDetails;

@Repository
public interface AgencyDetailsRepo extends JpaRepository<AgencyDetails, Long> {

	List<AgencyDetails> findByAgencyId(String agencyId);

	Page<AgencyDetails> findAllByAgencyFirstNameContainingOrAgencyStateNameContaining(String search, String search2,
			Pageable paging);



}
