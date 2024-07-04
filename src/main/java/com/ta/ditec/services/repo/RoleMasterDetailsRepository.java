package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.RoleMasterDetails;

@Repository
public interface RoleMasterDetailsRepository extends JpaRepository<RoleMasterDetails, Long>{

	List<RoleMasterDetails> findByStatusId(String statusId);

	Page<RoleMasterDetails> findAllByStatusNameContaining(String search, Pageable paging); 

}
