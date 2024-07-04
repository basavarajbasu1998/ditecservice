package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.StatusMasterDetails;

@Repository
public interface StatusMasterDetailsRepository extends JpaRepository<StatusMasterDetails, Long> {

	List<StatusMasterDetails> findByStatusId(String statusId);

	Page<StatusMasterDetails> findAllByStatusNameContaining(String search, Pageable paging); 

}
