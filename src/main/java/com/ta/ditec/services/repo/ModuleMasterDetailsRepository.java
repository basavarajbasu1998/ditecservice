package com.ta.ditec.services.repo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ta.ditec.services.model.ModuleMasterDetails;

@Repository
public interface ModuleMasterDetailsRepository extends JpaRepository<ModuleMasterDetails, Long>{

	List<ModuleMasterDetails> findByModuleId(String moduleId);

	Page<ModuleMasterDetails> findAllByModuleNameContaining(String search, Pageable paging); 

}
