package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.OrganisationMasterDetails;

@Repository
public interface OrganisationMasterDetailsRepository extends JpaRepository<OrganisationMasterDetails, Long> {

	List<OrganisationMasterDetails> findByOrganisationId(String organizationId);

	List<OrganisationMasterDetails> findByOrganisationContactEmailId(String email);

	Page<OrganisationMasterDetails> findAll(Pageable paging);

	Page<OrganisationMasterDetails> findAllByOrganisationNameContainingOrOrganisationContactNameContaining(
			String organisationName, String organisationContactName, Pageable paging);

	boolean existsByOrganisationContactEmailId(String organisationContactEmailId);

	boolean existsByOrganisationContactMobileNumber(String organisationContactMobileNumber);

}
